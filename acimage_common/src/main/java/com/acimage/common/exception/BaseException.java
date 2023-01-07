package com.acimage.common.exception;

import com.acimage.common.result.Result;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseException extends RuntimeException{
    Integer code;
    String msg;

    public Result asResult(){
        return new Result(code,null,msg);
    }
}
