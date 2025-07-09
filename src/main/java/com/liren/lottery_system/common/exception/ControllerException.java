package com.liren.lottery_system.common.exception;

import com.liren.lottery_system.common.pojo.CodeStatus;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ControllerException extends RuntimeException {
    private Integer code;
    private String msg;

    public ControllerException(CodeStatus errorCode) {
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }
}
