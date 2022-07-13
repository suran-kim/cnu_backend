package com.programmers.java.lambda;

// 공급을 하는 supply에 무언가를 return하게 한다. T타입 리턴
@FunctionalInterface
public interface MySupplier <T>{
    T supply();
}
