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


### JDK Dynamic Proxy
proxy 방식에는 JDK Dynamic Proxy 와 CGLib Proxy 가 있다.
 
 - **JDK Dynamic Proxy**
     - 인터페이스 기반
    - 프록시 타겟은 인터페이스의 **구현체**
    - 별도의 객체(Proxy 클래스)를 만들지 않고 다이나믹하게 Proxy 생성 가능
    -  스프링에서 내부적으로 사용하는 기법
 
 
 
 - **CGLib Proxy** 
   - 클래스 기반
   - 인터페이스 없이 Dynamic Proxy 가능


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

### Schema-based AOP, @AspectJ
- AOP 구현기능이 포함된 소프트웨어
- Schema-based AOP는 xml방식, @AspectJ는 어노테이션을 사용하는 방식
- 최근에는 **@AspectJ** 를 많이 사용한다.
- 스프링이 어노테이션을 읽어서 AOP 방식으로 Aspect를 적용할 수 있게 한다.
- pom.xml에 spring-boot-starter-aop 의존성 추가가 필요하다.




### AOP 주요 용어


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
  
  
### AOP 코드

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




```java
```



### transactionManager를 이용한 트랜잭션

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
            jdbcTemplate.update("UPDATE customers SET name = :email WHERE customer_id = UNHEX(REPLACE(:customerId, '-', ''))", toParamMap(customer));
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

### Jdbc Template를 이용한 트랜잭션

_**트랜잭션이 보장되는 코드2 - (jdbc Template를 이용한 트랜잭션 매니저 콜백)**_

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
                jdbcTemplate.update("UPDATE customers SET name = :email WHERE customer_id = UNHEX(REPLACE(:customerId, '-', ''))", toParamMap(customer));
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




> _**오류 해결**_
- _테스트 시 log가 출력되지 않는 오류 _
@Around("execution(public * org.prgrms.kdt..*.*())") 
-> 어라운드 어드바이스의 execution에 메소드 매개인자를 적지 않아서 생긴 오류 
- _테스트 시 사용자 지정 annotation인 TrackTime이 제대로 동작하지 않고 
`java.lang.IllegalStateException: Failed to load ApplicationContext` 오류가 출력_
-> 어라운드 어드바이스에 어노테이션 인자를 줄 때 정확한 풀네임을 기입하지 않았음
- _테스트 시 객체의 DateTime 데이터의 나노초가 비교 대상와 일치하지 않아서 발생하는 오류 _
```
java.lang.AssertionError: Expected: same property values as Customer [createdAt: <2022-08-02T21:59:26.939686900>, customerId: <ba97c432-c3ab-4c6c-8efe-b723a1c96de0>, email: "a@gamil.com", lastLoginAt: null, name: "a"]
     but: createdAt was <2022-08-02T21:59:26.939687>
```    
  -> 아직 해결 못함 T.T
  - 

> _**새로 알게된 용어**_
- _**클래스 로더**_ 
"자바 클래스들은 시작 시 한번에 로드되지 않고, 애플리케이션에서 필요할 때 로드된다. 클래스 로더는 JRE의 일부로써 **런타임에 클래스를 동적으로 JVM에 로드** 하는 역할을 수행하는 모듈이다. 자바의 클래스들은 자바 프로세스가 새로 초기화되면 클래스로더가 차례차례 로딩되며 작동한다."
출처 : https://leeyh0216.github.io/posts/java_class_loader/
- **_getCanonicalName()_**
- _**ProceedingJoinPoint 인터페이스**_
  @Around Advice에서 사용할 공통 기능 메서드는 대부분 파라미터로 전달 받은 ProceedingJoinPoint의 **proceed() 메서드**만 호출하면 된다. 
  개발도중 호출되는 대상 객체에 대한 정보, 실행되는 메서드에 대한 정보, 메서드를 호출할 때 전달된 인자에 대한 정보가 필요할 때가 있다. ProceedingJoinPoint 인터페이스는 이들 정보에 접근할 수 있도록 여러 메소드를 제공하고 있다.
출처: https://ktko.tistory.com/entry/Spring-ProceedingJoinPoint의-메서드

>  _**TIP**_
- AOP를 이용하면 비즈니스 로직에 쉽게 부가기능 추가 가능 
- 런타임은 코드가 컴파일되고 클래스가 로드되어 객체가 만들어진 상태.
- 스프링의 AOP는 **bean으로 등록된 객체**에만 proxy객체가 만들어진다.
그래서 Bean으로 등록되지 않은 객체에는 advice가 적용되지 않는다.
- intelliJ에서는 advise에서 advise가 적용될 메소드들을 미리 보여준다.
  _(단, 해당 메소드들이 Bean으로 등록되어져 있어야 한다.)_
- 프로젝트 진행 시 비즈니스 로직 이외의 AOP같은 공통 기능은 프레임워크 팀에서 담당하게 된다. 

> _**더 공부하면 좋을 포스팅**_


>  _**Rf**_
- [yshjft님의 2022년 4월 15일 TIL](https://velog.io/@yshjft/2022%EB%85%84-4%EC%9B%94-15%EC%9D%BC-TIL)
- [향로 님의 AOP 정리 (3)](https://jojoldu.tistory.com/71)
- https://www.codejava.net/frameworks/spring/understanding-spring-aop
