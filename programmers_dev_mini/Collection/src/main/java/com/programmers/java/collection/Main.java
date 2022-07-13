package com.programmers.java.collection;

// LinkedList사용을 위해서 import해야함
import java.util.Arrays;
import java.util.function.Consumer;

public class Main {
    public static void main(String[] args) {
            new MyCollection<>(Arrays.asList("A", "B", "C", "D", "E"))
                    .foreach(System.out::println);
        }
        // new Consumer<Integer>() { 구현부 } 에서는 Consumer인터페이스의 accept메소드를
        // Integer값을 입력받고 그것을 출력하는 기능으로 오버라이드 한다.
        // .foreach()에서는 위 코드에서 생성된 Consumer인스턴스(Integer타입)를 입력받아 메소드를 실행해준다.
        // MyCollection은 T타입 리스트를 입력받는다. new MyCollection생성자 인자로 Array.asList(1, 2, 3, 4, 5) 를 입력해준다.

        // 람다표현식으로 매우 간결한 코드 완성

        // 출력결과
        //    A
        //    B
        //    C
        //    D
        //    E
    }


