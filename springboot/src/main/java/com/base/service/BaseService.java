package com.base.service;

import com.base.bean.BaseQo;
import com.base.bean.ExtCondition;
import com.base.bean.GridQo;
import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;

/**
 */
public interface BaseService<T,ID extends Serializable> {
    public T insert(T entity);

    public T selectByPrimaryKey(ID id);

    int delete(T entity);

    int deleteByPrimaryKey(ID id);

    int updateByPrimaryKey(T entity);

    int updateByPrimaryKeySelective(T entity);

    public List<T> selectAll();


    public T selectOne(T query);

    public List<T> select(T query);

    /**
     * 单表分页查询,不返回分页信息
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    public List<T> selectPageList(T query, int pageNum, int pageSize);
    /**
     * 单表分页查询，返回分页信息
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<T> selectPageInfo(T query, int pageNum, int pageSize);


    /**
     * 根据表格查询条件获取表格对应的数据
     * @return
     */
    public PageInfo<T> selectGridData(GridQo query);

    List<T> selectPageListByCondition(ExtCondition condition, int pageNum, int pageSize);

    PageInfo<T> selectPageInfoByCondition(ExtCondition condition, int pageNum, int pageSize);

    /**
     * 根据实体对象查询列表数据，安实体中的所有非空字段合成=条件
     * @param query
     * @return
     */
    List<T> selectByQo(BaseQo<T> query);

    /**
     * 根据实体对象查询列表数据，安实体中的所有非空字段合成=条件，如果是String类型并且前后有%则合成like条件.
     * @param query 查询条件
     * @return
     */
    List<T> selectByQoWithLike(BaseQo<T> query);


    /**
     * 根据实体对象查询列表数据，安实体中的所有非空字段合成=条件，如果是String类型并且前后有%则合成like条件.
     * @param query 查询条件
     * @return 返回带有分页信息的PageInfo对象
     */
    PageInfo<T> selectPageInfoByQoWithLike(BaseQo<T> query);





    /**
     * 批量写入数据
     *
     * @param entitys
     * @return
     */
    List<T> insertBatch(List<T> entitys);



    /**
     * 根据主键Id批量删除数据，主键字段必须有且是一个否则在启动的时候会报错
     *
     * @param entitys
     * @return
     */
    int deleteBatchById(List<T> entitys);


    /**
     * 通过主键批量更新的字段
     *
     * @param entitys
     * @return
     */
    List<T> updateBatchByPrimaryKey(List<T> entitys);

    /**
     * 通过主键批量更新不为null的字段
     *
     * @param entitys
     * @return
     */
    List<T> updateBatchByPrimaryKeySelective(List<T> entitys);


}
