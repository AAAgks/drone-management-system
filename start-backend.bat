REM ================================================================
REM  后端启动脚本
REM  1. 切换到 UTF-8 代码页 (65001)，避免中文乱码
REM  2. 启动 MySQL 服务（如果未运行，静默忽略错误）
REM  3. 设置 JDK 8 环境变量
REM  4. 运行 Spring Boot（mvn spring-boot:run）
REM ================================================================
@echo off
chcp 65001 >nul
echo ========================================
echo   启动后端服务 (Spring Boot 8080)
echo ========================================
REM 尝试启动 MySQL 服务
net start MySQL96 2>nul
REM 使用 JDK 8 编译和运行
set JAVA_HOME=D:\jdk8u492-b09
set PATH=D:\jdk8u492-b09\bin;%PATH%
cd /d "%~dp0"
call mvn spring-boot:run
pause
