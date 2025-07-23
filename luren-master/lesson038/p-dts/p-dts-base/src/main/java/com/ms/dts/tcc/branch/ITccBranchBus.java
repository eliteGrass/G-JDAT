package com.ms.dts.tcc.branch;

import cn.hutool.core.lang.TypeReference;
import com.itsoku.common.ResultDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * <b>description</b>：tcc事务分支具体业务代码需要实现的接口，框架会在该接口上加拦截器进行处理 <br>
 * <b>time</b>：2024/4/29 15:30 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface ITccBranchBus<T extends ITccBranchRequest> extends ITccBranch<T>, InitializingBean {

    Logger LOGGER = LoggerFactory.getLogger(ITccBranchBus.class);

    /**
     * fast json数据转换用的
     * <p>
     * 使用的地方见：com.ms.dts.business.service.tcc.interceptor.TccBranchStepBeforeInterceptor
     *
     * @return
     */
    TypeReference<ResultDto<TccBranchContext<T>>> getTccBranchContextTypeReference();

    @Override
    default void afterPropertiesSet(){
        try {
            Class.forName("com.ms.dts.business.service.tcc.interceptor.TccBranchStepInterceptor");
        } catch (ClassNotFoundException e) {
            String msg = "<!-- tcc事务处理组件start -->\n" +
                    "<dependency>\n" +
                    "    <groupId>com.itsoku</groupId>\n" +
                    "    <artifactId>p-dts-business-service-starter</artifactId>\n" +
                    "    <version>${project.version}</version>\n" +
                    "</dependency>\n" +
                    "<!-- tcc事务处理组件end -->";
            LOGGER.error("当前项目中必须引入maven配置:\n\n{}\n\n", msg);
            LOGGER.error(e.getMessage(), e);
            System.exit(-1);
        }
    }
}
