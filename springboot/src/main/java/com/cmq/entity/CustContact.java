package com.cmq.entity;

import com.alibaba.fastjson.JSONObject;
import com.base.bean.BaseEntity;

/**
 * Created by Administrator on 2018/11/29.
 */
public class CustContact extends BaseEntity<Long> {
    Long contactId;
    Long custId;

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    String contactName;

}
