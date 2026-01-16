# 企业级抽奖系统

基于 Spring Boot 的分布式抽奖营销平台，支持活动管理、奖品配置、用户抽奖、中奖通知等核心功能。系统采用异步解耦架构，具备完善的异常处理与数据一致性保障机制，能够应对高并发抽奖场景。

## 📋 目录

- [项目简介](#项目简介)
- [技术栈](#技术栈)
- [核心功能](#核心功能)
- [系统架构](#系统架构)
- [技术亮点](#技术亮点)
- [快速开始](#快速开始)
- [项目结构](#项目结构)
- [接口文档](#接口文档)
- [开发计划](#开发计划)

---

## 🎯 项目简介

本系统是一个完整的企业级抽奖营销解决方案，包含以下核心能力：

- **活动管理**：支持创建抽奖活动、配置活动奖品、管理参与用户
- **抽奖功能**：基于 MQ 的异步抽奖处理，支持高并发场景
- **中奖通知**：自动邮件通知中奖用户
- **数据一致性**：完善的事务回滚机制，保障数据准确性
- **状态流转**：活动、奖品、用户状态的自动化管理

## 🛠 技术栈

### 后端框架
- **Spring Boot 3.5.3** - 核心框架
- **MyBatis 3.0.3** - 持久层框架
- **MySQL** - 关系型数据库
- **Redis** - 缓存中间件

### 消息队列
- **RabbitMQ** - 异步消息处理
- **死信队列** - 消息重试机制

### 工具库
- **Hutool** - Java 工具集（加密、验证码）
- **JWT** - 用户认证
- **Lombok** - 代码简化
- **Spring Mail** - 邮件发送

### 其他
- **线程池** - 异步任务处理
- **@Transactional** - 事务管理

## ✨ 核心功能

### 1. 用户管理
- 用户注册（支持密码和验证码登录）
- JWT 令牌认证
- 敏感信息加密存储
- 登录拦截器

### 2. 奖品管理
- 奖品创建与配置
- 奖品图片上传
- 奖品等级管理（一等奖、二等奖等）
- 奖品状态流转

### 3. 活动管理
- 活动创建与配置
- 活动奖品关联
- 活动用户管理
- 活动详情查询（支持 Redis 缓存）
- 分页查询活动列表

### 4. 抽奖功能
- 基于 RabbitMQ 的异步抽奖
- 抽奖结果持久化
- 中奖记录查询
- 多维度缓存（奖品维度 + 活动维度）

### 5. 中奖通知
- 异步邮件发送
- 中奖信息个性化推送
- 线程池并发处理

### 6. 状态管理
- 活动状态流转（RUNNING → DONE）
- 奖品状态流转（AVAILABLE → DONE）
- 用户状态流转（AVAILABLE → DONE）
- 责任链模式实现状态扭转

## 🏗 系统架构

### 整体架构图

```
┌─────────────┐
│   前端/客户端  │
└──────┬──────┘
       │
       ▼
┌─────────────────────────────────┐
│         Controller 层            │
│  - UserController               │
│  - PrizeController              │
│  - ActivityController           │
└──────┬──────────────────────────┘
       │
       ▼
┌─────────────────────────────────┐
│          Service 层              │
│  - UserService                  │
│  - PrizeService                 │
│  - ActivityService              │
│  - DrawPrizeService             │
│  - ConvertStatusService         │
└──────┬──────────────────────────┘
       │
       ▼
┌─────────────────────────────────┐
│         MQ & 缓存层              │
│  - RabbitMQ (异步处理)           │
│  - Redis (缓存)                 │
└──────┬──────────────────────────┘
       │
       ▼
┌─────────────────────────────────┐
│         数据持久层               │
│  - MyBatis Mapper               │
│  - MySQL 数据库                 │
└─────────────────────────────────┘
```

### 核心流程：抽奖处理流程

```
抽奖请求
   │
   ▼
发送到 RabbitMQ
   │
   ▼
MqReceiver 消费消息
   │
   ├─→ 校验消息有效性
   │
   ├─→ 状态扭转（责任链模式）
   │   ├─ PrizeStatusOperator
   │   ├─ UserStatusOperator
   │   └─ ActivityStatusOperator
   │
   ├─→ 保存中奖结果（DB + Redis）
   │
   └─→ 异步发送邮件通知
       │
       └─→ 异常处理
           ├─→ 回滚状态
           ├─→ 删除中奖记录
           └─→ 进入死信队列
```

## 💡 技术亮点

### 1. 异步解耦 - MQ 削峰填谷

使用 RabbitMQ 实现抽奖请求与业务处理的异步解耦：

```java
// 发送抽奖消息到 MQ
rabbitTemplate.convertAndSend(
    DirectRabbitConfig.EXCHANGE_NAME,
    DirectRabbitConfig.ROUTING,
    message
);
```

**优势**：
- 削峰填谷，应对高并发抽奖
- 解耦业务逻辑，提升系统稳定性
- 异步处理，提升用户体验

### 2. 事务一致性 - 完善的回滚机制

当 MQ 消息处理失败时，自动回滚相关数据：

```java
try {
    // 1. 扭转状态
    convertStatus(drawPrizeRequestDTO);

    // 2. 保存中奖结果
    saveDrawPrizeResult(drawPrizeRequestDTO);

    // 3. 发送通知
    sendNotification(winnerRecords);

} catch (Exception e) {
    // 回滚状态 + 删除中奖记录
    rollback(drawPrizeRequestDTO);
    throw e; // 进入死信队列重试
}
```

### 3. 设计模式 - 责任链 + 策略模式

采用**责任链模式 + 策略模式**封装状态扭转逻辑：

```java
// 抽象状态算子
public abstract class AbstractStatusOperator {
    public abstract Integer sequence();              // 执行顺序
    public abstract Boolean needConvert(...);        // 是否需要转换
    public abstract Boolean convert(...);            // 执行转换
}

// 具体实现
- PrizeStatusOperator    (sequence = 1)
- UserStatusOperator     (sequence = 1)
- ActivityStatusOperator (sequence = 2，依赖奖品状态)
```

**优势**：
- 状态转换顺序可控
- 新增状态类型只需新增算子
- 符合开闭原则，易扩展

### 4. 死信队列 - 消息可靠性保障

实现 RabbitMQ 死信队列机制：

```java
@RabbitListener(queues = DirectRabbitConfig.DLX_QUEUE_NAME)
public class DlxMqReceiver {
    // 接收失败消息，重新投递到原队列
    rabbitTemplate.convertAndSend(
        DirectRabbitConfig.EXCHANGE_NAME,
        DirectRabbitConfig.ROUTING,
        msg
    );
}
```

**配置**：
- 最大重试次数：5 次
- 超过重试次数后进入死信队列
- 支持手动/自动重新投递

### 5. 多级缓存设计

设计**奖品维度**与**活动维度**的双重缓存策略：

```java
// 奖品维度缓存
String key = activityId + "_" + prizeId;
redisUtil.set(Constants.WINNING_RECORDS_PREFIX + key, data, timeout);

// 活动维度缓存（活动结束后缓存）
String key = String.valueOf(activityId);
redisUtil.set(Constants.WINNING_RECORDS_PREFIX + key, data, timeout);
```

**缓存策略**：
- Cache-Aside 模式
- 活动详情缓存（减少多表查询）
- 中奖记录缓存（提升查询性能）

### 6. 异步通知 - 线程池 + 邮件

基于 Spring 线程池实现中奖邮件异步发送：

```java
@Async("asyncServiceExecutor")
public void pushWinningList(List<WinnerRecordEntity> records) {
    records.forEach(record -> {
        mailUtil.sendSampleMail(
            record.getWinnerEmail(),
            "中奖通知",
            content
        );
    });
}
```

## 🚀 快速开始

### 环境要求

- **JDK**: 17+
- **Maven**: 3.6+
- **MySQL**: 8.0+
- **Redis**: 6.0+
- **RabbitMQ**: 3.9+

### 安装步骤

1. **克隆项目**
   ```bash
   git clone https://github.com/lirendada/lottery-system.git
   cd lottery_system
   ```

2. **修改配置**
   编辑 `application.properties`，配置数据库、Redis、RabbitMQ 等信息：
   ```properties
   # 数据库配置
   spring.datasource.url=jdbc:mysql://localhost:3306/lottery_system
   spring.datasource.username=root
   spring.datasource.password=your_password

   # Redis 配置
   spring.data.redis.host=localhost
   spring.data.redis.port=6379

   # RabbitMQ 配置
   spring.rabbitmq.host=localhost
   spring.rabbitmq.port=5672
   spring.rabbitmq.username=guest
   spring.rabbitmq.password=guest

   # JWT 密钥
   jwt.secret=your_secret_key

   # 邮件配置
   spring.mail.host=smtp.qq.com
   spring.mail.username=your_email@qq.com
   spring.mail.password=your_email_auth_code
   ```

3. **初始化数据库**
   ```sql
   CREATE DATABASE lottery_system;
   -- 执行 SQL 脚本（如有）
   ```

4. **启动项目**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

5. **访问接口**
   ```
   基础 URL: http://localhost:8080
   ```

## 📁 项目结构

```
lottery_system/
├── src/main/java/com/liren/lottery_system/
│   ├── common/                      # 公共模块
│   │   ├── advice/                  # 统一响应封装
│   │   ├── config/                  # 配置类（RabbitMQ、JWT等）
│   │   ├── constant/                # 常量定义
│   │   ├── enums/                   # 枚举类
│   │   ├── exception/               # 自定义异常
│   │   ├── interceptor/             # 拦截器
│   │   ├── mq/                      # MQ 消费者
│   │   ├── pojo/                    # 数据对象
│   │   └── utils/                   # 工具类
│   ├── controller/                  # 控制层
│   ├── mapper/                      # 数据访问层
│   ├── service/                     # 业务逻辑层
│   │   ├── operator/                # 状态算子（责任链模式）
│   │   └── impl/                    # 实现类
│   └── LotterySystemApplication.java
├── src/main/resources/
│   ├── mapper/                      # MyBatis XML
│   ├── application.properties       # 配置文件
│   └── logback-spring.xml           # 日志配置
└── pom.xml                          # Maven 配置
```

## 📚 接口文档

### 用户模块

| 接口 | 方法 | 描述 |
|------|------|------|
| `/user/register` | POST | 用户注册 |
| `/user/login/password` | POST | 密码登录 |
| `/user/login/code` | POST | 验证码登录 |

### 奖品模块

| 接口 | 方法 | 描述 |
|------|------|------|
| `/prize/create` | POST | 创建奖品 |
| `/prize/list` | GET | 获取奖品列表 |

### 活动模块

| 接口 | 方法 | 描述 |
|------|------|------|
| `/activity/create` | POST | 创建活动 |
| `/activity/list` | GET | 分页查询活动 |
| `/activity/detail` | GET | 获取活动详情 |

### 抽奖模块

| 接口 | 方法 | 描述 |
|------|------|------|
| `/draw/prize` | POST | 发起抽奖（异步） |
| `/draw/winning-records` | GET | 获取中奖记录 |

## 🔧 开发计划

- [ ] 前端管理页面开发
- [ ] 接口文档集成（Swagger/Knife4j）
- [ ] 单元测试完善
- [ ] Docker 容器化部署
- [ ] CI/CD 流水线配置
- [ ] 性能测试与优化

## 📄 许可证

本项目采用 MIT 许可证，详见 [LICENSE](LICENSE) 文件。

## 👨‍💻 作者

lirendada

## 🙏 致谢

感谢所有贡献者的支持！

---

**💬 如果觉得项目对你有帮助，请给个 Star ⭐️**
