package com.cmq.demo.schema.factory.abs;

import com.cmq.demo.schema.factory.simple.HuaWeiPhone;
import com.cmq.demo.schema.factory.simple.Phone;

/**
 * Created by Administrator on 2019/11/21.
 * 华为品牌工厂
 */
public class HuaWeiFactory implements BrandFactory{
    @Override
    public Laptop productLaptop() {
        return new HuaWeiLaptop();
    }

    @Override
    public Phone productPhone() {
        return new HuaWeiPhone();
    }
    public static HuaWeiFactory newInstance() {
        HuaWeiFactory fragment = new HuaWeiFactory();
        return fragment;
    }
}
