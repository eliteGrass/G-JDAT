# MyBatis 基础示例

## 项目说明
这是一个简单的MyBatis基础示例项目，演示了MyBatis的基本CRUD操作。

## 项目结构
```
learnMybatis/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/liteGrass/
│   │   │       ├── entity/          # 实体类
│   │   │       │   └── User.java
│   │   │       ├── mapper/          # Mapper接口
│   │   │       │   └── UserMapper.java
│   │   │       ├── util/            # 工具类
│   │   │       │   └── MyBatisUtil.java
│   │   │       └── Main.java        # 主程序
│   │   └── resources/
│   │       ├── mapper/              # Mapper XML文件
│   │       │   └── UserMapper.xml
│   │       ├── sql/                 # SQL脚本
│   │       │   └── user.sql
│   │       ├── db.properties        # 数据库配置
│   │       ├── mybatis-config.xml   # MyBatis配置
│   │       └── logback.xml          # 日志配置
│   └── test/
│       └── java/
│           └── com/liteGrass/test/
│               └── UserMapperTest.java  # 单元测试
└── pom.xml
```

## 数据库配置
- 主机: 192.168.179.129:3306
- 数据库: mybatis_demo
- 用户名: root
- 密码: 100544

## 使用步骤

### 1. 初始化数据库
执行SQL脚本创建数据库和表：
```bash
mysql -h 192.168.179.129 -u root -p100544 < src/main/resources/sql/user.sql
```

### 2. 运行主程序
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.liteGrass.Main"
```

### 3. 运行单元测试
```bash
mvn test
```

## 功能说明

### UserMapper接口提供的方法：
- `insert(User user)` - 插入用户
- `deleteById(Long id)` - 根据ID删除用户
- `update(User user)` - 更新用户信息
- `selectById(Long id)` - 根据ID查询用户
- `selectByUsername(String username)` - 根据用户名查询用户
- `selectAll()` - 查询所有用户
- `selectByCondition(User user)` - 根据条件查询用户列表
- `count()` - 统计用户总数

## 主要技术点
1. MyBatis核心配置文件(mybatis-config.xml)
2. Mapper接口和XML映射文件
3. SqlSessionFactory和SqlSession的使用
4. 结果映射(ResultMap)
5. 动态SQL(if、set、where标签)
6. 自动生成主键
7. 属性配置文件的使用
