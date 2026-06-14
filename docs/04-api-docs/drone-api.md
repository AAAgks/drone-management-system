# 无人机管理系统 API 文档

> 后端服务：`http://localhost:8080` | 前端代理：`http://localhost:3000/api`

---

## 目录

1. [认证 API](#1-认证-api)
2. [无人机 CRUD API](#2-无人机-crud-api)
3. [AI 智能生成 API](#3-ai-智能生成-api)
4. [通用响应格式](#通用响应格式)

---

## 通用响应格式

所有接口统一返回 JSON：

```json
{
  "code": 200,        // 200=成功, 401=未登录, 404=不存在, 500=服务端错误
  "msg": "操作成功",
  "data": { ... }
}
```

---

## 1. 认证 API

### 1.1 登录

```
POST /api/login
Content-Type: application/json

{ "username": "admin", "password": "123456" }
```

返回：
```json
{ "code": 200, "msg": "登录成功", "data": "登录成功" }
```

错误返回：
```json
{ "code": 401, "msg": "用户名或密码错误", "data": null }
```

### 1.2 注销

```
POST /api/logout
```

### 1.3 获取当前用户

```
GET /api/current-user
```

已登录时返回：
```json
{ "code": 200, "data": "admin" }
```

未登录时返回：
```json
{ "code": 401, "msg": "未登录", "data": null }
```

---

## 2. 无人机 CRUD API

### 2.1 分页列表

```
GET /api/drones?pageNum=1&pageSize=10&name=大疆&model=Mavic&status=1
```

参数说明：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| pageNum | int | 否 | 页码，默认 1 |
| pageSize | int | 否 | 每页条数，默认 10 |
| name | string | 否 | 名称模糊搜索 |
| model | string | 否 | 型号模糊搜索 |
| status | int | 否 | 状态：1=正常, 0=停用 |

返回：
```json
{
  "code": 200,
  "data": {
    "rows": [ { ... } ],
    "total": 100,
    "pages": 10,
    "pageNum": 1
  }
}
```

### 2.2 查询详情

```
GET /api/drones/{id}
```

### 2.3 新增

```
POST /api/drones
Content-Type: application/json

{
  "name": "大疆 Air 3",
  "model": "Air 3",
  "serialNumber": "UAV-202501-001",
  "manufacturer": "大疆创新",
  "type": "多旋翼",
  "weight": 0.72,
  "maxSpeed": 68.0,
  "maxAltitude": 5000.0,
  "endurance": 46.0,
  "rangeDistance": 32.0,
  "maxPayload": 0.35,
  "cameraType": "双摄 4K/60fps",
  "batteryCapacity": 4241,
  "gpsAccuracy": 1.5,
  "operatingTemperature": "-10℃~40℃",
  "waterproofLevel": "IP43",
  "status": "正常",
  "description": "消费级便携航拍无人机",
  "purchaseDate": "2025-01-15"
}
```

### 2.4 修改

```
PUT /api/drones/{id}
Content-Type: application/json
```

请求体同新增，但**所有字段均可选**，只覆盖非 null 字段。

### 2.5 删除

```
DELETE /api/drones/{id}
```

逻辑删除，将 `deleted` 字段设为 1。

---

## 3. AI 智能生成 API

```
POST /api/drones/ai-generate
Content-Type: application/json

{
  "name": "军用侦察无人机 X-100",
  "description": "用于战场侦察和目标跟踪的长航时无人机"
}
```

后端根据名称和描述中的关键词（军用/工业/消费级）自动生成合理的技术参数。

返回完整的 DroneDTO 对象（id 为 null，需用户确认后通过 POST 保存）。

---

## 4. 认证机制

- 基于 **Apache Shiro** Session 认证
- 登录成功后自动在 Cookie 中设置 session ID
- 后续请求自动携带 Cookie，无需 token
- /api/login、/api/logout、/api/current-user 为公开接口
- 其他 `/api/**` 接口均需登录
