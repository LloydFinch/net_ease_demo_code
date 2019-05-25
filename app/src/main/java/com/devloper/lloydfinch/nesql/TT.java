package com.devloper.lloydfinch.nesql;

public class TT {

    public static void main(String[] args) {
        test();
    }

    private static void test() {
        DbField annotation = Hello.class.getAnnotation(DbField.class);
        System.out.println(annotation);
    }
}

class Hello {
    @DbField
    public String hello;
}
