package com.cmq.bean;

import com.base.aspect.DataMasking;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.l;

/**
 * Created by Administrator on 2019/7/28.
 */
@Component
public class Book {
    /**
     * 加载application.properties中的值
     */
    @Value("${book.name}")
    String name ;
    @Value("${book.id}")
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    //注解只能在方法上 如果是在属性上 @Pointcut("@annotation 会拦截不到
    @DataMasking("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
