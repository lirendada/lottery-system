package com.liren.lottery_system.controller;

import com.liren.lottery_system.common.pojo.dto.PrizeRequestDTO;
import com.liren.lottery_system.common.utils.JsonUtil;
import com.liren.lottery_system.service.PictureService;
import com.liren.lottery_system.service.PrizeService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class PrizeController {
    @Resource(name = "prizeServiceImpl")
    private PrizeService prizeService;

    @PostMapping("/prize/create")
    public long createPrize(@RequestPart("param") @Validated PrizeRequestDTO param,
                              @RequestPart("prizePic") MultipartFile prizePic) {
        log.info("createPrize controller, param: {}", JsonUtil.toJson(param));
        return prizeService.createPrize(param, prizePic);
    }
}
