package com.programmers.java.poly;

public class UserService implements Login {
    // Login 에 의존한다. (Login login이 없으면 동작이 수행될 수 없음)
    private Login login;

    public UserService(Login login) {
        this.login = login;
    }

    @Override
    public void login() {
        login.login();
    }
}
 /*
 의존성이 있다는 것은 클래스 간에 의존 관계가 있다는 것을 뜻한다.
 즉, 한 클래스가 바뀔 때 다른 클래스가 영향을 받는다는 것을 뜻한다.
 프로그램이 의존하는 의존체의 결정을 외부에 맡기면 의존도를 낮출 수 있다.

 위 프로그램은 로그인의 기능을 수행하는 구현체가 들어오면, 그 구현체를 통해 로그인을 실행한다.
 그게 카카오 로그인이든 네이버 로그인이든 그 외 어떤 다른 서비스의 로그인이더라도
 Login 인터페이스만 맞춰져있다면 UserService는 어떤 것이든 받아들일 수 있다.
 UserService는 구상체(KakaoLogin, NaverLogin)를 이용해 로그인 기능을 구현하고 있기 때문에 Login에 의존한다고 한다.
 만약, 5번째 줄의 코드에서 다음과 같이 내부 의존체를 결정해준다면 이 서비스는 종속적이게 될 것이다.
 private Login login =  new KakaoLogin();
 이 경우, KakaoLogin 클래스에 변화가 생기면 UserService클래스도 영향을 받게 된다.

 그러나 의존체 결정(의존성)을 외부에 맡긴다면 프로그램은 여러 기능을 수행할 능력을 잠재하게 된다.
 ======> 의존도를 낮춘다.
 */
/*
 login 구현체와 UserService간 맺어진 관계를 결합성이라고 한다.
 만약 5번째 줄의 코드가 다음과 같이 작성되어
 private KakaoLogin login;
 private NaverLogin login;
 KakaoLogin타입의 login와 NaverLogin타입의 login를 둘 다 소유하고,
 경우에 따라 둘 중 하나를 이용하도록 만들었다면 결합도가 높은 것이다.
 위처럼 구상체와의 결합은 결합도를 높이지만
 추상체와의 결합은 결합도가 낮아졌다고 이야기한다.
 의존성을 외부로부터 전달받았다면 의존성을 주입받았다고 표현할 수 있다.
 이것을 의존성 주입(Dependency Injection)이라고 하고 줄여서 DI라고 한다.

*/



