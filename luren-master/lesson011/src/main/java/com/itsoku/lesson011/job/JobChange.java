package com.itsoku.lesson011.job;

import com.itsoku.lesson011.dto.Job;
import lombok.Data;

import java.util.List;

/**
 * job变更信息
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/3 0:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Data
public class JobChange {
    //新增的job
    private List<Job> addJobList;
    //删除的job
    private List<Job> deleteJobList;
    //更新的job
    private List<Job> updateJobList;
}
