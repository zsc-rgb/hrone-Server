# 第1阶段：项目基础搭建

## 🎯 学习目标

- 理解Maven多模块项目结构
- 掌握Spring Boot项目的基本配置
- 成功启动第一个Spring Boot应用
- 理解各模块的职责划分

## 📋 任务清单

- [x] 创建父POM（hrone-Server/pom.xml）
- [ ] 创建hrone-admin模块（启动入口）
- [ ] 创建hrone-common模块（通用工具）
- [ ] 创建hrone-framework模块（框架核心）
- [ ] 创建hrone-system模块（系统管理）
- [ ] 编写启动类
- [ ] 编写配置文件
- [ ] 测试项目启动

## 🏗️ 项目结构说明

```
hrone-Server/
├── hrone-admin/           ← Web入口模块，包含启动类和Controller
├── hrone-common/          ← 通用工具模块，包含工具类、常量、异常等
├── hrone-framework/       ← 框架核心模块，包含配置、安全认证等
├── hrone-system/          ← 系统管理模块，包含用户、角色、菜单等业务
├── docs/                  ← 学习文档
├── sql/                   ← 数据库脚本
└── pom.xml               ← 父POM，管理所有依赖版本
```

### 模块职责

| 模块 | 职责 | 依赖关系 |
|------|------|----------|
| **hrone-admin** | Web入口，包含Controller和启动类 | 依赖所有其他模块 |
| **hrone-framework** | 框架配置，如安全、过滤器、拦截器 | 依赖common和system |
| **hrone-system** | 业务模块，如用户、角色、菜单 | 依赖common |
| **hrone-common** | 基础工具，如工具类、常量、异常 | 不依赖其他模块 |

## 📝 实现步骤

### 步骤1：创建父POM

父POM已创建（`pom.xml`），主要内容：

**关键配置：**
```xml
<!-- 1. 定义项目信息 -->
<groupId>com.hrone</groupId>
<artifactId>hrone-server</artifactId>
<version>1.0.0</version>
<packaging>pom</packaging>

<!-- 2. 定义属性（版本号） -->
<properties>
    <java.version>1.8</java.version>
    <spring-boot.version>2.5.14</spring-boot.version>
    <mybatis-plus.version>3.4.2</mybatis-plus.version>
    <!-- ... 更多版本定义 -->
</properties>

<!-- 3. 依赖管理（不会实际引入，只是声明版本） -->
<dependencyManagement>
    <dependencies>
        <!-- SpringBoot依赖 -->
        <!-- MyBatis-Plus依赖 -->
        <!-- 工具类依赖 -->
    </dependencies>
</dependencyManagement>

<!-- 4. 声明子模块 -->
<modules>
    <module>hrone-admin</module>
    <module>hrone-common</module>
    <module>hrone-framework</module>
    <module>hrone-system</module>
</modules>
```

**知识点：**
- `<dependencyManagement>`：只声明版本，不实际引入依赖
- `<modules>`：声明子模块，Maven会自动管理模块间的依赖
- `<packaging>pom</packaging>`：表示这是一个父项目

---

### 步骤2：创建 hrone-common 模块

**为什么先创建common？**
因为其他模块都会依赖它，所以要最先创建。

#### 2.1 创建模块POM

创建文件：`hrone-Server/hrone-common/pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <!-- 父项目 -->
    <parent>
        <groupId>com.hrone</groupId>
        <artifactId>hrone-server</artifactId>
        <version>1.0.0</version>
    </parent>

    <artifactId>hrone-common</artifactId>
    <description>通用工具模块</description>

    <dependencies>
        <!-- Spring Boot Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- Hutool工具类 -->
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
        </dependency>

        <!-- FastJSON -->
        <dependency>
            <groupId>com.alibaba.fastjson2</groupId>
            <artifactId>fastjson2</artifactId>
        </dependency>

        <!-- Lombok（简化代码） -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <scope>provided</scope>
        </dependency>
    </dependencies>
</project>
```

**知识点：**
- 子模块通过`<parent>`继承父POM
- 依赖不需要写版本号，因为父POM已经在`dependencyManagement`中定义了

#### 2.2 创建包结构

创建以下目录结构：
```
hrone-common/
└── src/
    └── main/
        └── java/
            └── com/
                └── hrone/
                    └── common/
                        ├── constant/      # 常量
                        ├── core/          # 核心类
                        │   └── domain/    # 通用实体
                        ├── enums/         # 枚举
                        ├── exception/     # 异常
                        └── utils/         # 工具类
```

---

### 步骤3：创建 hrone-system 模块

#### 3.1 创建模块POM

创建文件：`hrone-Server/hrone-system/pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.hrone</groupId>
        <artifactId>hrone-server</artifactId>
        <version>1.0.0</version>
    </parent>

    <artifactId>hrone-system</artifactId>
    <description>系统管理模块</description>

    <dependencies>
        <!-- 依赖通用模块 -->
        <dependency>
            <groupId>com.hrone</groupId>
            <artifactId>hrone-common</artifactId>
        </dependency>

        <!-- MyBatis-Plus -->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
        </dependency>

        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <scope>provided</scope>
        </dependency>
    </dependencies>
</project>
```

#### 3.2 创建包结构

```
hrone-system/
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── hrone/
    │   │           └── system/
    │   │               ├── domain/       # 实体类
    │   │               ├── mapper/       # MyBatis Mapper
    │   │               ├── service/      # 业务逻辑
    │   │               │   └── impl/
    │   │               └── controller/   # 控制器
    │   └── resources/
    │       └── mapper/                   # MyBatis XML
    │           └── system/
    └── test/
        └── java/
```

---

### 步骤4：创建 hrone-framework 模块

#### 4.1 创建模块POM

创建文件：`hrone-Server/hrone-framework/pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.hrone</groupId>
        <artifactId>hrone-server</artifactId>
        <version>1.0.0</version>
    </parent>

    <artifactId>hrone-framework</artifactId>
    <description>框架核心模块</description>

    <dependencies>
        <!-- 依赖系统模块 -->
        <dependency>
            <groupId>com.hrone</groupId>
            <artifactId>hrone-system</artifactId>
        </dependency>

        <!-- Spring Security -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>

        <!-- Redis -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>

        <!-- JWT -->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt</artifactId>
        </dependency>

        <!-- Druid数据源 -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
        </dependency>
    </dependencies>
</project>
```

#### 4.2 创建包结构

```
hrone-framework/
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── hrone/
        │           └── framework/
        │               ├── config/       # 配置类
        │               ├── security/     # 安全认证
        │               ├── aspectj/      # AOP切面
        │               └── interceptor/  # 拦截器
        └── resources/
```

---

### 步骤5：创建 hrone-admin 模块

这是启动入口模块，最后创建。

#### 5.1 创建模块POM

创建文件：`hrone-Server/hrone-admin/pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.hrone</groupId>
        <artifactId>hrone-server</artifactId>
        <version>1.0.0</version>
    </parent>

    <artifactId>hrone-admin</artifactId>
    <packaging>jar</packaging>
    <description>Web服务入口</description>

    <dependencies>
        <!-- 依赖框架核心模块（会自动传递其他依赖） -->
        <dependency>
            <groupId>com.hrone</groupId>
            <artifactId>hrone-framework</artifactId>
        </dependency>

        <!-- MySQL驱动 -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>

        <!-- Swagger -->
        <dependency>
            <groupId>com.github.xiaoymin</groupId>
            <artifactId>knife4j-spring-boot-starter</artifactId>
        </dependency>

        <!-- Spring Boot DevTools（热部署） -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <!-- Spring Boot Maven插件 -->
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>2.5.14</version>
                <configuration>
                    <fork>true</fork>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
        <finalName>${project.artifactId}</finalName>
    </build>
</project>
```

#### 5.2 创建启动类

创建文件：`hrone-Server/hrone-admin/src/main/java/com/hrone/HROneApplication.java`

```java
package com.hrone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 * 
 * @author hrone
 */
@SpringBootApplication
public class HROneApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(HROneApplication.class, args);
        System.out.println("\n" +
            "  _   _ ____   ___             \n" +
            " | | | |  _ \\ / _ \\ _ __   ___ \n" +
            " | |_| | |_) | | | | '_ \\ / _ \\\n" +
            " |  _  |  _ <| |_| | | | |  __/\n" +
            " |_| |_|_| \\_\\\\___/|_| |_|\\___|\n" +
            "\n" +
            "HROne 后端系统启动成功！\n");
    }
}
```

**知识点：**
- `@SpringBootApplication`：组合注解，包含：
  - `@SpringBootConfiguration`：配置类
  - `@EnableAutoConfiguration`：自动配置
  - `@ComponentScan`：组件扫描

#### 5.3 创建配置文件

创建文件：`hrone-Server/hrone-admin/src/main/resources/application.yml`

```yaml
# 服务器配置
server:
  port: 8080
  servlet:
    context-path: /
  tomcat:
    uri-encoding: UTF-8

# Spring配置
spring:
  application:
    name: hrone-server
  profiles:
    active: dev

# 日志配置
logging:
  level:
    com.hrone: debug
    org.springframework: info
```

创建文件：`hrone-Server/hrone-admin/src/main/resources/application-dev.yml`

```yaml
# 开发环境配置
spring:
  # 数据源配置（暂时注释，后续阶段会用到）
  # datasource:
  #   type: com.alibaba.druid.pool.DruidDataSource
  #   driver-class-name: com.mysql.cj.jdbc.Driver
  #   url: jdbc:mysql://localhost:3306/hrone_db?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
  #   username: root
  #   password: root
```

#### 5.4 创建测试Controller

创建文件：`hrone-Server/hrone-admin/src/main/java/com/hrone/controller/TestController.java`

```java
package com.hrone.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试Controller
 */
@RestController
@RequestMapping("/test")
public class TestController {
    
    @GetMapping("/hello")
    public Map<String, Object> hello() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "Hello, HROne!");
        result.put("data", "项目启动成功！");
        return result;
    }
}
```

---

## 🚀 启动项目

### 1. 在IDEA中导入项目

1. 打开IDEA
2. File → Open → 选择 `hrone-Server` 目录
3. 等待Maven下载依赖（第一次会比较慢）

### 2. 运行启动类

1. 找到 `HROneApplication.java`
2. 右键 → Run 'HROneApplication'
3. 看到控制台输出ASCII艺术字，说明启动成功

### 3. 测试接口

打开浏览器访问：http://localhost:8080/test/hello

应该看到：
```json
{
  "code": 200,
  "msg": "Hello, HROne!",
  "data": "项目启动成功！"
}
```

---

## ✅ 完成检查

- [ ] 项目能成功启动，没有报错
- [ ] 能访问测试接口并返回正确结果
- [ ] 理解了多模块项目的依赖关系
- [ ] 理解了各模块的职责划分

---

## 💡 知识点总结

### 1. Maven多模块项目

**为什么要用多模块？**
- **模块化**：每个模块职责清晰，便于维护
- **复用性**：common模块可以被多个项目复用
- **编译效率**：修改某个模块只需编译该模块
- **团队协作**：不同团队可以负责不同模块

**依赖传递：**
```
hrone-admin 
  → hrone-framework 
    → hrone-system 
      → hrone-common
```
admin依赖framework，会自动获得framework的所有依赖。

### 2. Spring Boot自动配置

Spring Boot的核心特性：
- **约定优于配置**：提供默认配置，减少配置工作
- **起步依赖**：一个依赖包含多个相关依赖
- **自动配置**：根据classpath自动配置Bean

### 3. 配置文件加载顺序

1. `application.yml`：主配置
2. `application-{profile}.yml`：环境配置
3. 通过 `spring.profiles.active` 指定激活哪个环境

---

## 📚 扩展阅读

1. Maven多模块项目最佳实践
2. Spring Boot启动流程详解
3. Spring Boot配置文件详解

---

## 🎉 恭喜！

您已经完成了第1阶段的学习！

接下来进入 **第2阶段：通用工具模块** 的学习。

在第2阶段，我们将实现：
- 统一响应结果
- 基础工具类
- 异常处理体系
- 通用实体类

---

**下一步：** 阅读 `docs/phase-02-通用工具.md`

