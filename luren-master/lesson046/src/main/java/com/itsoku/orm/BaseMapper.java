package com.itsoku.orm;

import com.github.pagehelper.PageHelper;
import com.itsoku.lambda.LambdaUtils;
import com.itsoku.lambda.SFunction;
import com.itsoku.orm.page.PageQuery;
import com.itsoku.orm.page.PageResult;
import com.itsoku.utils.CollUtils;
import org.apache.ibatis.annotations.*;

import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Mapper 基类
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/10 13:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface BaseMapper<Id, T> {

    /**
     * 插入记录
     *
     * @param po
     * @return 影响行数
     */
    default int insert(T po) {
        // id是自动生成的
        if (ORMUtils.get(po.getClass()).getIdColumn().getIdType() == IdType.AUTO) {
            return this._insertAutoIncrement(po);
        }
        return this._insert(po);
    }

    /**
     * 批量插入
     *
     * @param poList
     * @return 影响行数
     */
    default int insertBatch(Collection<T> poList) {
        return this._insertBatch(poList);
    }

    /**
     * 按照主键更新一个对象
     *
     * @param po
     * @return 影响行数
     */
    default int update(T po) {
        return this._update(po, Filters.UPDATE_ABLE_FILTER, Filters.ID_FILTER);
    }

    /**
     * 按照主键更新一个对象，只更新非 null 字段
     *
     * @param po
     * @return 影响行数
     */
    default int updateNonNull(T po) {
        return this._update(po, new Filters.UpdateNonNullFilter(po).and(Filters.UPDATE_ABLE_FILTER), Filters.ID_FILTER);
    }

    /**
     * 按照主键更新一个对象，只更新指定的属性
     *
     * @param po
     * @param props 需更新的属性列表
     * @return 影响行数
     */
    default int updateWith(T po, SFunction<T, ?>... props) {
        if (props == null || props.length == 0) {
            throw new RuntimeException("The update column cannot be empty");
        }
        List<String> includes = Arrays.stream(props).map(LambdaUtils::getPropFromLambda).collect(Collectors.toList());
        return this._update(po, new Filters.IncludeFilter(includes).and(Filters.UPDATE_ABLE_FILTER), Filters.ID_FILTER);
    }

    /**
     * 按照主键和version字段作为条件更新
     *
     * @param po
     * @return 影响行数
     */
    default int optimisticUpdate(T po) {
        int update = _update(po, Filters.UPDATE_ABLE_FILTER, Filters.ID_AND_VERSION_FILTER);
        if (update == 1) {
            //更新成功，将版本号+1
            ORMUtils.setNewVersion(po);
        }
        return update;
    }

    /**
     * 按照主键和version字段作为条件更新，只更新非 null 字段
     *
     * @param po
     * @return 影响行数
     */
    default int optimisticUpdateNonNull(T po) {
        int update = _update(po, new Filters.UpdateNonNullFilter(po).and(Filters.UPDATE_ABLE_FILTER), Filters.ID_AND_VERSION_FILTER);
        if (update == 1) {
            //更新成功，将版本号+1
            ORMUtils.setNewVersion(po);
        }
        return update;
    }

    /**
     * 按照主键和version字段作为条件更新，只更新指定的属性
     *
     * @param po
     * @param props 需更新的属性列表
     * @return 影响行数
     */
    default int optimisticUpdateWith(T po, SFunction<T, ?>... props) {
        if (props == null || props.length == 0) {
            throw new RuntimeException("The update column cannot be empty");
        }
        List<String> includes = Arrays.stream(props).map(LambdaUtils::getPropFromLambda).collect(Collectors.toList());
        int update = _update(po, new Filters.IncludeFilter(includes).and(Filters.UPDATE_ABLE_FILTER), Filters.ID_AND_VERSION_FILTER);
        if (update == 1) {
            //更新成功，将版本号+1
            ORMUtils.setNewVersion(po);
        }
        return update;
    }

    /**
     * 根据主键删除记录
     *
     * @param id
     * @return 影响行数
     */
    default int deleteById(Id id) {
        Class<T> poClass = getPoClass();
        return this._deleteByCriteria(poClass, _fromId(id), new HashMap<>());
    }

    /**
     * 根据主键批量删除
     *
     * @param idList
     * @return 影响行数
     */
    default int deleteByIds(Collection<Id> idList) {
        if (idList == null || idList.isEmpty()) {
            return 0;
        }
        return this._deleteByCriteria(getPoClass(), _fromIds(idList), new HashMap<>());
    }

    /**
     * 根据条件删除
     *
     * @param criteria
     * @return 影响行数
     */
    default int delete(Criteria<T> criteria) {
        return this._deleteByCriteria(getPoClass(), criteria, new HashMap<>());
    }

    /**
     * 根据条件查询记录
     *
     * @param criteria
     * @return
     */
    default List<T> find(Criteria<T> criteria) {
        return this._findByCriteria(getPoClass(), criteria, new HashMap<>());
    }

    /**
     * 查询满足条件的一条记录
     *
     * @param criteria
     * @return
     */
    default T findOne(Criteria<T> criteria) {
        PageResult<T> pageResult = this.findPage(PageQuery.of(1, 1, false), criteria);
        if (pageResult != null && !pageResult.getList().isEmpty()) {
            return pageResult.getList().get(0);
        }
        return null;
    }

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    default T findById(Id id) {
        return findOne(_fromId(id));
    }

    /**
     * 根据主键列表查询多条列表
     *
     * @param idList
     * @return
     */
    default List<T> findByIds(Collection<Id> idList) {
        if (CollUtils.isEmpty(idList)) {
            return CollUtils.emptyArrayList();
        }
        return find(_fromIds(idList));
    }

    /**
     * 根据主键列表查询记录，返回Map，key为主键，map为记录
     *
     * @param idList
     * @return
     */
    default Map<Id, T> findMapByIds(Collection<Id> idList) {
        if (CollUtils.isEmpty(idList)) {
            return CollUtils.emptyHashMap();
        }
        TableColumn idColumn = getTable().getIdColumn();
        List<T> poList = this.findByIds(idList);
        return (Map<Id, T>) CollUtils.convertMap(poList, po -> ReflectionUtils.getValue(po, idColumn.getJavaName()));
    }

    /**
     * 查询满足条件的记录数
     *
     * @param criteria
     * @return
     */
    default long count(Criteria<T> criteria) {
        return this._countByCriteria(getPoClass(), criteria, new HashMap<>());
    }

    /**
     * 按条件分页查询
     *
     * @param pageQuery 分页条件
     * @param criteria  查询条件
     * @return
     */
    default PageResult<T> findPage(PageQuery pageQuery, Criteria<T> criteria) {
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize(), pageQuery.count());
        List<T> list = this._findByCriteria(getPoClass(), criteria, new HashMap<>());
        return PageResult.of(list);
    }

    default Class<T> getPoClass() {
        Class<T> poClass = (Class<T>) ((ParameterizedType) getClass().getInterfaces()[0].getGenericInterfaces()[0]).getActualTypeArguments()[1];
        return poClass;
    }

    default Table getTable() {
        return ORMUtils.get(getPoClass());
    }


    /**
     * 插入
     *
     * @param po
     * @return
     */
    @InsertProvider(type = SqlProvider.class, method = "insert")
    int _insert(@Param("po") T po);

    /**
     * 插入，id是自增的
     *
     * @param po
     * @return
     */
    @InsertProvider(type = SqlProvider.class, method = "insert")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "po.id", resultType = Long.class, before = false)
    int _insertAutoIncrement(@Param("po") T po);


    /**
     * 批量插入
     *
     * @param poList
     * @return
     */
    @InsertProvider(value = SqlProvider.class, method = "insertBatch")
    int _insertBatch(@Param("poList") Collection<T> poList);


    /**
     * 更新
     *
     * @param po
     * @param setColumnPredicate
     * @param whereColumnPredicate
     * @return
     */
    @UpdateProvider(value = SqlProvider.class, method = "update")
    int _update(@Param("po") T po, @Param("setColumnPredicate") Predicate<TableColumn> setColumnPredicate, @Param("whereColumnPredicate") Predicate<TableColumn> whereColumnPredicate);

    @DeleteProvider(value = SqlProvider.class, method = "deleteByCriteria")
    int _deleteByCriteria(@Param("poClass") Class<T> poClass, @Param("criteria") Criteria<T> criteria, @Param("map") Map<String, Object> map);

    @SelectProvider(value = SqlProvider.class, method = "findByCriteria")
    List<T> _findByCriteria(@Param("poClass") Class<T> poClass, @Param("criteria") Criteria<T> criteria, @Param("map") Map<String, Object> map);

    @SelectProvider(value = SqlProvider.class, method = "countByCriteria")
    long _countByCriteria(@Param("poClass") Class<T> poClass, @Param("criteria") Criteria<T> criteria, @Param("map") Map<String, Object> map);

    default Criteria<T> _fromId(Id id) {
        Class<T> entityClass = getPoClass();
        Table table = ORMUtils.get(entityClass);
        Criteria<T> criteria = Criteria.from(entityClass);
        Optional<TableColumn> idColumn = table.getTableColumnList().stream().filter(TableColumn::isPk).findAny();
        if (!idColumn.isPresent()) {
            throw new RuntimeException("entity class:" + entityClass.getName() + " has no id column");
        }
        String javaName = idColumn.get().getJavaName();
        Condition condition = new Condition();
        condition.setPropertyName(javaName);
        condition.setOperator(Operator.equal);
        condition.setValue(id);
        criteria.getConditions().add(condition);
        return criteria;
    }

    default Criteria<T> _fromIds(Collection<Id> idList) {
        Class<T> entityClass = getPoClass();
        Table table = ORMUtils.get(entityClass);
        Criteria<T> criteria = Criteria.from(entityClass);
        Optional<TableColumn> idColumn = table.getTableColumnList().stream().filter(TableColumn::isPk).findAny();
        if (!idColumn.isPresent()) {
            throw new RuntimeException("entity class:" + entityClass.getName() + " has no id column");
        }
        String javaName = idColumn.get().getJavaName();
        Condition condition = new Condition();
        condition.setPropertyName(javaName);
        condition.setOperator(Operator.in);
        condition.setValue(idList);
        criteria.getConditions().add(condition);
        return criteria;
    }
}
