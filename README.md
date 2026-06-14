# 无人机信息管理系统

> Drone Information Management System — 前后端不分离的单体应用

一个基于 Spring Boot + Vue 3 的无人机信息管理平台，支持无人机信息的录入、查询、编辑、删除以及 AI 智能属性生成。前端构建为静态资源后由 Spring Boot 直接托管，**无需分别启动前后端**，一键运行。

## 技术栈

| 层级 | 技术 | 版本 |
|------|------|------|
| **后端框架** | Spring Boot | 2.2.13.RELEASE |
| **ORM** | MyBatis + PageHelper | 2.1.4 / 1.3.0 |
| **安全认证** | Apache Shiro | 1.7.1 |
| **数据库连接池** | Alibaba Druid | 1.2.6 |
| **参数校验** | Hibernate Validator | 6.0.21 |
| **数据库** | MySQL 8.0+（生产） / SQLite 3（开发） | — |
| **前端框架** | Vue 3 + Vue Router | 3.4+ / 4.6+ |
| **UI 框架** | Bootstrap 3 + jQuery | 3.3.7 / 3.3.1 |
| **HTTP 客户端** | Axios | 1.17+ |
| **构建工具** | Vite 5（前端） + Maven（后端） | — |
| **运行环境** | Java 1.8+ | — |

## 项目结构

```
Drone-Information-Management-System/
├── src/main/java/com/drone/         # 后端 Java 源码
│   ├── common/                       #   - 通用类（R 响应封装、PageResult、Constants）
│   ├── config/                       #   - 配置类（MyBatis、Shiro、WebMvc）
│   ├── controller/                   #   - REST 控制器
│   ├── domain/                       #   - 领域对象
│   │   ├── dto/                      #       - 数据传输对象
│   │   ├── entity/                   #       - 数据库实体
│   │   └── query/                    #       - 查询条件
│   ├── exception/                    #   - 异常处理（BusinessException、全局异常处理）
│   ├── interceptor/                  #   - 拦截器（认证、日志）
│   ├── mapper/                       #   - MyBatis Mapper 接口
│   └── service/                      #   - 业务服务（含 AI 属性生成）
├── src/main/resources/
│   ├── static/                       # 🔴 前端构建产物（Vite 打包后在此，Spring Boot 直接托管）
│   ├── mapper/                       # MyBatis XML 映射文件
│   ├── application.yml               # 主配置文件
│   ├── application-mysql.yml         # MySQL 数据源配置
│   ├── application-sqlite.yml        # SQLite 数据源配置
│   ├── schema.sql                    # MySQL 建表脚本（启动自动执行）
│   ├── schema-sqlite.sql             # SQLite 建表脚本
│   └── shiro-users.ini               # Shiro 用户配置
├── frontend/                         # 前端 Vue 3 源码（开发用，打包后无需此目录）
│   ├── src/
│   │   ├── api/                      #   - API 请求封装（auth、drones、request）
│   │   ├── components/               #   - Vue 组件（表格、表单、详情、搜索栏等）
│   │   ├── composables/              #   - 组合式函数（useAuth、useDrones）
│   │   ├── pages/                    #   - 路由页面（登录页、控制台页）
│   │   └── router/                   #   - 路由配置与守卫
│   ├── index.html                    #   - HTML 入口
│   ├── vite.config.js                #   - Vite 构建配置
│   └── package.json
├── docs/
│   └── init-mysql.sql                # 数据库初始化脚本（含测试数据）
├── pom.xml                           # Maven 配置
├── start.bat                         # Windows 一键启动脚本
└── .gitignore
```

## 快速开始

### 环境要求

- **JDK 1.8+**
- **Maven 3.6+**
- **MySQL 8.0+**（推荐）或 SQLite 3（无需安装）

### 1. 初始化数据库（二选一）

#### 方案 A：使用 MySQL（推荐）

```bash
# 导入数据库初始化脚本（含测试数据）
mysql -u root -p < docs/init-mysql.sql
```

默认连接配置（可在 `src/main/resources/application-mysql.yml` 中修改）：
- 数据库：`drone_db`
- 用户名：通过环境变量 `MYSQL_USERNAME` 或默认 `xiaogan`
- 密码：通过环境变量 `MYSQL_PASSWORD` 或默认值

#### 方案 B：使用 SQLite（免安装）

无需额外操作，应用启动后会自动在运行目录创建 `drone.db` 文件。

修改 `src/main/resources/application.yml` 激活 SQLite profile：
```yaml
spring:
  profiles:
    active: sqlite   # 改为 sqlite
```

### 2. 启动应用

#### 方式一：一键启动（Windows）

双击 `start.bat`，脚本会自动检查前端构建产物并启动应用。

#### 方式二：Maven 命令

```bash
# 在项目根目录执行
mvn spring-boot:run
```

#### 方式三：打包运行

```bash
# 打包
mvn clean package -DskipTests

# 运行
java -jar target/drone-management-1.0.0.jar
```

### 3. 访问系统

打开浏览器访问：**http://localhost:8080**

| 项目 | 值 |
|------|-----|
| **默认用户名** | `admin` |
| **默认密码** | `admin123` |
| **端口** | `8080` |

## 功能特性

- 🔐 **用户认证**：基于 Apache Shiro Session 的登录/登出，路由守卫拦截未登录访问
- 📋 **无人机管理**：信息录入、编辑、删除（逻辑删除）、详情查看
- 🔍 **条件搜索**：按名称、型号、状态多维筛选，分页展示
- 🤖 **AI 智能填充**：根据无人机名称和描述自动推断等级（消费级/工业级/军用级），
  在合理范围内随机生成重量、速度、航程、载重、相机等 16 项技术参数
- 📄 **双数据库支持**：MySQL（生产）和 SQLite（开发/单机），通过 MyBatis databaseId 自动切换 SQL 方言
- 🏠 **前后端同源部署**：Vue 前端构建为静态资源，由 Spring Boot 直接托管，无需 Nginx 或单独启动前端服务

## 开发指南

### 前后端分离开发模式

如果你需要修改前端代码并实时预览，可以使用 Vite 开发服务器：

```bash
# 终端 1：启动后端
mvn spring-boot:run

# 终端 2：启动前端开发服务器
cd frontend
npm install
npm run dev
```

前端开发服务器运行在 `http://localhost:3000`，API 请求自动代理到后端 `localhost:8080`。

### 重新构建前端

修改前端代码后，重新打包到 `src/main/resources/static/`：

```bash
cd frontend
npm run build
```

构建产物会自动输出到后端静态资源目录，之后重启后端即可看到更新。

## 配置说明

### 切换数据库

编辑 `src/main/resources/application.yml`：

```yaml
spring:
  profiles:
    active: mysql    # 使用 MySQL
    # active: sqlite # 使用 SQLite
```

### 修改用户密码

编辑 `src/main/resources/shiro-users.ini`：

```ini
[users]
admin = 你的新密码, admin
```

### 修改服务端口

编辑 `src/main/resources/application.yml`：

```yaml
server:
  port: 8080    # 改为你需要的端口
```

## API 文档

所有 API 返回统一格式：

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": { ... }
}
```

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | `/api/login` | 用户登录 | 否 |
| POST | `/api/logout` | 用户登出 | 否 |
| GET | `/api/current-user` | 获取当前用户 | 否 |
| GET | `/api/drones` | 分页查询（支持 name/model/status） | 是 |
| GET | `/api/drones/{id}` | 查询详情 | 是 |
| POST | `/api/drones` | 新增无人机 | 是 |
| PUT | `/api/drones/{id}` | 编辑无人机 | 是 |
| DELETE | `/api/drones/{id}` | 逻辑删除 | 是 |
| POST | `/api/drones/ai-generate` | AI 智能生成属性 | 是 |

详细的 API 文档请参考 [docs/04-api-docs/](docs/04-api-docs/)。

## License

MIT
