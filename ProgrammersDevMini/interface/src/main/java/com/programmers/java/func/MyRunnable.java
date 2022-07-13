package com.programmers.java.func;

// 함수형 인터페이스 MyRunnable.
// 추상메소드가 하나밖에 없다.
// 그 하나의 추상메소드는 이름이 어떻든 어쨌든 하나 밖에 없다.
@FunctionalInterface
public interface MyRunnable {
    void run();
}

