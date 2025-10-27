# 超级石头剪刀布游戏 - 后端API

这是一个基于SpringBoot + MyBatis + MySQL的后端API项目，提供石头剪刀布游戏的完整后端服务。

## 技术栈

- **Spring Boot 2.7.14** - Web框架
- **MyBatis 2.3.1** - 持久层框架
- **MySQL 8.0** - 数据库
- **JWT (jjwt 0.11.5)** - 身份认证
- **Lombok** - 代码简化
- **Maven** - 项目构建工具

## 项目结构

```
src/main/java/com/example/superrps/
├── SuperRpsApplication.java          # 主应用入口
├── config/                            # 配置类
│   └── WebConfig.java                 # Web配置（CORS、拦截器）
├── controller/                        # 控制器层
│   ├── AuthController.java            # 认证相关API
│   └── GameController.java            # 游戏相关API
├── dao/                               # 数据访问层
│   ├── UserMapper.java
│   ├── UserStatsMapper.java
│   └── GameHistoryMapper.java
├── dto/                               # 数据传输对象
│   ├── ApiResponse.java               # 统一响应格式
│   ├── LoginRequest.java
│   ├── RegisterRequest.java
│   ├── AuthResponse.java
│   ├── AuthCheckResponse.java
│   └── UserInfoDTO.java
├── entity/                            # 实体类
│   ├── User.java                      # 用户实体
│   ├── UserStats.java                 # 用户统计实体
│   └── GameHistory.java               # 游戏历史实体
├── interceptor/                       # 拦截器
│   └── AuthInterceptor.java           # JWT认证拦截器
├── service/                           # 业务逻辑层
│   ├── AuthService.java               # 认证服务
│   └── GameService.java               # 游戏服务
└── util/                              # 工具类
    └── JwtUtil.java                   # JWT工具类

src/main/resources/
├── application.yml                    # 应用配置文件
└── mapper/                            # MyBatis映射文件
    ├── UserMapper.xml
    ├── UserStatsMapper.xml
    └── GameHistoryMapper.xml
```

## 快速开始

### 1. 环境要求

- JDK 1.8 或更高版本
- Maven 3.6+
- MySQL 8.0+

### 2. 数据库配置

#### 2.1 创建数据库

执行 `database/init.sql` 文件来创建数据库和表：

```bash
mysql -u root -p < database/init.sql
```

或者手动在MySQL中执行：

```sql
CREATE DATABASE IF NOT EXISTS `super_rps` 
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;
```

然后执行 `database/init.sql` 中的所有SQL语句。

#### 2.2 修改数据库连接配置

编辑 `src/main/resources/application.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/super_rps?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root
    password: 你的MySQL密码  # 👈 修改这里
```

### 3. 编译和运行

#### 方式一：使用Maven命令

```bash
# 清理并编译
mvn clean package

# 运行项目
mvn spring-boot:run
```

#### 方式二：直接运行jar包

```bash
# 编译打包
mvn clean package

# 运行jar包
java -jar target/super-rps-backend-1.0.0.jar
```

#### 方式三：在IDE中运行

直接运行 `SuperRpsApplication.java` 的main方法。

### 4. 访问应用

应用启动后，默认运行在：**http://localhost:8080**

## API接口文档

### 认证相关 API

#### 1. 用户注册

- **URL**: `/api/auth/register`
- **Method**: `POST`
- **Content-Type**: `application/json`

**请求体**:
```json
{
  "username": "testuser",
  "password": "123456",
  "email": "test@example.com"  // 可选
}
```

**响应**:
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
      "stats": {
        "gamesPlayed": 0,
        "gamesWon": 0,
        "currentLevel": 1
      }
    }
  }
}
```

#### 2. 用户登录

- **URL**: `/api/auth/login`
- **Method**: `POST`
- **Content-Type**: `application/json`

**请求体**:
```json
{
  "username": "testuser",
  "password": "123456"
}
```

**响应**: 同注册接口

#### 3. 检查认证状态

- **URL**: `/api/auth/check`
- **Method**: `GET`
- **Headers**: `Authorization: Bearer {token}` (可选)

**响应**:
```json
{
  "success": true,
  "code": 200,
  "message": "success",
  "data": {
    "authenticated": true,
    "userInfo": { ... }
  }
}
```

#### 4. 用户登出

- **URL**: `/api/auth/logout`
- **Method**: `POST`
- **Headers**: `Authorization: Bearer {token}` (可选)

**响应**:
```json
{
  "success": true,
  "code": 200,
  "message": "退出登录成功",
  "data": null
}
```

#### 5. 获取当前用户信息

- **URL**: `/api/auth/me`
- **Method**: `GET`
- **Headers**: `Authorization: Bearer {token}` (必须)

**响应**:
```json
{
  "success": true,
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "testuser",
    "email": "test@example.com",
    "stats": { ... }
  }
}
```

### 游戏相关 API

#### 6. 获取AI出招

- **URL**: `/api/game/ai-move`
- **Method**: `POST`
- **Content-Type**: `application/json`

**请求体**:
```json
{
  "difficulty": 1  // 难度级别（可选，默认1）
}
```

**响应**:
```json
{
  "success": true,
  "code": 200,
  "message": "success",
  "data": {
    "move": "rock"  // rock, paper, 或 scissors
  }
}
```

#### 7. 判断游戏结果

- **URL**: `/api/game/judge`
- **Method**: `POST`
- **Content-Type**: `application/json`

**请求体**:
```json
{
  "playerMove": "rock",
  "opponentMove": "scissors"
}
```

**响应**:
```json
{
  "success": true,
  "code": 200,
  "message": "success",
  "data": {
    "result": "win"  // win, lose, 或 draw
  }
}
```

#### 8. 保存游戏记录

- **URL**: `/api/game/save`
- **Method**: `POST`
- **Headers**: `Authorization: Bearer {token}` (必须)
- **Content-Type**: `application/json`

**请求体**:
```json
{
  "gameType": "ai",          // ai, pvp, 或 stage
  "level": 1,                // 关卡（可选，闯关模式使用）
  "result": "win",           // win, lose, 或 draw
  "moves": "{\"rounds\":[]}" // 游戏过程JSON字符串（可选）
}
```

**响应**:
```json
{
  "success": true,
  "code": 200,
  "message": "success",
  "data": null
}
```

#### 9. 获取游戏历史

- **URL**: `/api/game/history?page=1&pageSize=10`
- **Method**: `GET`
- **Headers**: `Authorization: Bearer {token}` (必须)

**响应**:
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
        "gameType": "ai",
        "level": 1,
        "result": "win",
        "moves": "{...}",
        "playedAt": "2025-10-27 12:00:00"
      }
    ],
    "total": 10,
    "page": 1,
    "pageSize": 10
  }
}
```

## API响应格式说明

所有API响应都遵循统一格式：

```json
{
  "success": true/false,  // 是否成功
  "code": 200,            // HTTP状态码
  "message": "...",       // 消息说明
  "data": { ... }         // 返回数据（可为null）
}
```

## 配置说明

### JWT配置

在 `application.yml` 中可以配置JWT密钥和过期时间：

```yaml
jwt:
  secret: SuperRPS-Secret-Key-2025-Change-This-In-Production-Environment
  expiration: 604800000  # 7天（毫秒）
```

**注意**：生产环境请务必修改secret为更复杂的密钥！

### 端口配置

默认端口是8080，可以通过环境变量或配置文件修改：

```yaml
server:
  port: ${PORT:8080}  # 默认8080
```

或通过环境变量：
```bash
export PORT=9090
mvn spring-boot:run
```

## 数据库表说明

### users（用户表）
- `id` - 用户ID（主键）
- `username` - 用户名（唯一）
- `password` - 密码（明文存储）
- `email` - 邮箱
- `avatar` - 头像URL
- `created_at` - 创建时间
- `last_login_at` - 最后登录时间

### user_stats（用户统计表）
- `user_id` - 用户ID（主键，外键）
- `games_played` - 游戏总场次
- `games_won` - 获胜场次
- `current_level` - 当前关卡
- `updated_at` - 更新时间

### game_history（游戏历史记录表）
- `id` - 记录ID（主键）
- `user_id` - 用户ID（外键）
- `game_type` - 游戏类型（pvp/stage/ai）
- `level` - 关卡数
- `result` - 游戏结果（win/lose/draw）
- `moves` - 游戏过程（JSON）
- `played_at` - 游戏时间

## 开发说明

### 添加新的API接口

1. 在对应的Controller中添加新方法
2. 如需数据库操作，在Mapper接口和XML中添加SQL
3. 业务逻辑在Service层实现
4. 如需认证，在WebConfig中配置拦截器路径

### 日志配置

应用已配置日志输出，可在 `application.yml` 中调整日志级别：

```yaml
logging:
  level:
    com.example.superrps: debug
    org.springframework.web: info
```

## 常见问题

### 1. 数据库连接失败

- 检查MySQL服务是否启动
- 确认数据库名称、用户名、密码是否正确
- 检查MySQL端口是否为3306

### 2. JWT Token验证失败

- 确认请求头格式：`Authorization: Bearer {token}`
- 检查token是否过期
- 验证jwt.secret配置是否正确

### 3. 端口被占用

修改 `application.yml` 中的端口号，或使用环境变量指定端口。

## 许可证

本项目仅供学习使用。

