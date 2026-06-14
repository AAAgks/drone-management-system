package com.drone.exception;

import com.drone.common.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理器。
 * <p>
 * 通过 {@code @RestControllerAdvice} 拦截所有 Controller 抛出的异常，
 * 统一转换为 {@link R} 格式的 JSON 错误响应，避免异常信息直接暴露给前端。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 业务异常处理。
     * <p>
     * 返回异常中携带的业务错误码和消息。
     */
    @ExceptionHandler(BusinessException.class)
    public R<Void> handleBusinessException(BusinessException e) {
        log.warn("业务异常：{}", e.getMessage());
        return R.fail(e.getCode(), e.getMessage());
    }

    /**
     * 参数校验异常处理（{@code @Valid} 注解触发）。
     * <p>
     * 收集所有字段校验失败的错误信息，以分号拼接返回。
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Void> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String errorMsg = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.warn("参数校验失败：{}", errorMsg);
        return R.fail(400, errorMsg);
    }

    /**
     * 参数绑定异常处理（GET 请求参数校验）。
     */
    @ExceptionHandler(BindException.class)
    public R<Void> handleBindException(BindException e) {
        String errorMsg = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.warn("参数绑定校验失败：{}", errorMsg);
        return R.fail(400, errorMsg);
    }

    /**
     * 兜底异常处理：捕获所有未被上述处理器匹配的异常。
     * <p>
     * 返回 500 状态码，日志中记录完整堆栈以便排查。
     */
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return R.fail("服务器内部错误：" + e.getMessage());
    }
}
