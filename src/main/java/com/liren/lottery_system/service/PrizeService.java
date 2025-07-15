package com.liren.lottery_system.service;

import com.liren.lottery_system.common.pojo.dto.CreatePrizeRequestDTO;
import com.liren.lottery_system.common.pojo.dto.GetPrizeRequestDTO;
import com.liren.lottery_system.common.pojo.dto.GetPrizeResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface PrizeService {
    long createPrize(CreatePrizeRequestDTO param, MultipartFile prizePic);

    GetPrizeResponseDTO getPrizeList(GetPrizeRequestDTO req);
}
