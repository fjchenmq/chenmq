package com.cmq.entity;

import com.base.bean.BaseEntity;
import lombok.Data;

import javax.persistence.Transient;

/**
 * Created by Administrator on 2018/11/29.
 */
@Data
public class Users extends BaseEntity<Long> {

    String name;
    @Transient
    String statusCd;

    //属性 begin
    @javax.persistence.Id
    public Long getId() {
        return super.getId();
    }

    public void setId(Long id) {
        super.setId(id);
    }

}
