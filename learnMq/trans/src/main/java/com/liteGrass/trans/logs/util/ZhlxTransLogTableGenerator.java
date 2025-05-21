package com.liteGrass.trans.logs.util;


/**
 * Copyright (c) 2024 Huahui Information Technology Co., LTD.
 * and China Nuclear Engineering & Construction Corporation Limited (Loongxin Authors).
 * All Rights Reserved.
 * <p>
 * This software is part of the Zhonghe Loongxin Development Platform (the "Platform").
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * <p>
 * For more information about the Platform, terms and conditions, and user licenses,
 * please visit our official website at www.icnecc.com.cn or contact us directly.
 */

import com.liteGrass.trans.config.prop.ZhlxTransLogProp;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

/**
 * @ClassName ZhlxTransLogTableGenerator
 * @Company Huahui Information Technology Co., LTD.
 * @Author liteGrass
 * @Date 2025/4/24 21:25
 * @Description
 */
public class ZhlxTransLogTableGenerator {

    private final DataSource dataSource;
    private final ZhlxTransLogProp properties;

    public ZhlxTransLogTableGenerator(DataSource dataSource, ZhlxTransLogProp properties) {
        this.dataSource = dataSource;
        this.properties = properties;
    }

    public void createTableIfNotExists() {
        try (Connection conn = dataSource.getConnection()) {
            String dbType = getDatabaseType(conn);
            if (!isTableExists(conn, properties.getTableName())) {
                String ddl = loadDDLScript(dbType);
                executeDDL(conn, ddl);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create log table", e);
        }
    }

    private String getDatabaseType(Connection conn) throws SQLException {
        String dbName = conn.getMetaData().getDatabaseProductName().toLowerCase();
        if (dbName.contains("mysql")) {
            return "mysql";
        }
        if (dbName.contains("postgresql")) {
            return "postgresql";
        }
        if (dbName.contains("h2")) {
            return "h2";
        }
        throw new UnsupportedOperationException("Unsupported database: " + dbName);
    }

    private boolean isTableExists(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        try (ResultSet rs = metaData.getTables(null, null, tableName, new String[]{"TABLE"})) {
            return rs.next();
        }
    }

    private String loadDDLScript(String dbType) {
        String path = String.format("META-INF/scripts/%s.sql", dbType);
        Resource resource = new ClassPathResource(path);
        try (Scanner scanner = new Scanner(resource.getInputStream())) {
            return scanner.useDelimiter("\\A").next();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load DDL script for " + dbType, e);
        }
    }

    private void executeDDL(Connection conn, String ddl) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(ddl.replace("${tableName}", properties.getTableName()));
        }
    }
}
