package com.liren.lottery_system.service.impl;

import com.liren.lottery_system.common.constant.Constants;
import com.liren.lottery_system.common.enums.*;
import com.liren.lottery_system.common.exception.ServiceException;
import com.liren.lottery_system.common.pojo.ActivityRecord;
import com.liren.lottery_system.common.pojo.dto.*;
import com.liren.lottery_system.common.pojo.entity.ActivityEntity;
import com.liren.lottery_system.common.pojo.entity.ActivityPrizeEntity;
import com.liren.lottery_system.common.pojo.entity.ActivityUserEntity;
import com.liren.lottery_system.common.pojo.entity.PrizeEntity;
import com.liren.lottery_system.common.pojo.vo.ActivityResponseVO;
import com.liren.lottery_system.common.utils.JsonUtil;
import com.liren.lottery_system.common.utils.RedisUtil;
import com.liren.lottery_system.mapper.*;
import com.liren.lottery_system.service.ActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityXmlMapper activityXmlMapper;

    @Autowired
    private PrizeXmlMapper prizeXmlMapper;

    @Autowired
    private UserXmlMapper userXmlMapper;

    @Autowired
    private ActivityPrizeXmlMapper activityPrizeXmlMapper;

    @Autowired
    private ActivityUserXmlMapper activityUserXmlMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    @Transactional(rollbackFor = Exception.class) // 一个方法里面如果涉及到多表，那就得开启事务
    public Long createActivity(CreateActivityRequestDTO req) {
        // 校验参数合法性
        checkInfo(req);

        // 活动数据入库
        ActivityEntity activity = new ActivityEntity();
        activity.setActivityName(req.getActivityName());
        activity.setDescription(req.getDescription());
        activity.setStatus(ActivityStatusEnum.RUNNING.name());
        activityXmlMapper.insertActivity(activity);

        // 活动奖品数据入库
        List<ActivityPrizeEntity> activityPrizes = req.getActivityPrizeList()
                .stream()
                .map(x -> {
                    ActivityPrizeEntity activityPrize = new ActivityPrizeEntity();
                    activityPrize.setActivityId(activity.getId());
                    activityPrize.setPrizeId(x.getPrizeId());
                    activityPrize.setPrizeAmount(x.getPrizeAmount());
                    activityPrize.setPrizeTiers(x.getPrizeTiers());
                    activityPrize.setStatus(PrizeStatusEnum.AVAILABLE.name());
                    return activityPrize;
                }).collect(Collectors.toList());
        activityPrizeXmlMapper.batchInsert(activityPrizes);

        // 活动人员数据入库
        List<ActivityUserEntity> activityUsers = req.getActivityUserList()
                .stream()
                .map(x -> {
                    ActivityUserEntity activityUser = new ActivityUserEntity();
                    activityUser.setActivityId(activity.getId());
                    activityUser.setUserId(x.getUserId());
                    activityUser.setUserName(x.getUserName());
                    activityUser.setStatus(UserStatusEnum.AVAILABLE.name());
                    return activityUser;
                }).collect(Collectors.toList());
        activityUserXmlMapper.batchInsert(activityUsers);

        // 整合活动详细数据，存放到redis中
        // 1. 先获取奖品基本属性
        List<Long> prizeIds = req.getActivityPrizeList()
                .stream()
                .map(ActivityPrizeDTO::getPrizeId)
                .distinct()
                .collect(Collectors.toList());
        List<PrizeEntity> prizes = prizeXmlMapper.listPrizeByIds(prizeIds);

        // 2. 传入需要整合的数据，整合得到一个大类
        ActivityDetailDTO activityDetailDTO = mergeIntoActivityDetail(activity, activityPrizes, prizes, activityUsers);

        // 3. 存储到redis中
        storeToRedis(activityDetailDTO);

        // 返回活动id
        return activityDetailDTO.getActivityId();
    }

    @Override
    public GetActivityResponseDTO getActivity(GetActivityRequestDTO req) {
        Integer currentPage = req.getCurrentPage();
        Integer pageSize = req.getPageSize();
        if(currentPage == null || pageSize == null) {
            throw new ServiceException(ServiceStatusEnum.PAGE_PARAMETER_ERROR.getCodeStatus());
        }

        Integer offset = (currentPage - 1) * pageSize;
        Integer activitySum = activityXmlMapper.countActivity();
        if(offset > activitySum) {
            throw new ServiceException(ServiceStatusEnum.PAGE_OVERMAX_ERROR.getCodeStatus());
        }
        List<ActivityEntity> activities = activityXmlMapper.listActivity(offset, pageSize); // 获取活动实体类

        // 转换
        List<ActivityRecord> activityRecordList = activities.stream()
                .map(x -> {
                    ActivityRecord activityRecord = new ActivityRecord();
                    activityRecord.setActivityId(x.getId());
                    activityRecord.setActivityName(x.getActivityName());
                    activityRecord.setDescription(x.getDescription());
                    if (ActivityStatusEnum.forName(x.getStatus()).equals(ActivityStatusEnum.RUNNING)) {
                        activityRecord.setValid(true);
                    } else {
                        activityRecord.setValid(false);
                    }
                    return activityRecord;
                }).collect(Collectors.toList());
        GetActivityResponseDTO activityResponseVO = new GetActivityResponseDTO();
        activityResponseVO.setRecords(activityRecordList);
        activityResponseVO.setTotal(activitySum / pageSize);
        return activityResponseVO;
    }



    @Override
    public ActivityDetailDTO getActivityDetail(Long activityId) {
        // 校验
        if(activityId == null) {
            log.error("用于获取活动详细实体类的activityId为空！");
            return null;
        }

        // 访问redis查看是否有对应数据，有的话直接返回
        // redis没有数据则返回mysql进行数据整合
        ActivityDetailDTO activityDetailFromRedis = getActivityDetailFromRedis(activityId);
        if(activityDetailFromRedis != null) {
            log.info("查询活动详细信息成功！detailDTO={}", activityDetailFromRedis);
            return activityDetailFromRedis;
        }

        // 获取活动数据
        ActivityEntity activity = activityXmlMapper.getActivity(activityId);
        if(activity == null) {
            throw new ServiceException(ServiceStatusEnum.ACTIVITY_NOT_FOUND.getCodeStatus());
        }

        // 获取活动奖品数据
        List<ActivityPrizeEntity> activityPrize = activityPrizeXmlMapper.listActivityPrize(activityId);
        List<PrizeEntity> prizes = prizeXmlMapper.listPrizeByIds(
                        activityPrize.stream()
                        .map(ActivityPrizeEntity::getPrizeId)
                        .collect(Collectors.toList()));

        // 获取活动用户数据
        List<ActivityUserEntity> activityUser = activityUserXmlMapper.listActivityUser(activityId);

        // 整合信息，存储到redis中，然后返回即可
        ActivityDetailDTO activityDetailDTO = mergeIntoActivityDetail(activity, activityPrize, prizes, activityUser);
        storeToRedis(activityDetailDTO);
        return activityDetailDTO;
    }

    @Override
    public void updateActivityDetail(Long activityId) {
        // 校验
        if(activityId == null) {
            log.error("用于获取活动详细实体类的activityId为空！");
            return;
        }

        // 获取活动数据
        ActivityEntity activity = activityXmlMapper.getActivity(activityId);
        if(activity == null) {
            throw new ServiceException(ServiceStatusEnum.ACTIVITY_NOT_FOUND_ERROR.getCodeStatus());
        }

        // 获取活动奖品数据
        List<ActivityPrizeEntity> activityPrize = activityPrizeXmlMapper.listActivityPrize(activityId);
        List<PrizeEntity> prizes = prizeXmlMapper.listPrizeByIds(
                activityPrize.stream()
                        .map(ActivityPrizeEntity::getPrizeId)
                        .collect(Collectors.toList()));

        // 获取活动用户数据
        List<ActivityUserEntity> activityUser = activityUserXmlMapper.listActivityUser(activityId);

        // 整合信息，存储到redis中，然后返回即可
        ActivityDetailDTO activityDetailDTO = mergeIntoActivityDetail(activity, activityPrize, prizes, activityUser);
        storeToRedis(activityDetailDTO);
    }


    /**
     * 存储活动详细实体类到Redis中
     */
    private void storeToRedis(ActivityDetailDTO activityDetailDTO) {
        if(activityDetailDTO == null || activityDetailDTO.getActivityId() == null) {
            log.warn("要存储活动详细实体类到Redis的活动信息不存在！");
            return;
        }

        try {
            redisUtil.set(Constants.ACTIVITY_PREFIX + activityDetailDTO.getActivityId(),
                    JsonUtil.toJson(activityDetailDTO),
                    Constants.ACTIVITY_EXPIRE_TIME);
        } catch (Exception e) {
            log.error("存储活动详细实体类到Redis失败，ActivityDetailDTO={}", JsonUtil.toJson(activityDetailDTO));
        }
    }

    /**
     * 从Redis中获取活动详细实体类
     */
    private ActivityDetailDTO getActivityDetailFromRedis(Long activityId) {
        if(activityId == null) {
            log.error("用于获取Redis中活动详细实体类的activityId为空！");
            return null;
        }

        try {
            String str = (String)redisUtil.get(Constants.ACTIVITY_PREFIX + activityId);
            if(!StringUtils.hasText(str)) {
                log.info("获取Redis中活动详细实体类为空，key={}", Constants.ACTIVITY_PREFIX + activityId);
                return null;
            }
            return JsonUtil.toObject(str, ActivityDetailDTO.class);
        } catch (Exception e) {
            log.error("获取Redis中活动详细实体类异常，key={}", Constants.ACTIVITY_PREFIX + activityId);
            return null;
        }
    }

    /**
     * 将多个实体类合并成一个活动详细实体类
     * @param activity 活动实体类
     * @param activityPrizes 活动奖品实体类
     * @param prizes 奖品实体类
     * @param activityUsers 活动人员实体类
     * @return
     */
    private ActivityDetailDTO mergeIntoActivityDetail(ActivityEntity activity,
                                         List<ActivityPrizeEntity> activityPrizes,
                                         List<PrizeEntity> prizes,
                                         List<ActivityUserEntity> activityUsers) {
        ActivityDetailDTO activityDetailDTO = new ActivityDetailDTO();

        // 整合活动信息
        activityDetailDTO.setActivityId(activity.getId());
        activityDetailDTO.setActivityName(activity.getActivityName());
        activityDetailDTO.setDescription(activity.getDescription());
        activityDetailDTO.setStatus(ActivityStatusEnum.forName(activity.getStatus()));

        // 整合奖品信息
        List<ActivityDetailDTO.PrizeDTO> prizeDTOList = activityPrizes
                .stream()
                .map(x -> {
                    ActivityDetailDTO.PrizeDTO prizeDTO = new ActivityDetailDTO.PrizeDTO();
                    prizeDTO.setPrizeId(x.getPrizeId());
                    prizeDTO.setPrizeAmount(x.getPrizeAmount());
                    prizeDTO.setPrizeTiers(PrizeTierEnum.forName(x.getPrizeTiers()));
                    prizeDTO.setStatus(PrizeStatusEnum.forName(x.getStatus()));

                    Optional<PrizeEntity> optionalPrizeDTO = prizes.stream()
                            .filter(y -> y.getId().equals(x.getPrizeId()))
                            .findFirst(); // 💥返回一个optional对象，而不是流对象
                    // 如果prizes为空，不执行当前方法，不为空才执行
                    optionalPrizeDTO.ifPresent(y -> {
                        prizeDTO.setName(y.getName());
                        prizeDTO.setDescription(y.getDescription());
                        prizeDTO.setPrice(y.getPrice());
                        prizeDTO.setImageUrl(y.getImageUrl());
                    });
                    return prizeDTO;
                }).collect(Collectors.toList());
        activityDetailDTO.setPrizeDTOList(prizeDTOList);

        // 整合人员信息
        List<ActivityDetailDTO.UserDTO> userDTOList = activityUsers
                .stream()
                .map(x -> {
                    ActivityDetailDTO.UserDTO userDTO = new ActivityDetailDTO.UserDTO();
                    userDTO.setUserId(x.getUserId());
                    userDTO.setUserName(x.getUserName());
                    userDTO.setStatus(UserStatusEnum.forName(x.getStatus()));
                    return userDTO;
                }).collect(Collectors.toList());
        activityDetailDTO.setUserDTOList(userDTOList);

        return activityDetailDTO;
    }


    /**
     * 校验活动请求信息
     */
    private void checkInfo(CreateActivityRequestDTO req) {
        if(req == null) {
            throw new ServiceException(ServiceStatusEnum.PARAMETER_INVALIDATED.getCodeStatus());
        }

        // 该活动是否已经存在并且正在执行中（需要获取该活动的数量）
        Integer activityCount = activityXmlMapper.countActivityByName(req.getActivityName());
        if(activityCount != 0) {
            throw new ServiceException(ServiceStatusEnum.ACTIVITY_ALREADY_EXIST.getCodeStatus());
        }

        // 该活动的奖品是否都存在（需要获取这些奖品，然后判断是否一致）
        // 根据传入的参数，先转化为List<Long> ids，然后去库里看看是否有对应的ids，从而判断是否都存在
        List<Long> InputPrizeIds = req.getActivityPrizeList()
                .stream()
                .map(ActivityPrizeDTO::getPrizeId)
                .distinct()
                .collect(Collectors.toList());
        List<Long> prizeIds = prizeXmlMapper.listPrizeIdByIds(InputPrizeIds);
        if(CollectionUtils.isEmpty(prizeIds)) {
            throw new ServiceException(ServiceStatusEnum.ACTIVITY_PRIZE_NOT_FOUND.getCodeStatus());
        }
        for(Long id : InputPrizeIds) {
            if(!prizeIds.contains(id)) {
                throw new ServiceException(ServiceStatusEnum.ACTIVITY_PRIZE_NOT_FOUND.getCodeStatus());
            }
        }

        // 该活动的奖品级别是否正确
        req.getActivityPrizeList().forEach(x -> {
            if(null == PrizeTierEnum.forName(x.getPrizeTiers())) {
                throw new ServiceException(ServiceStatusEnum.ACTIVITY_PRIZE_TIER_ERROR.getCodeStatus());
            }
        });

        // 该活动的用户是否都存在
        List<Long> InputUserIds = req.getActivityUserList()
                .stream()
                .map(ActivityUserDTO::getUserId)
                .distinct()
                .collect(Collectors.toList());
        List<Long> userIds = userXmlMapper.listUserIdByIds(InputUserIds);
        if(CollectionUtils.isEmpty(userIds)) {
            throw new ServiceException(ServiceStatusEnum.ACTIVITY_USER_NOT_FOUND.getCodeStatus());
        }
        InputUserIds.forEach(id -> {
            if(!userIds.contains(id)) {
                throw new ServiceException(ServiceStatusEnum.ACTIVITY_USER_NOT_FOUND.getCodeStatus());
            }
        });

        // 要求该活动的用户数量 >= 奖品数量
        int userNum = req.getActivityUserList().size();
        long prizeNum = req.getActivityPrizeList()
                .stream()
                .mapToLong(ActivityPrizeDTO::getPrizeAmount)
                .sum();
        if(userNum < prizeNum) {
            throw new ServiceException(ServiceStatusEnum.USER_NUMBER_TOO_SMALL.getCodeStatus());
        }
    }
}
