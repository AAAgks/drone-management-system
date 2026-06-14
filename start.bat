@echo off
chcp 65001 >nul
cd /d "%~dp0"

REM 设置项目级 JDK 8（不影响全局环境）
set JAVA_HOME=D:\jdk8u492-b09
set PATH=D:\jdk8u492-b09\bin;%PATH%

echo ============================================
echo     无人机信息管理系统 - 启动中...
echo ============================================
echo.
echo [0/3] 启动 MySQL 服务...
net start MySQL96 2>nul
echo.
echo [1/3] 检查前端构建产物...
if not exist "src\main\resources\static\index.html" (
    echo 前端未构建，正在构建中...
    cd frontend
    call npm install
    call npm run build
    cd ..
)
echo [2/3] 启动 Spring Boot 后端 (JDK 8)...
echo.
echo 访问地址: http://localhost:8080
echo 默认账号: admin / admin123
echo.
echo ============================================
call mvn spring-boot:run
pause
