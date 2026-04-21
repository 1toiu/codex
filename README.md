# web-crud-demo

一个用于学习 Java Web 开发和 GitHub 版本管理的用户管理 CRUD 项目。

项目最初来自 Tomcat 自动解压后的 WAR 包目录，后来恢复为标准 Maven Web 工程，并增加了页面美化、登录和管理员权限功能。

## 功能

- 用户列表展示
- 按姓名模糊查询用户
- 管理员新增用户
- 管理员编辑用户
- 管理员删除用户
- 登录和退出登录
- 普通用户只读访问
- 管理员权限拦截，普通用户不能直接访问新增、编辑、删除接口

## 演示账号

| 角色 | 账号 | 密码 | 权限 |
| --- | --- | --- | --- |
| 管理员 | `admin` | `admin123` | 查看、新增、编辑、删除 |
| 普通用户 | `user` | `user123` | 仅查看 |

> 当前账号写在 `LoginServlet` 中，适合学习和演示。后续可以优化为从数据库读取用户和角色。

## 技术栈

- Java 17 编译级别
- JDK 21 可运行和编译
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
├─ entity/User.java
├─ filter/AuthFilter.java
├─ filter/AdminFilter.java
├─ mapper/UserMapper.java
├─ servlet/LoginServlet.java
├─ servlet/LogoutServlet.java
├─ servlet/AddServlet.java
├─ servlet/DeleteServlet.java
├─ servlet/EditServlet.java
├─ servlet/UpdateServlet.java
├─ servlet/UserListServlet.java
└─ util/MyBatisUtil.java

src/main/resources
└─ mybatis-config.xml

src/main/webapp
├─ login.jsp
├─ list.jsp
├─ add.jsp
├─ edit.jsp
└─ assets/css/app.css
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

建表 SQL：

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
```

## 在 Eclipse 中运行

1. 使用 `File > Import > Maven > Existing Maven Projects` 导入项目。
2. 选择项目根目录。
3. 右键项目执行 `Maven > Update Project`。
4. 配置 Apache Tomcat 9 Runtime。
5. 在 Servers 视图中把项目添加到 Tomcat。
6. 启动 Tomcat。
7. 访问：

```text
http://localhost:8080/web-crud-demo/login
```

如果 Eclipse 中部署路径不是 `/web-crud-demo`，以 Servers 视图里 Modules 的 Path 为准。

## 打包

如果本机安装了 Maven，可以执行：

```powershell
mvn clean package
```

生成的 WAR 包通常位于：

```text
target/web-crud-demo.war
```

## 后续优化计划

- 把登录账号从硬编码改为数据库表
- 增加密码加密存储
- 增加权限不足页面
- 增加分页查询
- 增加表单校验和错误提示
- 整理 Maven 配置中的乱码注释
- 使用 GitHub 按功能提交历史版本

## 学习目标

这个仓库用于学习：

- Java Web 基础项目结构
- Servlet/JSP/MyBatis 的配合方式
- Tomcat 部署 WAR 包
- 从 WAR 恢复源码工程
- 使用 GitHub 管理项目历史版本
