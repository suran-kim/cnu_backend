package com.programmers.java.def3;



// 인터페이스 Ability를 상속받는 클래스 Duck
class Duck implements Swimmable, Walkable { }

// 인터페이스 Ability를 상속받는 클래스 Swan
class Swan implements Flyable, Walkable, Swimmable { }

/* 아무런 구현없이 Ability의 메소드들을 사용가능하다. 유레카! */

public class Main {
    public static void main(String[] args) {
        // 생성자 호출로 새 인스턴스를 만들어서 바로 인스턴스의 메소드 사용
        new Duck().swim();
        new Duck().walk();
        new Swan().fly();
        new Swan().swim();
        Ability.sayHello(); // static 메소드는 클래스명.메소드명()으로 사용하는 것이 바람직하다.
    }
}
