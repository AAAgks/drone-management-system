package com.drone.exception;

/**
 * 业务异常。
 * <p>
 * 当业务逻辑不满足条件时抛出（如记录不存在、状态不合法等），
 * 由 {@link GlobalExceptionHandler} 统一捕获并转换为 API 错误响应。
 */
public class BusinessException extends RuntimeException {

    /** 业务错误码（如 404 表示资源不存在） */
    private final int code;

    /** 默认 500 错误 */
    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    /** 自定义错误码 */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() { return code; }
}
