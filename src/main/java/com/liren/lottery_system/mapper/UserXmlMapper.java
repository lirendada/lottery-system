package com.liren.lottery_system.mapper;

import com.liren.lottery_system.common.pojo.entity.Encrypt;
import com.liren.lottery_system.common.pojo.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserXmlMapper {
    /**
     * 统计指定邮箱出现个数（用于判断是否已经注册邮箱）
     */
    Integer countEmail(@Param("email") String email);

    /**
     * 统计指定手机出现个数（用于判断是否已经注册手机）
     */
    Integer countPhone(@Param("phoneNumber") Encrypt phone); // 注意只需要@Param注解中名称和sql语句中占位符名称一致即可，形参名称不要求！

    /**
     * 插入用户信息
     */
    Integer insertUser(UserEntity user);

    /**
     * 根据邮箱找到对应用户
     */
    UserEntity getUserByEmail(@Param("email") String email);

    /**
     * 根据手机找到对应用户
     */
    UserEntity getUserByPhone(@Param("phoneNumber") Encrypt encrypt);

    /**
     * 获取用户列表
     */
    List<UserEntity> ListUser();

    /**
     * 根据传入的用户id获取对应库中的用户id，用于判断用户是否存在
     */
    List<Long> listUserIdByIds(List<Long> userIds);
}
