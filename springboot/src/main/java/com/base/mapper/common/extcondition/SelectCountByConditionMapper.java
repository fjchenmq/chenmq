package com.base.mapper.common.extcondition;

import com.base.bean.ExtCondition;
import com.base.mapper.provider.ExtConditionProvider;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * 通用Mapper接口,Condition查询
 *
 * @param <T> 不能为空
 * @author liuzh
 */
@RegisterMapper
public interface SelectCountByConditionMapper<T> {

    /**
     * 根据Condition条件进行查询总数
     *
     * @param condition
     * @return
     */
    @SelectProvider(type = ExtConditionProvider.class, method = "dynamicSQL")
    int selectCountByCondition(ExtCondition condition);

}
