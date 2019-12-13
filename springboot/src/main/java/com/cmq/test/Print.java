package com.cmq.test;

import com.cmq.bean.Person;

/**
 * Created by Administrator on 2019/1/23.
 */
public class Print {

    public void excutePrint(PrintService ps) {
        ps.print("excutePrint");
    }
    public  void  print(Person person){
        System.out.println(person.getName());
    };

}
