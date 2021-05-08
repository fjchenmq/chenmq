package com.cmq.entity;

import com.alibaba.fastjson.JSONObject;
import com.base.bean.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

import static javafx.scene.input.KeyCode.J;

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
    CustContact custContact;
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


    public static void  main(String [] args) {
        Cust cust = new Cust();
        cust.setCustName("客户名称");
        CustContact custContact = new CustContact();
        custContact.setContactName("联系人");
        custContact.setStatusCd("状态");
        cust.setCustContact(custContact);
        print(cust);
    }
    public  static void print(BaseEntity entity){
        String json = JSONObject.toJSONString(entity);
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONObject custContact =  jsonObject.getJSONObject("custContact");
        custContact.put("contactName","联系人111");
        Users users = new Users();
        users.setName("aaa");
        custContact.put("users",users);
        Cust cust = JSONObject.parseObject(jsonObject.toString(),Cust.class);
        System.out.println(json);
        System.out.println(jsonObject.toJSONString());
    }
}

