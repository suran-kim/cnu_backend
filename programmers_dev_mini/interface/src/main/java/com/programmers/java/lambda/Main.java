package com.programmers.java.lambda;

public class Main {
    public static void main(String[] args) {
        // 제너릭을 좀 더 활용해보자.
        MySupplier<String> s = () -> "Hello World";

        MyMapper <String, Integer> m = String::length;

        // Integer를 받고 Integer를 return하는 인스턴스 매개변수 m2 생성
        // int가 들어오면 제곱해서 return하는 메소드로 오버라이딩
        MyMapper <Integer, Integer> m2 = i -> i*i;

        // Integer를 받고 String을 return하는 인스턴스 매개변수 m3
        // Integer.toHexString(i)은 매개변수를 16진수로 변환해 String을 return한다.
        //MyMapper <Integer, String> m3 = i -> Integer.toHexString(i); 는 아래와 같이 메소드레퍼런스 표현 가능.
        MyMapper <Integer, String> m3 = Integer::toHexString;

        // String을 받고 출력하는 인스턴스 매개변수 c 생성
        MyConsumer<String> c = System.out::println;

        MyRunnable r = () ->
            c.consume(
                m3.map( // Integer를 받아 String을 return
                        m2.map( // Intger를 받아 Integer을 return
                                m.map( // String을 받아 그 길이를 Intger로 return
                                        s.supply() // "Hello World"
                                )
                        )
                )
        );

        r.run(); // 출력 : 79

    }
}
