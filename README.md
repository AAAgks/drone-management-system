# 🛸 无人机信息管理系统

<p align="center">
  <img src="https://img.shields.io/badge/Java-1.8-007396?logo=openjdk&logoColor=white" alt="Java">
  <img src="https://img.shields.io/badge/Spring%20Boot-2.2.13-6DB33F?logo=springboot&logoColor=white" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Vue-3.4-4FC08D?logo=vuedotjs&logoColor=white" alt="Vue 3">
  <img src="https://img.shields.io/badge/Vite-5.0-646CFF?logo=vite&logoColor=white" alt="Vite">
  <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql&logoColor=white" alt="MySQL">
  <img src="https://img.shields.io/badge/MyBatis-2.1-FF6600" alt="MyBatis">
  <img src="https://img.shields.io/badge/Shiro-1.7-929292?logo=apache&logoColor=white" alt="Shiro">
  <img src="https://img.shields.io/badge/license-MIT-green" alt="License">
</p>

<p align="center">
  前后端分离架构的无人机信息管理平台，提供完整的 CRUD、智能属性生成、权限认证等功能。
</p>

---

## 📋 目录

- [功能特性](#-功能特性)
- [技术栈](#-技术栈)
- [项目结构](#-项目结构)
- [数据库设计](#-数据库设计)
- [API 概览](#-api-概览)
- [快速开始](#-快速开始)
- [认证机制](#-认证机制)
- [AI 智能生成](#-ai-智能生成)

---

## ✨ 功能特性

### 🚁 无人机管理

- **分页查询** — 支持按名称、型号、状态多维筛选 + 页码/每页条数动态调整
- **新增录入** — 覆盖 **21 个技术指标**（名称、型号、序列号、制造商、类型、重量、速度、高度、续航、航程、载重、相机、电池、GPS、工作温度、防水等级等）
- **编辑修改** — 差异更新策略，仅覆盖非空字段
- **逻辑删除** — 软删除设计，数据可恢复
- **详情查看** — 单条无人机完整信息展示

### 🤖 AI 智能生成

- 输入**名称 + 描述**，自动推断无人机等级（消费级 / 工业级 / 军用级）
- 在合理数值范围内随机生成各项技术参数
- 生成结果可预览后再确认保存，降低录入成本

### 🔐 安全认证

- 基于 **Apache Shiro** 的 Session 认证
- 登录/注销/当前用户接口
- 路由守卫拦截未认证请求

### 🎨 前端体验

- **Vue 3 Composition API** 编写，逻辑复用度高
- **Hash 路由**，部署无配置负担
- **懒加载** 页面组件，首屏更快
- 响应式布局，适配不同屏幕

---

## 🛠 技术栈

### 后端

| 技术 | 版本 | 用途 |
|------|------|------|
| Java | 1.8 | 运行环境 |
| Spring Boot | 2.2.13 | Web 框架 |
| MyBatis | 2.1.4 | ORM / 数据访问 |
| PageHelper | 1.3.0 | 物理分页 |
| Apache Shiro | 1.7.1 | 认证授权 |
| Alibaba Druid | 1.2.6 | 数据库连接池 |
| Hibernate Validator | 6.0.21 | 参数校验 |
| MySQL | 8.0 | 主数据库 |
| SQLite | 3.36 | 轻量备选数据库 |
| Lombok | — | 减少样板代码 |
| H2 | — | 测试数据库 |

### 前端

| 技术 | 版本 | 用途 |
|------|------|------|
| Vue | 3.4 | 前端框架 |
| Vue Router | 4.6 | 路由管理 |
| Vite | 5.0 | 构建工具 |
| Axios | 1.17 | HTTP 请求 |
| Bootstrap | 3.3 | UI 样式 |
| jQuery | 3.3 | DOM 辅助 |

---

## 📁 项目结构

```
drone-management-system/
│
├── src/                               # ☕ 后端源码 (Spring Boot)
│   ├── main/
│   │   ├── java/com/drone/
│   │   │   ├── DroneApplication.java          # 启动入口
│   │   │   ├── common/                        # 通用类
│   │   │   │   ├── R.java                     #   统一响应体 { code, msg, data }
│   │   │   │   ├── PageResult.java            #   分页结果封装
│   │   │   │   └── Constants.java             #   常量定义
│   │   │   ├── config/                        # 配置类
│   │   │   │   ├── MyBatisConfig.java         #   MyBatis 配置
│   │   │   │   ├── ShiroConfig.java           #   Shiro 认证配置
│   │   │   │   └── WebMvcConfig.java          #   CORS / 拦截器
│   │   │   ├── controller/                    # 控制器
│   │   │   │   ├── DroneController.java       #   无人机 CRUD API
│   │   │   │   └── LoginController.java       #   登录认证 API
│   │   │   ├── domain/                        # 领域模型
│   │   │   │   ├── entity/Drone.java          #   实体类（映射 drone 表）
│   │   │   │   ├── dto/                       #   DTO 传输对象
│   │   │   │   └── query/DroneQuery.java      #   查询条件封装
│   │   │   ├── mapper/DroneMapper.java        #   MyBatis Mapper 接口
│   │   │   ├── service/                       # 业务层
│   │   │   │   ├── DroneService.java          #   无人机业务接口
│   │   │   │   ├── AiAttributeService.java    #   AI 生成服务接口
│   │   │   │   ├── LoginService.java          #   登录服务接口
│   │   │   │   └── impl/                      #   服务实现
│   │   │   ├── interceptor/                   # 拦截器
│   │   │   │   ├── AuthInterceptor.java       #   认证拦截器
│   │   │   │   └── LogInterceptor.java        #   日志拦截器
│   │   │   └── exception/                     # 异常处理
│   │   │       ├── BusinessException.java     #   业务异常
│   │   │       └── GlobalExceptionHandler.java #  全局异常处理器
│   │   └── resources/
│   │       ├── application.yml                # 主配置文件
│   │       ├── application-mysql.yml          # MySQL 环境配置
│   │       ├── application-sqlite.yml         # SQLite 环境配置
│   │       └── mapper/                        # MyBatis XML 映射文件
│   └── test/                                  # 测试代码
│
├── frontend/                          # 🖥️ 前端源码 (Vue 3 + Vite)
│   ├── src/
│   │   ├── main.js                            # 应用入口
│   │   ├── App.vue                            # 根组件
│   │   ├── pages/                             # 页面组件
│   │   │   ├── LoginPage.vue                  #   登录页
│   │   │   └── DashboardPage.vue              #   仪表盘页（主界面）
│   │   ├── components/                        # 业务组件
│   │   │   ├── Navbar.vue                     #   导航栏
│   │   │   ├── LoginForm.vue                  #   登录表单
│   │   │   ├── DroneTable.vue                 #   无人机数据表格
│   │   │   ├── DroneForm.vue                  #   新增/编辑表单
│   │   │   ├── DroneDetail.vue                #   详情面板
│   │   │   └── SearchBar.vue                  #   搜索栏
│   │   ├── api/                               # API 请求封装
│   │   │   ├── request.js                     #   Axios 实例（统一拦截）
│   │   │   ├── auth.js                        #   认证接口
│   │   │   └── drones.js                      #   无人机接口
│   │   ├── composables/                       # 组合式函数 (Hooks)
│   │   │   ├── useAuth.js                     #   认证状态管理
│   │   │   └── useDrones.js                   #   无人机数据管理
│   │   └── router/                            # 路由
│   │       ├── index.js                       #   路由表（懒加载）
│   │       └── guards.js                      #   导航守卫
│   ├── dist/                                  # 构建产物
│   ├── vite.config.js                         # Vite 配置（含代理）
│   └── package.json
│
├── docs/                              # 📄 项目文档
│   ├── init-mysql.sql                         # 数据库初始化脚本（含测试数据）
│   └── 04-api-docs/
│       └── drone-api.md                       # API 接口文档
│
├── harness_stu_Anti/                  # 🧪 测试工具集
├── start-backend.bat                  # 后端一键启动
├── start-frontend.bat                 # 前端一键启动
├── pom.xml                            # Maven 构建配置
└── reasonix.toml                      # 项目分析配置
```

---

## 🗄️ 数据库设计

### drone 表（无人机信息表）

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT PK | 主键自增 |
| `name` | VARCHAR(100) | 无人机名称 |
| `model` | VARCHAR(100) | 型号 |
| `serial_number` | VARCHAR(100) | 序列号（唯一索引） |
| `manufacturer` | VARCHAR(100) | 制造商 |
| `type` | VARCHAR(50) | 类型：多旋翼 / 固定翼 / 直升机 / 垂直起降 |
| `weight` | DOUBLE | 重量（kg） |
| `max_speed` | DOUBLE | 最大速度（km/h） |
| `max_altitude` | DOUBLE | 最大高度（m） |
| `endurance` | DOUBLE | 续航（min） |
| `range_distance` | DOUBLE | 最大航程（km） |
| `max_payload` | DOUBLE | 最大载重（kg） |
| `camera_type` | VARCHAR(200) | 相机类型 |
| `battery_capacity` | INT | 电池容量（mAh） |
| `gps_accuracy` | DOUBLE | GPS 精度（cm） |
| `operating_temperature` | VARCHAR(50) | 工作温度范围 |
| `waterproof_level` | VARCHAR(20) | 防水等级 |
| `status` | TINYINT | 状态：1=正常，0=停用 |
| `description` | VARCHAR(500) | 描述 |
| `purchase_date` | DATETIME | 购入日期 |
| `ai_generated` | TINYINT | 是否 AI 生成 |
| `deleted` | TINYINT | 逻辑删除：0=正常，1=已删除 |

内置了 5 条真实无人机测试数据（大疆 Mavic 3、Matrice 300 RTK、极飞 P100 Pro、翼龙-2、道通 EVO Lite+）。

---

## 📡 API 概览

> 详见 [docs/04-api-docs/drone-api.md](docs/04-api-docs/drone-api.md)

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | `/api/login` | 用户登录 | ❌ |
| POST | `/api/logout` | 退出登录 | ❌ |
| GET | `/api/current-user` | 获取当前用户 | ❌ |
| GET | `/api/drones` | 分页查询无人机列表 | ✅ |
| GET | `/api/drones/{id}` | 查询无人机详情 | ✅ |
| POST | `/api/drones` | 新增无人机 | ✅ |
| PUT | `/api/drones/{id}` | 修改无人机 | ✅ |
| DELETE | `/api/drones/{id}` | 逻辑删除无人机 | ✅ |
| POST | `/api/drones/ai-generate` | AI 智能生成属性 | ✅ |

### 统一响应格式

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": { ... }
}
```

| code | 含义 |
|------|------|
| 200 | 操作成功 |
| 401 | 未登录 / 登录失败 |
| 404 | 资源不存在 |
| 500 | 服务端异常 |

---

## 🚀 快速开始

### 环境要求

- **JDK** 1.8+
- **Maven** 3.6+
- **MySQL** 5.7+ / 8.0+
- **Node.js** 18+

### 1. 克隆项目

```bash
git clone https://github.com/AAAgks/drone-management-system.git
cd drone-management-system
```

### 2. 初始化数据库

```bash
mysql -u root -p < docs/init-mysql.sql
```

脚本会自动创建 `drone_db` 库、`drone` 表，并插入 5 条示例数据。

### 3. 配置数据库连接

编辑 `src/main/resources/application-mysql.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/drone_db?useSSL=false&serverTimezone=UTC&characterEncoding=utf-8
    username: root
    password: 你的密码
```

> 💡 如果不想装 MySQL，也可使用 SQLite 模式：修改 `application.yml` 中 `spring.profiles.active: sqlite`。

### 4. 启动后端

```bash
mvn spring-boot:run
```

或双击 `start-backend.bat`

后端启动后访问：**http://localhost:8080**

### 5. 启动前端

```bash
cd frontend
npm install
npm run dev
```

或双击 `start-frontend.bat`

前端启动后访问：**http://localhost:3000**

### 6. 登录

默认账号：`admin` / `123456`

---

## 🔐 认证机制

```
Browser                           Server
  │                                 │
  │  POST /api/login                │
  │  { username, password }         │
  │ ─────────────────────────────>  │  Shiro 验证
  │                                 │  └─ 创建 Session
  │  Set-Cookie: JSESSIONID=xxx     │
  │ <─────────────────────────────  │
  │                                 │
  │  GET /api/drones                │
  │  Cookie: JSESSIONID=xxx         │
  │ ─────────────────────────────>  │  AuthInterceptor
  │                                 │  └─ 校验 Session
  │  200 { data: [...] }            │
  │ <─────────────────────────────  │
```

- 基于 **Apache Shiro** Session 机制
- 登录成功后 Cookie 自动携带 Session ID
- 后续请求无需手动传 Token
- `/api/login`、`/api/logout`、`/api/current-user` 为公开接口
- 其余 `/api/**` 接口由 `AuthInterceptor` 拦截校验

---

## 🤖 AI 智能生成

这是本项目的一个特色功能：输入无人机名称和描述，自动推断等级并生成合理的技术参数。

### 工作流程

```
用户输入 name + description
         │
         ▼
  ┌─────────────────┐
  │  关键词匹配分级   │  "军用/侦察/打击" → 军用级
  │                  │  "工业/植保/测绘" → 工业级
  │                  │  "消费/航拍/便携" → 消费级
  └────────┬────────┘
           ▼
  ┌─────────────────┐
  │  按等级随机生成   │  各等级有独立的数值范围
  │  · 消费级：轻量  │  例：军用级 重量 500~15000kg
  │  · 工业级：中量  │       消费级 重量 0.1~2kg
  │  · 军用级：重量  │
  └────────┬────────┘
           ▼
    返回 DroneDTO
    (id=null, 需用户确认后保存)
```

### 调用示例

```json
POST /api/drones/ai-generate
{
  "name": "军用侦察无人机 X-100",
  "description": "用于战场侦察和目标跟踪的长航时无人机"
}
```

返回完整的无人机属性，包括自动生成的重量、速度、续航、载重、GPS 精度等参数。

---

## 📝 License

MIT © AAAgks
