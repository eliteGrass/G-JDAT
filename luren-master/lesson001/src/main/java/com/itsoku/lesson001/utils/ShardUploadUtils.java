package com.itsoku.lesson001.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/3/26 23:11 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class ShardUploadUtils {

    /**
     * 当文件不存在的时候创建文件
     *
     * @param file
     * @throws IOException
     */
    public static File createFileNotExists(File file) throws IOException {
        if (!file.exists()) {
            FileUtils.forceMkdirParent(file);
            file.createNewFile();
        }
        return file;
    }

    /**
     * 获取分片数量
     *
     * @param fileSize 文件大小（byte）
     * @param partSize 分片大小（byte）
     * @return
     */
    public static int shardNum(long fileSize, long partSize) {
        if (fileSize % partSize == 0) {
            return (int) (fileSize / partSize);
        } else {
            return (int) (fileSize / partSize) + 1;
        }
    }
}
