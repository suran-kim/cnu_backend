>_본 포스팅은 프로그래머스 미니 데브 코스를 공부하며 
학습을 기록하기 위한 목적으로 작성된 글입니다._

# AOP(Aspect Orient Programming)
- **관점** 지향 프로그래밍
- 혹은 _**기능, 부가기능, 관심** 지향 프로그래밍_



- 코드 핵심부(**기능**)를 간결하게 유지하면서 비즈니스 로직에는 핵심적이지 않은 동작(**부가 기능**)들을 프로그램에 추가할 수 있다.

<br/>


## 🍃 Cross Cutting Concerns 

![](https://velog.velcdn.com/images/suran-kim/post/4a925dea-0945-4b90-80b6-e98a6fb6cc3c/image.png)
출처 : https://www.codejava.net/frameworks/spring/understanding-spring-aop

- 공통 관심사항 (Cross Cutting Concerns)
- 여러 Layer에서 공통적으로 해결해야 하는 문제 
- 부가기능은 Layer에 횡단으로 걸쳐 적용해야 한다.


- Cross Cutting Concerns을 해결해주는 방법 --> _**AOP**_
- **AOP**는 Layer의 **핵심기능**과 **부가기능**을 분리한다.


<br/>


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
<br/>
<br/>

## 🍃 AOP 적용 시점 (Weaving)

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

<br/>
<br/>

## 🍃 Spring AOP
<br/>


### 💡 JDK Dynamic Proxy
proxy 방식에는 JDK Dynamic Proxy 와 CGLib Proxy 가 있다.
 
 - **JDK Dynamic Proxy**
     - 인터페이스 기반
    - 프록시 타겟은 인터페이스의 **구현체**
    - 별도의 객체(Proxy 클래스)를 만들지 않고 다이나믹하게 Proxy 생성 가능
    -  스프링에서 내부적으로 사용하는 기법
 
 
 
 - **CGLib Proxy** 
   - 클래스 기반
   - 인터페이스 없이 Dynamic Proxy 가능

<br/>
<br/>

_**JDK Dynamic Proxy 구현 실습**_
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

<br/>
<br/>


### 💡 @AspectJ
- AOP 구현기능이 포함된 소프트웨어
- Schema-based AOP는 xml방식, @AspectJ는 어노테이션을 사용하는 방식
- 최근에는 **@AspectJ** 를 많이 사용한다.
- 스프링이 어노테이션을 읽어서 AOP 방식으로 Aspect를 적용할 수 있게 한다.
- pom.xml에 spring-boot-starter-aop 의존성 추가가 필요하다.


<br/>
<br/>


### 💡 AOP 주요 용어


![](https://velog.velcdn.com/images/suran-kim/post/761a49f4-0993-49f8-814f-fa74e4244698/image.png)
[이미지 출처 https://mossgreen.github.io/Spring-Certification-Spring-AOP/](https://mossgreen.github.io/Spring-Certification-Spring-AOP/)

- **타겟(Target)**
  - AOP 적용대상
  -  핵심기능을 담고있는 모듈. 부가기능을 부여할 대상.


- **조인포인트(Join Point)**
  - **어드바이스** 적용가능 지점 (Ex: 메소드)
  - 타겟 객체가 구현한 인터페이스의 모든 메소드
  
  
  

- **포인트컷(Pointcut)**
  - **어드바이스** 적용 지점(타겟의 메소드)를 표현한 **표현식**
  - 논리연산자 사용가능


- **애스펙트(Aspect) **
  - 애스펙트 = 어드바이스 + 포인트컷
  - 부가기능(어드바이스) set를 모듈화한 자바 클래스
  - spring에서는 Aspect를 Bean으로 등록해서 사용한다.
  

- **어드바이스(Advice)**
  - 타겟의 특정 Join Point에 제공할 **부가기능**
  - 애스펙트 클래스의 **메소드**
  - 메소드 호출 시 동작 방식을 어노테이션으로 지정 
  (+ 메소드 호출 시 **포인트컷**을 전달해야한다.)
  - @Before, @After, @Around., @AfterReturning, @AfterThrowing 등이 있다.
  
![](https://velog.velcdn.com/images/suran-kim/post/1a301cf7-7d5e-4121-bc0d-0a75abb3da88/image.png)
[이미지 출처 https://mossgreen.github.io/Spring-Certification-Spring-AOP/  ](https://mossgreen.github.io/Spring-Certification-Spring-AOP/)

  
- _**프록시 (Proxy)**_
  - 타겟을 감싸서 타겟의 요청을 대신 받아주는 랩핑(Wrapping) 오브젝트
  - 호출자 (클라이언트)에서 타겟을 호출하면 타겟이 아닌 **타겟을 감싸고 있는 프록시**가 호출되어, 타겟 메소드 실행 전에 선처리, 타겟 메소드 실행 후, 후처리를 실행시키도록 구성되어있다. 
  _(AOP에서 프록시는 호출을 가로채고, 어드바이스에 등록된 기능을 수행 후 타겟 메소드를 호출한다.)_
  출처 : [향로 님의 AOP 정리 (3)](https://jojoldu.tistory.com/71)
  
  
  


- **위빙(Weaving)**
  - 타겟의 특정 Join Point에 **어드바이스를 적용하는 과정**
  - AOP가 적용되는 것
  - 컴파일 시점, 클래스 로딩 시점, 런타임 시점 중 하나


<br/>
<br/>

  
### 💡 AOP 코드

_**Aspect 코드**_
```java
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect; // @Aspect는 spring이 아닌 aspectj에서 제공하는 어노테이션
import org.aspectj.lang.annotation.Around;

@Aspect
@Component  // @Aspect는 Bean으로 등록되어야 한다.
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);
    // Aspect는 advice를 담고있다.

    // advice
    @Around("execution(public * org.prgrms.kdt..*.*(..))") //  return타입 상관없이 public 메소드가 execution 할 때 log 어드바이스를 @Around로 적용
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Before method called. {}", joinPoint.getSignature().toString());
        var result = joinPoint.proceed(); // invoke
        log.info("After method called with result => {}", result);
        return result;
    }
}
```

<br/>



- _**포인트컷 지정자 (PCD)**_
  - **execution**
  @`어드바이스`("execution(`접근지정자` `반환타입` `패키지명`.`클래스명`.`메소드명`(`인자 타입`)  <br/>
  _예)_
  _`@Around("execution(public * org.prgrms.kdt.customer..*.*(..))")`_
  org.prgrms.kdt.custome의 전체 클래스의 모든 메소드에 적용
  메소드 인자가 몇 개여도 상관 없다.  <br/> <br/>
  _예2)_   [    출처 : https://mossgreen.github.io/Spring-Certification-Spring-AOP/](https://mossgreen.github.io/Spring-Certification-Spring-AOP/)
  ![](https://velog.velcdn.com/images/suran-kim/post/a89cbd19-7894-4000-8587-cccd2c741cd0/image.png) <br/> 👆 Bean에 있는 모든 메소드(public, private 포함)에 어드바이스를 적용할 수도 있다.
  (스프링 AOP는 **인터페이스 기반**이기 떄문에 public만 가능하다. <- 무슨 말일까? )
  👆 `접근지정자`와  `반환타입`이 둘 다 ' \* '이면 표시는 ' \* ' 하나로 생략 가능하다.
  <br/><br/>
  
  - **within**
  여러 연산자를 사용할 수있다.  
  _메소드가 아닌 특정 타입_ 에 속한 메소드를 포인트 컷으로 설정한다.
  <br/>
  _예)_ [    출처 : https://mossgreen.github.io/Spring-Certification-Spring-AOP/](https://mossgreen.github.io/Spring-Certification-Spring-AOP/)
  ![](https://velog.velcdn.com/images/suran-kim/post/4861a21d-2ff3-48ea-ae41-0ecdd51ff15d/image.png)



<br/><br/>
  
_**@AspectJ의 포인트컷 정의**_
- 스프링의 @AspectJ에서는 포인트컷을 미리 정의해둘 수 있다.
   
_예시 1)  포인트컷 정의를 advice 바깥으로 빼놓은 경우_
```java
@Aspect
@Component  // @Aspect는 Bean으로 등록되어야 한다.
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);
    // Aspect는 advice를 담고있다.

    // Pointcut 정의
    @Pointcut("execution(public * org.prgrms.kdt..*Service.*(..))")
    public void servicePublicMethodPointcut() {};

    // advice
    @Around("servicePublicMethodPointcut()") //  미리 정의한 poincut 사용
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Before method called. {}", joinPoint.getSignature().toString());
        var result = joinPoint.proceed(); // invoke
        log.info("After method called with result => {}", result);
        return result;
    }
}
```
<br/>

_예시 2)  포인트컷을 정의하는 클래스를 따로 생성한 경우_
```java
@Aspect
@Component  // @Aspect는 Bean으로 등록되어야 한다.
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);
    // Aspect는 advice를 담고있다.

    // advice
    @Around("org.prgrms.kdt.aop.CommonPoincut.repositoryInsertMethodPointcut()") //  CommonPointcut클래스에서 정의한 poincut 에 의해 어드바이스 동작
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Before method called. {}", joinPoint.getSignature().toString());
        var result = joinPoint.proceed(); // invoke
        log.info("After method called with result => {}", result);
        return result;
    }
}

```

```java
// 포인트컷이 정의된 클래스
public class CommonPointcut {

    // Pointcut 정의
    @Pointcut("execution(public * org.prgrms.kdt..*Service.*(..))")
    public void servicePublicMethodPointcut() {
    }

    @Pointcut("execution(* org.prgrms.kdt..*Repository.*(..))")
    public void repositoryMethodPointcut() {
    }

    @Pointcut("execution(* org.prgrms.kdt..*Repository.insert(..))")
    public void repositoryInsertMethodPointcut() {
    }
}

```

<br/>

_예시 3)  어노테이션을 정의해서 **어노테이션이 붙은 메소드**에만 어드바이스를 적용하는 경우 _
```java
@Aspect
@Component  // @Aspect는 Bean으로 등록되어야 한다.
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);
    // Aspect는 advice를 담고있다.

    // advice
    @Around("@annotation(org.prgrms.kdt.aop.TrackTime)") // 사용자지정 TrackTime 어노테이션이 부여된 메소드에만 어드바이스 적용
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Before method called. {}", joinPoint.getSignature().toString());
        var startTime = System.nanoTime(); // 1 -> 1000,000,000 : 메소드 호출 시각을 알기 위한 코드
        var result = joinPoint.proceed(); // invoke
        var endTime = System.nanoTime() - startTime;
        log.info("After method called with result => {}, and time by {} nanoseconds", result, endTime);
        return result;
    }
}

// 테스트 결과
// 2022-08-02 19:37:26.343  INFO   --- [           main] org.prgrms.kdt.aop.LoggingAspect         : Before method called. Voucher org.prgrms.kdt.voucher.VoucherRepository.insert(Voucher)
// 2022-08-02 19:37:26.343  INFO   --- [           main] org.prgrms.kdt.aop.LoggingAspect         : After method called with result => FixedAmountVoucher{voucherId=cf5385c7-936d-4e74-a5b3-e5a622ec8964, amount=100}, and time by 125700 nanoseconds
```

```java


// 어노테이션 TrackTime이 정의된 @인터페이스 클래스
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TrackTime {
}

```

```java
// 테스트 클래스 
@Autowired
VoucherRepository voucherRepository;

@Test
@DisplayName("Aop test")
public void testOrderService() {

// voucher insert 시 logging이 되는지 확인
var fixedAmountVoucher = new FixedAmountVoucher(UUID.randomUUID(), 100);
voucherRepository.insert(fixedAmountVoucher);

    }
}

```


<br/><br/>

# Spring Transaction 관리

![](https://velog.velcdn.com/images/suran-kim/post/85478678-1c89-4e44-9994-b80c2be4093e/image.png)


- pom.xml에 `spring-boot-starter-jdbc` 의존성 필요



- 이전에 했던 trasction 처리를 Spring JDBC 템플릿으로 처리해보자 

_**트랜잭션이 보장되지 않는 코드**_
```java
// Spring JDBC Template를 이용한 트랜잭션 처리
    public void testTransaction(Customer customer){
        try {
            jdbcTemplate.update("UPDATE customers SET name = :name WHERE customer_id = UNHEX(REPLACE(:customerId, '-', ''))", toParamMap(customer));
            jdbcTemplate.update("UPDATE customers SET name = :email WHERE customer_id = UNHEX(REPLACE(:customerId, '-', ''))", toParamMap(customer));
        } catch (DataAccessException e) {
            logger.error("Got error", e);
        }
    }
```

```java

// 테스트 코드
    @Test
    @Order(7)
    @DisplayName("트랜잭션 테스트")
    public void testTransaction() {
        var prevOne = customerJDBCRepository.findById(newCustomer.getCustomerId());
        assertThat(prevOne.isEmpty(), is(false));
        var newOne = new Customer(UUID.randomUUID(), "a", "a@gamil.com", LocalDateTime.now()); // 추가된 값과 동일해야 하는 값
        var insertedNewOne = customerJDBCRepository.insert(newOne); // 새로운 고객 데이터를 insert

        // 두 번째 이메일을 요청해서 에러 발생시킴
        // 트랜잭션 처리
        customerJDBCRepository.testTransaction(
                new Customer(insertedNewOne.getCustomerId(),  // testTransaction 메소드로 잘못된 Customer 정보 전달
                        "b",
                        prevOne.get().getEmail(),
                        newOne.getCreatedAt()));

        var maybeNewOne = customerJDBCRepository.findById(insertedNewOne.getCustomerId());

        // update이전 데이터와 이후 데이터가 동일한지 체크
       assertThat(maybeNewOne.isEmpty(), is(false));
        assertThat(maybeNewOne.get(), samePropertyValuesAs(newOne));
    }
    
// 결과
// java.lang.AssertionError: 
// Expected: same property values as Customer [createdAt: <2022-08-02T21:15:03.716403400>, customerId: <2bbb3ed1-6283-4878-92ec-e0efad5d1330>, email: "a@gamil.com", lastLoginAt: null, name: "a"]
//      but: createdAt was <2022-08-02T21:15:03.716403>

```




<br/>
<br/>


## 🍃 programatic transaction management

직접 API를 호출하면서 구현하는 방식
<br/>

### 💡 transactionManager

_**트랜잭션이 보장되는 코드1**_

```java
// PlatformTransactionManager의 구현체. CustomerNamedJDBCRepository와 의존성 존재
    private final PlatformTransactionManager transactionManager; // JdbcTemplate을 사용하는 트랜잭션을 위한 PlatformTransactionManager 생성

// 생성자 추가 (NamedParameterJdbcTemplate, DataSourceTransactionManager)
    public CustomerNamedJDBCRepository(NamedParameterJdbcTemplate jdbcTemplate, PlatformTransactionManager transactionManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionManager = transactionManager;
    }

// Spring JDBC Template를 이용한 트랜잭션 처리
    public void testTransaction(Customer customer){
        // transactionManager로부터 트랜잭션 status를 가져온다. (transactionManager 의존성 추가) 
        var transaction = transactionManager.getTransaction(new DefaultTransactionDefinition()); // getTransaction 시 트랜잭션 하나를 생성하는 것으로 볼 수 있음
        try {
            jdbcTemplate.update("UPDATE customers SET name = :name WHERE customer_id = UNHEX(REPLACE(:customerId, '-', ''))", toParamMap(customer));
            jdbcTemplate.update("UPDATE customers SET email = :email WHERE customer_id = UNHEX(REPLACE(:customerId, '-', ''))", toParamMap(customer));
            // 업데이트 성공 시 commit
            transactionManager.commit(transaction); // 인자로 트랜잭션 status 전달
        } catch (DataAccessException e) {
            logger.error("Got error", e);
            transactionManager.rollback(transaction); // 인자로 트랜잭션 status 전달
        }
    }
```




```java
// 테스트 코드
    static class Config {
		
        …
        
        // JdbcTemplate을 사용하는 트랜잭션을 위한 PlatformTransactionManager Bean설정
        @Bean
        public PlatformTransactionManager platformTransactionManager(DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }
    }
    
    
    @Test
    @Order(7)
    @DisplayName("트랜잭션 테스트")
    public void testTransaction() {
        var prevOne = customerJDBCRepository.findById(newCustomer.getCustomerId());
        assertThat(prevOne.isEmpty(), is(false));
        var newOne = new Customer(UUID.randomUUID(), "a", "a@gamil.com", LocalDateTime.now()); // 추가된 값과 동일해야 하는 값
        var insertedNewOne = customerJDBCRepository.insert(newOne); // 새로운 고객 데이터를 insert

        // 두 번째 이메일을 요청해서 에러 발생시킴
        // 트랜잭션 처리
        customerJDBCRepository.testTransaction(
                new Customer(insertedNewOne.getCustomerId(),  // testTransaction 메소드로 잘못된 Customer 정보 전달
                        "b",
                        prevOne.get().getEmail(),
                        newOne.getCreatedAt()));

        var maybeNewOne = customerJDBCRepository.findById(insertedNewOne.getCustomerId());

        // update이전 데이터와 이후 데이터가 동일한지 체크
       assertThat(maybeNewOne.isEmpty(), is(false));
       assertThat(maybeNewOne.get(), samePropertyValuesAs(newOne));
    }

```
<br/>
<br/>


### 💡 Transaction Template
_**트랜잭션이 보장되는 코드2 - (Transaction Template를 이용한 트랜잭션 매니저 콜백)**_

```java

@Repository // 컴포넌트 대상이 되기 위해 @Repository 추가
public class CustomerNamedJDBCRepository implements CustomerRepository {

    // TransactionTemplate 추가
    private final TransactionTemplate transactionTemplate;

	// 생성자 (NamedParameterJdbcTemplate, TransactionTemplate)
    public CustomerNamedJDBCRepository(NamedParameterJdbcTemplate jdbcTemplate, TransactionTemplate transactionTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;
    }

    // Spring JDBC Template를 이용한 트랜잭션 처리
    public void testTransaction(Customer customer){
        // transactionManager로부터 트랜잭션 status를 가져온다.
        transactionTemplate.execute(new TransactionCallbackWithoutResult() { // 트랜잭션 콜백 전달 가능 -> return값이 없을 때는 TransactionCallbackWithoutResult()를 사용한다.
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                jdbcTemplate.update("UPDATE customers SET name = :name WHERE customer_id = UNHEX(REPLACE(:customerId, '-', ''))", toParamMap(customer));
                jdbcTemplate.update("UPDATE customers SET email = :email WHERE customer_id = UNHEX(REPLACE(:customerId, '-', ''))", toParamMap(customer));
            }
        });
    }
}
// 장점:
// 트랜잭션 매니저를 만들 필요가 없다.
// 트랜잭션 매니저를 이용한 커밋과 롤백을 처리할 필요가 없다.
// 예외 발생 시 자동으로 롤백 처리를 한다.
```


```java
// 테스트 코드
    static class Config {
		
        …
        
        // JdbcTemplate을 사용하는 트랜잭션을 위한 PlatformTransactionManager Bean설정
        @Bean
        public PlatformTransactionManager platformTransactionManager(DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }

        // JdbcTemplate을 사용하는 트랜잭션을 위한 TransactionTemplate Bean설정
        @Bean
        public TransactionTemplate transactionTemplate(PlatformTransactionManager platformTransactionManager) {
            return new TransactionTemplate(platformTransactionManager); // 트랜잭션 매니저를 인자로 받는다.
        }
        
}
    
    
    @Test
    @Order(7)
    @DisplayName("트랜잭션 테스트")
    public void testTransaction() {
        var prevOne = customerJDBCRepository.findById(newCustomer.getCustomerId());
        assertThat(prevOne.isEmpty(), is(false));
        var newOne = new Customer(UUID.randomUUID(), "a", "a@gamil.com", LocalDateTime.now()); // 추가된 값과 동일해야 하는 값
        var insertedNewOne = customerJDBCRepository.insert(newOne); // 새로운 고객 데이터를 insert

        try{
            // 두 번째 이메일을 요청해서 에러 발생시킴
            // 트랜잭션 처리
            customerJDBCRepository.testTransaction(
                    new Customer(insertedNewOne.getCustomerId(),  // testTransaction 메소드로 잘못된 Customer 정보 전달
                            "b",
                            prevOne.get().getEmail(),
                            newOne.getCreatedAt()));
        }catch(DataAccessException e) {
            logger.error("Got error when testing transaction", e);
        }

        var maybeNewOne = customerJDBCRepository.findById(insertedNewOne.getCustomerId());

        // update이전 데이터와 이후 데이터가 동일한지 체크
       assertThat(maybeNewOne.isEmpty(), is(false));
       assertThat(maybeNewOne.get(), samePropertyValuesAs(newOne));
    }


```
<br/>
<br/>



## 🍃 declarative transaction management

스프링에서는 어노테이션을 통해 선언형(declarative)으로 트랜잭션을 사용하는 방법을 제공한다.
이를 _선언적 트랜잭션 관리_ 라고 부른다.
<br/>

**_선언적 트랜잭션 관리_ **
- **@Transactional 어노테이션**을 사용한다.
  - 해당 메서드를 하나의 트랜잭션 안에서 진행할 수 있도록 해주는 역할을 한다.
  - 어노테이션 사용을 위해서는 AOP설정이 필요하다.
  - _@EnableTransactionManagement_ 로 한 번에 AOP 설정 가능 
- **AOP - proxy 기술 사용**
  - 제공하는 부가기능은 `커밋`, `롤백` 등이다.
  - 주로 **service layer**에서 사용된다. (프록시 사용도 service layer에서 진행)
- 여러 DML을 묶고, 실수가 발생하면 전체코드를 롤백한다.
- **비즈니스 로직에 집중**할 수 있도록 해준다.

<br/>

_**선언적 트랜잭션 순서**_

  1. **@Transactional 어노테이션**을 사용 -> spring AOP에서 해당 클래스 타입의 **프록시** 자동 생성
 

 2. 프록시에 @Transactional 어노테이션이 지시하는 **코드가 삽입**된다. (commit, rollback …)
 <br/>

  _결론 - 중복되는 코드 생략 가능! 비즈니스 로직에 집중!_


```java
// @Transactional
    @Override
    @Transactional
    public void createCustomers(List<Customer> customers) {
        customers.forEach(customerRepository::insert);
    }
```
```java
// 테스트 클래스

    @Configuration
    @EnableTransactionManagement // @EnableTransactionManagement 추가 
    static class Config {
		
        …
        // NamedParameterJdbcTemplate을 주입받는 jdbcTemplate
        @Bean
        public CustomerRepository customerRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
            return new CustomerNamedJDBCRepository(namedParameterJdbcTemplate);
        }

        @Bean
        public CustomerService customerService(CustomerRepository customerRepository) {
            return new CustomerServiceImpl(customerRepository);
        }
}


    // 롤백 시나리오 테스트
    @Test
    @DisplayName("다건 추가 실패 시 전체 트랜잭션이 롤백되어야 한다.")
    void multiInsertRollbackTest() {
        var customers = List.of(
                new Customer(UUID.randomUUID(), "c", "c@gamil.com", LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS)),
                new Customer(UUID.randomUUID(), "d", "c@gamil.com", LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS))
        );

        try {
            customerService.createCustomers(customers);
        } catch (DataAccessException e) {}

        var allCustomersRetrieved = customerRepository.findAll();
        assertThat(allCustomersRetrieved.size(), is(0));  // 잘못된 DML이 실행되었으므로 롤백 -> allCustomersRetrieved에 데이터가 없어야 한다.
        assertThat(allCustomersRetrieved.isEmpty(), is(true));
        assertThat(allCustomersRetrieved, not(containsInAnyOrder(samePropertyValuesAs(customers.get(0)), samePropertyValuesAs(customers.get(1))))); // 데이터의 형태가 동일해야하기 때문에  id만 비교하지 않고 전체를 비교하는 게 좋다.
    }
```
<br/>
<br/>


## 🍃 트랜잭션 전파(Transaction propagation)
트랜잭션 전파란 @Transactional 어노테이션이 적용된 트랜잭션이 진행되는 도중에 
**또 다른 @Transactional 어노테이션 트랜잭션**이 처리되는 것을 말한다.

- @Transactional(propagation = `Propagation.값`)




| <center>값</center>  | <center>설명</center> |
| :-  | :--- |
|REQUIRED | **기본값** <br/>현재 진행 중인 트랜잭션이 있다면 사용한다.<br/>현재 진행 중인 트랜잭션이 없다면 새로운 트랜잭션을 시작한다.   | 
| MANDATORY| REQUIRED와 유사<br/>단, 호출 전에 반드시 **진행 중인 트랜잭션이 존재**해야 한다. <br/>진행 중인 트랜잭션이 존재하지 않을 경우 예외 발생. | 
| REQUIRED_NEW | 항상 새로운 트랜잭션이 시작된다.<br/> 이미 진행 중인 트랜잭션이 있다면 잠시 중단하고 **새로운 트랜잭션을 시작**한다.<br/>새로 시작된 트랜잭션이 종료(메소드 종료)된 후에 기존 트랜잭션이 이어서 동작한다. | 
| SUPPORTS | 트랜잭션이 필요하지 않다.<br/>하지만 진행 중인 트랜잭션이 있다면 해당 트랜잭션을 사용한다.  | 
| NOT_SUPPORTED | 트랜잭션이 필요하지 않다. <br/> 진행 중인 트랜잭션이 있다면 잠시 중단하고 <br/>메소드 실행이 종료된 후에 기존 트랜잭션을 계속 진행한다. | 
| NEVER | 트랜잭션이 필요하지 않다. <br/> 진행 중인 트랜잭션이 있다면 예외 발생. | 
| NESTED | 이미 진행 중인 트랜잭션(부모 트랜잭션)이 존재하면 중첩 트랜잭션을 시작한다. <br/>중첩 트랜잭션은 부모 트랜잭션의 커밋, 롤백에 영향을 받지만 <br/>중첩 트랜잭션의 커밋, 롤백은 부모 트랜잭션에게 영향을 주지 않는다.<br/>만약 부모 트랜잭션이 없다면 **REQUIRED**와 동일하게 작동한다 _(-> 새로운 트랜잭션 시작)_<br/> 💡 _DB 벤더에 의존적이고, 벤더에 따라 지원이 안되는 경우도 많은 전파방식이다._ | 

<br/>
<br/>

## 🍃 트랜잭션 격리 수준(Transaction Isolation Level)

Transaction Isolation Level 이란 동시에 여러 트랜잭션이 처리될 때 특정 트랜잭션에서 
_다른 트랜잭션이 변경하거나 조회하는 데이터_ 를 볼 수 있도록 허용할지 말지를 결정하는 것이다.


<br/>




| <center>격리 수준</center>  | <center>설명</center> | <center>발생가능 문제</center> |<center>고립 <br/>수준</center> |<center>동<br/>시<br/>성 <br/>수준</center> |
| :-  | :--- |:-- | :--- |:-- |
|LV.0 <br/>**READ_UNCOMMITED** | 트랜잭션에서 처리 중인, **아직 커밋되지 않은 데이터**를 <br/>다른 트랜잭션이 읽는 것을 허용   | - Dirty Read <br/> - Non-Repeatable  <br/> - Phantom Read |<center>_**낮음**_</center>|<center>_**높음**_</center>|
| LV.1 <br/>**READ_COMMITED**| 트랜잭션이 커밋되어 확정된 데이터만 읽는 것을 허용 | <br/>- Non-Repeatable <br/>- Phantom Read  | ||
| LV.2 <br/>**REPEATABLE_READ** | 선행 트랜잭션이 읽은 데이터는 <br/>트랜잭션이 종료될 때까지 후행 트랜잭션이 **삭제/변경**할 수 없다. <br/>_같은 데이터를 두 번 쿼리했을 때 일관성 있는 결과 리턴_| - Phantom Read|||
| LV.3 <br/>**SERIALIZABLE** | 선행 트랜잭션이 읽은 데이터는 <br/>후행 트랜잭션이 그 테이블의 데이터에 **삽입/삭제/변경**할 수 없다.<br/>_완벽한 읽기 일관성 모드 제공_  |<center>X</center> |<center>_**높음**_</center>|<center>_**낮음**_</center>|
<br/>
<br/>


### 💡 격리에 따른 이슈(문제 유형)

- _**Dirty Read**_
아직 **커밋되지 않은 수정 중인 데이터**를 다른 트랜잭션에서 읽을 수 있도록 허용할 때 발생한다. 
_( 해당 트랜잭션이 롤백하는 경우 문제 발생 )_


- _**Non-Repeatable Read**_
한 트랜잭션 내에서 같은 쿼리를 두 번 수행할 때, 그 사이에 다른 트랜잭션이 
값을 **수정 또는 삭제**하여 두 쿼리가 상이하게 나타나는 **비일관성**이 발생하는 문제.

- _**Phantom Read**_
한 트랜잭션 안에서 일정 범위의 레코드를 두 번 이상 읽을 때, 
첫 번째 쿼리에서 없던 **유령 레코드**가 두 번째 쿼리에서 나타나는 현상.

<br/>
<br/>

# 정리
AOP 개념, 스프링에서 트랜잭션을 제공해주는 방법 (트랜잭션 매니저, 트랜잭션 템플릿)
@Transcational을 통해 AOP가 어떻게 적용되는가? 
Transaction propagation(트랜잭션 전파)와 Transaction Isolation Level(트랜잭션 격리 수준) 을 학습했다.

<br/><br/>


---

> _**오류 해결**_
- _테스트 시 log가 출력되지 않는 오류 _
@Around("execution(public * org.prgrms.kdt..*.*())") s
-> 어라운드 어드바이스의 execution에 메소드 매개인자를 적지 않아서 생긴 오류 
- _테스트 시 사용자 지정 annotation인 TrackTime이 제대로 동작하지 않고 
`java.lang.IllegalStateException: Failed to load ApplicationContext` 오류가 출력_
-> 어라운드 어드바이스에 어노테이션 인자를 줄 때 정확한 풀네임을 기입하지 않았음
- _테스트 시 객체의 DateTime 데이터의 나노초가 비교 대상와 일치하지 않아서 발생하는 오류 _
```
java.lang.AssertionError: Expected: same property values as Customer [createdAt: <2022-08-02T21:59:26.939686900>, customerId: <ba97c432-c3ab-4c6c-8efe-b723a1c96de0>, email: "a@gamil.com", lastLoginAt: null, name: "a"]
     but: createdAt was <2022-08-02T21:59:26.939687>
```    
  -> 원인 : System이 가지고 있는 시계의 문제
   LocatDateTime의 정밀도가 운영체제마다 다르기 때문에 생긴 오류.  Mac은 정밀도가 마이크로(6자리), Window는 정밀도가 밀리(3자리) 라서 생긴일
   -> 해결 : `LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS)`
   now 생성 시 정밀도를 맞춰서 사용하기
  

> _**새로 알게된 용어**_
- _**클래스 로더**_ 
"자바 클래스들은 시작 시 한번에 로드되지 않고, 애플리케이션에서 필요할 때 로드된다. 클래스 로더는 JRE의 일부로써 **런타임에 클래스를 동적으로 JVM에 로드** 하는 역할을 수행하는 모듈이다. 자바의 클래스들은 자바 프로세스가 새로 초기화되면 클래스로더가 차례차례 로딩되며 작동한다."
출처 : https://leeyh0216.github.io/posts/java_class_loader/
- **_getCanonicalName()_**
- _**ProceedingJoinPoint 인터페이스**_
  @Around Advice에서 사용할 공통 기능 메서드는 대부분 파라미터로 전달 받은 ProceedingJoinPoint의 **proceed() 메서드**만 호출하면 된다. 
  개발도중 호출되는 대상 객체에 대한 정보, 실행되는 메서드에 대한 정보, 메서드를 호출할 때 전달된 인자에 대한 정보가 필요할 때가 있다. ProceedingJoinPoint 인터페이스는 이들 정보에 접근할 수 있도록 여러 메소드를 제공하고 있다.
출처: https://ktko.tistory.com/entry/Spring-ProceedingJoinPoint의-메서드
- _**declarative(선언형) vs imperative(명령형)**_
선언형은 무엇을 할지를 나열하고 명령형은 어떻게 할지를 구현한다.
출처 : https://sung-studynote.tistory.com/109
- _**containsInAnyOrder()**_
JUnit으로 객체가 property(field)로 특정값들을 가지고 있는지 체크할 때는 hasProperty를 사용
이 테스트를 Collection에 있는 객체들을 대상으로 체크하고 싶다면 containsInAnyOrder와 함께 사용
출처 : https://blog.leocat.kr/notes/2019/09/01/junit-check-property-in-collection

>  _**TIP**_
- AOP를 이용하면 비즈니스 로직에 쉽게 부가기능 추가 가능 
- 런타임은 코드가 컴파일되고 클래스가 로드되어 객체가 만들어진 상태.
- 스프링의 AOP는 **bean으로 등록된 객체**에만 proxy객체가 만들어진다.
그래서 Bean으로 등록되지 않은 객체에는 advice가 적용되지 않는다.
- intelliJ에서는 advise에서 advise가 적용될 메소드들을 미리 보여준다.
  _(단, 해당 메소드들이 Bean으로 등록되어져 있어야 한다.)_
- 프로젝트 진행 시 비즈니스 로직 이외의 AOP같은 공통 기능은 프레임워크 팀에서 담당하게 된다. 
- 트랜잭션 어노테이션은 _서비스_ 부분에서 많이 사용한다. 여러 DB 액션 (DML)을 묶는 것이 _서비스_이기 때문이다. AOP코드에서 @around로 코드를 감싸서 로직을 분리한 것처럼 트랜잭션 로직도 유사한 방식으로 동작한다. 트랜잭션 어노테이션을 사용하면  _서비스_에는 트랜잭션 매니저나 데이터 소스를 주입할 필요가 없어진다. 이것이 가능하게 하는 언더라인 기술에는 AOP, 프록시가 있다.(트랜잭션 어노테이션 사용 시 레포지토리를 주입받아서 사용하는 서비스(호출자)에서 프록시를 사용한다.)
- @Transactional(isolation = isolation.`고립수준`) 으로 고립수준을 지정해줄 수도 있다.
 @Transactional(isolation = isolation.`default`)는 DB 벤더에서 설정한 디폴트 고립수준을 따른다는 뜻이다. 
 DB의 디폴트 고립수준을 알 수 있는 쿼리 -> `SELECT @@SESSION.transaction_isolation;`

> _**더 공부하면 좋을 포스팅**_


>  _**Rf**_
- [yshjft님의 2022년 4월 15일 TIL](https://velog.io/@yshjft/2022%EB%85%84-4%EC%9B%94-15%EC%9D%BC-TIL)
- [향로 님의 AOP 정리 (3)](https://jojoldu.tistory.com/71)
- [Code Java의 Understanding Spring AOP](https://www.codejava.net/frameworks/spring/understanding-spring-aop)
- [ EricJeong 님의 [Spring] 트랜잭션의 전파 설정별 동작](https://deveric.tistory.com/86)
- [Nesoy 님의 트랜잭션의 격리 수준(isolation Level)이란?](https://nesoy.github.io/articles/2019-05/Database-Transaction-isolation)
- [이재현 님의 트랜잭션 수준 읽기 일관성](http://wiki.gurubee.net/pages/viewpage.action?pageId=21200923)