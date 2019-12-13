package com.base.aspect;

import com.base.bean.BaseEntity;
import com.base.sequence.DbUtil;
import com.base.util.ReflectUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.nutz.lang.Mirror;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

/**
 * 数据库操作拦截器.
 * 1. 生成主键ID
 * 2.设置createDate，createStaff
 * 3.设置updateDate，updateStaff
 * 2017-09-22 修正有些实体没有使用Mapper插件生成，生成主键的之后会找不到对应的表信息错误
 *
 * 由于目前的路径不符合com.ztesoft.bss..*.,故需要重新定义com.ztesoft.iotcmp..*.
 * Modified by hehuang 20180820.
 */
@Component
@Aspect
public class InsertMapperAspect {

    private static final Logger log = LoggerFactory.getLogger(InsertMapperAspect.class);

    /**
     * Mapper中以insert开头的方法.
     */
    @Pointcut("execution(* com.ztesoft.iotcmp..*.mapper.*.insert*(..))  "
            + "|| execution(* com.ztesoft.iotcmp..*.mapper..*.*.insert*(..)) ")
    public void insertAspect() {
        log.info(" i am coming");
    }


    /**
     * Mapper中以update开头的方法.
     */
    @Pointcut("execution(* com.ztesoft.iotcmp..*.mapper.*.update*(..)) "
            + "|| execution(* com.ztesoft.iotcmp..*.mapper..*.*.update*(..)) ")
    public void updateAspect() {
    }

    /**
     * Mapper中以batchInsert开头的方法.
     */
    @Pointcut("execution(* com.ztesoft.iotcmp..*.mapper.*.batchInsert*(..)) "
        + "|| execution(* com.ztesoft.iotcmp..*.mapper..*.*.batchInsert*(..)) ")
    public void batchInsertAspect() {
    }


    /**
     * 批量插入操作前置拦截器.
     *
     * @param joinPoint 函数调用参数
     */
    @Before("batchInsertAspect()")
    public void batchInsert(JoinPoint joinPoint) {
        Object entity = joinPoint.getArgs()[0];
        if (Collection.class.isAssignableFrom(entity.getClass())) {
            //批量操作的时候给每个成员成主键字段
            Collection list = (Collection) entity;
            for (Object item : list) {
                generateBatchForEntity(item);
            }
        }
    }

    //批量设置通用字段信息
    protected void generateBatchForEntity(Object entity) {
        if (entity instanceof BaseEntity) {
            try {
                BaseEntity baseEntity = (BaseEntity) entity;
                if (baseEntity.getCreateDate() == null) {
                    baseEntity.setCreateDate(new Date());
                }
                if (baseEntity.getStatusCd() == null) {
                   // baseEntity.setStatusCd(MetaCommonConstants.COMMON_STATUS_VALIDATE);
                }
                if (baseEntity.getCreateStaff() == null) {
                    try {
                        //baseEntity.setCreateStaff(ContextUtil.getUserId());
                    } catch (Exception ex) {
                        log.warn("尝试从上下文中获取工号信息失败:{}", ex.getMessage());
                    }
                }
                // 更改时间也设置为当前时间
                baseEntity.setStatusDate(new Date());

                baseEntity.setStatusDate(new Date());
                baseEntity.setUpdateDate(new Date());
                try {
                   // baseEntity.setUpdateStaff(ContextUtil.getUserId());
                } catch (Exception ex) {
                    log.warn("尝试从上下文中获取工号信息失败:{}", ex.getMessage());
                }
                // 设置通用字段
                populateAppProperties(baseEntity);
            } catch (Exception ex) {
                log.info("设置基础字段异常:{}", ex.getMessage());
            }

        }
    }


    /**
     * 更新操作前置拦截器.
     * 设置updateDate和updateStaff字段的值
     *
     * @param joinPoint 函数调用参数
     */
    @Before("updateAspect()")
    public void beforeUpdate(JoinPoint joinPoint) {
        Object entity = joinPoint.getArgs()[0];

        if (Collection.class.isAssignableFrom(entity.getClass())) {
            //批量操作的时候给每个成员成设置更新的默认字段信息
            Collection list = (Collection) entity;
            for (Object item : list) {
                generateDefaultUpdateInfo(item);
            }
        } else {
            generateDefaultUpdateInfo(entity);
        }

    }

    /**
     * 设置基类的updateDate和updateStaff字段.
     *
     * @param entity 实体类
     */
    protected void generateDefaultUpdateInfo(Object entity) {
        try {
            if (entity instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) entity;
                baseEntity.setStatusDate(new Date());
                baseEntity.setUpdateDate(new Date());
                try {
                    //baseEntity.setUpdateStaff(ContextUtil.getUserId());
                } catch (Exception ex) {
                    log.warn("尝试从上下文中获取工号信息失败:{}", ex.getMessage());
                }

            }
        } catch (Exception ex) {
            log.warn("设置更新人和更新时间异常:{}", ex.getMessage());
        }

    }


    /**
     * 为实体生成id主键.
     * 实体要有且只有一个主键才会生成，支持String，Long，Integer三种累心的主键生成。
     *
     * @param entity 实体对象
     */
    protected void generateIdForEntity(Object entity) {
        try {
            Set<EntityColumn> pkColumns = EntityHelper.getPKColumns(entity.getClass());
            //只支持一个主键的设置
            if (pkColumns.size() == 1) {
                EntityColumn[] entityColumns = pkColumns.toArray(new EntityColumn[pkColumns.size()]);
                EntityColumn c = entityColumns[0];
                String propertyName = c.getProperty();

                Mirror mirror = Mirror.me(entity);
                Object id = mirror.getValue(entity, propertyName);
                if (id == null) {
                    Class<?> javaType = c.getJavaType();
                    id = newId(javaType, c);
                    mirror.setValue(entity, propertyName, id);
                    log.debug("为实体类{}生成了主键值为{}", javaType, id);
                }
            } else {
                log.info("主键ID不止一个，目前没有自动生成主键的功能");
            }
        } catch (Exception ex) {
            log.debug("为{}生成主键id生成失败:{}", entity.getClass().getName(), ex.getMessage());
        }

        if (entity instanceof BaseEntity) {
            try {
                BaseEntity baseEntity = (BaseEntity) entity;
                if (baseEntity.getCreateDate() == null) {
                    baseEntity.setCreateDate(new Date());
                }
                if (baseEntity.getStatusCd() == null) {
                    //baseEntity.setStatusCd(MetaCommonConstants.COMMON_STATUS_VALIDATE);
                }
                if (baseEntity.getCreateStaff() == null) {
                    try {
                      //  baseEntity.setCreateStaff(ContextUtil.getUserId());
                    } catch (Exception ex) {
                        log.warn("尝试从上下文中获取工号信息失败:{}", ex.getMessage());
                    }
                }
                // 更改时间也设置为当前时间
                baseEntity.setStatusDate(new Date());

                // 设置通用字段
                populateAppProperties(baseEntity);
            } catch (Exception ex) {
                log.info("设置基础字段异常:{}", ex.getMessage());
            }

        }
    }

    private void populateAppProperties(BaseEntity baseEntity) throws Exception {
        // 设置orgId
        Field field = ReflectUtils.getField(baseEntity.getClass(), "orgId");

        if (field != ReflectUtils.NONE_FIELD) {
            field.setAccessible(true);
            if (field.get(baseEntity) == null) {
            }
        }
    }


    /**
     * insert拦截器.
     *
     * @param joinPoint 拦截入参信息
     */
    @Before("insertAspect()")
    public void beforeInsert(JoinPoint joinPoint) {
        Object entity = joinPoint.getArgs()[0];
        if (entity == null) {
            return;
        }
        if (Collection.class.isAssignableFrom(entity.getClass())) {
            //批量操作的时候给每个成员成主键字段
            Collection list = (Collection) entity;
            for (Object item : list) {
                generateIdForEntity(item);
            }
        }


    }

    /**
     * 生成ID.
     *
     * @param javaType java类型
     * @param c        信息
     * @return 返回主键Id的值对象
     */
    protected Object newId(Class<?> javaType, EntityColumn c) {
        String tableName = c.getTable().getName();
        String fieldName = c.getColumn();
        //SEQ_前缀加表名作为系列名称
        Object newId = null;
        if (javaType.equals(String.class)) {
            newId = DbUtil.getSequenceString(tableName, fieldName);
        } else if (javaType.equals(Long.class)) { //long类型的
            newId = DbUtil.getSequenceNumber(tableName, fieldName);
        } else if (javaType.equals(Integer.class)) { //Integer类型的
            Long longId = DbUtil.getSequenceNumber(tableName, fieldName);
            if (longId != null) {
                newId = Integer.valueOf(longId.intValue());
            }
        } else {
            log.info("目前没有生成类型为{}的逻辑", javaType.getName());
        }
        return newId;
    }
}
