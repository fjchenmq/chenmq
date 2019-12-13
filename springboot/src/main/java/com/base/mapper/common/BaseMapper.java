package com.base.mapper.common;

import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 *
 * 继承ExtConditionMapper会报以下错误
 * java.lang.NoSuchMethodException: tk.mybatis.mapper.provider.ConditionProvider.<init>()
 * 解决：
 *  使用tk.mybatis 4.0以上版本 并且在ExtConditionMapper上注解@RegisterMapper
 */
@RegisterMapper
public interface BaseMapper<T> extends Mapper<T>, ExtConditionMapper<T>, BatchOpMapper<T>, EmptyMapper {
}
