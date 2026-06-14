package com.drone;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * 数据库初始化工具（独立运行，非测试用例）。
 *
 * <p>用法：在 IDE 中直接运行 main() 方法，或通过命令行：
 * <pre>mvn exec:java -Dexec.mainClass="com.drone.InitDb"</pre>
 *
 * <p>前提：MySQL 已启动且 {@code root/root} 可连接。
 */
public class InitDb {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/"
                + "?useUnicode=true&characterEncoding=utf8"
                + "&serverTimezone=Asia/Shanghai"
                + "&allowPublicKeyRetrieval=true&useSSL=false";
        String user = "root";
        String pass = "root";

        try {
            // MySQL 8.x 驱动自动注册，无需显式 Class.forName
            try (Connection conn = DriverManager.getConnection(url, user, pass);
                 Statement stmt = conn.createStatement()) {

                String sqlFile = "docs/init-mysql.sql";
                String content = new String(Files.readAllBytes(Paths.get(sqlFile)), "UTF-8");
                String[] queries = content.split(";");

                for (String q : queries) {
                    String trimmed = q.trim();
                    if (trimmed.isEmpty()) continue;
                    System.out.println("Executing: " + trimmed.substring(0, Math.min(60, trimmed.length())) + "...");
                    stmt.execute(trimmed);
                }
                System.out.println("\nDatabase initialized successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
