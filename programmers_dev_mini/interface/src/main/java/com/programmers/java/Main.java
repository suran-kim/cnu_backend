package com.programmers.java;

// java.lang.Runnable
// 위 패키지 안에 Runnable이라는 인터페이스 존재


interface MyRunnable {
    void myRun();
}

interface YourRunnable {
    void yourRun();
}


public class Main implements MyRunnable, YourRunnable{ // immplement는 여러개를 가질 수 있음
    public static void main(String[] args) {
        // Main m =  new Main();
        // 인스턴스가 Main으로 호출된다면 myRun()과 yourRun()을 모두 사용할 수 있음
        MyRunnable m =  new Main();
        m.myRun();
        // 호출할 수 있는 메소드를 제한시킬 수 있다.
    }


    @Override
    public void myRun() {
        System.out.println("Hello myRun");
    }

    @Override
    public void yourRun() {

    }
}
