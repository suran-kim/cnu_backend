package com.programmers.java.lambda;

//Comsumer는 소비하는 녀석이다. 아무것도 return하지 않는다.
@FunctionalInterface
public interface MyConsumer <T>{
    void consume(T i);
}
