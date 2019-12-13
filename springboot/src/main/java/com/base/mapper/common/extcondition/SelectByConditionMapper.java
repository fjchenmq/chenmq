package com.base.mapper.common.extcondition;

import com.base.bean.ExtCondition;
import com.base.mapper.provider.ExtConditionProvider;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

/**
 * 通用Mapper接口,Condition查询
 *
 * @param <T> 不能为空
 * @author liuzh
 */
@RegisterMapper
public interface SelectByConditionMapper<T> {

    /**
     * 根据Condition条件进行查询
     *
     * @param condition
     * @return
     */
    @SelectProvider(type = ExtConditionProvider.class, method = "dynamicSQL")
    List<T> selectByCondition(ExtCondition condition);

}
