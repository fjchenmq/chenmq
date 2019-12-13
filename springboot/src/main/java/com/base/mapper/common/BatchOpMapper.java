package com.base.mapper.common;

import com.base.mapper.provider.BatchOpProvider;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

/**
 *
 * 批量操作借口类
 * @param <T> 不能为空
 */
@RegisterMapper
public interface BatchOpMapper<T> {
    /**
     * 批量写入数据
     *
     * @param entitys
     * @return
     */
    @InsertProvider(type = BatchOpProvider.class, method = "dynamicSQL")
    void insertBatch(List<T> entitys);

    /**
     * 批量写入数据,排除null字段
     *
     * @param entitys
     * @return
     */
    @InsertProvider(type = BatchOpProvider.class, method = "dynamicSQL")
    void insertBatchSelective(List<T> entitys);



    /**
     * 根据主键Id批量删除数据，主键字段必须有且是一个否则在启动的时候会报错
     *
     * @param entitys
     * @return
     */
    @DeleteProvider(type = BatchOpProvider.class, method = "dynamicSQL")
    int deleteBatchById(List<T> entitys);


    /**
     * 通过主键批量更新的字段
     *
     * @param entitys
     * @return
     */
    @UpdateProvider(type = BatchOpProvider.class, method = "dynamicSQL")
    void updateBatchByPrimaryKey(List<T> entitys);

    /**
     * 通过主键批量更新不为null的字段
     *
     * @param entitys
     * @return
     */
    @UpdateProvider(type= BatchOpProvider.class, method= "dynamicSQL")
    void updateBatchByPrimaryKeySelective(List<T> entitys);
}
