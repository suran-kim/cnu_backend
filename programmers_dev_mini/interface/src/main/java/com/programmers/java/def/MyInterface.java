package com.programmers.java.def;

public interface MyInterface { // 인터페이스 == 추상메소드로만 이뤄진 클래스
    void method1(); // 추상메소드 : 구현이 없다.

    default void sayHello() { // 메소드 : 구현이 있다.(구현체)
        System.out.println("Hello World");
    }
    // 대신, 구현체를 가지기 위해서는 default 키워드를 사용해야한다.

}
