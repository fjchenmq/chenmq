package com.cmq.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by Administrator on 2019/11/13.
 */
@Data
public class LoginInfo implements Serializable{

    private String staffCode;
    private String staffName;
    private String username;
    private String password;
}
