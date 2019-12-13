package com.base.mapper.common.extcondition;

import com.base.bean.ExtCondition;
import com.base.mapper.provider.ExtConditionProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * 通用Mapper接口,Condition查询
 *
 * @param <T> 不能为空
 * @author liuzh
 */
@RegisterMapper
public interface UpdateByConditionMapper<T> {

    /**
     * 根据Condition条件更新实体`record`包含的全部属性，null值会被更新
     *
     * @param record
     * @param condition
     * @return
     */
    @UpdateProvider(type = ExtConditionProvider.class, method = "dynamicSQL")
    int updateByCondition(@Param("record") T record, @Param("example") ExtCondition condition);

}
