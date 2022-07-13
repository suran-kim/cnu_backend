package com.programmers.java.poly;

public class Main {
    public static void main(String[] args) {
        // UserService타입 객체를 생성하고 생성자 매개변수로 KakaoLogin()인스턴스를 준다.
        UserService s = new UserService(new NaverLogin());
        // 의문 : 생성자 매개변수로 KakaoLogin인스턴스를 준다??
        //       UserService타입의 매개인자는 Login타입인데도 이게 가능?
        // 답   : 가능. 다형성이잖아 : )

        // 매개변수 s를 이용해 UserService의 메소드 login()을 사용한다.
        s.login();
    }
}
