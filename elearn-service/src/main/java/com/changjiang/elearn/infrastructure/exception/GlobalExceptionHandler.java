package com.changjiang.elearn.infrastructure.exception;


import com.changjiang.elearn.api.dto.CommonRespDataDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public CommonRespDataDto handleBusinessException(BusinessException e) {
        log.error("业务异常", e);
        CommonRespDataDto response = new CommonRespDataDto();
        response.setCode("1");
        response.setMessage(e.getMessage());
        return response;
    }

    @ExceptionHandler(Exception.class)
    public CommonRespDataDto handleException(Exception e) {
        log.error("系统异常", e);
        CommonRespDataDto response = new CommonRespDataDto();
        response.setCode("500");
        response.setMessage("系统异常");
        return response;
    }
} 