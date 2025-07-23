import com.itsoku.orm.SqlBuilder;
import org.apache.ibatis.javassist.compiler.ast.Variable;
import org.junit.jupiter.api.Test;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/9 22:54 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class SqlBuilderTest {
    SqlBuilder sqlBuilder = new SqlBuilder();

    @Test
    public void testSelect() {
        sqlBuilder.select("id", "no", "name", "sex", "birthday");
        sqlBuilder.from("t_student s");
        sqlBuilder.where(" and s.id = ?");
        sqlBuilder.where(" AND s.name like ?");
        System.out.println(sqlBuilder.toString());
    }

    @Test
    public void testSelect2() {
        sqlBuilder.from("t_student s");
        sqlBuilder.select("id", "no", "name", "sex", "birthday");
        sqlBuilder.where(" s.id = ?");
        sqlBuilder.where(" or s.name like ?");
        System.out.println(sqlBuilder.toString());
    }

    @Test
    public void testUpdate() {
        sqlBuilder.update("t_student")
                .set("version=version+1")
                .set("create_time=current_timestamp")
                .where("id=#{id}")
                .where("is_deleted = 0");
        System.out.println(sqlBuilder.toString());
    }

    @Test
    public void testInsert() {
        sqlBuilder.insert("t_user");
        sqlBuilder.values("id", "#{po.id}");
        sqlBuilder.values("version", "#{po.version}");
        System.out.println(sqlBuilder.toString());
    }
}
