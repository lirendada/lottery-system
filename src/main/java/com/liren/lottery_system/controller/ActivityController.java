package com.liren.lottery_system.controller;

import com.liren.lottery_system.common.pojo.dto.ActivityDetailDTO;
import com.liren.lottery_system.common.pojo.dto.CreateActivityRequestDTO;
import com.liren.lottery_system.common.pojo.dto.GetActivityRequestDTO;
import com.liren.lottery_system.common.pojo.dto.GetActivityResponseDTO;
import com.liren.lottery_system.common.pojo.vo.ActivityDetailResponseVO;
import com.liren.lottery_system.common.pojo.vo.ActivityResponseVO;
import com.liren.lottery_system.common.utils.BeanTransformUtil;
import com.liren.lottery_system.common.utils.JsonUtil;
import com.liren.lottery_system.service.ActivityService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ActivityController {
    @Resource(name = "activityServiceImpl")
    private ActivityService activityService;

    @PostMapping("/activity/create")
    public Long createActivity(@RequestBody @Validated CreateActivityRequestDTO req) {
        log.info("createActivity controller, CreateActivityRequestDTO={}", JsonUtil.toJson(req));
        return activityService.createActivity(req);
    }

    @GetMapping("/activity/find-list")
    public ActivityResponseVO getActivityList(GetActivityRequestDTO req) {
        log.info("getActivityList controller, GetActivityRequestDTO={}", JsonUtil.toJson(req));
        GetActivityResponseDTO activity = activityService.getActivity(req);
        return BeanTransformUtil.trans(activity);
    }

    @GetMapping("/activity-detail/find")
    public ActivityDetailResponseVO getActivityDetail(Long activityId) {
        log.info("getActivityDetail controller, activityId={}", activityId);
        ActivityDetailDTO activityDetailDTO = activityService.getActivityDetail(activityId);
        return BeanTransformUtil.trans(activityDetailDTO);
    }
}
