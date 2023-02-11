package com.acimage.common.web.exceptionhandler;


import com.acimage.common.result.Result;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.utils.ExceptionUtils;
import com.acimage.common.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;


@Slf4j
@RestControllerAdvice
public class ArgumentValidateExceptionHandler {
    /**
     * 单参数校验失败
     */
    @ExceptionHandler(value = {ConstraintViolationException.class})
    public Result doConstraintViolationException(ConstraintViolationException ex) {
        log.warn(ex.getMessage());
        //获取报错信息
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        StringBuilder message = new StringBuilder();

        int i = 0;
        for (ConstraintViolation<?> violation : violations) {
            message.append(violation.getMessage());
            i++;
            if (i < violations.size() - 1) {
                message.append(";");
            }
        }
        //记录日志
        logWarnMessage(ex, message.toString());
        return Result.fail(message.toString());
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public Result doMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ExceptionUtils.printIfDev(ex);
        //获取报错信息
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        StringBuilder logMessage = new StringBuilder();
        StringBuilder userMessage = new StringBuilder();

        for (int i = 0; i < fieldErrors.size(); i++) {
            FieldError fieldError = fieldErrors.get(i);
            logMessage.append(String.format("参数：%s 值：%s  错误：%s",
                    fieldError.getField(),
                    fieldError.getRejectedValue(),
                    fieldError.getDefaultMessage()));
            userMessage.append(fieldError.getDefaultMessage());
            if (i < fieldErrors.size() - 1) {
                logMessage.append(";");
                userMessage.append(";");
            }
        }
        //记录日志
        logWarnMessage(ex, logMessage.toString());
        return Result.fail(userMessage.toString());
    }

    @ExceptionHandler(value = {BindException.class})
    public Result doBindException(BindException ex) {
        ExceptionUtils.printIfDev(ex);
        //获取报错信息
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        StringBuilder logMessage = new StringBuilder();
        StringBuilder userMessage = new StringBuilder();

        for (int i = 0; i < fieldErrors.size(); i++) {
            FieldError fieldError = fieldErrors.get(i);
            logMessage.append(String.format("参数：%s 值：%s  错误：%s",
                    fieldError.getField(),
                    fieldError.getRejectedValue(),
                    fieldError.getDefaultMessage()));

            userMessage.append(fieldError.getDefaultMessage());

            if (i < fieldErrors.size() - 1) {
                logMessage.append(";");
                userMessage.append(";");
            }
        }
        //记录日志
        logWarnMessage(ex, logMessage.toString());
        return Result.fail(userMessage.toString());
    }

    private void logWarnMessage(Exception ex, String message) {
        String exceptionName = ex.getClass().getSimpleName();
        log.warn("用户：{} {} from {}", UserContext.getUsername(), message, exceptionName);
    }
}


