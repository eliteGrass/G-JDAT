package com.liteGrass;

import com.liteGrass.entity.User;
import com.liteGrass.mapper.UserMapper;
import com.liteGrass.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * MyBatis Demo主类
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== MyBatis 基础示例 ===\n");

        // 使用try-with-resources自动关闭SqlSession
        try (SqlSession sqlSession = MyBatisUtil.getSqlSession(true)) {
            // 获取Mapper接口代理对象
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            UserMapper userMapper2 = sqlSession.getMapper(UserMapper.class);

            // 1. 查询所有用户
            System.out.println("1. 查询所有用户:");
            List<User> users = userMapper.selectAll();
            users.forEach(System.out::println);
            System.out.println();

            // 2. 根据ID查询用户
            System.out.println("2. 根据ID查询用户(ID=1):");
            User user = userMapper.selectById(1L);
            System.out.println(user);
            System.out.println();

            // 3. 根据用户名查询
            System.out.println("3. 根据用户名查询(username=lisi):");
            User userByName = userMapper.selectByUsername("lisi");
            System.out.println(userByName);
            System.out.println();

            // 4. 插入新用户
            System.out.println("4. 插入新用户:");
            User newUser = new User();
            newUser.setUsername("tianqi");
            newUser.setPassword("123456");
            newUser.setEmail("tianqi@example.com");
            newUser.setPhone("13800138004");
            newUser.setAge(24);

            int insertResult = userMapper.insert(newUser);
            System.out.println("插入成功，影响行数: " + insertResult + ", 新用户ID: " + newUser.getId());
            System.out.println();

            // 5. 更新用户
            System.out.println("5. 更新用户信息:");
            User updateUser = new User();
            updateUser.setId(newUser.getId());
            updateUser.setAge(25);
            updateUser.setEmail("tianqi_new@example.com");

            int updateResult = userMapper.update(updateUser);
            System.out.println("更新成功，影响行数: " + updateResult);
            System.out.println();

            // 6. 条件查询
            System.out.println("6. 条件查询(username包含'wang'):");
            User condition = new User();
            condition.setUsername("wang");
            List<User> conditionUsers = userMapper.selectByCondition(condition);
            conditionUsers.forEach(System.out::println);
            System.out.println();

            // 7. 统计用户总数
            System.out.println("7. 统计用户总数:");
            long count = userMapper.count();
            System.out.println("用户总数: " + count);
            System.out.println();

            // 8. 删除用户
            System.out.println("8. 删除用户(ID=" + newUser.getId() + "):");
            int deleteResult = userMapper.deleteById(newUser.getId());
            System.out.println("删除成功，影响行数: " + deleteResult);

        } catch (Exception e) {
            System.err.println("执行出错: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n=== 示例执行完成 ===");
    }
}
