# web-crud-demo

一个用于学习 Java Web、MyBatis、MySQL 和 GitHub 版本管理的用户管理系统课程设计项目。

项目最初来自 Tomcat 自动解压后的 WAR 包目录，后续恢复成标准 Maven Web 工程，并逐步补充了登录、权限控制、导入导出、备份恢复、分页查询、数据库登录、密码修改和操作日志等功能。

## 功能概览

- 用户列表展示
- 按姓名模糊查询用户
- 分页查询
- 管理员新增、编辑、删除用户
- 登录、退出登录
- 数据库账号登录
- 当前登录用户修改密码
- 普通用户只读访问
- 管理员权限拦截
- CSV 导出当前查询结果
- CSV 导入用户数据
- 全量备份用户数据
- 全量恢复用户数据
- 操作日志记录与查看

## 演示账号

默认账号保存在数据库表 `t_account` 中：

| 角色 | 账号 | 密码 | 权限 |
| --- | --- | --- | --- |
| 管理员 | `admin` | `admin123` | 查看、新增、编辑、删除、导入、备份、恢复、查看日志 |
| 普通用户 | `user` | `user123` | 仅查看、修改自己的登录密码 |

## 技术栈

- Java 17 编译级别
- JDK 21 本机运行与编译
- Servlet 4.0
- JSP + JSTL
- MyBatis 3.5.13
- MySQL 8
- Apache Tomcat 9
- Maven WAR 项目

## 项目结构

```text
src/main/java/com/example
├─ auth/AuthUtil.java
├─ entity/AuthAccount.java
├─ entity/OperationLog.java
├─ entity/User.java
├─ filter/AdminFilter.java
├─ filter/AuthFilter.java
├─ mapper/AuthAccountMapper.java
├─ mapper/OperationLogMapper.java
├─ mapper/UserMapper.java
├─ servlet/AddServlet.java
├─ servlet/BackupServlet.java
├─ servlet/ChangePasswordServlet.java
├─ servlet/DeleteServlet.java
├─ servlet/EditServlet.java
├─ servlet/ExportServlet.java
├─ servlet/ImportServlet.java
├─ servlet/LoginServlet.java
├─ servlet/LogoutServlet.java
├─ servlet/OperationLogServlet.java
├─ servlet/RestoreServlet.java
├─ servlet/UpdateServlet.java
├─ servlet/UserListServlet.java
└─ util
   ├─ MyBatisUtil.java
   └─ OperationLogUtil.java

src/main/resources
└─ mybatis-config.xml

src/main/webapp
├─ add.jsp
├─ change-password.jsp
├─ edit.jsp
├─ list.jsp
├─ login.jsp
├─ logs.jsp
└─ assets/css/app.css

docs
└─ schema.sql
```

## 数据库配置

默认数据库连接配置在：

```text
src/main/resources/mybatis-config.xml
```

默认连接信息：

```text
URL: jdbc:mysql://localhost:3306/maven_demo
Username: root
Password: 空
```

初始化 SQL：

```sql
CREATE DATABASE IF NOT EXISTS maven_demo
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

USE maven_demo;

CREATE TABLE IF NOT EXISTS t_user (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  age INT NOT NULL,
  email VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS t_account (
  id INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS t_operation_log (
  id INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  action VARCHAR(50) NOT NULL,
  detail VARCHAR(255) NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT IGNORE INTO t_account (username, password) VALUES
  ('admin', 'admin123'),
  ('user', 'user123');
```

也可以直接执行：

```text
docs/schema.sql
```

## 在 Eclipse 中运行

1. 使用 `File > Import > Maven > Existing Maven Projects` 导入项目
2. 选择项目根目录
3. 右键项目执行 `Maven > Update Project`
4. 配置 Apache Tomcat 9 Runtime
5. 在 `Servers` 视图中把项目添加到 Tomcat
6. 启动 Tomcat
7. 访问：

```text
http://localhost:8080/web-crud-demo/login
```

如果 Eclipse 中部署路径不是 `/web-crud-demo`，以 `Servers` 视图里 `Modules` 的 `Path` 为准。

## 打包

如果本机安装了 Maven，可以执行：

```powershell
mvn clean package
```

生成的 WAR 包通常位于：

```text
target/web-crud-demo.war
```

如果本地 Maven 解析插件时有异常，也可以用 `javac` 做源码编译验证。

## 当前已完成模块

- 基础 CRUD
- 登录 / 退出登录
- 管理员 / 普通用户权限
- CSV 导入 / 导出
- 数据备份 / 恢复
- 分页查询
- 数据库登录
- 密码修改
- 操作日志

## 后续优化方向

- 密码加密存储
- 更细粒度的表单校验
- 统计首页
- 账号管理
- 更完善的异常提示
- 操作日志筛选查询

## 学习目标

这个仓库适合用来练习：

- Java Web 基础项目结构
- Servlet / JSP / MyBatis 的协作方式
- MySQL 表设计与接入
- Tomcat 部署 WAR 包
- 从 WAR 恢复 Maven 源码工程
- 使用 GitHub 管理课程设计开发过程
