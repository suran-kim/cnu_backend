package com.programmers.java.def;

public class Main implements MyInterface{
    public static void main(String[] args) {
        new Main().sayHello(); // Override한 Bye World가 출력된다.

    }

    @Override
    public void sayHello() {
        System.out.println("Bye World");
    }

    @Override
    public void method1() {
        throw new RuntimeException(); //아무 것도 호출하지 않음
    }
}


/*
놀라운 점 : sayHello()는 Override하지 않았음에도 불구하고 main()함수 내에서 실행된다.
*/
