# 🛸 无人机信息管理系统（不分离版）

<p align="center">
  <img src="https://img.shields.io/badge/Java-1.8-007396?logo=openjdk&logoColor=white" alt="Java">
  <img src="https://img.shields.io/badge/Spring%20Boot-2.2.13-6DB33F?logo=springboot&logoColor=white" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Vue-3.4-4FC08D?logo=vuedotjs&logoColor=white" alt="Vue 3">
  <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql&logoColor=white" alt="MySQL">
  <img src="https://img.shields.io/badge/MyBatis-2.1-FF6600" alt="MyBatis">
  <img src="https://img.shields.io/badge/Shiro-1.7-929292?logo=apache&logoColor=white" alt="Shiro">
  <img src="https://img.shields.io/badge/license-MIT-green" alt="License">
  <img src="https://img.shields.io/badge/架构-不分离-orange" alt="Monolith">
</p>

<p align="center">
  前后端不分离的单体应用 — Vue 前端构建为静态资源，由 Spring Boot 直接托管，<strong>一键启动</strong>无需分别部署。
</p>

---

## 📋 目录

- [与分离版的区别](#-与分离版的区别)
- [功能特性](#-功能特性)
- [技术栈](#-技术栈)
- [项目结构](#-项目结构)
- [快速开始](#-快速开始)
- [配置说明](#-配置说明)
- [API 概览](#-api-概览)
- [开发指南](#-开发指南)

---

## 🔀 与分离版的区别

| 维度 | 不分离版（本分支） | 分离版（main 分支） |
|------|-------------------|---------------------|
| **架构** | 单体应用，前端打包进后端 | 前后端独立部署 |
| **启动方式** | `start.bat` 一键启动 | 分别启动后端 + 前端 |
| **端口** | 仅 8080 | 后端 8080 + 前端 3000 |
| **前端开发** | 需单独启动 Vite 开发服务器 | 自带 Vite HMR 热更新 |
| **部署** | 一个 jar 包搞定 | 需 Nginx / 分别部署 |
| **适用场景** | 快速演示、小型部署 | 团队协作、大型生产环境 |

---

## ✨ 功能特性

### 🚁 无人机管理

- **分页查询** — 按名称、型号、状态多维筛选，支持页码/每页条数动态调整
- **新增录入** — 覆盖 21 个技术指标（名称、型号、序列号、制造商、类型、重量、速度、高度、续航、航程、载重、相机、电池、GPS、工作温度、防水等级等）
- **编辑修改** — 差异更新策略，仅覆盖非空字段
- **逻辑删除** — 软删除设计，数据可恢复
- **详情查看** — 单条无人机完整信息展示

### 🤖 AI 智能生成

- 输入名称 + 描述，自动推断无人机等级（消费级 / 工业级 / 军用级）
- 在合理数值范围内随机生成各项技术参数
- 生成结果可预览后确认保存

### 🔐 安全认证

- 基于 Apache Shiro 的 Session 认证
- 路由守卫拦截未认证请求
- 前后端同源，无跨域问题

### 🏠 一键部署

- Vue 前端构建为静态资源，由 Spring Boot 直接托管
- 双击 `start.bat` 即可完成 MySQL 启动 + 应用启动
- 无需安装 Node.js（已内建前端构建产物）

---

## 🛠 技术栈

| 层级 | 技术 | 版本 | 用途 |
|------|------|------|------|
| **后端框架** | Spring Boot | 2.2.13 | Web 框架 |
| **ORM** | MyBatis + PageHelper | 2.1.4 / 1.3.0 | 数据访问 / 分页 |
| **安全认证** | Apache Shiro | 1.7.1 | 认证授权 |
| **连接池** | Alibaba Druid | 1.2.6 | 数据库连接池 |
| **参数校验** | Hibernate Validator | 6.0.21 | 请求校验 |
| **数据库** | MySQL 8.0 / SQLite 3 | — | 双数据库支持 |
| **前端框架** | Vue 3 + Vue Router | 3.4+ / 4.6+ | SPA 框架 |
| **UI** | Bootstrap 3 + jQuery | 3.3 | 样式与 DOM 操作 |
| **HTTP** | Axios | 1.17 | API 请求 |
| **构建** | Vite 5 + Maven | — | 前后端构建 |
| **运行环境** | Java 1.8 | — | JDK |

---

## 📁 项目结构

```
drone-management-system/
├── src/main/java/com/drone/         # ☕ 后端 Java 源码
│   ├── DroneApplication.java        #   启动入口
│   ├── common/                      #   通用类（R 响应、PageResult、Constants）
│   ├── config/                      #   配置类（MyBatis、Shiro、WebMvc）
│   ├── controller/                  #   REST 控制器（Drone、Login）
│   ├── domain/                      #   领域模型（entity/dto/query）
│   ├── exception/                   #   异常处理
│   ├── interceptor/                 #   拦截器（认证、日志）
│   ├── mapper/                      #   MyBatis Mapper 接口
│   └── service/                     #   业务服务（含 AI 属性生成）
├── src/main/resources/
│   ├── static/                      # 🔴 前端构建产物（Spring Boot 直接托管）
│   ├── mapper/                      #   MyBatis XML 映射文件
│   ├── application.yml              #   主配置文件
│   ├── application-mysql.yml        #   MySQL 数据源
│   ├── application-sqlite.yml       #   SQLite 数据源
│   ├── schema.sql                   #   MySQL 建表脚本
│   ├── schema-sqlite.sql            #   SQLite 建表脚本
│   └── shiro-users.ini              #   Shiro 用户配置
├── frontend/                        # 🖥️ Vue 3 前端源码（开发用）
│   ├── src/
│   │   ├── api/                     #   API 请求封装
│   │   ├── components/              #   Vue 组件
│   │   ├── composables/             #   组合式函数
│   │   ├── pages/                   #   路由页面
│   │   └── router/                  #   路由配置与守卫
│   ├── vite.config.js               #   Vite 构建配置
│   └── package.json
├── docs/
│   └── init-mysql.sql               # 数据库初始化脚本（含测试数据）
├── start.bat                        # 🔥 Windows 一键启动脚本
├── pom.xml                          # Maven 构建配置
└── .gitignore
```

---

## 🚀 快速开始

### 环境要求

- **JDK 1.8** — 项目已配置为使用 `D:\jdk8u492-b09`（不影响全局 JDK）
- **Maven 3.6+**
- **MySQL 8.0+**（推荐）或 SQLite 3

### 1. 克隆项目

```bash
# 克隆仓库后切换到不分离版分支
git clone https://github.com/AAAgks/Drone-Management-System.git
cd Drone-Management-System
git checkout combined
```

### 2. 初始化数据库（二选一）

#### 方案 A：MySQL（推荐）

```bash
mysql -u root -p < docs/init-mysql.sql
```

默认连接：`localhost:3306/drone_db`，用户名/密码可通过环境变量覆盖：
- `MYSQL_USERNAME`（默认 `xiaogan`）
- `MYSQL_PASSWORD`

#### 方案 B：SQLite（免安装）

无需额外操作。修改 `src/main/resources/application.yml`：

```yaml
spring:
  profiles:
    active: sqlite
```

### 3. 启动应用

双击 **`start.bat`** 一键启动，脚本会自动：

1. 启动 MySQL 服务（MySQL96）
2. 检查前端构建产物（如缺失则自动构建）
3. 以 JDK 8 启动 Spring Boot

或使用 Maven 命令：

```bash
mvn spring-boot:run
```

### 4. 访问系统

打开浏览器访问：**http://localhost:8080**

| 项目 | 值 |
|------|-----|
| 默认用户名 | `admin` |
| 默认密码 | `admin123` |
| 端口 | `8080` |

---

## 🔧 配置说明

### 切换数据库

编辑 `src/main/resources/application.yml`，修改 `spring.profiles.active` 为 `mysql` 或 `sqlite`。

### 修改用户密码

编辑 `src/main/resources/shiro-users.ini`：

```ini
[users]
admin = 你的新密码, admin
```

### 修改端口

编辑 `src/main/resources/application.yml`：

```yaml
server:
  port: 8080
```

---

## 📡 API 概览

所有接口返回统一格式：`{ "code": 200, "msg": "操作成功", "data": {...} }`

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | `/api/login` | 用户登录 | ❌ |
| POST | `/api/logout` | 退出登录 | ❌ |
| GET | `/api/current-user` | 获取当前用户 | ❌ |
| GET | `/api/drones` | 分页查询无人机 | ✅ |
| GET | `/api/drones/{id}` | 查询详情 | ✅ |
| POST | `/api/drones` | 新增无人机 | ✅ |
| PUT | `/api/drones/{id}` | 编辑无人机 | ✅ |
| DELETE | `/api/drones/{id}` | 逻辑删除 | ✅ |
| POST | `/api/drones/ai-generate` | AI 智能生成 | ✅ |

| code | 含义 |
|------|------|
| 200 | 操作成功 |
| 401 | 未登录 |
| 404 | 资源不存在 |
| 500 | 服务端异常 |

---

## 💻 开发指南

### 修改前端代码

如果需要修改前端并实时预览，使用 Vite 开发服务器：

```bash
# 终端 1：启动后端
mvn spring-boot:run

# 终端 2：启动前端开发服务器
cd frontend
npm install
npm run dev
```

前端开发服务器运行在 `http://localhost:3000`，API 请求自动代理到 `localhost:8080`。

### 重新构建前端

```bash
cd frontend
npm run build
```

构建产物输出到 `src/main/resources/static/`，重启后端即可。

### 打包部署

```bash
mvn clean package -DskipTests
java -jar target/drone-management-1.0.0.jar
```

---

## 📝 License

MIT © AAAgks
