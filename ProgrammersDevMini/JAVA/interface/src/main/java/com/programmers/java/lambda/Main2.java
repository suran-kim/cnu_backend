package com.programmers.java.lambda;

// Predicate인터페이스, Consumer인터페이스 사용을 위해서는 클래스를 import해주어야 한다.
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Main2 {
    public static void main(String[] args) {
        // 호스트
        // 호출하는 측에서 기능 제공
        new Main2().filteredNumbers(30,
                i -> i % 3 == 0 && i > 0, // 3의 배수 && 0이 아님
                System.out::println
                );

    }

//  *****Predicate사용 예제*****
//  Predicate<T> 어떤 타입이든 들어오면 boolean값으로 return
    // 이 코드는 반복만 수행한다.
    void filteredNumbers(int max, Predicate<Integer> p, Consumer<Integer> c) {
        for (int i = 0 ; i < max ; i++) {  // i 번만큼 반복. 반복하는 기능만 수행
            if(p.test(i)) c.accept(i); // p가 트루면 c를 출력한다.
        }
    }



    void loop(int n, MyConsumer<Integer> consumer) {
        int sum = 0;
        for (int i = 0; i < n ; i++){
            consumer.consume(i);
        }
    }
}
