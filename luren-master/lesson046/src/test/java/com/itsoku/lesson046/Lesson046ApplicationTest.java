package com.itsoku.lesson046;

import cn.hutool.json.JSONUtil;
import com.itsoku.lesson046.po.AccountPO;
import com.itsoku.lesson046.po.UserPO;
import com.itsoku.lesson046.service.IAccountService;
import com.itsoku.lesson046.service.IUserService;
import com.itsoku.orm.Criteria;
import com.itsoku.orm.CriteriaBuilder;
import com.itsoku.orm.Joint;
import com.itsoku.orm.page.PageQuery;
import com.itsoku.orm.page.PageResult;
import com.itsoku.utils.CollUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/10 11:58 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@SpringBootTest
@Slf4j
public class Lesson046ApplicationTest {
    @Autowired
    private IUserService userService;
    @Autowired
    private IAccountService accountService;

    /**
     * 查询所有用户
     */
    @Test
    public void test001() {
        List<UserPO> userPOList = this.userService.find(null);
        log.info("查询所有用户:{}", JSONUtil.toJsonPrettyStr(userPOList));
    }

    /**
     * 查询需要的字段(id、name)
     */
    @Test
    public void test002() {
        Criteria<UserPO> criteria = CriteriaBuilder.from(UserPO.class).includes(UserPO::getId, UserPO::getUserName).build();
        //查询所有用户
        List<UserPO> userPOList = this.userService.find(criteria);
        log.info("查询需要的字段(id、name):{}", JSONUtil.toJsonPrettyStr(userPOList));
    }

    /**
     * 复杂条件分页查询：select id as id,user_name as userName,age as age from t_user where id >= 1 and (age<20 or age>30) order by id asc
     */
    @Test
    public void test003() {
        Criteria<UserPO> criteria = CriteriaBuilder.from(UserPO.class)
                .greaterEqual(UserPO::getId, 1L)
                .and(CriteriaBuilder.from(UserPO.class).lessThan(UserPO::getAge, 20).greaterThanIfPresent(Joint.OR, UserPO::getAge, 30))
                .asc(UserPO::getId)
                .build();
        List<UserPO> userPOList = this.userService.find(criteria);
        log.info("复杂条件查询:{}", JSONUtil.toJsonPrettyStr(userPOList));
    }


    /**
     * 根据id查询
     */
    @Test
    public void test004() {
        Long userId = 1L;
        UserPO userPO = this.userService.findById(userId);
        log.info("根据id查询:{}", JSONUtil.toJsonPrettyStr(userPO));
    }

    /**
     * 根据id列表批量查询
     */
    @Test
    public void test005() {
        List<Long> userIdList = Arrays.asList(1L, 2L);
        List<UserPO> userPOList = this.userService.findByIds(userIdList);
        log.info("根据id列表批量查询:{}", JSONUtil.toJsonPrettyStr(userPOList));
    }

    /**
     * 根据主键列表查询记录，返回Map，key为主键，map为记录
     */
    @Test
    public void test006() {
        List<Long> userIdList = Arrays.asList(1L, 2L);
        Map<Long, UserPO> userPOMap = this.userService.findMapByIds(userIdList);
        log.info("根据id列表批量查询:{}", JSONUtil.toJsonPrettyStr(userPOMap));
    }

    /**
     * 查询满足条件的一条记录
     */
    @Test
    public void test007() {
        Criteria<UserPO> criteria = CriteriaBuilder.from(UserPO.class).desc(UserPO::getId).build();
        UserPO userPO = this.userService.findOne(criteria);
        log.info("查询id最大的一条记录:{}", JSONUtil.toJsonPrettyStr(userPO));
    }

    /**
     * 查询满足条件的记录数量
     */
    @Test
    public void test020() {
        // 查询年龄大于30的用户数量
        Criteria<UserPO> criteria = CriteriaBuilder.from(UserPO.class).greaterEqual(UserPO::getAge, 30L).build();
        long count = this.userService.count(criteria);
        log.info("查询年龄大于30的用户数量:{}", count);
    }

    /**
     * 分页查询用户记录：select id as id,user_name as userName,age as age from t_user order by id asc limit 1,5
     */
    @Test
    public void test030() {
        Criteria<UserPO> criteria = CriteriaBuilder.from(UserPO.class).asc(UserPO::getId).build();
        PageResult<UserPO> page = this.userService.findPage(PageQuery.of(1, 5), criteria);
        log.info("按条件分页查询:{}", JSONUtil.toJsonPrettyStr(page));
    }

    /**
     * 按条件分页查询：select id as id,user_name as userName,age as age from t_user where id >= 1 LIMIT 2, 5
     */
    @Test
    public void test031() {
        Criteria<UserPO> criteria = CriteriaBuilder.from(UserPO.class).greaterEqual(UserPO::getId, 1L).desc(UserPO::getId).build();
        PageResult<UserPO> page = this.userService.findPage(PageQuery.of(2, 5, true), criteria);
        log.info("按条件分页查询:{}", JSONUtil.toJsonPrettyStr(page));
    }

    /**
     * 分页获取用户id：select id from t_user order by id asc
     */
    @Test
    public void test032() {
        Criteria<UserPO> criteria = CriteriaBuilder.from(UserPO.class).desc(UserPO::getId).build();
        int pageNum = 1;
        int pageSize = 5;
        while (true) {
            PageResult<Long> userIdPage = this.userService
                    .findPage(PageQuery.of(pageNum, pageSize, true), criteria)
                    .map(UserPO::getId);
            log.info("第{}页，每页{}条，查询结果：{}", pageNum, pageSize, JSONUtil.toJsonStr(userIdPage));

            if (CollUtils.isEmpty(userIdPage.getList()) || CollUtils.size(userIdPage.getList()) < pageSize) {
                break;
            }
            pageNum++;
        }
    }


    /**
     * 插入记录，id是自动增长类型的
     *
     * @see UserPO#getId() 上标注有 @TableId(type = IdType.AUTO)
     */
    @Test
    public void test101() {
        UserPO userPO = UserPO.builder().userName("路人").age(40).build();
        int count = this.userService.insert(userPO);
        log.info("count:{},userPO:{}", count, JSONUtil.toJsonPrettyStr(userPO));
    }

    /**
     * 插入记录，id 手动填充
     */
    @Test
    public void test102() {
        AccountPO accountPO = AccountPO.builder()
                .id("2")
                .name("luren")
                .balance(new BigDecimal("99.88"))
                .build();
        int count = this.accountService.insert(accountPO);
        log.info("count:{}", count);
        //查询最新记录
        accountPO = this.accountService.findById(accountPO.getId());
        log.info("accountPO:{}", JSONUtil.toJsonPrettyStr(accountPO));
    }

    /**
     * 插入记录，id 手动填充
     */
    @Test
    public void test104() {
        List<UserPO> poList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            poList.add(UserPO.builder().userName("批量插入用户-"+i).age(20+i).build());
        }
        this.userService.insertBatch(poList);

        //获取插入的用户：select id as id,user_name as userName,age as age from t_user where user_name like ?;
        Criteria<UserPO> criteria = CriteriaBuilder.from(UserPO.class).contains(UserPO::getUserName, "批量插入用户").build();
        List<UserPO> userPOList = this.userService.find(criteria);
        log.info("userPOList:{}", JSONUtil.toJsonPrettyStr(userPOList));
    }

    /**
     * 按照主键更新一个对象
     */
    @Test
    public void test201() {
        String accountId = "1";
        //更新前
        AccountPO accountPO = this.accountService.findById(accountId);
        log.info("更新前 accountPO：{}", JSONUtil.toJsonPrettyStr(accountPO));

        //更新
        accountPO.setBalance(new BigDecimal("188.88"));
        int count = this.accountService.update(accountPO);
        log.info("count:{}", count);

        //更新后，查询最新记录
        accountPO = this.accountService.findById(accountId);
        log.info("更新后 accountPO:{}", JSONUtil.toJsonPrettyStr(accountPO));
    }

    /**
     * 按照主键更新一个对象，只更新非 null 字段
     */
    @Test
    public void test202() {
        String accountId = "1";
        AccountPO accountPO = AccountPO.builder().id(accountId).balance(new BigDecimal("999.99")).build();

        //只更新不为空的字段
        int count = this.accountService.updateNonNull(accountPO);
        log.info("count:{}", count);

        //更新后
        accountPO = this.accountService.findById(accountId);
        log.info("accountPO:{}", JSONUtil.toJsonPrettyStr(accountPO));
    }

    /**
     * 按照主键更新一个对象，只更新指定的属性
     */
    @Test
    public void test203() {
        String accountId = "1";
        AccountPO accountPO = this.accountService.findById(accountId);
        log.info("更新前 accountPO：{}", JSONUtil.toJsonPrettyStr(accountPO));

        //只更新用户的余额
        int count = this.accountService.updateWith(accountPO, AccountPO::getBalance);
        log.info("count:{}", count);

        //更新后
        accountPO = this.accountService.findById(accountId);
        log.info("更新后 accountPO：{}", JSONUtil.toJsonPrettyStr(accountPO));
    }


    /**
     * 按照主键和version字段作为条件更新
     */
    @Test
    public void test204() {
        String accountId = "1";
        //更新前
        AccountPO accountPO = this.accountService.findById(accountId);
        log.info("更新前 accountPO：{}", JSONUtil.toJsonPrettyStr(accountPO));

        //更新，条件为：id = #{accountId} and version = #{version}
        accountPO.setBalance(new BigDecimal("188.88"));
        int count = this.accountService.optimisticUpdate(accountPO);
        log.info("count:{}", count);

        //更新后
        accountPO = this.accountService.findById(accountId);
        log.info("更新后 accountPO:{}", JSONUtil.toJsonPrettyStr(accountPO));
    }

    /**
     * 按照主键和version字段作为条件更新，只更新非 null 字段
     */
    @Test
    public void test205() {
        //根据id和version作为条件，更新用户的余额
        String accountId = "1";
        AccountPO accountPO = AccountPO.builder()
                .id(accountId)
                .balance(new BigDecimal("999.99"))
                .version(0L)
                .build();

        int count = this.accountService.optimisticUpdateNonNull(accountPO);
        log.info("count:{}", count);

        //更新后
        accountPO = this.accountService.findById(accountId);
        log.info("accountPO:{}", JSONUtil.toJsonPrettyStr(accountPO));
    }

    /**
     * 按照主键和version字段作为条件更新，只更新指定的属性
     */
    @Test
    public void test206() {
        String accountId = "1";
        //更新前
        AccountPO accountPO = this.accountService.findById(accountId);
        log.info("更新前 accountPO：{}", JSONUtil.toJsonPrettyStr(accountPO));

        //只更新 balance，更新条件为：id = #{accountId} and version = #{version}
        accountPO.setBalance(new BigDecimal("188.88"));
        int count = this.accountService.optimisticUpdateWith(accountPO, AccountPO::getBalance);
        log.info("count:{}", count);

        //更新后
        accountPO = this.accountService.findById(accountId);
        log.info("更新后 accountPO:{}", JSONUtil.toJsonPrettyStr(accountPO));
    }

    /**
     * 根据主键删除记录
     */
    @Test
    public void test301() {
        int count = this.userService.deleteById(1L);
        log.info("根据主键删除记录，count：{}", count);
    }

    /**
     * 根据主键批量删除记录
     */
    @Test
    public void test302() {
        List<Long> idList = CollUtils.newArrayList(1L, 2L, 3L);
        int count = this.userService.deleteByIds(idList);
        log.info("根据id列表批量删除记录，count：{}", count);
    }

    /**
     * 根据条件删除记录
     */
    @Test
    public void test303() {
        // 删除用户名中包含 路人 的用户
        Criteria<UserPO> criteria = CriteriaBuilder.from(UserPO.class).contains(UserPO::getUserName, "路人").build();
        int count = this.userService.delete(criteria);
        log.info("根据条件删除记录，count：{}", count);
    }
}
