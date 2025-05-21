package com.liteGrass.designPatterns.structuralType.adaterMode;

/**
 * @Author liteGrass
 * @Date 2025-02-05  22:18
 * @Description
 */
public class LegacyFileReaderImpl implements LegacyFileReader {
    @Override
    public String loadFile(String fileName) {
        System.out.println("旧方式读取文件" + fileName);
        return "旧文件内容";
    }
}
