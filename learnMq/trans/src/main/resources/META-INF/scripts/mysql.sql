CREATE TABLE IF NOT EXISTS ${tableName} (
    id varchar(255) PRIMARY KEY,
    method_name VARCHAR(255) NOT NULL,
    method_params TEXT,
    method_result TEXT,
    operate_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    trans_status VARCHAR(255) NOT NULL,
    trans_id VARCHAR(255) NOT NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;