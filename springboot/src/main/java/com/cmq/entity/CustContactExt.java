package com.cmq.entity;

import com.alibaba.fastjson.JSONObject;
import com.base.bean.BaseEntity;
import lombok.Data;

/**
 * Created by Administrator on 2018/11/29.
 */
@Data
public class CustContactExt extends CustContact {
    private  String extName;
    public static void  main(String [] args) {
        Cust cust = new Cust();
        cust.setCustName("客户名称");
        CustContactExt custContact = new CustContactExt();
        custContact.setContactName("联系人");
        custContact.setStatusCd("状态");
        custContact.setExtName("ext");
        cust.setCustContact(custContact);
        String str = JSONObject.toJSONString(cust);
        System.out.println(str);
        Cust cust1 = JSONObject.parseObject(str,Cust.class);
        System.out.println(JSONObject.toJSONString(cust1));

    }
}
