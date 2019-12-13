package com.cmq.demo.schema.factory.base;

import com.cmq.demo.schema.factory.simple.HuaWeiPhone;
import com.cmq.demo.schema.factory.simple.Phone;

/**
 * Created by Administrator on 2019/11/21.
 * 华为手机工厂
 */
public class HuaWeiPhoneFactory implements PhoneFactory {
    @Override
    public Phone create() {
        return new HuaWeiPhone();
    }
    
    public static HuaWeiPhoneFactory newInstance() {
        HuaWeiPhoneFactory fragment = new HuaWeiPhoneFactory();
        return fragment;
    }
}
