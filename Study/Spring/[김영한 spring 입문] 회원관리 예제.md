# 목차

- 비즈니스 요구사항 정리
- 회원 도메인과 리포지토리 만들기 

- 회원 리포지토리 테스트 케이스 작성
- 회원 서비스 개발
- 회원 서비스 테스트(Junit 테스트 프레임워크 사용)







# 1. 비즈니스 요구사항 정리

> 
_☝ 들어가기 전에..._ 
이 강의의 목적은 **간단한 스프링 생태계 전반에 대한 이해**와 **스프링의 동작 원리를 이해**하는데에 있으므로 예제의 비즈니스 요구사항은 실제 비즈니스 사례와 다르게 매우 단순하게 설정하였다. 복잡한 비즈니스에 대해 공부하고 싶다면 활용 1편 강의를 수강하면 될 듯.

_구축해야하는 서비스의 요구사항_은 다음과 같다.

**데이터**: 회원ID, 이름

**기능**: 회원 등록, 조회


> *참고로, 가상의 시나리오에서는 아직 **데이터 저장소**가 선정되지 않은 상태이다. 
(가상의 시나리오에서 스프링의 특성을 더욱 잘 설명하기 위함)

---
## 웹 어플리케이션의 계층 구조

일반적인 웹 어플리케이션의 계층 구조는 다음과 같다.

![](https://velog.velcdn.com/images/suran-kim/post/bb7ecffb-f857-4668-8a1a-b862fcd7bb3a/image.png)






_**컨트롤러 **_
- **웹 MVP의 컨트롤러** 역할, 혹은 **API의 컨트롤러 **역할을 한다. 

_**서비스**_
- **핵심 비즈니스 로직**을 구현한 **객체**.
_(EX: 회원은 중복가입을 할 수 없다.)_
- 비즈니스 도메인 객체를 가지고 핵심 로직이 돌아가도록 구현한 객체.

_**리포지토리**_
- 도메인 객체를 저장하고 불러올 수 있는 객체
- 데이터베이스에 접근, 도메인 객체를 DB에 저장하고 관리


_**도메인 **_
- 주로 **데이터베이스**에 저장하고 관리되는 **비즈니스 도메인 객체**
_(EX: 회원, 주문, 쿠폰 등)_


---

## 클래스 의존관계 
![](https://velog.velcdn.com/images/suran-kim/post/84737c96-7ba5-4c4f-b494-79983b8bc57d/image.png)



회원 비즈니스 로직에는 
_**MemberService**_(회원 서비스): 존재.

_**MemberRepository: **_
- 회원을 저장한다. 
- 인터페이스로 설계한다. 
- 아직 데이터 저장소가 선정되지 않았기 때문에 인터페이스의 구현체를 만들어서 데이터를 저장한다. db선정 후 구현체와 바꿔 끼우기 위해 필요한 것이 인터페이스이기 때문에 인터페이스 정의.

_**MemoryMemberRepository: **_
- MemberRepository의 구현체. 
- 향후 사용할 DB의 확정 시 바꿔끼운다. 


> _참고사항_
- 아직 데이터 저장소가 선정되지 않았기 때문에 우선 인터페이스로 구현 클래스를 변경할 수 있도록 설계한다.
- 초기 개발 단계에서는 가벼운 메모리 기반의 데이터 저장소를 구현체로 만들어서 사용한다.
- 고려되는 데이터 저장소는 RDB, NoSQL 등등 다양한 종류가 있다.




# 2. 도메인과 리포지토리 제작
## 전체 코드

_**Member**_
```java
public class Member {

    private Long id; // 시스템이 저장하는 값
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

_**MeberRepository (인터페이스)**_
```java
public interface MemberRepository {
    Member save(Member memer); // 회원 저장 시 회원 반환
    Optional<Member> findById(Long id); // id로 회원을 찾아서 반환
    Optional<Member> findByName(String name);
    List<Member> findAll();
}
```
**_MemoryMemberRepository_**
```java

```



## _Member_

```java
public class Member {

    private Long id; // 시스템이 저장하는 값
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

domain 패키지를 만들고,  
domain/Member.java 를 만들어준다.

요구사항에 따라 ID와 name필드를 제작한다. 
각각 getter, setter를 만들어준다.

> _참고: _
getter, setter의 사용에 대한 여러 가지 의견이 있지만 예제의 단순성을 위해 일단 사용하도록 한다.





---

## _MeberRepository (인터페이스)_


이번에는
회원 객체를 저장하는 회원 리포지토리를 제작한다.
```java
public interface MemberRepository {
    Member save(Member member); // 회원 저장 시 회원 반환
    Optional<Member> findById(Long id); // id로 회원을 찾아서 반환
    Optional<Member> findByName(String name);
    List<Member> findAll();
}
```


repository 패키지를 만들고, 
repository/MeberRepository.java 를 만들어준다.


네 가지 기능을 가진 인터페이스가 만들어진다.

save()는 회원을 저장소에 저장한다.
findById()는 회원의 id로 회원을 찾는다.
findByName()는 회원의 이름으로 회원을 찾는다.
findAll()는 저장된 모든 회원의 리스트를 반환한다.

>   _**Optional**_
java8에 추가된 기능
입력값이 NULL이라면 반환 시 그대로 반환하는 대신 Optional로 NULL을 감싸서 반환
  
---

## _MemoryMemberRepository_
  
MemberRepository의 구현체.
MemberRepository를 implements하여 구현한다.
  
  ```java


  ```
 
### _id, name_ 


```java
    private static Map<Long, Member> store = new HashMap<>(); // 저장소
    private static long sequence = 0L; // 키값 생성
```
 
메모리(저장)의 역할을 하기 위해 Map을 사용한다. 키값을 id로 할 것이기 때문에 id의 타입인 Long타입, 밸류는 Member타입으로 설정한다. 

_sequence_
키값을 생성해주는 역할을 한다. 

### _save 메소드_

```java
    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }
```
member 객체의 name 필드는 사용자가 이미 작성한 값이므로 세팅이 되어있다. 
따라서 id 필드를 설정해줘야 하는데 이를 `save 메소드`에서 구현한다. 
`store` 에 넣기 전에 member의 id값을 설정해주고
`store`에 put을 이용해 `키값`으로는 설정한 id값을, `밸류`로는 member 객체를 준다.
그리고 저장된 member 객체를 `return`한다.

  
>  _ 주의:_
실무에서는 데이터의 동시성 문제가 존재할 수 있기 때문에 공유되는 변수 사용 시
`ConcurrentHashMap`을 사용해야 하지만, 예제에서는 단순성을 위해 `HashMap`을 사용한다.
sequence 역시 실무에서는 동시성 문제로 AtomicLong 등을 사용하지만 예제에서는 Long타입으로 진행한다.
  

### _findById 메소드_
```java
    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }
```

`findById`는 id를 입력받아 해당하는 Member를 반환하는 메소드이다. 
HashMap에서 매개변수로 입력한 키값에 해당하는 밸류를 반환하는 `get()`메소드를 사용하면
`store.get(id)`이라는 코드로 간단하게 Member를 반환할 수 있다.

_그런데 만약 반환해야하는 결과가 NULL이라면 어떻게 해야할까? _

그럴 때 사용하는 것이 `Optional`이다.
반환 값이 NULL일 가능성이 있을 경우에는 `Optional`로 결과를 감싸서 반환한다.
`Optional.ofNullable(store.get(id))`을 사용하면 
메소드를 호출하는 클라이언트 측에서 어떤 처리를 하도록 할 수 있게 한다.



### _findByName 메소드_

```java
    
    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream() // 루프
                .filter(member -> member.getName().equals(name)) // 검증
                .findAny();
    }
```

람다식을 사용하여 
`member.getName()`이 파라미터로 넘어온 `name`과 같은지 확인하는 코드를 작성한다. 

`member.getName()`과 `name`이 같은 경우에만 `filter`가 되고 
같은 경우를 찾는다면 `findAny`로 반환한다. 
그래서 이 식에서 `findAny`의 결과는 `Optional`로 반환이 되는 것이다.


> _**Collection< V >  values()**_
values메서드는 HashMap의 모든 요소의 `값(밸류)`만 묶어 반환하는 메소드이다. <br/>  
[참고자료 : 📌 팽이돌리기 님의 HashMap 메소드 및 사용법](https://gre-eny.tistory.com/97)

> _**Stream.filter()**_
filter메서드는 요소들을 조건에 따라 걸러내는 작업을 해준다.
길이의 제한, 특정문자포함 등 의 작업을 하고 싶을때 사용 가능하다.<br/> 
[참고자료 : 📌 코딩벌레 님의 [Java] 자바 스트림Stream (map,filter,sorted / collect,foreach)](https://dpdpwl.tistory.com/81)  
[참고자료 : 📌 코드 차차님의 Java - filter, map, flatMap 사용 방법 및 예제](https://codechacha.com/ko/stream-filter/)

> _**Stream.findAny()**_
어떤 조건에 일치하는 stream의 **element(요소) 하나**를 찾을 때 사용한다.
비슷한 용도의 메서드로 `findFirst()`가 있다.<br/>
[참고자료 : 📌 코드 차차 님의 Java Stream - findAny()와 findFirst()의 차이점](https://codechacha.com/ko/java8-stream-difference-findany-findfirst/)  

> _**람다식(Lambda)**_
stream 연산들은 매개변수로 함수형 인터페이스를 받도록 되어있는데, 
람다식의 반환값이 바로 함수형 인터페이스이다. 
람다식은 `괄호()`와 `화살표 ->` 를 통해 **함수형 인터페이스**의 인스턴스를 생성하고, 
인터페이스의 메서드를 이름 없이 익명 함수로 구현하여 사용한다.  <br/> 
람다식 사용 시 코드가 간결해지고, 
함수를 따로 만드는 과정 없이 한 번에 처리할 수 있기 때문에 생산성이 높아진다. <br/>  
[참고자료 : 📌 coding captain 님의 [Java] 람다식(Lambda) 익히기 (update.2022-04-16)](https://makecodework.tistory.com/entry/Java-%EB%9E%8C%EB%8B%A4%EC%8B%9DLambda-%EC%9D%B5%ED%9E%88%EA%B8%B0)
[참고자료 : 📌 망나니개발자님의 [Java] 람다식(Lambda Expression)과 함수형 인터페이스(Functional Interface) - (2/5)](https://mangkyu.tistory.com/113)




---
  
  ?의문의 오류
![](https://velog.velcdn.com/images/suran-kim/post/045fba10-88dc-47f6-a5e6-5b409cb1b21d/image.png)


  ... `MemoryMemberRepository`를 구현하면서 `save 메소드`를 오버라이딩할 때, `member.setId(++sequence)`의 `setId` 부분에 반복해서 오류 표시가 나타났다. 코드를 살펴보며 원인을 찾아보니 상단의 Member 클래스 import문에서 Member클래스가 패키지 경로가 아닌, `reflection 클래스`를 통해 import되고 있었다.  `MemoryMemberRepository` 뿐만 아니라 MemoryMemberRepository문이 상속받는 `MeberRepository`인터페이스의 import문에서도 Member 클래스가 `reflection 클래스`를 통해 import되고 있던 것을 발견해 둘 다 패키지 경로로 import하도록 수정해줬다.
  분명 reflection과 관련된 문제 같은데 추후에라도 그 이유를 알고싶어서 기록해둔다.
  
  
<BR/>
<BR/>
<BR/>

> ### _**색인**_
_** MyBaits, JDBC, JPL**_
DB에 저장하는 기술 프레임워크
저장하는 기술에는 MyBaits, JDBC, JPL 등이 있다.
