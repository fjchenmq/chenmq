package com.cmq.test;

import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/12/18.
 */
public class Jdk8Test {
    public static void main(String[] args) {

        List<String> list = Arrays.asList("a", "b", "c", "d");

        List<String> collect = list.stream().map(String::toUpperCase).collect(Collectors.toList());
        System.out.println(collect); //[A, B, C, D]

        //映射转换成其他对象
        List<Integer> num = Arrays.asList(1, 2, 3, 4, 5);

        List<Integer> sortedList = num.stream().sorted((a, b) -> {
            return b.compareTo(a);
        }).collect(Collectors.toList());
        System.out.println("sortedList");
        System.out.println(sortedList);

        String add ="add";
        //2.打印出前2个元素
        num.stream().limit(2).forEach(i -> System.out.print(i + " "+add+" "));
        System.out.println();
        //求和
        Integer sum = num.stream().reduce(new BinaryOperator<Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) {
                return integer + integer2;
            }
        }).get();

        sum = num.stream().reduce((integer, integer2) -> {
            return integer + integer2;
        }).get();

        System.out.println("reduce 求和:"+sum);

        List<Integer> collect1 = num.stream().map(n -> n * 2).collect(Collectors.toList());
        System.out.println(collect1); //[2, 4, 6, 8, 10]

        List<Integer> filter = Arrays.asList(1, 2, 3, 4, 5);
        //过滤d
        List<Integer> rlt = null;
        int base =2;
        rlt = num.stream().filter(a -> {
            return a % base == 0;//被2整除}

        }).collect(Collectors.toList());
        System.out.println(rlt); //[2, 4]

        rlt = num.stream().filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer num) {
                return num % 4 == 0;//被2整除}
            }
        }).
            collect(Collectors.toList());


        System.out.println(rlt); //[2, 4]

        String printStr ="printStr";
        new Print().excutePrint(str->{
            System.out.println(88888);
            System.out.println(str);
        });

        new Print().excutePrint(new PrintService() {
            @Override
            public void print(String str) {
                System.out.println(str);
            }
        });


        Map map = new HashMap(16);
       /* for(int i=0;i<100;i++){
            map.put(i,i);
        }*/
        map.put("a","1");
        map.put("b","2");
        map.put("c","3");
        map.put("d","4");
        map.forEach((key,val)->{
            System.out.println(key+":"+val);
        });
        System.out.println(ThreadLocalRandom.current().nextLong());;
        //Assert.notNull(null,"aaa");

        System.out.println("11j"=="11j");

        Integer a = 256;
        Integer b =256;
        System.out.println(a==b);
        System.out.println(a.equals(b));

        switch (a){
            case 256:
                System.out.println(1);
            case 2:
                System.out.println(2);
        }

        String str = "a,b,c,,";
        String[] ary = str.split(",");
        // 预期大于 3，结果是 3
        System.out.println(ary.length);



    }
}
