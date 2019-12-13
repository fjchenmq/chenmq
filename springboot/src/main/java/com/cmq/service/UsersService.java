package com.cmq.service;

import com.cmq.entity.Users;
import com.github.pagehelper.PageInfo;

/**
 * Created by Administrator on 2018/11/29.
 */
public interface UsersService {
    public Users getOne(Long id);

    public Users create(Users users);

    public PageInfo<Users> pageQuery();
}
