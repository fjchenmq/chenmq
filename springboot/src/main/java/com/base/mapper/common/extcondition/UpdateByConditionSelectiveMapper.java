package com.base.mapper.common.extcondition;

import com.base.bean.ExtCondition;
import com.base.mapper.provider.ExtConditionProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;

/**
 * 通用Mapper接口,Condition查询
 *
 * @param <T> 不能为空
 * @author liuzh
 */
public interface UpdateByConditionSelectiveMapper<T> {

    /**
     * 根据Condition条件更新实体`record`包含的不是null的属性值
     *
     * @param record
     * @param condition
     * @return
     */
    @UpdateProvider(type = ExtConditionProvider.class, method = "dynamicSQL")
    int updateByConditionSelective(@Param("record") T record,
        @Param("example") ExtCondition condition);

}
