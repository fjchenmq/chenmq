package com.cmq.test;

import java.io.File;

/**
 * Created by Administrator on 2019/6/24.
 */
public class Test {
    public static void main(String[] args) {
        int a = 3;
        System.out.println(a << 2);
        System.out.println(a >> 1);

        Integer i1 = 256;
        Integer i2 = 256;
        System.out.println(i1==i2);
        String s1 = "256";
        String s2 = "256";
        System.out.println(s1 == s2);

        String s3 = new String("256");
        String s4 = new String("256");
        System.out.println(s3 == s4);

    }
}
