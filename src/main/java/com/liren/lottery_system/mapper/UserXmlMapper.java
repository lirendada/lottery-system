package com.liren.lottery_system.mapper;

import com.liren.lottery_system.common.pojo.entity.Encrypt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserXmlMapper {
    /**
     * 统计指定邮箱出现个数（用于判断是否已经注册邮箱）
     */
    Integer countEmail(@Param("email") String email);

    /**
     * 统计指定手机出现个数（用于判断是否已经注册手机）
     */
    Integer countPhone(@Param("phoneNumber") Encrypt phone);
}
