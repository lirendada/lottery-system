package com.liren.lottery_system.service.impl;

import com.liren.lottery_system.common.enums.ServiceStatusEnum;
import com.liren.lottery_system.common.exception.ServiceException;
import com.liren.lottery_system.common.pojo.dto.CreatePrizeRequestDTO;
import com.liren.lottery_system.common.pojo.dto.GetPrizeRequestDTO;
import com.liren.lottery_system.common.pojo.dto.GetPrizeResponseDTO;
import com.liren.lottery_system.common.pojo.entity.PrizeEntity;
import com.liren.lottery_system.mapper.PrizeXmlMapper;
import com.liren.lottery_system.service.PictureService;
import com.liren.lottery_system.service.PrizeService;
import io.jsonwebtoken.lang.Collections;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.liren.lottery_system.common.pojo.PrizeRecord;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PrizeServiceImpl implements PrizeService {
    @Resource(name = "pictureServiceImpl")
    private PictureService pictureService;

    @Autowired
    private PrizeXmlMapper prizeXmlMapper;

    @Override
    public long createPrize(CreatePrizeRequestDTO param, MultipartFile prizePic) {
        // 上传图片到本地
        String newFileName = pictureService.uploadPicture(prizePic);

        // 存放信息到数据库
        PrizeEntity prize = new PrizeEntity();
        prize.setName(param.getPrizeName());
        prize.setPrice(param.getPrice());
        prize.setDescription(param.getDescription());
        prize.setImageUrl(newFileName);
//        log.info("创建PrizeEntity：{}", prize);
        int result = prizeXmlMapper.insertPrize(prize);
        if(result != 1) {
            throw new ServiceException(ServiceStatusEnum.INSERT_PRIZE_ERROR.getCodeStatus());
        }

        // 返回存放信息体的id
        return prize.getId();
    }

    @Override
    public GetPrizeResponseDTO getPrizeList(GetPrizeRequestDTO req) {
        Integer currentPage = req.getCurrentPage();
        Integer pageSize = req.getPageSize();
        if (currentPage == null || pageSize == null) {
            throw new ServiceException(ServiceStatusEnum.PAGE_PARAMETER_ERROR.getCodeStatus());
        }

        // 获取奖品总数量
        Integer total = prizeXmlMapper.countPrize();
        if(total < (currentPage - 1)*pageSize) {
            throw new ServiceException(ServiceStatusEnum.PAGE_OVERMAX_ERROR.getCodeStatus());
        }

        // 获取对应页的奖品内容
        List<PrizeEntity> prizeList = prizeXmlMapper.listPrize(currentPage, pageSize);
        if(Collections.isEmpty(prizeList)) {
            throw new ServiceException(ServiceStatusEnum.GET_PRIZE_LIST_ERROR.getCodeStatus());
        }

        // 进行转化
        List<PrizeRecord> records = prizeList.stream()
                .map(x -> {
                    PrizeRecord record = new PrizeRecord();
                    record.setPrizeId(x.getId());
                    record.setPrizeName(x.getName());
                    record.setDescription(x.getDescription());
                    record.setPrice(x.getPrice());
                    record.setImageUrl(x.getImageUrl());
                    return record;
                }).collect(Collectors.toList());

        // 封装返回
        return new GetPrizeResponseDTO(total, records);
    }
}
