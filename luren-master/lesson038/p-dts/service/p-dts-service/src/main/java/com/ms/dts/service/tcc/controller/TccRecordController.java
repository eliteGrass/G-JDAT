package com.ms.dts.service.tcc.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.itsoku.common.ResultDto;
import com.itsoku.enums.TccBusTypeEnum;
import com.itsoku.utils.FrameUtils;
import com.itsoku.utils.ResultUtils;
import com.ms.dts.base.ResultStatus;
import com.ms.dts.base.model.TccBranchLogPO;
import com.ms.dts.base.model.TccDisposeLogPO;
import com.ms.dts.base.model.TccRecordPO;
import com.ms.dts.service.base.service.ITccBranchLogService;
import com.ms.dts.service.base.service.ITccDisposeLogService;
import com.ms.dts.service.base.service.ITccRecordService;
import com.ms.dts.service.tcc.bus.ITccBus;
import com.ms.dts.tcc.enums.TccBranchMethodEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/17 11:25 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Controller("com.ms.dts.service.tcc.controller.TccRecordController")
@RequestMapping("/tccRecord/")
public class TccRecordController {
    @Autowired
    private ITccRecordService tccRecordService;

    @Autowired
    private ITccDisposeLogService tccDisposeLogService;

    @Autowired
    private ITccBranchLogService tccBranchLogService;

    @Autowired
    private ITccBus tccBus;

    /**
     * tcc分布式事务记录列表
     *
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String list(Model model) {
        LambdaQueryWrapper<TccRecordPO> queryWrapper = Wrappers.lambdaQuery(TccRecordPO.class)
                .orderByDesc(TccRecordPO::getAddtime, TccRecordPO::getId);
        List<TccRecordPO> list = this.tccRecordService.list(queryWrapper);
        for (TccRecordPO tccRecordPO : list) {
            tccRecordPO.putExtData("status", ResultStatus.getDesc(tccRecordPO.getStatus()));
            tccRecordPO.putExtData("busType", TccBusTypeEnum.getDesc(tccRecordPO.getBusType()));
            tccRecordPO.putExtData("nextDisposeTime", tccRecordPO.getStatus() == 0 ? FrameUtils.timestampToDateString(tccRecordPO.getNextDisposeTime()) : "");
            tccRecordPO.putExtData("addtime", FrameUtils.timestampToDateString(tccRecordPO.getAddtime()));
            tccRecordPO.putExtData("uptime", FrameUtils.timestampToDateString(tccRecordPO.getUptime()));
        }
        model.addAttribute("tccRecordList", list);
        return "tccRecordList";
    }

    /**
     * 详情
     *
     * @param tccRecordId
     * @param model
     * @return
     */
    @GetMapping("/detail")
    public String list(@RequestParam("tccRecordId") String tccRecordId, Model model) {
        TccRecordPO tccRecordPO = this.tccRecordService.getById(tccRecordId);
        tccRecordPO.putExtData("status", ResultStatus.getDesc(tccRecordPO.getStatus()));
        tccRecordPO.putExtData("busType", TccBusTypeEnum.getDesc(tccRecordPO.getBusType()));
        tccRecordPO.putExtData("nextDisposeTime", tccRecordPO.getStatus() == 1 ? "" : FrameUtils.timestampToDateString(tccRecordPO.getNextDisposeTime()));
        tccRecordPO.putExtData("addtime", FrameUtils.timestampToDateString(tccRecordPO.getAddtime()));
        tccRecordPO.putExtData("uptime", FrameUtils.timestampToDateString(tccRecordPO.getUptime()));
        model.addAttribute("tccRecordPO", tccRecordPO);

        List<TccBranchLogPO> tccBranchLogPOList = this.tccBranchLogService.getListByTccRecordId(tccRecordId);
        for (TccBranchLogPO tccBranchLogPO : tccBranchLogPOList) {
            tccBranchLogPO.putExtData("method", TccBranchMethodEnums.getMethodName(tccBranchLogPO.getMethod()));
            tccBranchLogPO.putExtData("status", ResultStatus.getDesc(tccBranchLogPO.getStatus()));
            tccBranchLogPO.putExtData("addtime", FrameUtils.timestampToDateString(tccBranchLogPO.getAddtime()));
            tccBranchLogPO.putExtData("uptime", FrameUtils.timestampToDateString(tccBranchLogPO.getUptime()));
        }
        model.addAttribute("tccBranchLogPOList", tccBranchLogPOList);

        List<TccDisposeLogPO> tccDisposeLogPOList = this.tccDisposeLogService.getListByTccRecordId(tccRecordId);
        for (TccDisposeLogPO tccDisposeLogPO : tccDisposeLogPOList) {
            tccDisposeLogPO.putExtData("costtime", tccDisposeLogPO.getEndtime() - tccDisposeLogPO.getStarttime());
            tccDisposeLogPO.putExtData("addtime", FrameUtils.timestampToDateString(tccDisposeLogPO.getAddtime()));
            tccDisposeLogPO.putExtData("starttime", FrameUtils.timestampToDateString(tccDisposeLogPO.getStarttime()));
            tccDisposeLogPO.putExtData("endtime", FrameUtils.timestampToDateString(tccDisposeLogPO.getEndtime()));
        }
        model.addAttribute("tccDisposeLogPOList", tccDisposeLogPOList);
        return "tccRecordDetail";
    }

    /**
     * 补偿
     *
     * @param tccRecordId
     * @return
     */
    @GetMapping("/recover")
    @ResponseBody
    public ResultDto<Boolean> recover(@RequestParam("tccRecordId") String tccRecordId) {
        this.tccBus.recover(tccRecordId);
        return ResultUtils.successData(true);
    }
}
