package com.cmq.demo.schema.factory.abs;

import com.cmq.demo.schema.factory.simple.Phone;

/**
 * Created by Administrator on 2019/11/21.
 * 品牌工厂
 */
public interface BrandFactory {
    Laptop productLaptop();

    Phone productPhone();
}
