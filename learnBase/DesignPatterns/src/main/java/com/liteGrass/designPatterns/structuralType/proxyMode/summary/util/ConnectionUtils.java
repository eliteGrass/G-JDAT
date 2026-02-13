package com.liteGrass.designPatterns.structuralType.proxyMode.summary.util;


import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: ConnectionUtils
 * @Author: liteGrass
 * @Date: 2025/11/4 15:09
 * @Description: 连接相关对象
 */
public class ConnectionUtils {

    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();


    /**
     * 懒加载 --- 先进性代理对象的创建，不进行时机对象的创建，在使用的时候在进行创建
     *
     * @return
     */
    public static Connection getConnection() {
        Connection connection = threadLocal.get();
        if (ObjectUtil.isNotNull(connection)) {
            return connection;
        }
        // 创建代理对象
        ConnectionProxy connectionProxy = new ConnectionProxy();
        threadLocal.set(connectionProxy);
        return connectionProxy;
    }


    /**
     * 特别重的一个连接对象
     */
    public static interface Connection {

        void beginTransaction();

        void commit();

        void rollback();

    }

    @Slf4j
    static class MyConnection implements Connection {

        @Override
        public void beginTransaction() {
            log.info("beginTransaction --- 开启事务");
        }

        @Override
        public void commit() {
            log.info("commit --- 提交事务");
        }

        @Override
        public void rollback() {
            log.info("rollback --- 回滚事务");
        }

    }

    @Slf4j
    static class ConnectionProxy implements Connection {

        private Connection connection;

        private Connection getConnection() {
            if (ObjectUtil.isEmpty(connection)) {
                log.info("ConnectionProxy --- 进行懒加载");
                connection = new MyConnection();
            }
            return connection;
        }


        @Override
        public void beginTransaction() {
            Connection conn = getConnection();
            log.info("ConnectionProxy --- 开启事务");
        }

        @Override
        public void commit() {
            Connection conn = getConnection();
            log.info("ConnectionProxy --- 提交事务");
        }

        @Override
        public void rollback() {
            Connection conn = getConnection();
            log.info("ConnectionProxy --- 回滚事务");
        }

    }

}
