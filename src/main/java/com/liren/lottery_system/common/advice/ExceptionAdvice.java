package com.liren.lottery_system.common.advice;

import com.liren.lottery_system.common.exception.ControllerException;
import com.liren.lottery_system.common.exception.ServiceException;
import com.liren.lottery_system.common.enums.GlobalStatusEnum;
import com.liren.lottery_system.common.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@ResponseBody
@ControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler
    public Object handler(Exception e) {
        log.error("Exception异常：" + e);
        return Result.fail(GlobalStatusEnum.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
    }

    @ExceptionHandler
    public Object handler(ControllerException e) {
        log.error("ControllerException异常：" + e);
        return Result.fail(GlobalStatusEnum.INTERNAL_SERVER_ERROR.getCode(), e.getMsg());
    }

    @ExceptionHandler
    public Object handler(ServiceException e) {
        log.error("ServiceException异常：" + e);
        return Result.fail(GlobalStatusEnum.INTERNAL_SERVER_ERROR.getCode(), e.getMsg());
    }

    @ExceptionHandler
    public Object handler(IllegalArgumentException e) {
        log.error("IllegalArgumentException不合法参数异常：" + e);
        return Result.fail(GlobalStatusEnum.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
    }

    @ExceptionHandler
    public Object handler(NullPointerException e) {
        log.error("NullPointerException空指针异常：" + e);
        return Result.fail(GlobalStatusEnum.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
    }

    @ExceptionHandler
    public Object handler(NoResourceFoundException e) {
        log.error("NoResourceFoundException资源未找到异常：" + e);
        return Result.fail(GlobalStatusEnum.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
    }

}
