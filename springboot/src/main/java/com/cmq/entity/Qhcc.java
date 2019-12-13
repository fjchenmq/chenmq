package com.cmq.entity;

import com.base.bean.BaseEntity;
import lombok.Data;

import javax.persistence.Transient;

/**
 * Created by Administrator on 2018/11/29.
 */
@Data
public class Qhcc   extends BaseEntity<Long> {
    String type;
    String price;
    String longAmount;
    String longAmountChange;
    String shortAmount;
    String shortAmountChange;
    String netAmount;
    String TypeName;
    @Transient
    String statusCd;
    //属性 begin


    //属性 begin
    @javax.persistence.Transient
    public Long getId() {
        return super.getId();
    }

    @javax.persistence.Id
    public Long getCustId() {
        return super.getId();
    }

    public void setCustId(Long custId) {
        super.setId(custId);
    }

}
