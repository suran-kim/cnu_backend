package com.programmers.java.lambda;

// MyMapper는 input타입이 있고 output타입이 있다. 제너릭이 둘 필요.
@FunctionalInterface
public interface MyMapper <IN, OUT>{
    OUT map(IN s);

}
