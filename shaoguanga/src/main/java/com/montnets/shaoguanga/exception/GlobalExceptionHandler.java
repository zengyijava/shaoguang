package com.montnets.shaoguanga.exception;

import com.montnets.shaoguanga.bean.RespBody;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author WJH
 * @Description
 * @date 2021/4/7 15:37
 * @Email ibytecode2020@gmail.com
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public RespBody handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        RespBody respBody = new RespBody();
        respBody.setSuccess(false);
        respBody.setCount(-1);
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String collect = fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
        respBody.setContent(collect);
        return respBody;
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public RespBody handlerException(Exception e) {
        RespBody respBody = new RespBody();
        respBody.setSuccess(false);
        respBody.setCount(-1);
        respBody.setContent("服务器内部错误" + e.getMessage());
        return respBody;
    }

}
