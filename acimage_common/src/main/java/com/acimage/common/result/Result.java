package com.acimage.common.result;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result <T> {
    private Integer code;
    private T data;
    private String msg;
    public static Result fail(String message){
        return new Result(Code.ERR,null,message);
    }
    public static Result ok(){
        return new Result(Code.OK,null,"");
    }
    public static <T> Result<T> ok(T data){
        return new Result(Code.OK,data,"");
    }
    public static boolean isOk(Result<?> result){
        return Code.OK.equals(result.getCode());
    }

    public Boolean isOk(){
        return Code.OK.equals(code);
    }
}
