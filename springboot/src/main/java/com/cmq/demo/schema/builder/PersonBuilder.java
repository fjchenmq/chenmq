package com.cmq.demo.schema.builder;

import java.util.TreeSet;

/**
 * Created by Administrator on 2019/11/21.
 * 建造者（build）模式
 */
public class PersonBuilder {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    private String name;
    private String age;

    public static class Builder {
        private String name;
        private String age;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setAge(String age) {
            this.age = age;
            return this;
        }

        public PersonBuilder create() {
            PersonBuilder person = new PersonBuilder();
            person.setAge(this.age);
            person.setName(this.name);
            return person;
        }

    }

    public static Builder newInstance() {
        // Builder fragment = new PersonBuilder().new Builder();
        Builder fragment = new Builder();
        return fragment;
    }

    public static void main(String[] args) {
        PersonBuilder person = PersonBuilder.newInstance().setName("张三").setAge("18").create();
        TreeSet treeSet = new TreeSet();
        treeSet.add(person);
        System.out.println(person.getName());
    }
}
