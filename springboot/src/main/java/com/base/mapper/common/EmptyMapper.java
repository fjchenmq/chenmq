package com.base.mapper.common;

/**
 * 空Mapper接口定义.
 * 用于一个Entity的操作需要拆分到多个Mapper中的情况，同一个Entity只能有一个Mapper继承自BaseMapper<Entity>,
 *    那么第二个Mapper也是和这个Entity相关的就不要再继承自BaseMapper，继承自这个EmptyMapper就行了。
 */
public interface EmptyMapper {
}
