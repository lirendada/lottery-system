package com.liren.lottery_system.service;

import org.springframework.web.multipart.MultipartFile;

public interface PictureService {
    String uploadPicture(MultipartFile file);
}
