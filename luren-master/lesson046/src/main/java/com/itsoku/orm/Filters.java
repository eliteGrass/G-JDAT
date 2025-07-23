package com.itsoku.orm;

import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/9 20:43 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface Filters {
    Predicate<TableColumn> UPDATE_ABLE_FILTER = tableColumn -> !tableColumn.isPk() && !tableColumn.isVersioned();
    Predicate<TableColumn> ID_FILTER = tableColumn -> tableColumn.isPk();
    Predicate<TableColumn> ID_AND_VERSION_FILTER = tableColumn -> tableColumn.isPk() || tableColumn.isVersioned();

    class NonNullFilter implements Predicate<TableColumn> {

        private final Object obj;

        public NonNullFilter(Object obj) {
            this.obj = obj;
        }

        @Override
        public boolean test(TableColumn tableColumn) {
            Object value = ReflectionUtils.getValue(this.obj, tableColumn.getJavaName());
            return value != null;
        }
    }

    class UpdateNonNullFilter implements Predicate<TableColumn> {
        private final NonNullFilter nonNullFilter;

        public UpdateNonNullFilter(Object object) {
            this.nonNullFilter = new NonNullFilter(object);
        }

        @Override
        public boolean test(TableColumn column) {
            if (column.isPk()) {
                return false;
            }
            return nonNullFilter.test(column);
        }
    }

    class IncludeFilter implements Predicate<TableColumn> {
        private final Collection<String> includes;

        public IncludeFilter(@NonNull Collection<String> includes) {
            this.includes = includes;
        }

        @Override
        public boolean test(TableColumn column) {
            return includes.contains(column.getJavaName());
        }
    }

}
