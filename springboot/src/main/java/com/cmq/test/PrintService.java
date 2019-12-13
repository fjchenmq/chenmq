package com.cmq.test;

import javax.sound.midi.Soundbank;

/**
 * Created by Administrator on 2019/1/23.
 */
@FunctionalInterface
public interface PrintService {
    void print(String str);
  /*  default void print() {
        System.out.println("----Print----");
    }*/
}
