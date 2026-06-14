package com.drone.config;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * MyBatis 配置。
 * <p>
 * 核心功能：通过 {@code databaseIdProvider} 根据数据库产品名（MySQL / SQLite）
 * 自动选择 Mapper XML 中对应的 SQL 语句，实现一套代码兼容两种数据库。
 * <p>
 * 例如在 DroneMapper.xml 中，同一个 {@code id="selectByCondition"} 方法
 * 可以分别定义 {@code databaseId="mysql"} 和 {@code databaseId="sqlite"} 两套 SQL。
 */
@Configuration
public class MyBatisConfig {

    /**
     * 数据库厂商标识提供器。
     * <p>
     * 根据 JDBC 连接的产品名将 "MySQL" 映射为 {@code mysql}，"SQLite" 映射为 {@code sqlite}，
     * 供 Mapper XML 中的 {@code databaseId} 属性使用。
     */
    @Bean
    public DatabaseIdProvider databaseIdProvider() {
        VendorDatabaseIdProvider provider = new VendorDatabaseIdProvider();
        Properties props = new Properties();
        props.setProperty("MySQL", "mysql");   // MySQL 产品名 → databaseId = "mysql"
        props.setProperty("SQLite", "sqlite"); // SQLite 产品名 → databaseId = "sqlite"
        provider.setProperties(props);
        return provider;
    }
}
