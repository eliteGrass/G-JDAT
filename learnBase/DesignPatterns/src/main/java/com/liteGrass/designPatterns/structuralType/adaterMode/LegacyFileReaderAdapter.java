package com.liteGrass.designPatterns.structuralType.adaterMode;

/**
 * @Author liteGrass
 * @Date 2025-02-05  22:20
 * @Description
 */
public class LegacyFileReaderAdapter implements FileReader {

    private LegacyFileReader legacyFileReader;

    private int size = 1024;

    public LegacyFileReaderAdapter(LegacyFileReader legacyFileReader) {
        this.legacyFileReader = legacyFileReader;
    }

    public LegacyFileReaderAdapter(LegacyFileReader legacyFileReader, int size) {
        this.legacyFileReader = legacyFileReader;
        this.size = size;
    }

    @Override
    public String reader(String fileName, int size) {
        String s = legacyFileReader.loadFile(fileName);
        // 获取前1024个字节，进行截取
        System.out.println("获取前" + size + "个字节");
        return "转换成功";
    }
}
