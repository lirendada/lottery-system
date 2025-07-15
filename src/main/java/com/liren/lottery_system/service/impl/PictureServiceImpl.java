package com.liren.lottery_system.service.impl;

import com.liren.lottery_system.common.enums.ServiceStatusEnum;
import com.liren.lottery_system.common.exception.ServiceException;
import com.liren.lottery_system.service.PictureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class PictureServiceImpl implements PictureService {
    @Value("${pic.local-path}")
    private String picLocalPath;

    @Override
    public String uploadPicture(MultipartFile file) {
        // 保证目录存在
        File dir = new File(picLocalPath);
        if(!dir.exists()) {
            dir.mkdirs();
        }

        // 获取文件后缀，然后生成随机文件名拼上后缀，作为新文件名
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf('.'));
        String newFileName = UUID.randomUUID() + suffix;

        // 存放文件到对应路径
        try {
            file.transferTo(new File(picLocalPath + "/" + newFileName));
        } catch (IOException e) {
            throw new ServiceException(ServiceStatusEnum.UPLOAD_PHOTO_ERROR.getCodeStatus());
        }

        // 返回新文件名
        return newFileName;
    }
}
