package com.base.mapper.common.extcondition;

import com.base.bean.ExtCondition;
import com.base.mapper.provider.ExtConditionProvider;
import org.apache.ibatis.annotations.DeleteProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * 通用Mapper接口,Condition查询
 *
 * @param <T> 不能为空
 * @author liuzh
 */
@RegisterMapper
public interface DeleteByConditionMapper<T> {

    /**
     * 根据Condition条件删除数据
     *
     * @param condition
     * @return
     */
    @DeleteProvider(type = ExtConditionProvider.class, method = "dynamicSQL")
    int deleteByCondition(ExtCondition condition);

}
