# 超级石头剪刀布 - 前后端接口对接说明

## 📋 修改总结

已根据前端代码调整后端接口，确保前后端通信格式完全一致。

### ✅ 主要修改内容

#### 1. API响应格式统一

**修改前（旧格式）：**
```json
{
  "code": 200,
  "message": "success",
  "data": { ... }
}
```

**修改后（新格式）：**
```json
{
  "success": true,    // ← 新增：前端使用此字段判断成功/失败
  "code": 200,
  "message": "success",
  "data": { ... }
}
```

#### 2. 登录/注册响应格式调整

**修改前：**
```json
{
  "token": "xxx",
  "userInfo": { ... }  // ← 字段名不匹配
}
```

**修改后：**
```json
{
  "token": "xxx",
  "user": { ... }      // ← 改为user，与前端匹配
}
```

#### 3. 新增登出接口

- **URL**: `POST /api/auth/logout`
- **说明**: 前端需要此接口，虽然JWT无状态，但可用于记录日志
- **响应**: 返回成功消息

---

## 🔌 完整API列表

### 认证相关 API

| 接口 | 方法 | URL | 是否需要登录 | 说明 |
|------|------|-----|--------------|------|
| 用户注册 | POST | `/api/auth/register` | ❌ | 新用户注册 |
| 用户登录 | POST | `/api/auth/login` | ❌ | 用户登录获取token |
| 检查认证 | GET | `/api/auth/check` | ❌ | 验证token有效性 |
| 用户登出 | POST | `/api/auth/logout` | ❌ | 登出（前端清除token） |
| 获取用户信息 | GET | `/api/auth/me` | ✅ | 获取当前用户详细信息 |

### 游戏相关 API

| 接口 | 方法 | URL | 是否需要登录 | 说明 |
|------|------|-----|--------------|------|
| 获取AI出招 | POST | `/api/game/ai-move` | ❌ | 获取AI的出招 |
| 判断游戏结果 | POST | `/api/game/judge` | ❌ | 判断石头剪刀布结果 |
| 保存游戏记录 | POST | `/api/game/save` | ✅ | 保存游戏对局记录 |
| 获取游戏历史 | GET | `/api/game/history` | ✅ | 查询历史对局记录 |

---

## 📝 接口详细说明

### 1. 用户注册

**请求示例：**
```javascript
POST /api/auth/register
Content-Type: application/json

{
  "username": "testuser",
  "password": "123456",
  "email": "test@example.com"  // 可选
}
```

**响应示例：**
```json
{
  "success": true,
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "user": {
      "id": 1,
      "username": "testuser",
      "email": "test@example.com",
      "avatar": null,
      "stats": {
        "gamesPlayed": 0,
        "gamesWon": 0,
        "currentLevel": 1,
        "winRate": 0.0
      }
    }
  }
}
```

### 2. 用户登录

**请求示例：**
```javascript
POST /api/auth/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "123456"
}
```

**响应示例：** 同注册接口

### 3. 检查认证状态

**请求示例：**
```javascript
GET /api/auth/check
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

**响应示例：**
```json
{
  "success": true,
  "code": 200,
  "message": "success",
  "data": {
    "authenticated": true,
    "userInfo": {
      "id": 1,
      "username": "testuser",
      "email": "test@example.com",
      "stats": { ... }
    }
  }
}
```

### 4. 用户登出

**请求示例：**
```javascript
POST /api/auth/logout
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9... // 可选
```

**响应示例：**
```json
{
  "success": true,
  "code": 200,
  "message": "退出登录成功",
  "data": null
}
```

### 5. 保存游戏记录

**请求示例：**
```javascript
POST /api/game/save
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
Content-Type: application/json

{
  "gameType": "pvp",  // pvp=玩家对战, ai=人机, stage=闯关
  "level": null,      // 闯关模式才有关卡号
  "result": "win",    // win/lose/draw
  "moves": "{\"stage1\":[...],\"stage2\":[...],\"final\":{...}}"
}
```

**响应示例：**
```json
{
  "success": true,
  "code": 200,
  "message": "success",
  "data": null
}
```

### 6. 获取游戏历史

**请求示例：**
```javascript
GET /api/game/history?page=1&pageSize=10
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

**响应示例：**
```json
{
  "success": true,
  "code": 200,
  "message": "success",
  "data": {
    "list": [
      {
        "id": 1,
        "userId": 1,
        "gameType": "pvp",
        "level": null,
        "result": "win",
        "moves": "{\"stage1\":[...]}",
        "playedAt": "2025-10-27 12:00:00"
      }
    ],
    "total": 10,
    "page": 1,
    "pageSize": 10
  }
}
```

---

## 🔧 前端配置

前端项目运行在 **8081端口**，后端在 **8080端口**。

前端JavaScript中的API配置：

```javascript
// auth.js
const API_CONFIG = {
  baseURL: 'http://localhost:8080/api/auth',
  endpoints: {
    login: '/login',
    register: '/register',
    logout: '/logout',
    checkAuth: '/check'
  }
};

// game.js
const API_CONFIG = {
  baseURL: 'http://localhost:8080/api/game',
  endpoints: {
    save: '/save'
  }
};

// history.js
const API_CONFIG = {
  baseURL: 'http://localhost:8080/api/game',
  endpoints: {
    history: '/history'
  }
};
```

---

## 🚀 启动说明

### 1. 启动后端（端口8080）

```bash
cd "Back end"
mvn spring-boot:run
```

### 2. 启动前端（端口8081）

```bash
cd "Front end"
mvn spring-boot:run
```

### 3. 访问前端

打开浏览器访问：http://localhost:8081

---

## 📌 注意事项

1. **密码格式**：后端使用明文存储密码（测试环境）
2. **JWT Token**：有效期7天
3. **CORS配置**：后端已配置允许跨域访问
4. **响应格式**：所有接口统一使用 `{ success, code, message, data }` 格式
5. **认证方式**：使用 `Authorization: Bearer {token}` 请求头

---

## ✅ 已完成的对接工作

- [x] 统一API响应格式（添加success字段）
- [x] 修改认证响应格式（userInfo → user）
- [x] 新增登出接口 `/api/auth/logout`
- [x] 更新拦截器配置
- [x] 更新README文档
- [x] 编译测试通过

---

## 🔍 测试建议

### 使用curl测试

```bash
# 1. 注册用户
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"newuser","password":"123456"}'

# 2. 登录
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"123456"}'

# 3. 检查认证（需要token）
curl -X GET http://localhost:8080/api/auth/check \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"

# 4. 保存游戏记录（需要token）
curl -X POST http://localhost:8080/api/game/save \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{"gameType":"pvp","result":"win","moves":"{}"}'

# 5. 获取历史记录（需要token）
curl -X GET "http://localhost:8080/api/game/history?page=1&pageSize=10" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

生成时间：2025-10-27
版本：v1.0

