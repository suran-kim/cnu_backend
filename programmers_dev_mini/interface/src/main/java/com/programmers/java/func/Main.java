package com.programmers.java.func;

class Greeting implements MySupply {
    @Override
    public String supply() {
       return "Hello World";
    }
}


class SayHello implements MyRunnable {
    @Override
    public void run() {
        // 이렇게 하면 더 간결하다.
        System.out.println(new Greeting().supply());
    }
}

public class Main {
    public static void main(String[] args) {
/*
        new class XXX implements MySupply {
            @Override
            public String supply() {
                return null;
            }
        }.supply

*/

// 이름 없는 클래스를 생성한다 => 익명클래스
// 위의 코드로 동작하는 기능을 아래의 코드로 줄여준 것이다.
// 아래 코드는 인터페이스를 직접 new하는 형식이다.

        new MySupply() {
            @Override
            public String supply() {
                return "Hello Wolrd";
            }
        }.supply();


        new MyRunnable() {
            @Override
            public void run() {

            }
        }.run();


        MyRunnable r = new MyRunnable() {
            @Override
            public void run() {
                MySupply s = new MySupply() {
                    @Override
                    public String supply() {                //구
                        return "Hello Hello";               //현
                    }                                       //부
                };
                System.out.println(s.supply());
            }
        };

        r.run();
    }
}
// MyRunnalbe() 익명클래스 내부의 run() 오버라이딩 코드에서
// MySupply()를 익명 클래스로 만들어서 supply()가 "Hello Hello" 를 return하도록 오버라이딩한다.
// 만들어진 익명클래스 인스턴스를 MySupply타입 참조변수 s가 참조하도록 하고 s.supply()를 출력한다.

// ... 까지가 run()의 동작이 되도록 MyRunnalbe 익명클래스의 run()을 오버라이딩한다.