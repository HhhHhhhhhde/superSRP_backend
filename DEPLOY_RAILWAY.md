# Railway 部署指南 - 超级石头剪刀布后端

## 🚀 快速部署（5分钟）

### 准备工作

Railway会自动为你提供MySQL数据库，无需自己安装！

---

## 步骤1：推送代码到GitHub

```bash
# 1. 进入后端项目目录
cd "d:\学习资料\哈基米哟大三上多\Server-side Development\Back end"

# 2. 初始化Git（如果还没有）
git init

# 3. 添加所有文件
git add .

# 4. 提交
git commit -m "部署后端到Railway"

# 5. 在GitHub创建新仓库
# 访问 https://github.com/new
# 仓库名建议：super-rps-backend

# 6. 推送代码
git remote add origin https://github.com/你的用户名/super-rps-backend.git
git branch -M main
git push -u origin main
```

---

## 步骤2：部署到Railway

### 2.1 创建后端服务

1. 访问 https://railway.app
2. 使用GitHub账号登录
3. 点击 **"New Project"**
4. 选择 **"Deploy from GitHub repo"**
5. 选择刚才创建的 `super-rps-backend` 仓库
6. 等待3-5分钟自动部署

### 2.2 添加MySQL数据库

1. 在项目页面，点击 **"+ New"**
2. 选择 **"Database"** → **"Add MySQL"**
3. Railway会自动创建MySQL实例并生成连接信息

### 2.3 配置环境变量

点击后端服务 → **"Variables"** 标签 → 添加以下环境变量：

```bash
# Railway会自动提供以下MySQL变量（无需手动添加）：
# MYSQLHOST
# MYSQLPORT
# MYSQLDATABASE
# MYSQLUSER
# MYSQLPASSWORD

# 你需要手动添加的变量：
SPRING_DATASOURCE_URL=jdbc:mysql://${MYSQLHOST}:${MYSQLPORT}/${MYSQLDATABASE}?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
SPRING_DATASOURCE_USERNAME=${MYSQLUSER}
SPRING_DATASOURCE_PASSWORD=${MYSQLPASSWORD}

# JWT密钥（重要！请改成你自己的随机字符串）
JWT_SECRET=your-super-secret-jwt-key-change-this-in-production-2025
JWT_EXPIRATION=604800000

# 端口（Railway自动设置，通常不需要改）
PORT=8080
```

**注意**：Railway会自动替换 `${MYSQLHOST}` 等变量。

### 2.4 初始化数据库

1. 在Railway项目页面，点击MySQL数据库服务
2. 点击 **"Data"** 标签
3. 点击 **"Query"** 运行SQL查询
4. 复制 `database/init.sql` 的内容并执行

或者使用MySQL客户端连接：
```bash
# 从Railway获取连接信息
Host: containers-us-west-xxx.railway.app
Port: 6379
Database: railway
User: root
Password: （在Variables中查看MYSQLPASSWORD）
```

### 2.5 生成访问域名

1. 点击后端服务
2. 进入 **"Settings"** 标签
3. 点击 **"Generate Domain"**
4. 会得到类似 `super-rps-backend.up.railway.app` 的域名

---

## 步骤3：连接前后端

### 3.1 更新前端API地址

修改前端项目中的API基础路径：

**文件：** `Front end/src/main/resources/static/js/auth.js`

```javascript
// 第8行，修改为你的后端域名
const API_CONFIG = {
  baseURL: 'https://your-backend.up.railway.app/api/auth',
  // ... 其他配置
};
```

**文件：** `Front end/src/main/resources/static/js/game.js`

```javascript
// 第17行
const API_CONFIG = {
  baseURL: 'https://your-backend.up.railway.app/api/game',
  // ... 其他配置
};
```

**文件：** `Front end/src/main/resources/static/js/history.js`

```javascript
// 第5行
const API_CONFIG = {
  baseURL: 'https://your-backend.up.railway.app/api/game',
  // ... 其他配置
};
```

### 3.2 重新部署前端

```bash
cd "d:\学习资料\哈基米哟大三上多\Server-side Development\Front end"
git add .
git commit -m "更新API地址"
git push
```

Railway会自动重新部署前端。

---

## 验证部署

### 测试后端API

```bash
# 测试健康检查（替换为你的域名）
curl https://your-backend.up.railway.app/api/auth/check

# 应该返回：
# {"success":true,"code":200,"message":"success","data":{"authenticated":false,"userInfo":null}}
```

### 测试前端

访问你的前端域名：`https://supersrp-production.up.railway.app/`

尝试注册/登录，如果成功则说明前后端已连通！

---

## 📊 监控和日志

### 查看后端日志

1. 进入Railway项目
2. 点击后端服务
3. 点击 **"Deployments"** 查看部署历史
4. 点击 **"View Logs"** 查看实时日志

### 查看数据库

1. 点击MySQL服务
2. 点击 **"Data"** 标签
3. 可以直接查询数据

---

## ❓ 常见问题

### Q1: 数据库连接失败

**症状**：日志显示 `Communications link failure`

**解决**：
1. 检查环境变量是否正确设置
2. 确认MySQL服务已启动
3. 检查 `SPRING_DATASOURCE_URL` 格式

### Q2: 启动失败 - 端口冲突

**症状**：`Port 8080 already in use`

**解决**：
确保 `application.yml` 中端口配置为：
```yaml
server:
  port: ${PORT:8080}
```

### Q3: CORS错误

**症状**：前端显示跨域错误

**解决**：
后端已配置CORS，检查 `WebConfig.java` 中的配置：
```java
@Override
public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")
            .allowedOriginPatterns("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600);
}
```

### Q4: 数据库初始化失败

**解决**：
手动在Railway MySQL Query界面执行 `init.sql` 的内容。

---

## 💰 费用说明

- **Railway免费额度**：$5/月
- **本项目预计消耗**：
  - 后端服务：$1-2/月
  - MySQL数据库：$1-2/月
  - 总计：约$2-4/月

免费额度完全够用！

---

## 🔄 更新部署

修改代码后：

```bash
git add .
git commit -m "更新说明"
git push
```

Railway会自动检测推送并重新部署！

---

## 📝 环境变量完整列表

```env
# 数据库配置（Railway自动提供）
MYSQLHOST=containers-us-west-xxx.railway.app
MYSQLPORT=6379
MYSQLDATABASE=railway
MYSQLUSER=root
MYSQLPASSWORD=xxxxx

# 数据源配置（手动添加）
SPRING_DATASOURCE_URL=jdbc:mysql://${MYSQLHOST}:${MYSQLPORT}/${MYSQLDATABASE}?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
SPRING_DATASOURCE_USERNAME=${MYSQLUSER}
SPRING_DATASOURCE_PASSWORD=${MYSQLPASSWORD}

# JWT配置（手动添加）
JWT_SECRET=your-secret-key-here
JWT_EXPIRATION=604800000

# 端口（Railway自动设置）
PORT=8080
```

---

## ✅ 部署检查清单

- [ ] 代码已推送到GitHub
- [ ] Railway项目已创建
- [ ] MySQL数据库已添加
- [ ] 环境变量已配置
- [ ] 数据库已初始化（执行init.sql）
- [ ] 后端域名已生成
- [ ] 前端API地址已更新
- [ ] 前端已重新部署
- [ ] 测试注册/登录功能

---

**部署成功后，你将拥有：**
- ✅ 在线可访问的前端：https://supersrp-production.up.railway.app/
- ✅ 在线可访问的后端API：https://your-backend.up.railway.app/api/
- ✅ 云端MySQL数据库
- ✅ 自动部署流程

**祝部署顺利！** 🚀

