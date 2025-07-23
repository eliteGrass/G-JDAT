package com.itsoku.lesson024.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itsoku.lesson024.mapper.OperLogMapper;
import com.itsoku.lesson024.po.OperLogPO;
import com.itsoku.lesson024.service.IOperLogService;
import org.springframework.stereotype.Service;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/22 13:40 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Service
public class OperLogServiceImpl extends ServiceImpl<OperLogMapper, OperLogPO> implements IOperLogService {

}
