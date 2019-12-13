package com.base.bean;

import lombok.Getter;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.type.TypeHandler;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.entity.IDynamicTableName;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.util.StringUtil;

import java.util.*;

/**
 * 通用的Example查询对象
 *
 * @author liuzh
 *
 */
public class ExtCondition implements IDynamicTableName {
    protected String orderByClause;

    protected boolean distinct;

    protected boolean exists;

    protected boolean notNull;

    protected Set<String> selectColumns;

    protected List<Criteria> oredCriteria;

    protected Class<?> entityClass;

    protected EntityTable table;
    //属性和列对应
    protected Map<String, EntityColumn> propertyMap;
    //动态表名
    protected String tableName;

    protected OrderBy ORDERBY;
    /**
     * 默认exists为true
     *
     * @param entityClass
     */
    public ExtCondition(Class<?> entityClass) {
        this(entityClass, true);
    }

    /**
     * 带exists参数的构造方法，默认notNull为false，允许为空
     *
     * @param entityClass
     * @param exists      - true时，如果字段不存在就抛出异常，false时，如果不存在就不使用该字段的条件
     */
    public ExtCondition(Class<?> entityClass, boolean exists) {
        this(entityClass, exists, false);
    }

    /**
     * 带exists参数的构造方法
     *
     * @param entityClass
     * @param exists      - true时，如果字段不存在就抛出异常，false时，如果不存在就不使用该字段的条件
     * @param notNull     - true时，如果值为空，就会抛出异常，false时，如果为空就不使用该字段的条件
     */
    public ExtCondition(Class<?> entityClass, boolean exists, boolean notNull) {
        this.exists = exists;
        this.notNull = notNull;
        oredCriteria = new ArrayList<Criteria>();
        this.entityClass = entityClass;
        table = EntityHelper.getEntityTable(entityClass);
        //根据李领北建议修改#159
        propertyMap = table.getPropertyMap();
        this.ORDERBY = new OrderBy(this, propertyMap);
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public OrderBy orderBy(String property) {
        this.ORDERBY.orderBy(property);
        return this.ORDERBY;
    }

    public static class OrderBy {
        private ExtCondition example;
        private Boolean isProperty;
        //属性和列对应
        protected Map<String, EntityColumn> propertyMap;
        protected boolean notNull;

        public OrderBy(ExtCondition example, Map<String, EntityColumn> propertyMap) {
            this.example = example;
            this.propertyMap = propertyMap;
        }

        private String property(String property) {
            if (propertyMap.containsKey(property)) {
                return propertyMap.get(property).getColumn();
            } else if (notNull) {
                throw new RuntimeException("当前实体类不包含名为" + property + "的属性!");
            } else {
                return null;
            }
        }

        public OrderBy orderBy(String property) {
            String column = property(property);
            if (column == null) {
                isProperty = false;
                return this;
            }
            if (StringUtil.isNotEmpty(example.getOrderByClause())) {
                example.setOrderByClause(example.getOrderByClause() + "," + column);
            } else {
                example.setOrderByClause(column);
            }
            isProperty = true;
            return this;
        }

        public OrderBy desc() {
            if (isProperty) {
                example.setOrderByClause(example.getOrderByClause() + " DESC");
                isProperty = false;
            }
            return this;
        }

        public OrderBy asc() {
            if (isProperty) {
                example.setOrderByClause(example.getOrderByClause() + " ASC");
                isProperty = false;
            }
            return this;
        }
    }

    public Set<String> getSelectColumns() {
        return selectColumns;
    }

    /**
     * 指定要查询的属性列 - 这里会自动映射到表字段
     *
     * @param properties
     * @return
     */
    public ExtCondition selectProperties(String... properties) {
        if (properties != null && properties.length > 0) {
            if (this.selectColumns == null) {
                this.selectColumns = new LinkedHashSet<String>();
            }
            for (String property : properties) {
                if (propertyMap.containsKey(property)) {
                    this.selectColumns.add(propertyMap.get(property).getColumn());
                }
            }
        }
        return this;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria(propertyMap, exists, notNull);
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * 设置表名
     *
     * @param tableName
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String getDynamicTableName() {
        return tableName;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;
        protected List<Criterion> orCriteria;
        //字段是否必须存在
        protected boolean exists;
        //值是否不能为空
        protected boolean notNull;
        //属性和列对应
        protected Map<String, EntityColumn> propertyMap;

        protected GeneratedCriteria(Map<String, EntityColumn> propertyMap, boolean exists, boolean notNull) {
            super();
            this.exists = exists;
            this.notNull = notNull;
            criteria = new ArrayList<Criterion>();
            orCriteria = new ArrayList<Criterion>();
            this.propertyMap = propertyMap;
        }

        private String column(String property) {
            if (propertyMap.containsKey(property)) {
                return propertyMap.get(property).getColumn();
            } else if (exists) {
                throw new RuntimeException("当前实体类不包含名为" + property + "的属性!");
            } else {
                return null;
            }
        }

        private String property(String property) {
            if (propertyMap.containsKey(property)) {
                return property;
            } else if (exists) {
                throw new RuntimeException("当前实体类不包含名为" + property + "的属性!");
            } else {
                return null;
            }
        }

        public boolean isValid() {
            return criteria.size() > 0 || orCriteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        public List<Criterion> getOrCriteria() {
            return orCriteria;
        }

        protected void addCriterion(String condition,boolean isOr) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            if (condition.startsWith("null")) {
                return;
            }
            if(isOr){
                orCriteria.add(new Criterion(condition));
            }else{
                criteria.add(new Criterion(condition));
            }

        }

        protected void addCriterion(String condition, Object value, String property,boolean isOr) {
            if (value == null) {
                if (notNull) {
                    throw new RuntimeException("Value for " + property + " cannot be null");
                } else {
                    return;
                }
            }
            if (property == null) {
                return;
            }
            if(isOr){
                orCriteria.add(new Criterion(condition, value));
            }else{
                criteria.add(new Criterion(condition, value));
            }

        }
        protected void addLikeCriterion(String condition, String likeValue, String property, String escapeChar,
                                    boolean isOr) {
            if (likeValue == null) {
                if (notNull) {
                    throw new RuntimeException("Value for " + property + " cannot be null");
                } else {
                    return;
                }
            }
            if (property == null) {
                return;
            }
            if (isOr) {
                orCriteria.add(new Criterion(condition, likeValue, escapeChar));
            } else {
                criteria.add(new Criterion(condition, likeValue, escapeChar));
            }

        }

        protected void addCriterion(String condition, Object value1, Object value2, String property,boolean isOr) {
            if (value1 == null || value2 == null) {
                if (notNull) {
                    throw new RuntimeException("Between values for " + property + " cannot be null");
                } else {
                    return;
                }
            }
            if (property == null) {
                return;
            }
            if(isOr){
                orCriteria.add(new Criterion(condition, value1, value2));
            }else{
                criteria.add(new Criterion(condition, value1, value2));
            }

        }

        public Criteria andIsNull(String property) {
            addCriterion(column(property) + " is null", false);
            return (Criteria) this;
        }

        public Criteria andIsNotNull(String property) {
            addCriterion(column(property) + " is not null", false);
            return (Criteria) this;
        }

        public Criteria andEqualTo(String property, Object value) {
            addCriterion(column(property) + " =", value, property(property), false);
            return (Criteria) this;
        }

        public Criteria andNotEqualTo(String property, Object value) {
            addCriterion(column(property) + " <>", value, property(property), false);
            return (Criteria) this;
        }

        public Criteria andGreaterThan(String property, Object value) {
            addCriterion(column(property) + " >", value, property(property), false);
            return (Criteria) this;
        }

        public Criteria andGreaterThanOrEqualTo(String property, Object value) {
            addCriterion(column(property) + " >=", value, property(property), false);
            return (Criteria) this;
        }

        public Criteria andLessThan(String property, Object value) {
            addCriterion(column(property) + " <", value, property(property), false);
            return (Criteria) this;
        }

        public Criteria andLessThanOrEqualTo(String property, Object value) {
            addCriterion(column(property) + " <=", value, property(property), false);
            return (Criteria) this;
        }

        public Criteria andIn(String property, Iterable values) {
            addCriterion(column(property) + " in", values, property(property), false);
            return (Criteria) this;
        }

        public Criteria andNotIn(String property, Iterable values) {
            addCriterion(column(property) + " not in", values, property(property), false);
            return (Criteria) this;
        }

        public Criteria andBetween(String property, Object value1, Object value2) {
            addCriterion(column(property) + " between", value1, value2, property(property), false);
            return (Criteria) this;
        }

        public Criteria andNotBetween(String property, Object value1, Object value2) {
            addCriterion(column(property) + " not between", value1, value2, property(property), false);
            return (Criteria) this;
        }

        public Criteria andLike(String property, String value) {
            addCriterion(column(property) + "  like", value, property(property), false);
            return (Criteria) this;
        }

        public Criteria andNotLike(String property, String value) {
            addCriterion(column(property) + "  not like", value, property(property), false);
            return (Criteria) this;
        }

        /**
         * 合成的like条件中支持转义字符.
         * @param property 属性名称，驼峰式的
         * @param value like值拼接好的含有%,比如 SEQ/_% 匹配以SEQ_开头的
         * @param escapeValue 转义字符比如 /
         * @return Criteria
         */
        public Criteria andLike(String property, String value, String escapeValue) {
            addLikeCriterion(column(property) + "  like", value, property(property), escapeValue, false);
            return (Criteria) this;
        }

        /**
         * 合成的not like条件中支持转义字符.
         * @param property 属性名称，驼峰式的
         * @param value like值拼接好的含有%,比如 SEQ/_% 匹配以SEQ_开头的
         * @param escapeValue 转义字符比如 /
         * @return
         */
        public Criteria andNotLike(String property, String value, String escapeValue) {
            addLikeCriterion(column(property) + "  not like", value, property(property), escapeValue, false);
            return (Criteria) this;
        }

        /**
         * 手写条件
         *
         * @param condition 例如 "length(countryname)<5"
         * @return
         */
        public Criteria andCondition(String condition) {
            addCriterion(condition, false);
            return (Criteria) this;
        }





        /**
         * 手写左边条件，右边用value值
         *
         * @param condition 例如 "length(countryname)="
         * @param value     例如 5
         * @return
         */
        public Criteria andCondition(String condition, Object value) {
            criteria.add(new Criterion(condition, value));
            return (Criteria) this;
        }

        /**
         * 手写左边条件，右边用value值
         *
         * @param condition   例如 "length(countryname)="
         * @param value       例如 5
         * @param typeHandler 类型处理
         * @return
         * @deprecated 由于typeHandler起不到作用，该方法会在4.x版本去掉
         */
        @Deprecated
        public Criteria andCondition(String condition, Object value, String typeHandler) {
            criteria.add(new Criterion(condition, value, typeHandler));
            return (Criteria) this;
        }

        /**
         * 手写左边条件，右边用value值
         *
         * @param condition   例如 "length(countryname)="
         * @param value       例如 5
         * @param typeHandler 类型处理
         * @return
         * @deprecated 由于typeHandler起不到作用，该方法会在4.x版本去掉
         */
        @Deprecated
        public Criteria andCondition(String condition, Object value, Class<? extends TypeHandler> typeHandler) {
            criteria.add(new Criterion(condition, value, typeHandler.getCanonicalName()));
            return (Criteria) this;
        }

        /**
         * 将此对象的不为空的字段参数作为相等查询条件
         *
         * @param param 参数对象
         * @author Bob {@link}0haizhu0@gmail.com
         * @Date 2015年7月17日 下午12:48:08
         */
        public Criteria andEqualTo(Object param) {
            MetaObject metaObject = SystemMetaObject.forObject(param);
            String[] properties = metaObject.getGetterNames();
            for (String property : properties) {
                //属性和列对应Map中有此属性
                if (propertyMap.get(property) != null) {
                    Object value = metaObject.getValue(property);
                    //属性值不为空
                    if (value != null) {
                        andEqualTo(property, value);
                    }
                }
            }
            return (Criteria) this;
        }




        // or操作


        public Criteria orIsNull(String property) {
            addCriterion(column(property) + " is null", true);
            return (Criteria) this;
        }

        public Criteria orIsNotNull(String property) {
            addCriterion(column(property) + " is not null", true);
            return (Criteria) this;
        }

        public Criteria orEqualTo(String property, Object value) {
            addCriterion(column(property) + " =", value, property(property), true);
            return (Criteria) this;
        }

        public Criteria orNotEqualTo(String property, Object value) {
            addCriterion(column(property) + " <>", value, property(property), true);
            return (Criteria) this;
        }

        public Criteria orGreaterThan(String property, Object value) {
            addCriterion(column(property) + " >", value, property(property), true);
            return (Criteria) this;
        }

        public Criteria orGreaterThanOrEqualTo(String property, Object value) {
            addCriterion(column(property) + " >=", value, property(property), true);
            return (Criteria) this;
        }

        public Criteria orLessThan(String property, Object value) {
            addCriterion(column(property) + " <", value, property(property), true);
            return (Criteria) this;
        }

        public Criteria orLessThanOrEqualTo(String property, Object value) {
            addCriterion(column(property) + " <=", value, property(property), true);
            return (Criteria) this;
        }

        public Criteria orIn(String property, Iterable values) {
            addCriterion(column(property) + " in", values, property(property), true);
            return (Criteria) this;
        }

        public Criteria orNotIn(String property, Iterable values) {
            addCriterion(column(property) + " not in", values, property(property), true);
            return (Criteria) this;
        }

        public Criteria orBetween(String property, Object value1, Object value2) {
            addCriterion(column(property) + " between", value1, value2, property(property), true);
            return (Criteria) this;
        }

        public Criteria orNotBetween(String property, Object value1, Object value2) {
            addCriterion(column(property) + " not between", value1, value2, property(property), true);
            return (Criteria) this;
        }

        public Criteria orLike(String property, String value) {
            addCriterion(column(property) + "  like", value, property(property), true);
            return (Criteria) this;
        }

        public Criteria orNotLike(String property, String value) {
            addCriterion(column(property) + "  not like", value, property(property), true);
            return (Criteria) this;
        }

        /**
         * 合成或like条件支持传入转义字符.
         * @param property 属性名称，驼峰式的
         * @param value like值拼接好的含有%,比如 SEQ/_% 匹配以SEQ_开头的
         * @param escapeValue 转义字符比如 /
         * @return
         */
        public Criteria orLike(String property, String value, String escapeValue) {
            addLikeCriterion(column(property) + "  like", value, property(property), escapeValue, true);
            return (Criteria) this;
        }

        /**
         * 合成或not like条件支持传入转义字符.
         * @param property 属性名称，驼峰式的
         * @param value like值拼接好的含有%,比如 SEQ/_% 匹配以SEQ_开头的
         * @param escapeValue 转义字符比如 /
         * @return
         */
        public Criteria orNotLike(String property, String value, String escapeValue) {
            addLikeCriterion(column(property) + "  not like", value, property(property), escapeValue, true);
            return (Criteria) this;
        }

        /**
         * 手写条件
         *
         * @param condition 例如 "length(countryname)<5"
         * @return
         */
        public Criteria orCondition(String condition) {
            addCriterion(condition, true);
            return (Criteria) this;
        }





        /**
         * 手写左边条件，右边用value值
         *
         * @param condition 例如 "length(countryname)="
         * @param value     例如 5
         * @return
         */
        public Criteria orCondition(String condition, Object value) {
            orCriteria.add(new Criterion(condition, value));
            return (Criteria) this;
        }

        /**
         * 手写左边条件，右边用value值
         *
         * @param condition   例如 "length(countryname)="
         * @param value       例如 5
         * @param typeHandler 类型处理
         * @return
         * @deprecated 由于typeHandler起不到作用，该方法会在4.x版本去掉
         */
        @Deprecated
        public Criteria orCondition(String condition, Object value, String typeHandler) {
            orCriteria.add(new Criterion(condition, value, typeHandler));
            return (Criteria) this;
        }

        /**
         * 手写左边条件，右边用value值
         *
         * @param condition   例如 "length(countryname)="
         * @param value       例如 5
         * @param typeHandler 类型处理
         * @return
         * @deprecated 由于typeHandler起不到作用，该方法会在4.x版本去掉
         */
        @Deprecated
        public Criteria orCondition(String condition, Object value, Class<? extends TypeHandler> typeHandler) {
            orCriteria.add(new Criterion(condition, value, typeHandler.getCanonicalName()));
            return (Criteria) this;
        }

        /**
         * 将此对象的不为空的字段参数作为相等查询条件
         *
         * @param param 参数对象
         * @author Bob {@link}0haizhu0@gmail.com
         * @Date 2015年7月17日 下午12:48:08
         */
        public Criteria orEqualTo(Object param) {
            MetaObject metaObject = SystemMetaObject.forObject(param);
            String[] properties = metaObject.getGetterNames();
            for (String property : properties) {
                //属性和列对应Map中有此属性
                if (propertyMap.get(property) != null) {
                    Object value = metaObject.getValue(property);
                    //属性值不为空
                    if (value != null) {
                        orEqualTo(property, value);
                    }
                }
            }
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria(Map<String, EntityColumn> propertyMap, boolean exists, boolean notNull) {
            super(propertyMap, exists, notNull);
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        /**
         * 是否是like条件.
         * like条件多合成escape
         */
        @Getter
        private boolean likeValueFlag;
        @Getter
        private String escapeValue;


        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof Collection<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }
        protected Criterion(String condition, String likeValue, String escapeValue) {
            super();
            this.condition = condition;
            this.value = likeValue;
            this.escapeValue = escapeValue;
            this.likeValueFlag = true;
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }
    }
}
