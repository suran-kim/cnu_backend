package com.programmers.java.def3;


// 특정 기능을 수행할 수 있는 static메소드를 인터페이스에 담아두고
// 인터페이스 자체의 메소드를 호출하는 것으로 기능을 수행할 수 있다.
// Abiliy 인터페이스는 함수 제공자
public interface Ability {
    static void sayHello() {
        System.out.println("헬로");
    }
}

interface Flyable {
    default void fly() {
        System.out.println("날아볼까");
    }
}

interface  Swimmable {
    default void swim() {
        System.out.println("수영할까");
    }
}

interface Walkable {
    default void walk(){
        System.out.println("걸어볼까");
    }
}
