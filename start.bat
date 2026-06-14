@echo off
chcp 65001 >nul
cd /d "%~dp0"
echo ============================================
echo     无人机信息管理系统 - 启动中...
echo ============================================
echo.
echo [1/2] 检查前端构建产物...
if not exist "src\main\resources\static\index.html" (
    echo 前端未构建，正在构建中...
    cd frontend
    call npm install
    call npm run build
    cd ..
)
echo [2/2] 启动 Spring Boot 后端...
echo.
echo 访问地址: http://localhost:8080
echo 默认账号: admin / admin123
echo.
echo ============================================
call mvn spring-boot:run
pause
