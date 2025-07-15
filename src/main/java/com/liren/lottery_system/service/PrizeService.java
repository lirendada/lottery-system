package com.liren.lottery_system.service;

import com.liren.lottery_system.common.pojo.dto.PrizeRequestDTO;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

public interface PrizeService {
    long createPrize(PrizeRequestDTO param, MultipartFile prizePic);

}
