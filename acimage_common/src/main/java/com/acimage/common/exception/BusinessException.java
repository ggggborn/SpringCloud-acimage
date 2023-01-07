package com.acimage.common.exception;

import com.acimage.common.result.Code;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BusinessException extends BaseException{

    public BusinessException(String msg){
        this.msg=msg;
        this.code= Code.ERR;
    }

    public BusinessException(Integer code, String msg) {
        super(code, msg);
    }
}
