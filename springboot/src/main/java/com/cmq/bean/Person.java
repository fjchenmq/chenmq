package com.cmq.bean;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * Created by chenmq on 2018/10/26.
 */
@Data
public class Person {

    String name;


    String age;

    String sex;
    
    public static Person newInstance() {
        Person fragment = new Person();
        return fragment;
    }
}
