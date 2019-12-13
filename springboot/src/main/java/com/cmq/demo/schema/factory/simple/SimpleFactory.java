package com.cmq.demo.schema.factory.simple;

/**
 * Created by Administrator on 2019/11/21.
 * 简单工厂模式，根据参数返回不同的对象
 * 简单工厂模式最大的优点在于实现对象的创建和对象的使用分离，将对象的创建交给专门的工厂类负责，
 * 但是其最大的缺点在于工厂类不够灵活，增加新的具体产品需要修改工厂类的判断逻辑代码，而且产品较多时，工厂方法代码将会非常复杂。
 */
public class SimpleFactory {
    public static Phone newInstance(int carType) {
        Phone fragment = null;
        switch (carType) {
            case 1:
                return fragment = new XiaoMiPhone();
            case 2:
                return fragment = new HuaWeiPhone();
        }
        return fragment;
    }

}
