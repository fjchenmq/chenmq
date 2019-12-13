package com.cmq.demo.arithmetic;

import java.util.Arrays;

import static org.apache.coyote.http11.Constants.a;

/**
 * Created by Administrator on 2019/12/3.
 */
public class ArithmeticTest {
    public static void main(String[] args) {
        bubble();
    }

    //冒泡排序法
    public static void bubble() {
        int[] arrays = {0, 9, 5, 1, 3, 10, 4, 6, 8, 12, 55, 112};
        int caclNum = 0;
        caclNum = 0;
        for (int i = 0; i < arrays.length; i++) {
            for (int j = i + 1; j < arrays.length; j++) {
                if (arrays[i] > arrays[j]) {
                    int temp = arrays[j];
                    arrays[j] = arrays[i];
                    arrays[i] = temp;
                }
                caclNum++;
            }
        }
        System.out.println("轮排序后的数组为: " + Arrays.toString(arrays));
        System.out.println("循环次数：" + caclNum);
        caclNum = 0;

        //外层循环，是需要进行比较的轮数，一共进行5次即可
        for (int i = 0; i < arrays.length - 1; i++) {
            //内存循环，是每一轮中进行的两两比较
            //并且每一轮结束后，下一次的两两比较中可以少比较一次
            for (int j = 0; j < arrays.length - i - 1; j++) {
                if (arrays[j] > arrays[j + 1]) {
                    int temp = arrays[j];
                    arrays[j] = arrays[j + 1];
                    arrays[j + 1] = temp;
                }
                caclNum++;
            }
        }
        System.out.println("轮排序后的数组为: " + Arrays.toString(arrays));
        System.out.println("循环次数：" + caclNum);

    }
}
