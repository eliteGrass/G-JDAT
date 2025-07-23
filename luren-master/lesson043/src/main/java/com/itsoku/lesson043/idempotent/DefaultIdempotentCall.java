package com.itsoku.lesson043.idempotent;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.itsoku.lesson043.common.BusinessException;
import com.itsoku.lesson043.common.BusinessExceptionUtils;
import com.itsoku.lesson043.idempotent.mapper.IdempotentCallMapper;
import com.itsoku.lesson043.idempotent.po.IdempotentCallPO;
import com.itsoku.lesson043.utils.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;

/**
 * 幂等默认实现
 *
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/2 12:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public abstract class DefaultIdempotentCall<I, O> implements IdempotentCall<I, O> {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    protected TransactionTemplate transactionTemplate;
    @Autowired
    protected IdempotentCallMapper idempotentCallMapper;

    @Override
    public IdempotentCallResponse<O> call(IdempotentCallRequest<I> request) {
        logger.info("request:{}", JSONUtils.toJsonStr(request));

        //1.插入幂等调用调用记日志
        IdempotentCallPO idempotentCallPO = this.getIdempotentCallPO(request);

        //2.判断记录状态（如果是 成功 || 失败，直接从json反序列化结果返回）
        if (IdempotentCallStatusEnum.success.getValue().equals(idempotentCallPO.getStatus()) ||
                IdempotentCallStatusEnum.fail.getValue().equals(idempotentCallPO.getStatus())) {
            String responseJson = idempotentCallPO.getResponseJson();
            return JSONUtils.toBean(responseJson, this.responseType(), true);
        }

        //3、执行业务
        IdempotentCallResponse<O> response = this.disposeBus(idempotentCallPO, request);
        return response;
    }

    /**
     * 是否有事务
     *
     * @return
     */
    protected boolean isTransaction() {
        return true;
    }

    private void updateIdempotentCallPOStatus(IdempotentCallPO idempotentCallPO, IdempotentCallResponse<O> response) {
        idempotentCallPO.setStatus(response.getStatus());
        idempotentCallPO.setResponseJson(JSONUtils.toJsonStr(response));
        int updateCount = this.idempotentCallMapper.updateById(idempotentCallPO);
        if (updateCount != 1) {
            throw BusinessExceptionUtils.businessException("系统繁忙，请稍后重试");
        }
    }


    private IdempotentCallPO getIdempotentCallPO(IdempotentCallRequest<I> request) {
        LambdaQueryWrapper<IdempotentCallPO> queryWrapper = Wrappers.lambdaQuery(IdempotentCallPO.class)
                .eq(IdempotentCallPO::getRequestId, request.getRequestId());
        IdempotentCallPO idempotentCallPO = this.idempotentCallMapper.findOne(queryWrapper);
        if (idempotentCallPO == null) {
            idempotentCallPO = this.insertIdempotentCallPO(request);
        }
        return idempotentCallPO;
    }

    private IdempotentCallPO insertIdempotentCallPO(IdempotentCallRequest<I> request) {
        IdempotentCallPO po = new IdempotentCallPO();
        po.setId(IdUtil.fastSimpleUUID());
        po.setRequestId(request.getRequestId());
        po.setStatus(IdempotentCallStatusEnum.init.getValue());
        po.setRequestJson(JSONUtils.toJsonStr(request));
        po.setVersion(0L);
        po.setCreateTime(LocalDateTime.now());
        this.idempotentCallMapper.insert(po);
        return po;
    }

    /**
     * 处理业务(需要在事务中执行)
     *
     * @param request
     * @return
     */
    protected IdempotentCallResponse<O> disposeBus(IdempotentCallPO idempotentCallPO, IdempotentCallRequest<I> request) {
        boolean transaction = this.isTransaction();
        if (!transaction) {
            //若没有事务，则会根据disposeLocalBus的返回值来确定业务是否执行成功，且 disposeLocalBus 内部需要自己确保幂等性
            IdempotentCallResponse<O> response = this.disposeLocalBus(request);
            //更新调用记录状态
            this.updateIdempotentCallPOStatus(idempotentCallPO, response);
            return response;
        } else {
            //有事务的情况下，下面会将业务方法 disposeLocalBus 放在spring事务中执行，disposeLocalBus 方法没有异常，则表示业务成功
            try {
                return this.transactionTemplate.execute(action -> {
                    IdempotentCallResponse<O> response = this.disposeLocalBus(request);
                    //没有异常，则将调用记录状态置为成功，并将结果序列化为json存储到表中
                    response.setStatus(IdempotentCallStatusEnum.success.getValue());
                    //将状态置为成功（这里面会有乐观锁[IdempotentCallPO有个version字段]作为条件更新，并发的时候也只有一个会成功）
                    this.updateIdempotentCallPOStatus(idempotentCallPO, response);
                    return response;
                });
            } catch (BusinessException e) {
                logger.error(e.getMessage(), e);
                //有异常，则将调用记录状态置为失败，并将结果序列化为json存储到表中
                IdempotentCallResponse<O> response = IdempotentCallResponse.fail(e.getCode(), e.getMessage());
                this.updateIdempotentCallPOStatus(idempotentCallPO, response);
                return response;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                //有异常，则将调用记录状态置为失败，并将结果序列化为json存储到表中
                IdempotentCallResponse<O> response = IdempotentCallResponse.fail(e.getMessage());
                this.updateIdempotentCallPOStatus(idempotentCallPO, response);
                return response;
            }
        }
    }

    /**
     * 处理本地业务，由子类实现
     *
     * @param request
     * @return
     */
    protected abstract IdempotentCallResponse<O> disposeLocalBus(IdempotentCallRequest<I> request);

    /**
     * 响应结果类型
     *
     * @return
     */
    protected abstract TypeReference<IdempotentCallResponse<O>> responseType();

}
