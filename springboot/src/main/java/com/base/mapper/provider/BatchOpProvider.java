package com.base.mapper.provider;

import com.base.sequence.DbUtil;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.IDynamicTableName;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Set;

/**
 * 合成的语句太长，没有换行oracle中居然会报默认奇妙的错误，都增加了\n换行
 */
public class BatchOpProvider extends MapperTemplateExt {
    /**
     * 开放mapperHelper属性给子类
     */
    public BatchOpProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }


    /**
     * 批量插入
     *
     * @param ms
     */
    public String insertBatch(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        if (DbUtil.isMysql() && !IDynamicTableName.class.isAssignableFrom(entityClass)) {
            //开始拼sql


            //不是动态表名，则使用values逗号分隔批量插入方式
            sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass)));

            //不跳过ID字段
            sql.append(SqlHelper.insertColumns(entityClass, false, false, false));
            sql.append(" VALUES ");
            sql.append("<foreach collection=\"list\" item=\"record\" separator=\",\" >");
            sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
            //获取全部列
            Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
            //当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
            for (EntityColumn column : columnList) {
                //不跳过ID字段
                //if (!column.isId() && column.isInsertable()) {
                sql.append(column.getColumnHolder("record") + ",");
                //}
            }
            sql.append("</trim>");
            sql.append("</foreach>");


        } else {
            if (DbUtil.isMysql()) {
                sql.append("<foreach collection=\"list\" item=\"record\"  separator=\";\"  >");
            } else {
                sql.append("<foreach collection=\"list\" item=\"record\" open=\"begin\" close=\"; end;\" separator=\";\" "
                        + ">");
            }


            //合成的语句太长没有换行会报错？！
            sql.append("\n");
            sql.append(MapperTemplateExt.insertIntoTable(entityClass, tableName(entityClass), "record"));
            sql.append(SqlHelper.insertColumns(entityClass, false, false, false));//不跳过ID字段，
            sql.append(" VALUES ");
            sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
            //获取全部列
            Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
            //当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
            for (EntityColumn column : columnList) {
                //不跳过ID字段
                //if (!column.isId() && column.isInsertable()) {
                sql.append(column.getColumnHolder("record") + ",");
                //}
            }
            sql.append("</trim>");

            sql.append("</foreach>");

        }

        return sql.toString();
    }


    /**
     * 批量插入,跳过空字段.
     *
     * @param ms
     */
    public String insertBatchSelective(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        StringBuilder sql = new StringBuilder();
        if (DbUtil.isMysql()) {
            sql.append("<foreach collection=\"list\" item=\"record\"  separator=\";\"  >");
        } else {
            sql.append("<foreach collection=\"list\" item=\"record\" open=\"begin\" close=\"; end;\" separator=\";\" "
                    + ">");
        }


        //合成的语句太长没有换行会报错？！
        sql.append("\n");
        sql.append(MapperTemplateExt.insertIntoTable(entityClass, tableName(entityClass), "record"));
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        for (EntityColumn column : columnList) {
            if (!column.isInsertable()) {
                continue;
            }
            sql.append(SqlHelper.getIfNotNull("record", column, column.getColumn() + ",", false));

        }
        sql.append("</trim>");
        sql.append(" VALUES ");
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        //获取全部列
        //当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
        for (EntityColumn column : columnList) {
            //
            if (column.isInsertable()) {
                sql.append(SqlHelper.getIfNotNull("record", column, column.getColumnHolder("record", null, ","),
                        false));
            }
        }
        sql.append("</trim>");

        sql.append("</foreach>");



        return sql.toString();
    }


    /**
     * 通过主键删除批量删除
     *
     * @param ms
     */
    public String deleteBatchById(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);

        StringBuilder sql = new StringBuilder();
        sql.append(MapperTemplateExt.deleteFromTable(entityClass, tableName(entityClass), "item"));
        //sql.append(SqlHelper.wherePKColumns(entityClass));


        sql.append("<where>");
        EntityColumn pkColumn = getPkColumn(entityClass);
        sql.append(pkColumn.getColumn()).append(" in (");
        sql.append("<foreach collection=\"list\" item=\"item\" separator=\",\">");
        sql.append(pkColumn.getColumnHolder("item"));
        sql.append("\n");
        sql.append("</foreach> )");
        sql.append("</where>");
        return sql.toString();
    }


    /**
     * 通过主键更新全部字段
     *
     * @param ms
     */
    public String updateBatchByPrimaryKey(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);

        StringBuilder sql = new StringBuilder();
        if (DbUtil.isMysql()) {
            sql.append("<foreach collection=\"list\" item=\"record\"  separator=\";\" >");
        } else {
            sql.append("<foreach collection=\"list\" item=\"record\" open=\"begin\" close=\"; end;\" separator=\";\" "
                    + ">");
        }
        sql.append("\n");
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass), "record"));
        sql.append(SqlHelper.updateSetColumns(entityClass, "record", false, false));
        sql.append(wherePKColumns("record", entityClass));

        sql.append("</foreach>");


        return sql.toString();
    }

    /**
     * 通过主键批量更新不为null的字段
     *
     * @param ms
     * @return
     */
    public String updateBatchByPrimaryKeySelective(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);

        StringBuilder sql = new StringBuilder();
        if (DbUtil.isMysql()) {
            sql.append("<foreach collection=\"list\" item=\"record\"  separator=\";\" >");
        } else {
            sql.append("<foreach collection=\"list\" item=\"record\" open=\"begin\" close=\"; end;\" separator=\";\" "
                    + ">");
        }
        sql.append("\n");
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass), "record"));
        sql.append(SqlHelper.updateSetColumns(entityClass, "record", true, false));
        sql.append(wherePKColumns("record", entityClass));

        sql.append("</foreach>");


        return sql.toString();
    }
}
