# 无人机信息管理系统

前后端分离架构的无人机信息管理平台。

## 技术栈

| 层级 | 技术 |
|------|------|
| 后端 | Java 1.8 + Spring Boot 2.2.13 + Maven |
| 前端 | Vue 3 + Vite + Bootstrap 3 + Vue Router |
| 数据库 | MySQL |

## 项目结构

```
├── src/                    # 后端源码（Spring Boot）
├── frontend/               # 前端源码（Vue 3 + Vite）
│   ├── src/                # Vue 组件
│   ├── dist/               # 前端构建产物
│   └── package.json
├── docs/                   # 项目文档
│   ├── init-mysql.sql      # 数据库初始化脚本
│   └── 04-api-docs/        # API 文档
├── start-backend.bat       # 后端启动脚本
├── start-frontend.bat      # 前端启动脚本
└── pom.xml                 # Maven 配置
```

## 快速开始

### 1. 初始化数据库

导入 `docs/init-mysql.sql` 到 MySQL。

### 2. 启动后端

```bash
mvn spring-boot:run
```

或双击 `start-backend.bat`

### 3. 启动前端

```bash
cd frontend
npm install
npm run dev
```

或双击 `start-frontend.bat`

## 功能

- 无人机信息录入与管理
- 数据可视化仪表盘
- 前后端分离 RESTful API
