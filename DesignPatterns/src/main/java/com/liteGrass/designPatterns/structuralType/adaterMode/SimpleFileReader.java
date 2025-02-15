package com.liteGrass.designPatterns.structuralType.adaterMode;

/**
 * @Author liteGrass
 * @Date 2025-02-05  22:15
 * @Description
 */
public class SimpleFileReader implements FileReader {

    @Override
    public String reader(String fileName, int size) {
        System.out.println("读取文件内容" + fileName + "读取多少字节" + size);
        return "文件内容";
    }
}
