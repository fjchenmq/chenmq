package com.cmq.entity;

import com.base.bean.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/11/29.
 */
@Data
@javax.persistence.Table(name = "CUST")
public class Cust extends BaseEntity<Long> implements Serializable {
    private static final long serialVersionUID = 1L;
    Long   custId;
    String custName;
    String custType;
    Long   shardingId;
    @Transient
    String          statusCd;
    @javax.persistence.Transient
    List<CustOrder> custOrders;

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

    public static final String namePath = "cust";

    @Getter
    @AllArgsConstructor
    public static enum FieldNames {
        custName("custName"), statusCd("statusCd");
        private String filedName;
        public static final String splitChar = "/";

        public String getNamePath() {
            return namePath + splitChar + filedName;
        }
    }

    @Override
    public int hashCode() {
        return custId.intValue();
    }
}
