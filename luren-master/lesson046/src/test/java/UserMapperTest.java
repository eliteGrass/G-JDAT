import com.itsoku.lesson046.mapper.UserMapper;
import com.itsoku.lesson046.po.UserPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/9 21:37 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Slf4j
public class UserMapperTest {
    static Configuration configuration;
    static SqlSessionFactory sqlSessionFactory;
    static UserMapper userMapper;

    @BeforeAll
    public static void before() throws IOException {
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis-config.xml"));
        configuration = sqlSessionFactory.getConfiguration();
        userMapper = sqlSessionFactory.openSession(true).getMapper(UserMapper.class);
    }

    @Test
    public void insert() {
        UserPO po = new UserPO();
        po.setUserName("路人");
        po.setAge(30);
        int count = userMapper.insert(po);
        log.info("count:{}", count);
        log.info("po:{}", po);
    }

    @Test
    public void insertBatch() {
        List<UserPO> userPOList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            userPOList.add(UserPO.builder().userName("路人-" + i).age(20 + i).build());
        }
        int count = userMapper.insertBatch(userPOList);
        log.info("count:{}", count);
    }
}
