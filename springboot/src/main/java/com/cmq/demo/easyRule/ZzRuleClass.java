package com.cmq.demo.easyRule;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.support.UnitRuleGroup;

/**
 * Created by chen.ming.qian on 2021/3/25.
 */
public class ZzRuleClass {
    @Rule(priority = 1)
    public static class FizzRule {
        @Condition
        public boolean isFizz(@Fact("number") int number) {
            return number % 5 == 0;
        }
        @Action
        public void printFizz() {
            System.out.println("fizz");
        }
    }

    @Rule(priority = 2)
    public static class BuzzRule {
        @Condition
        public boolean isBuzz(@Fact("number") int number) {
            return number % 7 == 0;
        }
        @Action
        public void printBuzz() {
            System.out.println("buzz");
        }
    }



    @Rule(priority = 3)
    public static class NonFizzBuzzRule {
        @Condition
        public boolean isNotFizzNorBuzz(@Fact("number") int number) {
            return number % 5 != 0 || number % 7 != 0;
        }
        @Action
        public void printInput(@Fact("number") int number) {
            System.out.println(number);
        }
    }

}
