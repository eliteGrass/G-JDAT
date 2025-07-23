import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BatchInsertExample {

    public static void main(String[] args) throws SQLException {
        String jdbcUrl = "jdbc:mysql://localhost:3306/luren?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8";
        String username = "root";
        String password = "root123";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(jdbcUrl, username, password);

            // SQL插入语句，使用占位符来防止SQL注入
            String sql = "INSERT INTO t_demo(c1) VALUES (?)";

            // 创建PreparedStatement对象，并设置为批处理模式
            preparedStatement = connection.prepareStatement(sql);
            for (int j = 0; j < 2000; j++) {
                connection.setAutoCommit(false); // 设置为手动提交

                long c1 = System.currentTimeMillis();
                // 循环添加多个批处理操作
                for (int i = 0; i < 5000; i++) {
                    // 设置占位符的值
                    preparedStatement.setLong(1, c1 + 1);
                    // 添加到批处理中
                    preparedStatement.addBatch();
                }

                // 执行批处理
                int[] batchResults = preparedStatement.executeBatch();
                System.out.println(batchResults.length);
                // 手动提交事务
                connection.commit();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // 发生异常时回滚事务
            if (connection != null) {
                connection.rollback();
            }
        } finally {
            // 关闭PreparedStatement和Connection
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
}