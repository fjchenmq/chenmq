package com.cmq.demo.schema.factory.abs;

import com.cmq.demo.schema.factory.simple.HuaWeiPhone;
import com.cmq.demo.schema.factory.simple.Phone;
import com.cmq.demo.schema.factory.simple.XiaoMiPhone;

/**
 * Created by Administrator on 2019/11/21.
 * 华为品牌工厂
 */
public class XiaoMiFactory implements BrandFactory {
    @Override
    public Laptop productLaptop() {
        return new XiaoMiLaptop();
    }

    @Override
    public Phone productPhone() {
        return new XiaoMiPhone();
    }
    public static XiaoMiFactory newInstance() {
        XiaoMiFactory instance = new XiaoMiFactory();
        return instance;
    }
}
