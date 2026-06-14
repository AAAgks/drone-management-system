package com.drone.common;

/**
 * 系统常量定义。
 * <p>
 * 集中管理系统中使用的魔法数字和固定字符串，避免硬编码。
 */
public final class Constants {

    /** 私有构造函数，防止实例化常量类 */
    private Constants() {}

    /** 无人机状态：正常 */
    public static final int STATUS_NORMAL = 1;
    /** 无人机状态：停用 */
    public static final int STATUS_DISABLED = 0;

    /** 数据来源：AI 自动生成 */
    public static final int AI_GENERATED = 1;
    /** 数据来源：手动录入 */
    public static final int AI_MANUAL = 0;

    /** 日期时间格式化模式 */
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
}
