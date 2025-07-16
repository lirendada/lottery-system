package com.liren.lottery_system.controller;

import com.liren.lottery_system.common.pojo.dto.CreatePrizeRequestDTO;
import com.liren.lottery_system.common.pojo.dto.GetPrizeRequestDTO;
import com.liren.lottery_system.common.pojo.dto.GetPrizeResponseDTO;
import com.liren.lottery_system.common.pojo.vo.PrizeResponseVO;
import com.liren.lottery_system.common.utils.BeanTransformUtil;
import com.liren.lottery_system.common.utils.JsonUtil;
import com.liren.lottery_system.service.PrizeService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class PrizeController {
    @Resource(name = "prizeServiceImpl")
    private PrizeService prizeService;

    /**
     * 创建奖品
     */
    @PostMapping("/prize/create")
    public long createPrize(@RequestPart("param") @Validated CreatePrizeRequestDTO param,
                              @RequestPart("prizePic") MultipartFile prizePic) {
        log.info("createPrize controller, param: {}", JsonUtil.toJson(param));
        return prizeService.createPrize(param, prizePic);
    }

    /**
     *获取奖品列表
     */
    @GetMapping("/prize/find-list")
    public PrizeResponseVO getPrizeList(GetPrizeRequestDTO req) {
        log.info("getPrizeList controller, GetPrizeRequestDTO: {}", JsonUtil.toJson(req));
        GetPrizeResponseDTO prizeList = prizeService.getPrizeList(req);
//        log.info("prizeList: {}", prizeList);
        return BeanTransformUtil.trans(prizeList);
    }


}
