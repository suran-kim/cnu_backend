>_본 포스팅은 프로그래머스 미니 데브 코스를 공부하며 
학습을 기록하기 위한 목적으로 작성된 글입니다._

# 테스트 클래스 생성

테스트 클래스 `KdtSpringContextTests`를 생성한다.


`import org.springframework.test.context.ContextConfiguration;`

`@ContextConfiguration` 을 이용하기 위해서는 
`test.context`의 안에 있는 클래스를 import해야한다.


`testcontext`로 만들어진 `applicationContext`가 만들어져야 한다.
그렇게 만들어진 `applicationContext`를
테스트 코드에서 `@Autowired`로 받아올 수 있다.


`hamcrest` 관련 패키지도 잊지 말고 import해준다.

_**코드**_

![](https://velog.velcdn.com/images/suran-kim/post/fd188b08-e15c-48f3-86bb-af1114812ef1/image.png)




> - _**notNullValue()**_
notNullValue()는 `hamcrest`의 매쳐 중 하나로 

그런데 위 코드를 실행하면 결과는 아래와 같다.

_**결과**_

![](https://velog.velcdn.com/images/suran-kim/post/ab020976-dc84-4dc5-914d-8c5b7669dcc1/image.png)

`@ContextConfiguration`는 어떤 식으로 
`applicationContext`가 만들어져야 하는지만 알려줄 뿐
실질적으로 junit과 상호작용해서 `applicationContext`이 만들어지게 하려면
`@ExtendWith()`를 사용해야한다.


> _**@ExtendWith()**_
junit5에서 제공하는 어노테이션 


_**코드**_
![](https://velog.velcdn.com/images/suran-kim/post/10a29d0e-3160-4ce9-acdb-ff3815e2b145/image.png)

_**결과**_

![](https://velog.velcdn.com/images/suran-kim/post/45218a2b-1e21-4e90-9187-3dad64edb674/image.png)



실제 `applicationContext`이 생성된 것을 확인 가능.


Junit5에서 동작하기 위해 `@ExtendWith()` 어노테이션을 함께 사용하면
실제 `spring test framework`를 사용할 수 있다.

내부적인 코드에 의해 `test.context`와 상호작용할 수 있게 되는 것이다. 

내부에 생성된 `applicationContext`에서 `@Bean`을 가져와 보자.


_**코드**_


![](https://velog.velcdn.com/images/suran-kim/post/2bec3a27-c8b4-440e-b602-68dd258416c8/image.png)


_**결과**_
![](https://velog.velcdn.com/images/suran-kim/post/4cd5baa2-b6b0-4ef7-bf93-47dda2ff2e3a/image.png)

`applicationContext`가 `Bean`으로 등록되어 있지 않은 것을 확인할 수 있다. 

![](https://velog.velcdn.com/images/suran-kim/post/a4798dd0-80e8-48e3-9421-fc3ad4380554/image.png)

`@ContextConfiguration()` 의 인자로 
xml을 주면 `xml기반의 bean`을 불러올 수도 있고,
클래스를 주면 `applicationContext`에 대해 Bean definition을 한 
클래스들 (Ex: `AppConfiguration`)을 주면 해당 클래스를 불러와서 읽고,
`applicationContext`를 만들어주는 것이다.


하지만 이 방법은 `porfile` 등 별도의 설정이 필요할 수 있기 때문에
이 방법을 쓰지 않고 별도의 ``applicationConfiguration` 클래스 파일을 만들어서 전달해줄 수도 있다. 

테스트 패키지 폴더의 밑에 생성해줄 수도 있고,
테스트 클래스 내부에서만 사용한다면 내부에 static으로 정의해줄 수도 있다.



> **_오류 !_**
`@ContextConfiguration(classes =  {AppConfiguration.class})` 코드의
`AppConfiguration.class` 에서 intellij가  AppConfiguration를 인식하지 못한다는
오류 안내가 나타났다. 테스트 코드인데 `AppConfiguration`클래스  import가 답이 될 리는 없으니
코드를 계속 비교해보다가 새로 만든 test클래스의 패키지 경로가 문제였다는 걸 깨달았다.
main의 java폴더에서 `AppConfiguration클래스`가 위치한 경로보다 
하위 패키지 경로에 test클래스를 작성했기 때문에 
코드에서 `AppConfiguration클래스`를 인지하지 못한 것 같다.
그래서 패키지 경로를 `AppConfiguration클래스`와 동일하게 맞춰주었다.



![](https://velog.velcdn.com/images/suran-kim/post/ec96df62-1012-43cd-a22b-68fffe302f93/image.png)



@ContextConfiguration에 별도로 Bean 설정을 정의한 클래스나 xml을 전달하지 않으면 
기본적으로 클래스 내부의 @Configuration이 달린 static 클래스를 찾게 된다.


![](https://velog.velcdn.com/images/suran-kim/post/d5ca7502-15d8-452f-9960-f5224368747e/image.png)


스프링에서는 조금 더 편리한 어노테이션을 제공해준다.


![](https://velog.velcdn.com/images/suran-kim/post/3d6c1558-492a-4783-aaf0-e7a4cbf6599d/image.png)


**`@SpringJUnitConfig`**를 사용하면

`@ExtendWith(SpringExtension.class)`
`@ContextConfiguration(classes =  AppConfiguration.class )`

위 두 코드를 하나의 코드로 작성할 수 있게 된다.
두 개의 코드에 인자로 전달했던 값을 그대로 `@SpringJUnitConfig`에게 전달할 수도 있다.





# 통합테스트 

이전에는 Mock을 이용한 통합테스트를 진행했다면
이번에는 `applicationContext`에서 가져와서 테스트를 진행한다.



`log 실습` 시 사용했던 
`@ComponentScan(
            basePackages = {"org.prgrms.kdt.voucher", "org.prgrms.kdt.order"})`
            코드를 테스트 코드에 입력해준다.

그렇게 하면
voucher와 order에 대한 컴포넌트 스캔 진행 -> 각 서비스와 레포지토리가 빈에 등록된다.

이제 `@Autowired`로 테스트 대상인 `OrderService`를 가져와서 테스트를 진행한다.

`applicationContext`가 만들어질 때 bean이 wiring되면서 context가 등록되어있는 다른 
VoucherRepository, `OrderService`와 의존관계가 resolve된 상태에서 `OrderService`가 테스트 클래스에 할당된 것이다. 이런 `OrderService`를 가지고 테스트를 진행할 수 있다.


Given, When, Then에 사용했던 코드를 활용할 수 있다.
그러나 몇몇 코드에는 수정이 필요하다.

예를 들어 

voucherRepository를 추가할 때
`var voucherRepository = new MemoryVoucherRepository();`
와 같은 코드 대신

` @Autowired` 
`       VoucherRepository voucherRepository;`

으로 추가해줄 수도 있을 것이다.



> _(+)오류 발생_
![](https://velog.velcdn.com/images/suran-kim/post/9a0e9968-90f8-49b3-8bd3-02ae984bc18e/image.png)
VoucherRepository를 구현하는 구현체 둘
JdbcVoucherRepository와 MemoryVoucherRepository <br/>
![](https://velog.velcdn.com/images/suran-kim/post/e579fd61-69c3-4c43-8b0b-2aa04369602d/image.png)
![](https://velog.velcdn.com/images/suran-kim/post/63d015cf-3702-48c1-9c50-f917fa2ad4c8/image.png)
둘이 @Profile에 매칭이 되어야 bean으로 등록이 된다.
@Profile 중 하나는 active하게 만들어줘야 한다.
이를 위해서는 테스트 시 테스트 클래스의 최상단에 @AvtiveProfiles()를 이용해
active하게 만들어줄 Profile을 전달한다.
![](https://velog.velcdn.com/images/suran-kim/post/ce377c51-bdd3-4dde-ae84-d4fe71ef9cd0/image.png)
만일 test환경이 아니라 JDBC 환경까지 생각한다면 Profile로 "dev"를 전달해줄 수도 있을 것이다. 



설명 
테스트 실행 시 @SpringJUnitConfig으로 test Context frameWork가 만들어지고 거기에서 
`applicationContext`를 만들어준다.  @Configuration에 의해 `applicationContext`가 만들어지고  @ComponentScan으로 각각의 서비스와 레포지토리를 빈에 등록한다. 
이 때 @ActiveProfiles("test")을 적용시켜서 test Profiles에 관련된 빈들이 생성된다.
Profiles을 작성해주지 않은 클래스들도 등록되고(?)
@Profiles을 작성해준 클래스들은 @Repository에 의해 등록이 되지만 
**@Profiles의 인자로 넣어준 Profiles을 사용하는 경우에만** 등록이 된다.

따라서 test @Profiles을 active하게 만들어주었기 때문에 test profiles을 사용하는
MemoryVoucherRepository가 사용되는 것이다.

그래서 실제 Orderservice Bean이 생성되고
필요한 dependency에 있는 객체들도 다 같이 생성되어 의존관계가 형성된다.

이 때는 Mock을 전달한 것이 아니라
실제 Bean으로 등록된 객체 간 상호협력관계를 테스트 한 것이다.

(= 통합테스트)

이것이 스프링에서 제공해주는 테스트 프레임워크이다.

_**@SpringBootTest**_
@SpringBootTest를 통해 테스트하게 되면 SpringBoot 어플리케이션이 실제 구동된다.
SpringBoot 어플리케이션에 대한 통합 테스트 진행 가능.(나중에 학습 예정)

일단은 스프링에서 제공해주는 테스트 프레임 워크 위주로 학습하는 것을 추천한다.