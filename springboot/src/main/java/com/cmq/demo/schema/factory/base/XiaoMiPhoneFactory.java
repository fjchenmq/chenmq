package com.cmq.demo.schema.factory.base;

import com.cmq.demo.schema.factory.simple.XiaoMiPhone;
import com.cmq.demo.schema.factory.simple.Phone;

/**
 * Created by Administrator on 2019/11/21.
 * 小米手机工厂
 */
public class XiaoMiPhoneFactory implements PhoneFactory {
    @Override
    public Phone create() {
        return new XiaoMiPhone();
    }
    public static XiaoMiPhoneFactory newInstance() {
        XiaoMiPhoneFactory fragment = new XiaoMiPhoneFactory();
        return fragment;
    }
}
