package com.cmq.demo.schema.factory;

import com.cmq.demo.schema.factory.abs.BrandFactory;
import com.cmq.demo.schema.factory.abs.HuaWeiFactory;
import com.cmq.demo.schema.factory.abs.XiaoMiFactory;
import com.cmq.demo.schema.factory.base.HuaWeiPhoneFactory;
import com.cmq.demo.schema.factory.base.XiaoMiPhoneFactory;
import com.cmq.demo.schema.factory.simple.Phone;
import com.cmq.demo.schema.factory.simple.SimpleFactory;

/**
 * Created by Administrator on 2019/11/21.
 */
public class FactoryTest {
    public static void main(String[] args) {
        /**
         * 简单工厂
         * 简单工厂模式最大的优点在于实现对象的创建和对象的使用分离，将对象的创建交给专门的工厂类负责，
         * 但是其最大的缺点在于工厂类不够灵活，增加新的具体产品需要修改工厂类的判断逻辑代码，而且产品较多时，工厂方法代码将会非常复杂。
         */
        System.out.println("--------简单工厂模式------");

        Phone xiaoMi1 = SimpleFactory.newInstance(1);
        Phone huaWei1 = SimpleFactory.newInstance(2);
        System.out.println(xiaoMi1.brand());
        System.out.println(huaWei1.brand());

        /**
         * ，通过定义一个抽象的核心工厂类，并定义创建产品对象的接口，创建具体产品实例的工作延迟到其工厂子类去完成。
         * 这样做的好处是核心类只关注工厂类的接口定义，而具体的产品实例交给具体的工厂子类去创建。
         * 当系统需要新增一个产品是，无需修改现有系统代码，只需要添加一个具体产品类和其对应的工厂子类，使系统的扩展性变得很好，
         * 符合面向对象编程的开闭原则
         */
        System.out.println("--------工厂模式------");
        Phone xiaoMi2 = XiaoMiPhoneFactory.newInstance().create();
        Phone huaWei2 = HuaWeiPhoneFactory.newInstance().create();
        System.out.println(xiaoMi2.brand());
        System.out.println(huaWei2.brand());

        /**
         * 此模式是对工厂方法模式的进一步扩展。在工厂方法模式中，一个具体的工厂负责生产一类具体的产品，
         * 即一对一的关系，但是，如果需要一个具体的工厂生产多种产品对象，那么就需要用到抽象工厂模式了。
         * 工厂方法模式产生一个对象，抽象工厂模式产生一族对象
         */
        System.out.println("------------抽象工厂-----------------------------");
        BrandFactory xiaoMiFactory = XiaoMiFactory.newInstance();
        BrandFactory huaweiFactory = HuaWeiFactory.newInstance();
        System.out.println(
            xiaoMiFactory.productLaptop().brand() + "   " + xiaoMiFactory.productPhone().brand());
        System.out.println(
            huaweiFactory.productLaptop().brand() + "   " + huaweiFactory.productPhone().brand());

    }
}
