package com.liteGrass.learnNetty.baseNetty.baseNIO;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName: RandomAccessFileDownLoadTread
 * @Author: liteGrass
 * @Date: 2025/6/30 10:12
 * @Description: 多线程文件下载
 */
public class RandomAccessFileDownLoadTread {

    // 虚拟线程
    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
    // 源文件
    private final File srcFile;
    // 目标文件
    private final File destFile;
    // 最大线程数
    private final long maxThreadSize = 30;
    // 每个线程最大读取的大小10M
    private final long maxReadSize = 1024 * 1024 * 10;
    // 缓冲区大小
    private final int byteSize = 1024 * 64;
    // 文件总大小
    private final long totalSize;
    // 是否开启多线程下载
    private boolean isMultiThread = false;
    // 开启的线程数
    private long threadNum = 0;

    public RandomAccessFileDownLoadTread(File srcFile, File destFile) {
        this.srcFile = srcFile;
        this.destFile = destFile;
        this.totalSize = srcFile.length();
        // 计算每次读取大小，向上取整
        this.threadNum = Math.min((totalSize + maxReadSize - 1) / maxReadSize, maxThreadSize);
    }

    public void down() throws IOException {
        if (threadNum <= 1) {
            singleThreadDownload();
        } else {
            multiThreadDownload();
        }
        // 进行关闭操作
        // shutdown：执行已经提交得任务，并等带提交得任务执行完成
        // shutdownNow：停止所以正在执行得任务，并且不在接受新任务，尝试中断正在执行得任务，返回未执行得任务列表
        executorService.shutdown();
    }



    /**
     * 多线程精心读写
     * 注意点
     * 1、每个线程创建自己得RandomAccessFile，房子进行seek得时候同时操作一个
     * 2、使用CountDownLatch进行等待所有线程完成
     * 3、结束位置进行计算，不能一直read
     */
    public void multiThreadDownload() {
        CountDownLatch countDownLatch = new CountDownLatch((int) threadNum);
        // 开始进行异步操作，开启多线程模式
        for (long l = 0; l < threadNum; l++) {
            final long startPos = maxReadSize * l;
            final long endPos = (l == threadNum - 1) ? totalSize : startPos + maxReadSize;
            executorService.submit(() -> {
                try (
                        RandomAccessFile src = new RandomAccessFile(srcFile, "r");
                        RandomAccessFile dest = new RandomAccessFile(destFile, "rw");
                ) {
                    // 开始进行相关操作，分段进行下载文件
                    byte[] buffer = new byte[byteSize];
                    // 设置读取位置
                    src.seek(startPos);
                    dest.seek(startPos);
                    long remaining = endPos - startPos;
                    while (remaining > 0) {
                        int bytesToRead = (int) Math.min(buffer.length, remaining);
                        int bytesRead = src.read(buffer, 0, bytesToRead);
                        // 读取不到文件得时候进行操作
                        if (bytesRead < 0) {
                            break;
                        }
                        dest.write(buffer, 0, bytesRead);
                        remaining -= bytesRead;
                    }
                } catch (IOException e) {
                    throw new RuntimeException("线程下载失败: " + startPos + "-" + endPos, e);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("下载被中断", e);
        }
    }


    public void singleThreadDownload() throws IOException {
        try (
                RandomAccessFile src = new RandomAccessFile(srcFile, "r");
                RandomAccessFile dest = new RandomAccessFile(destFile, "rw");
        ) {
            byte[] bytes = new byte[byteSize];
            int bytesRead = 0;
            while ((bytesRead = src.read(bytes)) != -1) {
                // 开始进行写入
                dest.write(bytes, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException("单线程进行下载失败", e);
        }

    }

}
