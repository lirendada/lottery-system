package com.liren.lottery_system.common.exception;

import com.liren.lottery_system.model.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@EqualsAndHashCode(callSuper = true) // 在生成equals()和hashCode()时，把父类的字段也包含进去。
public class ControllerException extends RuntimeException {
    private Integer code;
    private String msg;

    ControllerException(ErrorCode errorCode) {
        code = errorCode.getCode();
        msg = errorCode.getErrMsg();
    }
}
