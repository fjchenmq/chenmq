package com.base.mapper.common;

import com.base.mapper.common.extcondition.DeleteByConditionMapper;
import com.base.mapper.common.extcondition.SelectByConditionMapper;
import com.base.mapper.common.extcondition.SelectCountByConditionMapper;
import com.base.mapper.common.extcondition.UpdateByConditionMapper;
import com.base.mapper.common.extcondition.UpdateByConditionSelectiveMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * 通用Mapper接口,Condition查询
 *
 * @param <T> 不能为空
 * @author liuzh
 */
@RegisterMapper
public interface ExtConditionMapper<T> extends SelectByConditionMapper<T>,
    SelectCountByConditionMapper<T>, DeleteByConditionMapper<T>, UpdateByConditionMapper<T>,
    UpdateByConditionSelectiveMapper<T> {

}
