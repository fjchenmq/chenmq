package com.base.mapper.provider;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

/**
 * ExampleProvider实现类，基础方法实现类
 *
 * @author liuzh
 */
public class ExtConditionProvider extends MapperTemplate {

    public ExtConditionProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }



    /**
     * Example查询中的where结构，用于只有一个Example参数时
     *
     * @return
     */
    public static String conditionWhereClause() {
        return "<if test=\"_parameter != null\">" +
                "<where>\n" +
                "  <foreach collection=\"oredCriteria\" item=\"criteria\" separator=\"or\">\n" +
                "    <if test=\"criteria.valid\">\n" +
                getCriteriaSql()+
                "    </if>\n" +
                "  </foreach>\n" +
                "</where>" +
                "</if>";
    }

    protected static String getCriteriaSql(){
        return "             <trim prefix=\"(\" prefixOverrides=\"and\" suffix=\")\">\n" +
                "                <trim prefix=\"(\" prefixOverrides=\"and\" suffix=\")\">\n" +
                "                    <foreach collection=\"criteria.criteria\" item=\"criterion\">\n" +
                "                        <choose>\n" +
                "                        <when test=\"criterion.noValue\">\n" +
                "                          and ${criterion.condition}\n" +
                "                        </when>\n" +
                "                        <when test=\"criterion.likeValueFlag\">\n" +
                "                          and ${criterion.condition} #{criterion.value} escape '${criterion.escapeValue}' \n" +
                "                        </when>\n" +
                "                        <when test=\"criterion.singleValue\">\n" +
                "                          and ${criterion.condition} #{criterion.value}\n" +
                "                        </when>\n" +
                "                        <when test=\"criterion.betweenValue\">\n" +
                "                          and ${criterion.condition} #{criterion.value} and #{criterion.secondValue}\n" +
                "                        </when>\n" +
                "                        <when test=\"criterion.listValue\">\n" +
                "                          and ${criterion.condition}\n" +
                "                          <foreach close=\")\" collection=\"criterion.value\" item=\"listItem\" open=\"(\" separator=\",\">\n" +
                "                            #{listItem}\n" +
                "                          </foreach>\n" +
                "                        </when>\n" +
                "                        </choose>\n" +
                "                    </foreach>\n" +
                "                </trim>\n" +
                "                <trim prefix=\"and (\" prefixOverrides=\"or\" suffix=\")\">\n" +
                "                    <foreach collection=\"criteria.orCriteria\" item=\"criterion\">\n" +
                "                        <choose>\n" +
                "                        <when test=\"criterion.noValue\">\n" +
                "                          or ${criterion.condition}\n" +
                "                        </when>\n" +
                "                        <when test=\"criterion.likeValueFlag\">\n" +
                "                          or ${criterion.condition} #{criterion.value} escape '${criterion.escapeValue}'\n" +
                "                        </when>\n" +
                "                        <when test=\"criterion.singleValue\">\n" +
                "                          or ${criterion.condition} #{criterion.value}\n" +
                "                        </when>\n" +
                "                        <when test=\"criterion.betweenValue\">\n" +
                "                          or ${criterion.condition} #{criterion.value} and #{criterion.secondValue}\n" +
                "                        </when>\n" +
                "                        <when test=\"criterion.listValue\">\n" +
                "                          or ${criterion.condition}\n" +
                "                          <foreach close=\")\" collection=\"criterion.value\" item=\"listItem\" open=\"(\" separator=\",\">\n" +
                "                            #{listItem}\n" +
                "                          </foreach>\n" +
                "                        </when>\n" +
                "                        </choose>\n" +
                "                    </foreach>\n" +
                "                </trim>\n" +
                "            </trim>\n";
    }

    /**
     * Example-Update中的where结构，用于多个参数时，Example带@Param("example")注解时
     *
     * @return
     */
    public static String updateByConditionWhereClause() {
        return "<where>\n" +
                "  <foreach collection=\"example.oredCriteria\" item=\"criteria\" separator=\"or\">\n" +
                "    <if test=\"criteria.valid\">\n" +
                getCriteriaSql() +
                "    </if>\n" +
                "  </foreach>\n" +
                "</where>";
    }

    /**
     * 根据Example查询总数
     *
     * @param ms
     * @return
     */
    public String selectCountByCondition(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.selectCount(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(conditionWhereClause());
        return sql.toString();
    }

    /**
     * 根据Example删除
     *
     * @param ms
     * @return
     */
    public String deleteByCondition(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.deleteFromTable(entityClass, tableName(entityClass)));
        sql.append(conditionWhereClause());
        return sql.toString();
    }


    /**
     * 根据Example查询
     *
     * @param ms
     * @return
     */
    public String selectByCondition(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        //将返回值修改为实体类型
        setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append("<if test=\"distinct\">distinct</if>");
        //支持查询指定列
        sql.append(SqlHelper.exampleSelectColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(conditionWhereClause());
        sql.append(SqlHelper.exampleOrderBy(entityClass));
        return sql.toString();
    }

    /**
     * 根据Example查询
     *
     * @param ms
     * @return
     */
    public String selectByConditionAndRowBounds(MappedStatement ms) {
        return selectByCondition(ms);
    }

    /**
     * 根据Example更新非null字段
     *
     * @param ms
     * @return
     */
    public String updateByConditionSelective(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass), "example"));
        sql.append(SqlHelper.updateSetColumns(entityClass, "record", true, isNotEmpty()));
        sql.append(updateByConditionWhereClause());
        return sql.toString();
    }

    /**
     * 根据Example更新
     *
     * @param ms
     * @return
     */
    public String updateByCondition(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass), "example"));
        sql.append(SqlHelper.updateSetColumns(entityClass, "record", false, false));
        sql.append(updateByConditionWhereClause());
        return sql.toString();
    }
}
