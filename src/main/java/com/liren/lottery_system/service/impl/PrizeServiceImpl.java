package com.liren.lottery_system.service.impl;

import com.liren.lottery_system.common.enums.ServiceStatusEnum;
import com.liren.lottery_system.common.exception.ServiceException;
import com.liren.lottery_system.common.pojo.dto.PrizeRequestDTO;
import com.liren.lottery_system.common.pojo.entity.PrizeEntity;
import com.liren.lottery_system.mapper.PrizeXmlMapper;
import com.liren.lottery_system.service.PictureService;
import com.liren.lottery_system.service.PrizeService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class PrizeServiceImpl implements PrizeService {
    @Resource(name = "pictureServiceImpl")
    private PictureService pictureService;

    @Autowired
    private PrizeXmlMapper prizeXmlMapper;

    @Override
    public long createPrize(PrizeRequestDTO param, MultipartFile prizePic) {
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
}
