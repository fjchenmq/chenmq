package com.cmq.mapper;

import com.base.mapper.common.BaseMapper;
import com.cmq.entity.CustOrder;
import com.cmq.entity.Users;

import java.util.List;

/**
 * Created by Administrator on 2018/11/29.
 */
public  interface UsersMapper extends BaseMapper<Users> {
    public Users getOne(Long id);
    public Users create(Users users);
    public List<Users> pageQuery(Users users);
}
