package com.tim.gulimall.product.exception;

import com.tim.common.exception.BizCodeEnume;
import com.tim.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tim
 * @date 2022/7/21 14:12
 **/
@Slf4j
@RestControllerAdvice(basePackages = "com.tim.gulimall.product.controller")
public class GulimallExceptionControllerAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e) {
        log.error("数据校验异常{}, 异常类型{}", e.getMessage(), e.getClass());
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> erroMap = new HashMap<>();
        bindingResult.getFieldErrors().forEach(item -> {
            //获取错误提示
            String message = item.getDefaultMessage();
            //获取错误属性
            String field = item.getField();
            erroMap.put(field, message);
        });
        return R.error(BizCodeEnume.VAILD_EXCEPTION.getCode(), BizCodeEnume.VAILD_EXCEPTION.getMsg()).put("data", erroMap);
    }

    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable throwable) {
        return R.error();
    }
}
