package com.cmq.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2019/11/13.
 */
public class LoginInfo implements Serializable{
    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    private String staffCode;

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    private String staffName;
}
