package com.acimage.common.web.exceptionhandler;


import com.acimage.common.result.Code;
import com.acimage.common.result.Result;
import com.acimage.common.global.exception.BusinessException;
import com.acimage.common.global.exception.NoLoginException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value={NoLoginException.class})
    public Result doNoLoginException(NoLoginException ex){
        return Result.fail("未登录，请重新登录");
    }

    @ExceptionHandler(value={BusinessException.class})
    public Result doBusinessException(BusinessException ex){
        return new Result(ex.getCode(),null,ex.getMsg());
    }

    @ExceptionHandler(value={MaxUploadSizeExceededException.class})
    public Result doMaxUploadSizeExceededException(MaxUploadSizeExceededException ex){
        ex.printStackTrace();
        return new Result(Code.ERR,null,"文件大小超出限制");
    }

    @ExceptionHandler(value={RuntimeException.class})
    public Result doRuntimeException(RuntimeException ex){
        ex.printStackTrace();
        return new Result(Code.ERR,null,"未知运行错误，请刷新重试");
    }

    @ExceptionHandler(value={Exception.class})
    public Result doException(Exception ex){
        ex.printStackTrace();
        return new Result(Code.ERR,null,"未知错误，请刷新重试");
    }
}
