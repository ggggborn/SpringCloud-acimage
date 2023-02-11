package com.acimage.common.web.exceptionhandler;



import com.acimage.common.result.Result;
import com.acimage.common.global.exception.BusinessException;
import com.acimage.common.utils.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {



    @ExceptionHandler(value={BusinessException.class})
    public Result<?> doBusinessException(BusinessException ex){
        return Result.fail(ex.getMsg());
    }

    @ExceptionHandler(value={MaxUploadSizeExceededException.class})
    public Result<?> doMaxUploadSizeExceededException(MaxUploadSizeExceededException ex){
        ExceptionUtils.printIfDev(ex);
        log.error(ex.getMessage());
        return Result.fail("文件大小超出限制");
    }

    @ExceptionHandler(value={RuntimeException.class})
    public Result<?> doRuntimeException(RuntimeException ex){
        ExceptionUtils.printIfDev(ex);
        log.error(ex.getMessage());
        return Result.fail("未知错误，请刷新重试");
    }

    @ExceptionHandler(value={Exception.class})
    public Result<?> doException(Exception ex){
        ExceptionUtils.printIfDev(ex);
        log.error(ex.getMessage());
        return Result.fail("未知异常，请刷新重试");
    }
}
