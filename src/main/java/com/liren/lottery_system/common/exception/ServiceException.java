package com.liren.lottery_system.common.exception;

import com.liren.lottery_system.common.pojo.CodeStatus;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceException extends RuntimeException {
    private Integer code;
    private String msg;

    public ServiceException(CodeStatus errorCode) {
        code = errorCode.getCode();
        msg = errorCode.getMsg();
    }
}
