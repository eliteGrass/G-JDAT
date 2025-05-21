package com.liteGrass.designPatterns.structuralType.adaterMode;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author liteGrass
 * @Date 2025-02-05  22:25
 * @Description
 */
public class CachingFileReaderDecorator implements FileReader {

    private FileReader fileReader;

    private Map<String, String> cache = new HashMap<>();

    public CachingFileReaderDecorator(FileReader fileReader) {
        this.fileReader = fileReader;
    }

    @Override
    public String reader(String fileName, int size) {
        if (cache.containsKey(fileName)) {
            System.out.println("从缓存中进行获取");
            return cache.get(fileName);
        }
        String content = fileReader.reader(fileName, size);
        cache.put(fileName, content);
        return content;
    }
}
