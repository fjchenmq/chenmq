package com.base.service.impl;

import com.base.bean.BaseQo;
import com.base.bean.ExtCondition;
import com.base.bean.GridQo;
import com.base.bean.PagerQo;
import com.base.mapper.common.BaseMapper;
import com.base.service.BaseService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.nutz.lang.Mirror;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class BaseServiceImpl<T, ID extends Serializable> implements BaseService<T, ID> {

    //根据泛型注入
    @Autowired
    private BaseMapper<T> baseMapper;

    @Override
    public T insert(T entity) {
        baseMapper.insert(entity);
        return entity;
    }

    @Override
    public int delete(T entity) {
        return baseMapper.delete(entity);
    }

    @Override
    public int deleteByPrimaryKey(ID id) {
        return baseMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKey(T entity) {
        return baseMapper.updateByPrimaryKey(entity);
    }

    @Override
    public int updateByPrimaryKeySelective(T entity) {
        return baseMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public T selectOne(T query) {
        return baseMapper.selectOne(query);
    }

    @Override
    public List<T> select(T query) {
        return baseMapper.select(query);
    }

    @Override
    public List<T> selectAll() {
        return baseMapper.selectAll();
    }

    @Override
    public List<T> selectPageList(T query, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize, false);
        return baseMapper.select(query);
    }

    @Override
    public PageInfo<T> selectPageInfo(T query, int pageNum, int pageSize) {
        Page<T> page = PageHelper.startPage(pageNum, pageSize, true);
        page.setReasonable(false);
        baseMapper.select(query);
        return page.toPageInfo();
    }

    @Override
    public List<T> selectPageListByCondition(ExtCondition condition, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize, false);
        return baseMapper.selectByCondition(condition);
    }

    @Override
    public PageInfo<T> selectPageInfoByCondition(ExtCondition condition, int pageNum,
                                                 int pageSize) {
        Page<T> page = PageHelper.startPage(pageNum, pageSize, true);
        page.setReasonable(false);
        baseMapper.selectByCondition(condition);
        return page.toPageInfo();
    }

    @Override
    public List<T> selectByQo(BaseQo<T> query) {
        Class<?> modelClass = Mirror.getTypeParam(this.getClass(), 0);
        ExtCondition cnd = new ExtCondition(modelClass, false); //忽略不存在的属性
        ExtCondition.Criteria criteria = cnd.createCriteria();
        criteria.andEqualTo(query.getEntity());

        //处理排序方式
        addOrderBy(query, cnd);

        PageHelper.startPage(query.getPageNum(), query.getPageSize(), false);

        return baseMapper.selectByCondition(cnd);
    }

    @Override
    public PageInfo<T> selectPageInfoByQoWithLike(BaseQo<T> query) {
        ExtCondition cnd = initCnd(query);
        Page<T> page = PageHelper.startPage(query.getPageNum(), query.getPageSize(), true);
        page.setReasonable(false);
        baseMapper.selectByCondition(cnd);
        return page.toPageInfo();
    }

    protected ExtCondition initCnd(BaseQo<T> query) {
        Class<?> modelClass = Mirror.getTypeParam(this.getClass(), 0);
        ExtCondition cnd = new ExtCondition(modelClass, false); //忽略不存在的属性
        ExtCondition.Criteria criteria = cnd.createCriteria();
        //criteria.andEqualTo(query.getEntity());

        // 如果属性值为String类型并且含有%则使用like条件
        MetaObject metaObject = SystemMetaObject.forObject(query.getEntity());
        String[] properties = metaObject.getGetterNames();
        for (String property : properties) {
            Object value = metaObject.getValue(property);
            //属性值不为空
            if (value != null) {
                if (value instanceof String) {
                    String strValue = (String) value;
                    if (strValue.startsWith("%") && strValue.endsWith("%")) {
                        criteria.andLike(property, strValue, "/");
                    } else {
                        criteria.andEqualTo(property, value);
                    }
                } else {
                    criteria.andEqualTo(property, value);
                }
            }
        }

        //处理排序方式
        addOrderBy(query, cnd);
        return cnd;
    }

    @Override
    public List<T> selectByQoWithLike(BaseQo<T> query) {

        ExtCondition cnd = initCnd(query);
        PageHelper.startPage(query.getPageNum(), query.getPageSize(), false);

        return baseMapper.selectByCondition(cnd);
    }

    /**
     * 处理排序字段.
     *
     * @param query
     * @param cnd
     */
    protected void addOrderBy(PagerQo query, ExtCondition cnd) {
        if (StringUtils.isNotBlank(query.getSortName())) {
            String sortName = query.getSortName().trim();
            //如果含有空格，说明是有多个排序字段
            //按逗号分隔，然后再按空格解析排序方向.
            if (sortName.indexOf(' ') != -1) {
                String[] sortFields = sortName.split(",");
                for(String sortField: sortFields) {
                    String[] sortAndDir = sortField.trim().split(" +");
                    ExtCondition.OrderBy orderBy = cnd.orderBy(sortAndDir[0]);
                    if(sortAndDir.length>0 && "desc".equalsIgnoreCase(sortAndDir[1])){
                        orderBy.desc();
                    }else {
                        orderBy.asc();
                    }
                }
            }else {
                if ("asc".equalsIgnoreCase(query.getSortOrder())) {
                    cnd.orderBy(query.getSortName()).asc();
                } else if ("desc".equalsIgnoreCase(query.getSortOrder())) {
                    cnd.orderBy(query.getSortName()).desc();
                }
            }
        }
    }

    /**
     * 根据表格查询条件获取表格对应的数据.
     *
     * @param query 查询条件
     * @return 返回带有分页信息和列表的分页对象
     */
    @Override
    public PageInfo<T> selectGridData(GridQo query) {
        Page<T> page = PageHelper.startPage(query.getPageNum(), query.getPageSize(), true);
        page.setReasonable(false);
        Class<?> modelClass = Mirror.getTypeParam(this.getClass(), 0);
        ExtCondition cnd = new ExtCondition(modelClass, false); //忽略不存在的属性
        ExtCondition.Criteria criteria = cnd.createCriteria();
        String filerVal = query.getFilterVal();
        //为每个过滤字段设置like条件
        if (StringUtils.isNotBlank(filerVal)) {
            String filerCol = query.getFilterCol();
            String[] aCol = StringUtils.split(filerCol, ",");
            for (String col : aCol) {
                criteria.orLike(col, '%' + filerVal + "%");
            }
        }
        //处理排序方式
        addOrderBy(query, cnd);
        Map<String, Object> anyProperties = query.getAnyProperties();
        if (anyProperties != null) {
            Iterator<Map.Entry<String, Object>> itProps = anyProperties.entrySet().iterator();
            while (itProps.hasNext()) {
                Map.Entry<String, Object> e = itProps.next();
                String p = e.getKey();
                Object v = e.getValue();
                if (v == null) {
                    continue;
                }
                //字符串
                if (v instanceof String) {
                    criteria.andEqualTo(p, (String) v);
                    continue;
                }
                //整形
                if (v instanceof Integer || v instanceof Long) {
                    criteria.andEqualTo(p, v);
                    continue;
                }
                //列表
                if (Iterable.class.isAssignableFrom(v.getClass())) {
                    criteria.andIn(p, (Iterable) v);
                    continue;
                }

            }
        }

        baseMapper.selectByCondition(cnd);
        return page.toPageInfo();
    }

    /**
     * 单个查询
     *
     * @param id
     * @return
     */
    @Override
    public T selectByPrimaryKey(ID id) {
        return baseMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据主键Id批量删除数据，主键字段必须有且是一个否则在启动的时候会报错
     *
     * @param entitys
     * @return
     */
    @Override
    public int deleteBatchById(List<T> entitys) {
        return baseMapper.deleteBatchById(entitys);
    }

    /**
     * 批量写入数据
     *
     * @param entitys
     * @return
     */
    @Override
    public List<T> insertBatch(List<T> entitys) {
        baseMapper.insertBatch(entitys);
        return entitys;
    }

    /**
     * 通过主键批量更新的字段
     *
     * @param entitys
     * @return
     */
    @Override
    public List<T> updateBatchByPrimaryKey(List<T> entitys) {
        baseMapper.updateBatchByPrimaryKey(entitys);
        return entitys;
    }

    /**
     * 通过主键批量更新不为null的字段
     *
     * @param entitys
     * @return
     */
    @Override
    public List<T> updateBatchByPrimaryKeySelective(List<T> entitys) {
        baseMapper.updateBatchByPrimaryKeySelective(entitys);
        return entitys;
    }
}
