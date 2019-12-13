package com.cmq.service.impl;

import com.cmq.entity.Users;
import com.cmq.mapper.UsersMapper;
import com.cmq.service.UsersService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administratoron 2018/11/29.
 */
@Service
public class UsersServiceImpl implements UsersService {
    @Autowired
    private UsersMapper usersMapper;

    public Users getOne(Long id) {
        return usersMapper.getOne(id);
    }

    public Users create(Users users) {
        return usersMapper.create(users);
    }
    public PageInfo<Users> pageQuery( ) {

        Page<Users> page = PageHelper
            .startPage(1, 3, true);
        usersMapper.pageQuery(null);

        return page.toPageInfo();
    }
}
