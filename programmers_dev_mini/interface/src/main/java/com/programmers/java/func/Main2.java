package com.programmers.java.func;

public class Main2 {
    public static void main(String[] args) {
        MyRunnable r = new MyRunnable(){
            @Override
            public void run() {
                System.out.println("Hello");
            }
        };

        // 이것은 가능하다.
        MyRunnable r2 = () -> System.out.println("Hello");
        MySupply s1 = () ->  "Hello World";


        MyRunnable r3 = () -> {
            //익명 메소드로 MySupply의 supply()를 "Hello Hello Hello" 를 리턴하는 것으로 오버라이딩
            MySupply s2 = () -> "Hello Hello Hello";
            System.out.println(s2.supply()); // s2로부터 supply()호출해서 출력
        };
        r3.run();  //  ... 의 기능을 하는 r3. r3는 익명메소드로 run()을 오버라이딩한 것



    }
}
