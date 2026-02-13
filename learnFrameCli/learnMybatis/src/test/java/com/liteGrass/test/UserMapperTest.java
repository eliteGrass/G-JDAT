package com.liteGrass.test;

import com.liteGrass.entity.User;
import com.liteGrass.mapper.UserMapper;
import com.liteGrass.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * UserMapper测试类
 */
public class UserMapperTest {

    private SqlSession sqlSession;
    private UserMapper userMapper;

    @Before
    public void setUp() {
        // 获取SqlSession对象
        sqlSession = MyBatisUtil.getSqlSession();
        // 获取Mapper接口代理对象
        userMapper = sqlSession.getMapper(UserMapper.class);
    }

    @After
    public void tearDown() {
        // 关闭SqlSession
        if (sqlSession != null) {
            sqlSession.close();
        }
    }

    /**
     * 测试插入用户
     */
    @Test
    public void testInsert() {
        User user = new User();
        user.setUsername("zhaoliu");
        user.setPassword("123456");
        user.setEmail("zhaoliu@example.com");
        user.setPhone("13800138003");
        user.setAge(26);

        int result = userMapper.insert(user);
        sqlSession.commit();

        System.out.println("插入用户成功，影响行数: " + result);
        System.out.println("插入后的用户ID: " + user.getId());
    }

    /**
     * 测试根据ID删除用户
     */
    @Test
    public void testDeleteById() {
        Long id = 4L;
        int result = userMapper.deleteById(id);
        sqlSession.commit();

        System.out.println("删除用户成功，影响行数: " + result);
    }

    /**
     * 测试更新用户
     */
    @Test
    public void testUpdate() {
        User user = new User();
        user.setId(1L);
        user.setUsername("zhangsan_updated");
        user.setEmail("zhangsan_new@example.com");
        user.setAge(26);

        int result = userMapper.update(user);
        sqlSession.commit();

        System.out.println("更新用户成功，影响行数: " + result);
    }

    /**
     * 测试根据ID查询用户
     */
    @Test
    public void testSelectById() {
        Long id = 1L;
        User user = userMapper.selectById(id);

        System.out.println("查询用户: " + user);
    }

    /**
     * 测试根据用户名查询用户
     */
    @Test
    public void testSelectByUsername() {
        String username = "zhangsan";
        User user = userMapper.selectByUsername(username);

        System.out.println("查询用户: " + user);
    }

    /**
     * 测试查询所有用户
     */
    @Test
    public void testSelectAll() {
        List<User> users = userMapper.selectAll();

        System.out.println("用户总数: " + users.size());
        for (User user : users) {
            System.out.println(user);
        }
    }

    /**
     * 测试根据条件查询用户
     */
    @Test
    public void testSelectByCondition() {
        User condition = new User();
        condition.setUsername("zhang");
        condition.setAge(25);

        List<User> users = userMapper.selectByCondition(condition);

        System.out.println("查询结果数: " + users.size());
        for (User user : users) {
            System.out.println(user);
        }
    }

    /**
     * 测试统计用户总数
     */
    @Test
    public void testCount() {
        long count = userMapper.count();

        System.out.println("用户总数: " + count);
    }
}
