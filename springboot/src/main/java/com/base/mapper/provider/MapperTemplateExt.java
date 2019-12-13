package com.base.mapper.provider;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Set;

/**
 */
@Slf4j
public class MapperTemplateExt extends MapperTemplate {

    /**
     * 开放mapperHelper属性给子类
     */
    protected MapperHelper _mapperHelper;
    public MapperTemplateExt(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
        this._mapperHelper = mapperHelper;
    }



    protected EntityColumn getPkColumn(Class<?> entityClass){
        Set<EntityColumn> columnList = EntityHelper.getPKColumns(entityClass);
        if(columnList==null || columnList.size()>1){
            log.error("属性类没有标注主键或者是有多个主键字段,class={}", entityClass.getName());
            throw new RuntimeException("属性类没有标注主键或者是有多个主键字段,class="+entityClass.getName());
        }
        //返回一个主键字段
        return columnList.iterator().next();
    }


    /**
     * where主键条件
     *
     * @param entityClass
     * @return
     */
    public static String wherePKColumns(String entityName, Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("<where>");
        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getPKColumns(entityClass);
        //当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
        for (EntityColumn column : columnList) {
            sql.append(" AND " + column.getColumnEqualsHolder(entityName));
        }
        sql.append("</where>");
        return sql.toString();
    }


    /**
     * insert into tableName - 动态表名
     *
     * @param entityClass
     * @param defaultTableName
     * @return
     */
    public static String insertIntoTable(Class<?> entityClass, String defaultTableName, String paramName) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ");
        sql.append(SqlHelper.getDynamicTableName(entityClass, defaultTableName, paramName));
        sql.append(" ");
        return sql.toString();
    }


    /**
     * delete tableName - 动态表名
     *
     * @param entityClass
     * @param defaultTableName
     * @return
     */
    public static String deleteFromTable(Class<?> entityClass, String defaultTableName, String paramName) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ");
        sql.append(SqlHelper.getDynamicTableName(entityClass, defaultTableName, paramName));
        sql.append(" ");
        return sql.toString();
    }
}
