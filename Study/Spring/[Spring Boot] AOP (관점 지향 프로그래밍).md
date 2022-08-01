>_본 포스팅은 프로그래머스 미니 데브 코스를 공부하며 
학습을 기록하기 위한 목적으로 작성된 글입니다._

# AOP(Aspect Orient Programming)
- **관점** 지향 프로그래밍
- 혹은 _**기능, 부가기능, 관심** 지향 프로그래밍_



- 코드 핵심부(**기능**)를 간결하게 유지하면서 비즈니스 로직에는 핵심적이지 않은 동작(**부가 기능**)들을 프로그램에 추가할 수 있다.



## Cross Cutting Concerns 

![](https://velog.velcdn.com/images/suran-kim/post/4a925dea-0945-4b90-80b6-e98a6fb6cc3c/image.png)
출처 : https://www.codejava.net/frameworks/spring/understanding-spring-aop

- 공통 관심사항 (Cross Cutting Concerns)
- 여러 Layer에서 공통적으로 해결해야 하는 문제 
- 부가기능은 Layer에 횡단으로 걸쳐 적용해야 한다.


- Cross Cutting Concerns을 해결해주는 방법 --> _**AOP**_
- **AOP**는 Layer의 **핵심기능**과 **부가기능**을 분리한다.




_**Cross Cutting Concerns 예제**_
```java
    class 계좌이체서비스 {
        method 이체() {
            AAAA
            비즈니스 로직
            BBBB
        }

        method 계좌확인() {
            AAAA
            비즈니스 로직
            BBBB
        }
    }


    class 대출승인서비스 {
        method 승인() {
            AAAA
            비즈니스 로직
            BBBB
        }
    }
    
    // AAAA, BBBB는 부가기능 담당 모듈에서 관리하도록 만든다.
    // AOP는 부가기능 담당 모듈에서 AAAA, BBBB를 추가하는 방법이다. 


```

## AOP 적용 시점 (Weaving)

![](https://velog.velcdn.com/images/suran-kim/post/681bab83-b75a-479e-930c-6b6193b2116f/image.png)

[이미지 출처] (https://www.researchgate.net/figure/The-relationships-between-different-AOP-weaving-strategies_fig2_220888911)

1. _컴파일 시점_
  	- AOP 전용 프레임워크 이용
    - 컴파일 전에 공통 구현코드(부가 기능)을 **소스**에 삽입



2. _클래스 로딩 시점_
	- 클래스 로딩 시 **바이트 코드**에 공통 구현코드(부가 기능)을 삽입


3. _**런타임 시점**_
	- 스프링이 제공하는 AOP 방식
    - **proxy 객체**를 만들어서 공통 구현코드(부가 기능)가 동작하게 하는 방식


## Spring AOP
Proxy에는 두 가지 기법이 있다.
 
 - **JDK Dynamic Proxy**
     - 인터페이스 기반
    - 프록시 타겟은 인터페이스의 **구현체**
    - 별도의 객체(Proxy 클래스)를 만들지 않고 다이나믹하게 Proxy 생성 가능
    -  스프링에서 내부적으로 사용하는 기법
  



 
 
 - **CGLib Proxy** 
   - 클래스 기반
   - 인터페이스 없이 Dynamic Proxy 가능


_**JDK Dynamic Proxy**_
```java
// (구현체) 프록시 타겟 클래스
class CalculatorImpl implements Calculator {
    @Override
    public int add(int a, int b) {
        return a + b;
    }
}

// 인터페이스
interface Calculator {
    int add(int a, int b);
}

// 부가기능을 가지고 있는 Invocation Handler 구현
class LoggingInvocationHandler implements InvocationHandler {
    private static final Logger log = LoggerFactory.getLogger(LoggingInvocationHandler.class);
    private final Object target;

    // 생성자
    public LoggingInvocationHandler(Object target) {
        this.target = target;
    }
    // 타겟 오브젝트 호출
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("{} executed in {}", method.getName(), target.getClass().getCanonicalName()); // 호출된 메소드, 타겟이 된 클래스를 log로 출력
        return method.invoke(target, args); // 타겟의 메소드 실행

    }
}

public class JdkProxyTest {
    private static final Logger log = LoggerFactory.getLogger(JdkProxyTest.class);
    public static void main(String[] args) {
        // 타겟이 될 오브젝트
        var calculator = new CalculatorImpl();

        // Dynamic proxy로 proxy 객체 생성
        // 타겟 클래스에 전달. Proxy.newProxyInstance(ClassLoader loader, class<?>[] interfaces, InvocationHandler)
        Calculator proxyInstance = (Calculator) Proxy.newProxyInstance(
                LoggingInvocationHandler.class.getClassLoader(), // 부가기능 클래스 로드
                new Class[]{Calculator.class},
                new LoggingInvocationHandler(calculator)); // InvocationHandler는 인터페이스이므로 구현 필요. 인터페이스 호출 시마다 Handler 호출

        // InvocationHandler가 적용된 proxy 객체를 실행
        var result = proxyInstance.add(1, 2);
        log.info("Add -> {}", result);
    }
}


//결과

2022-08-01 20:59:20.495  INFO   --- [           main] org.prgrms.kdt.LoggingInvocationHandler  : add executed in org.prgrms.kdt.CalculatorImpl
2022-08-01 20:59:20.495  INFO   --- [           main] org.prgrms.kdt.JdkProxyTest              : Add -> 3
```
### 
###
###



> _**새로 알게된 용어**_
- _**클래스 로더**_ 
"자바 클래스들은 시작 시 한번에 로드되지 않고, 애플리케이션에서 필요할 때 로드된다. 클래스 로더는 JRE의 일부로써 **런타임에 클래스를 동적으로 JVM에 로드** 하는 역할을 수행하는 모듈이다. 자바의 클래스들은 자바 프로세스가 새로 초기화되면 클래스로더가 차례차례 로딩되며 작동한다."
출처 : https://leeyh0216.github.io/posts/java_class_loader/
- **_getCanonicalName()_**

>  _**TIP**_
- AOP를 이용하면 비즈니스 로직에 쉽게 부가기능 추가 가능 
- 런타임은 코드가 컴파일되고 클래스가 로드되어 객체가 만들어진 상태.


>  _**Rf**_
- https://www.codejava.net/frameworks/spring/understanding-spring-aop