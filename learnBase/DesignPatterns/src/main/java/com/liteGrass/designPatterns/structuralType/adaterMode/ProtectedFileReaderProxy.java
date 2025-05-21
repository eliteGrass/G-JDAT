package com.liteGrass.designPatterns.structuralType.adaterMode;

/**
 * @Author liteGrass
 * @Date 2025-02-05  22:28
 * @Description
 */
public class ProtectedFileReaderProxy implements FileReader {

    private FileReader fileReader;


    public ProtectedFileReaderProxy(FileReader fileReader) {
        this.fileReader = fileReader;
    }

    @Override
    public String reader(String fileName, int size) {
        if (size > 1024) {
            throw new RuntimeException("读取文件过大，不允许进行");
        }
        return fileReader.reader(fileName, size);
    }
}
