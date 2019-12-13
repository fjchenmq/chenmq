package com.cmq.entity;

import com.base.bean.BaseEntity;
import lombok.Data;

import javax.persistence.Transient;

/**
 * Created by Administrator on 2018/11/29.
 */
@Data
public class CustOrder   extends BaseEntity<Long> {
    Long   custId;
    String orderName;
    String orderType;
    String shardingId;
    @Transient
    String statusCd;

    //属性 begin
    @javax.persistence.Transient
    public Long getId() {
        return super.getId();
    }

    @javax.persistence.Id
    public Long getOrderId() {
        return super.getId();
    }
    public void setOrderId(Long orderId) {
        super.setId(orderId);
    }

}
