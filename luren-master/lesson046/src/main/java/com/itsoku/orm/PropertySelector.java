package com.itsoku.orm;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 对象的属性选择器，当includes 和 excludes 同时设置时，以excludes为准;当includes和excludes都不设置时，将返回全部属性
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/10 13:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class PropertySelector implements Serializable {
    /**
     * 包含哪些属性
     */
    private Set<String> includes;
    /**
     * 不包含哪些属性
     */
    private Set<String> excludes;

    public PropertySelector include(String... includes) {
        if (includes == null || includes.length == 0) {
            return this;
        }
        if (excludes != null && !excludes.isEmpty()) {
            throw new RuntimeException("you can't both assign excludes and includes");
        }
        if (this.includes == null) {
            this.includes = new HashSet<>();
        }
        this.includes.addAll(Arrays.asList(includes));
        return this;
    }

    public PropertySelector exclude(String... excludes) {
        if (excludes == null || excludes.length == 0) {
            return this;
        }
        if (includes != null && !includes.isEmpty()) {
            throw new RuntimeException("you can't both assign includes and excludes");
        }
        if (this.excludes == null) {
            this.excludes = new HashSet<>();
        }
        this.excludes.addAll(Arrays.asList(excludes));
        return this;
    }

    public Set<String> getIncludes() {
        return includes;
    }

    public Set<String> getExcludes() {
        return excludes;
    }
}
