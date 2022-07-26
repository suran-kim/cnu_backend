>_본 포스팅은 프로그래머스 미니 데브 코스를 공부하며 
학습을 기록하기 위한 목적으로 작성된 글입니다._




---

#### _테스트 시 주의사항_
- ❌테스트 메소드는 _추상메소드_이면 안된다❌
- ❌테스트 메소드는 어떤 값도 _return_ 해선 안된다.❌
- Happy path만 생각하는 것이 아니라, Unhappy path를 생각해야 한다.

---

단위테스트 시 사용하는 용어로 테스트 대상을 분류해보자.

_**SUT**_
- FixedAmountVoucherTest
- HamcrestAssertionTest 


_**Method**_
- testAssertEqual()
- testMinusDiscountAmount()
- testWithMinus()
- testVoucherCreation()
- hamcrestTest()
- hamcrestListMatcherTest()

---
_**테스트 방법론 **_

 **_TDD_**

- 테스트 케이스부터 작성하면서 코드를 구현하는 방법

 **_BDD_**

- 구현을 해놓고 테스트 케이스를 작성하는 방법

어떤 방법론이 옳다고 할 수는 없다. 
가장 중요한 건 테스트 코드가 존재하는 것이다.
비즈니스룰은 테스트 코드만으로 파악될 수도 있기 때문이다.

---


# 테스트 클래스 생성

intelliJ의 create Test를 통해 테스트 클래스를 쉽게 생성할 수 있다.

![](https://velog.velcdn.com/images/suran-kim/post/e7168cf7-90fa-43f0-a046-ed6bc0d92356/image.png)



- _**@Before**_
실제 테스트 시작 전 해야할 셋업이 있을 때


- _**@After**_
리소스 정리 및 클린업 코드 필요 시 사용


- _**Member **_
테스트 코드 작성을 원하는 멤버 선택

<br/>
<br/>
intelliJ의 검색기능을 통해 testMethod 검색 -> 메소드 작성 가능

inteliJ에서 test클래스를 작성하면 
main과 같은 패키지 구조로 테스트 클래스가 생성되고
assert문에 대한 _static import_가 자동으로 생성된다. 👇 

```java
import static org.junit.jupiter.api.Assertions.*;
```
<br/>


그래서 굳이 static문을 입력하지 않아도 쉽게 테스트 코드를 작성할 수 있게 된다.
(static import 미사용 시 메소드를 `Assertions.assertEquals(...)`의 형식으로 사용해야 함)

> **_static import_** 란?
import문을 static으로 가져오는 구문이다.
Junit에서의 테스트 코드 작성 시 사용하면 가독성을 높일 수 있다. <br/>
[참고자료 : 📌 kasania 님의 [Java] Static import에 대한 관찰] (https://velog.io/@kasania/Java-Static-import에-대한-관찰)


<br/>
<br/>

---

## assertEqual()

_**테스트 코드**_
![](https://velog.velcdn.com/images/suran-kim/post/91db4280-2760-4884-ac49-20c693343740/image.png)

<br/>
<br/>

_**결과**_
![](https://velog.velcdn.com/images/suran-kim/post/d2c58e07-6560-4b94-92f2-80e34043a155/image.png)


테스트가 성공적으로 수행되면 green light를 볼 수 있다.

그런데 테스트 수행 시 
`method명`만 봐서는 테스트 결과를 직관적으로 파악하기 힘들 때가 있다.  

이 때 `@DisplayName`를 이용할 수 있다.



> _**@DisplayName**_
테스트 코드의 가독성을 높일 수 있는 어노테이션. 
함수명에 대한 설명을 한글로 입력할 수 있다는 장점이 있다.
_@Nested_ 와 함께 사용하면 더욱 좋다. <br/>
[📌 참고자료 : 뱀귤 님의 [Spring] JUnit 5 에서 @Nested 와 @DisplayName 으로 가독성 있는 테스트 코드 작성하기](https://bcp0109.tistory.com/297)

<br/>
<br/>

_**코드**_
![](https://velog.velcdn.com/images/suran-kim/post/b6d08686-fee6-4c24-86fa-805bf1fd4f73/image.png)

<br/>
<br/>

_**결과**_
![](https://velog.velcdn.com/images/suran-kim/post/a85656d0-ed81-4549-a4fa-74fe804c6ccd/image.png)

이모지도 넣을 수 있다. 너무 귀여워~🎃

<br/>
<br/>


---



## assertThrows()
이번에는 Unhappy path, 에러가 발생하는 테스트를 진행해보겠다.

<br/>


_**테스트 코드 추가**_
```java
  @Test
  @DisplayName("할인 금액은 마이너스가 될 수 없다.")
  void testWithMinus() {
      assertThrows(IllegalArgumentException.class, () -> new FixedAmountVoucher(UUID.randomUUID(), -100)); 
  }
```
<br/>


`assertThrows()`메소드의 
첫 번째 인자에 `IllegalArgumentException.class`를,
두 번째 인자에는 에러가 발생해야 하는 람다식을 적어준다.

<br/>


> _**Assertion.assertThrows()**_
두 번째 인자를 실행해서 첫 번째 인자와 같은 예외 타입인지 검사한다. 
assertThrows()문의 `첫 번째 인자`에는 예외 발생 시의 예외 클래스를 적어줘야 한다.
assertThrows()문의 `두 번째 인자`는 실행가능한 Executable executable <br/>
[📌 참고자료 : Covenent 님의 완벽정리! Junit5로 예외 테스트하는 방법](https://covenant.tistory.com/256)

<br/>
<br/>



_**코드**_
![](https://velog.velcdn.com/images/suran-kim/post/c0066d4c-d947-4241-8118-59a5216d3c70/image.png)


_**결과**_
![](https://velog.velcdn.com/images/suran-kim/post/06884567-d59b-4e20-8e89-343facca91c4/image.png)



코드를 실행시켜보면 첫 번째 인자에 해당하는 예외가 발생하지 않았기 때문에 다음과 같은 오류가 발생한다. `IllegalArgumentException`이 발생하지 않았다는 소리이다.

<br/>


```java
org.opentest4j.AssertionFailedError: Expected java.lang.IllegalArgumentException to be thrown, but nothing was thrown.
```


<br/>
따라서 예외가 발생하게끔 FixedAmountVoucher 클래스의 생성자 메소드에 amount에 대한 로직을 작성해준다.

<br/>
<br/>

_**FixedAmountVoucher에 로직 추가**_
```java
public FixedAmountVoucher(UUID voucherId, long amount) {
    
	// 추가한 로직 ------------------------
	if (amount < 0) throw new IllegalArgumentException("Amount should be positive");    
	// -----------------------------------
    
	this.voucherId = voucherId;
	this.amount = amount;
}
```

<br/>
<br/>

_**결과**_
![](https://velog.velcdn.com/images/suran-kim/post/4b48ff5b-fe8f-4a59-a4de-6adfd34a883b/image.png)

로직을 설정하고 다시 테스트를 실행하면 `IllegalArgumentException`이 발생해서 테스트가 성공한다.

<br/>
<br/>

## @Disabled


만일 개발 중 테스트가 계속 실패하는데 현재 당장 테스트를 고칠 수 없는 상황이라면
@Disabled를 사용할 수 있다.
<br/>


_**코드**_
```java
    @Test
    @DisplayName("할인 금액은 마이너스가 될 수 없다.")
    @Disabled // 추가
    void testWithMinus() {
        assertThrows(IllegalArgumentException.class, () -> new FixedAmountVoucher(UUID.randomUUID(), -100));
    }
```

<br/>
<br/>

_**결과**_
![](https://velog.velcdn.com/images/suran-kim/post/1db824e2-a093-44fb-88db-38d1deb56a5e/image.png)


> _**@Disabled**_
테스트 클래스 또는 메소드를 비활성화 할 수 있다. <br/>
[📌 참고자료 : heejeong Kwon 님의 [JUnit] JUnit5 사용법 - 기본](https://gmlwjd9405.github.io/2019/11/26/junit5-guide-basic.html)

<br/>
<br/>



## @BeforeAll / @BeforeEach


_**@BeforeAll**_
클래스가 생성되기 전 맨 처음 한 번만 실행된다.

_**@BeforeEach**_
각 메서드가 실행되기 직전에 매번 호출된다.


<br/>
<br/>

_**FixedAmountVoucherTest에 코드 추가**_

```java
class FixedAmountVoucherTest {

	// 코드 추가---------------------------------------
    
    private static final Logger logger = LoggerFactory.getLogger(FixedAmountVoucherTest.class);

    @BeforeAll
    static void setup() {
        logger.info("@BeforeAll - 단 한 번 실행");
    }

    @BeforeEach
    void init() {
        logger.info("@BeforeEach - 매 테스트마다 실행");
    }
    
	//------------------------------------------------
    
    @Test
    @DisplayName("기본적인 assertEqual 테스트 🎃")
    void testAssertEqual() {
        assertEquals(2, 1 + 1);    // unexpected: 기대되는 값, actual: 판별해야하는 값
    }

    @Test
    @DisplayName("주어진 금액만큼 할인을 해야한다.")
    void testDiscount() {
        var sut = new FixedAmountVoucher(UUID.randomUUID(), 100);
        assertEquals(900, sut.discount(1000));
    }

    @Test
    @DisplayName("할인 금액은 마이너스가 될 수 없다.")
    void testWithMinus() {
        assertThrows(IllegalArgumentException.class, () -> new FixedAmountVoucher(UUID.randomUUID(), -100));
    }
}
    
```


log가 제대로 출력되지 않을 시 logback.xml파일을 확인하고 
logback-test.xml복사본을 만들어 root레벨을 debug단계로 수정해준다.


<br/>
<br/>


_**결과**_
![](https://velog.velcdn.com/images/suran-kim/post/ef6080f4-d056-4477-a7a1-88bfba8c8f4a/image.png)


<br/>
<br/>

(+) 추가로, @Before과는 반대로 코드가 실행된 후에 호출되는 @AfterAll, @AfterEach도 있다.

_**@AfterAll**_
모든 메소드가 실행된 뒤 마지막에 한 번만 실행된다.

_**@AfterEach**_
각 메서드가 실행된 직후에 매번 호출된다.

<br/>
<br/>

---

# Testcase에서의 예외 상황 설정

Ex ) 할인금액(amount)가 판매금액(beforeDiscount)보다 크다면?

<br/>

_**코드**_
![](https://velog.velcdn.com/images/suran-kim/post/43790237-dd13-485e-b40f-1c98d9b92fd2/image.png)


_**결과**_
![](https://velog.velcdn.com/images/suran-kim/post/9111bed5-0f40-4bc8-8b49-d4b8ce95d59a/image.png)


테스트를 실행해보면 기대값과 결과값이 다르므로 코드가 잘못됐다는 것을 알 수 있다.
이번에도 FixedAmountVoucher의 코드를 수정해보자.

<br/>
<br/>

**_
FixedAmountVoucher_**

_수정 전_
```java
    public long discount(long beforeDiscount) {
        return beforeDiscount - amount;
    }
```

_수정 후_
```java
    public long discount(long beforeDiscount) {
        var discountedAmount = beforeDiscount - amount;
        return (discountedAmount < 0) ? 0 : discountedAmount;
    }
```
<br/>
<br/>

_**수정 후 결과**_
![](https://velog.velcdn.com/images/suran-kim/post/1b7d84c7-a7ef-428b-a98d-56d2ef89196f/image.png)


테스트 코드를 작성해나가면서 기존에 미처 생각하지 못했던 부분을 발견하는 게 중요하다.

<br/>
<br/>

## assertAll()

> _**assertAll()**_
assertAll()은 여러 테스트를 한 번에 검증할 수 있다.
단, assertAll() 내에서 검증하는 모든 테스트를 통과해야만 한다. <br/>
[참고자료 : 📌 코동이 님의 AssertAll](https://escapefromcoding.tistory.com/358)

<br/>



예외상황2
Ex ) 할인금액으로 큰 금액을 입력 시 허용해줘야할까?

_**코드**_
```java
class FixedAmountVoucherTest {

    @Test
    @DisplayName("유효한 할인 금액으로만 생성할 수 있다.")
    void testVoucherCreation() { // 여러 개의 테스트 동시 실행
        assertAll("FixedAmountVoucher creation",
                () -> assertThrows(IllegalArgumentException.class, () -> new FixedAmountVoucher(UUID.randomUUID(), 0)),
                () -> assertThrows(IllegalArgumentException.class, () -> new FixedAmountVoucher(UUID.randomUUID(), -100)),
                () -> assertThrows(IllegalArgumentException.class, () -> new FixedAmountVoucher(UUID.randomUUID(), -100))
        );
    }
}
    
```
<br/>
<br/>

_**결과**_
![](https://velog.velcdn.com/images/suran-kim/post/44b12322-096c-4cbd-8f00-4e2466ceedaa/image.png)



**_
FixedAmountVoucher_**

_수정 전_
```java
    private final UUID voucherId;
    private final long amount;
    
    public FixedAmountVoucher(UUID voucherId, long amount) {
        if (amount < 0) throw new IllegalArgumentException("Amount should be positive");
        this.voucherId = voucherId;
        this.amount = amount;
    }
```

_수정 후_
```java
    private static final long MAX_VOUCHER_AMOUNT = 10000; // 프로그램에서 허용하는 amount의 최대값
    private final UUID voucherId;
    private final long amount;

    public FixedAmountVoucher(UUID voucherId, long amount) {     // 각각의 예외 메세지 정확하게 기입
        if (amount < 0) throw new IllegalArgumentException("Amount should be positive");
        if (amount == 0) throw new IllegalArgumentException("Amount should not be zero");
        if (amount > MAX_VOUCHER_AMOUNT) throw new IllegalArgumentException("Amount should be less than %d".formatted(MAX_VOUCHER_AMOUNT) );
        this.voucherId = voucherId;
        this.amount = amount;
    }

```

_**실행결과**_
![](https://velog.velcdn.com/images/suran-kim/post/433ac576-a99a-461b-999b-875f491afb2e/image.png)





<br/>
<br/>

---

# hamcrest 클래스

hamcrest 클래스는 다양한 매치룰을 쉽게 작성하고 테스트할 수 있는 매처(Matcher)들에 대한 라이브러리이다. _(테스트 프레임워크가 아니다.)_

hamcrest 클래스의 매처를 사용하면
테스트 검증을 위한 junit의 Assertion클래스와 연계하여 
junit의 assert 조건코드를 더 가독성있게 바꿀 수 있다.


spring.starter가 추가되어있다면 hamcrest도 자동으로 추가되어있다.
따라서 따로 dependency를 추가할 필요는 없다.


<br/>
<br/>

## assertThat()

새 테스트 클래스
HamcrestAssertionTest를 만든다.

<br/>

_**코드**_
![](https://velog.velcdn.com/images/suran-kim/post/a966fbdf-38e7-4009-83b1-815e1069a90d/image.png)

위 코드들의 결과는 모두 동일하다.

> _**assertThat**_
hamcrest 라이브러리를 통합하며 assertion에 있어 더 나은 방법을 제공하는 메소드이다.
hamcrest가 static 메서드로 제공하는 여러 matcher를 사용할 수 있다. <br/>
[참고자료 : 📌 JongMin 님의 Unit Test에서 AssertThat을 사용하자](https://jongmin92.github.io/2020/03/31/Java/use-assertthat/)

> _**equalTo()**_
equalTo()는 org.hamcrest.Matchers 클래스에서 제공하는 메소드이다.

> _**is()**_

<br/>
<br/>

_**코드**_
![](https://velog.velcdn.com/images/suran-kim/post/e74a251f-c157-4e8d-818b-3ff5afadf0b3/image.png)

<br/>
<br/>

> _**anyOf()**_
어떤 메소드 호출 시 결과 값이 나뉠 수 있을 때 사용한다.


<br/>
<br/>

_**코드**_
![](https://velog.velcdn.com/images/suran-kim/post/8781f296-b211-48df-b914-83c21e56ce2a/image.png)

역시 둘은 동일한 기능을 한다.

> _**not()**_

<br/>
<br/>

---

## collection에 대한 테스트

hamcrest는 collection에 대한 테스트에 있어 편리한 기능을 제공하기도 한다.
리스트에 대한 검증은 hamcrest를 사용하는 가장 큰 이유이다.

만약 배열의 크기를 assertEquall()로 구해야한다면 
배열의 사이즈를 구한 뒤 어떤 값과 같은지 비교해야하는 코드를 작성해야할 것이다.
그러나 assertThat()에서는 hasSize()를 사용할 수 있다.

<br/>
<br/>

_**코드**_
![](https://velog.velcdn.com/images/suran-kim/post/544705dd-6eb6-4704-b21a-7657dde794af/image.png)

> _**hasSize()**_
Collection의 크기 비교 시 사용

> _**everyItem()**_
Collection의 아이템 전체를 순회하면서 테스트한다.
- _**greaterThan()**_
입력한 값보다 더 큰 아이템이 있는지 비교

<br/>
<br/>

collection 내부에 어떤 아이템이 존재하는지도 확인할 수 있다.

![](https://velog.velcdn.com/images/suran-kim/post/89d7d815-593d-4209-aa8d-96ad556a3f9f/image.png)



> _**containsInAnyOrder()**_
순서가 중요하지 않을 때 사용.
순서를 포함하지 않고 매개변수로 입력한 아이템들이 존재하는지 확인
**_contains()_**
순서가 중요할 때 사용

 

> _**hasItem()**_
단 하나의 아이템이 collection과 일치하는지 확인 
hasItem()안에 매처를 넣을 수도 있다.
- **_greaterThanOrEqualTo()_**
컬렉션 내부에 입력 값보다 크거나 같은 값이 있는지 확인한다.
매처가 돌면서 개별적으로 매칭을 한다.

<br/>
<br/>

>rf
https://www.lesstif.com/java/hamcrest-junit-test-case-18219426.html