>_본 포스팅은 프로그래머스 미니 데브 코스를 공부하며 
학습을 기록하기 위한 목적으로 작성된 글입니다._

_**문자열 사용 SQL문**_
![](https://velog.velcdn.com/images/suran-kim/post/06f05422-e964-41b6-8899-0d40b39f9eb5/image.png)

<br/>

_** prepared Statement 사용 SQL문 (권장)**_
![](https://velog.velcdn.com/images/suran-kim/post/3db9cd0d-78db-4214-a5c9-46565f5d95e6/image.png)


<br/>

_**order_mgmt 테이블**_
![](https://velog.velcdn.com/images/suran-kim/post/26f09f94-47c7-49b1-84f1-cfb832b65270/image.png)

<br/><br/>

# 테이블 준비







![](https://velog.velcdn.com/images/suran-kim/post/9091a6c2-9be6-4d9b-a6ea-7e1ff6910f94/image.png)

intellij와 Mysql서버를 연동하고 쿼리 콘솔을 추가한다.

<br/>

_**코드**_
![](https://velog.velcdn.com/images/suran-kim/post/b36e2d53-3919-4318-8c30-18c122e1cea6/image.png)

<br/>

_**결과**_
![](https://velog.velcdn.com/images/suran-kim/post/3cf5b6d5-68cd-4562-bb4e-7282032d8e30/image.png)

<br/>

쿼리 콘솔에서 MySql과 동일하게 쿼리 입력이 가능하다.

<br/><br/>

# 드라이버 준비
<br/>

_**테이블 작성 코드**_
![](https://velog.velcdn.com/images/suran-kim/post/82550a38-dbe0-44e3-8c14-60cccdd084e4/image.png)



_**코드 설명**_
`customer_id`가 BINARY로 되어있는 이유는 `UUID`를 입력하기 위해서이다.
`last_login_at`의 타입은 datetime이고 기본값은 `Null`이다.
`created_at`의 기본값은 `CURRENT_TIMESTAMP()`이다.
`email`은 제약조건으로 UNIQUE를 설정했기 때문에 중복값이 설정될 수 없다.




> - _**CONSTRAINT**_
CONSTRAINT은 잘못된 데이터 입력을 방지하기 위한 제약조건이다.
[참고자료 : 📌 또루아빠 님의 [SQL기초] 제약조건](https://ttend.tistory.com/630)


<br/>

_**JdbcCustomerRepository클래스**_ 를 새로 생성한다.

로그 출력을 위한 `Logger` 코드를 입력해주고

JDBC 커넥션에 필요한 드라이버 설치를 위해
`pom.xml`에 의존성을 추가해준다.

_mysql jdbc driver maven repo 검색_ 후 복사 붙여넣기 하면 된다.
[maven repository 링크 첨부](https://mvnrepository.com/artifact/mysql/mysql-connector-java)

<br/>

_**의존성 추가 코드**_

![](https://velog.velcdn.com/images/suran-kim/post/8f14d9d7-3cbf-4ff3-bfe5-ac87bd90e22f/image.png)



컴파일 시에는 필요하지 않으므로 `<scope>runtime<scope/>` 코드를 추가한다.
메이븐에 추가한다는 버튼을 눌러주면 의존성 추가 끝!

더 뭔가를 할 필요 없이 `DriverManager`가 추가된 Jdbc 드라이버를 찾아서 사용할 수 있게 해준다.
<br/>

_**코드**_

![](https://velog.velcdn.com/images/suran-kim/post/e02aaee2-c382-47be-bf30-ea8d8c23cb29/image.png)

원래는 코드 상에 패스워드가 절 대!!!!!!!!!!!!!!!!!! 들어가서는 안 된다.
하지만 아직 JDBC를 이용하기 전 설정으로 configuration을 다 빼줘야 하기 때문에
사용해준다..? (무슨 말인지 사실 잘 모르겠다.)


`getConnection`은 사진 상에 보이는 것처럼 `try/catch`로 오류 처리를 해줘야한다. 
<br/>
_**코드**_
![](https://velog.velcdn.com/images/suran-kim/post/59da2b38-56c5-4f8b-9195-435db905ad6e/image.png)


하지만 커넥션은 문제 상황(예외상황 등) 발생 시 무조건 닫아야 한다
finally문에서 접근해서 `close`해줄 수 있도록
코드의 위로 빼준다.



![](https://velog.velcdn.com/images/suran-kim/post/d09b4820-eb99-4502-be63-48a450d3c94f/image.png)

`if (connection != null) connection.close()`에서도 오류가 발생할 수 있으므로 `throws` 로 예외를 던져준다.



위 코드에서는 커넥션을 가지고 오면 `createStatement()`로 statement를 만들어준다.
그런데 사실은 statement도 닫아주어야 한다.

그러므로 `finally`에서 접근할 수 있도록 마찬가지로 statement도 위로 빼준다.


![](https://velog.velcdn.com/images/suran-kim/post/9c9efa6e-c44c-4455-9601-a352a666b1d8/image.png)


만약 finally 내부에서도 오류 처리를 해야한다면 다시 try-catch를 사용한다. 
finally 문에서도 SQLException이 발생할 수 있다. 만약 오류가 발생한다면
logger를 이용해 오류 메세지를 출력할 수 있도록 한다.



![](https://velog.velcdn.com/images/suran-kim/post/d37a93d0-a158-437d-b8e4-97c9d5094411/image.png)
에러를 throw하지 않고 logging 처리 후 끝낸다면 
위와 같은 코드가 될 것이다.


`statement`를 추가했으니 `statement`를 통해 쿼리를 실행할 수 있다.

![](https://velog.velcdn.com/images/suran-kim/post/791875ae-9f05-48db-b984-cd5396b21a9f/image.png)


![](https://velog.velcdn.com/images/suran-kim/post/804e9bd8-5cdd-495a-85b2-79351a443204/image.png)

쿼리 작성 후 project SQL dialect를 MYSQL로 설정해준다.
connection이나 statement를 닫아주듯이, 마찬가지로 `resultSet`도 닫아주어야 한다.

![](https://velog.velcdn.com/images/suran-kim/post/aaa6680c-ffb3-4532-8762-74f56744dbcb/image.png)


예외가 발생하지 않고 잘 실행된다.


`resultSet`은 개별 행에 대한 커서가 존재해서 그것을 `next()`로 옮겨가며 한 건씩 가져와야 한다.



현재 _**Customer 테이블**_ 에는 3건의 데이터가 있다. 
![](https://velog.velcdn.com/images/suran-kim/post/26f09f94-47c7-49b1-84f1-cfb832b65270/image.png)


while문으로 반복을 돌리며 데이터를 꺼내보자.





![](https://velog.velcdn.com/images/suran-kim/post/a174f1e1-acbd-423f-b218-1d2f136cf32a/image.png)







![](https://velog.velcdn.com/images/suran-kim/post/4be7e05c-a970-45a2-90a1-71f33b8bc57a/image.png)

`next()`메소드를 사용하면 커서가 1행을 가리키게 된다. getString()으로 값을 꺼내서 로그로 출력해본다.

_**주의**_
- 커서는 초기에 아무 것도 가리키고 있지 않다. 아무 것도 가리키지 않은 상태에서 
`get()`메소드를 사용하면 오류가 발생한다.


콘솔창에 로그가 출력되지 않는다면 `logback.xml`파일의 log level을 수정해본다.

같은 방식으로 `custom_id`를 꺼내올 수도 있을 것이다. 그러나 `custom_id`는 _**바이너리타입**_ 으로 저장되어있다.

![](https://velog.velcdn.com/images/suran-kim/post/bbeec18a-7aca-462e-888c-e49f1b06dfa7/image.png)


그리고 `custom_id`는 자바코드에서 UUID로 정의됐었다.
그러므로 꺼내온 데이터를 사용하려면 getBytes로 불러온 데이터를 
다시 UUID로 변환해주는 과정이 필요하다.

이 때 _**`UUID.nameUUIDFromBytes()`**_메소드가 쓰인다.


![](https://velog.velcdn.com/images/suran-kim/post/b2ff931f-b012-422c-9d9b-289d3d47da2e/image.png)

![](https://velog.velcdn.com/images/suran-kim/post/7ac00eb3-5d9a-47d4-aef3-465a46bc0bef/image.png)


---

### **_try-with-resources_** 

Connection을 연결한 상태에서 어떤 문제가 발생했을 때 **반드시 Connection을 닫아주어야 한다**. 

왜냐하면 DataBase Connection 자체가 많은 리소스를 차지하기 때문에, 
Connection이 연결된 상태에서는 데이터베이스와 어플리케이션 모두에게 부담이 크기 때문이다.

그래서 쿼리를 진행한 뒤에 반드시 Connection을 닫아줘야 
데이터베이스와 어플리케이션의 부담이 덜어진다.

위의 코드에서는 예외 발생 이후
finally 내부의 try-catch문에서 무조건 
Connection, statement, resultSet을 닫아주고 있다.

`if (connection != null) connection.close();`
`if (statement != null) statement.close();`
`if (resultSet != null) resultSet.close();`

_그래서 코드가 상당히 지저분해졌다._

다행히도 java 10에서 **try-with-resources**를 지원하면서 
try-with-resources를 이용한 자원해제 처리가 가능해졌다.
try-with-resources를 사용하게 되면 try블록에서 Connection을 사용할 수 있고,
사용 후에 Connection이 자동으로 해제된다.


![](https://velog.velcdn.com/images/suran-kim/post/196774c2-2b8c-4663-8dbf-3286a7b22f67/image.png)



try-with-resources를 사용하려면 try에 autoComposerble의 구현체가 들어와야한다.
Connection, statement, resultSet은 모두 autoComposerble의 구현체이기 때문에 
위 코드에서는 첫 번째 `{}블록`이 끝난 시점에 Connection의 연결이 자동으로 닫힌다.


_**AutoCloseable을 구현한 Connection 인터페이스**_
![](https://velog.velcdn.com/images/suran-kim/post/d0326c36-f3ec-43fe-8aea-30d85e0a6b76/image.png)



정리해보자면,
_이전 코드의 `finally`문에서 처리했던 것처럼 모든 예외가 발생하거나
try블록이 끝나면 자동으로 close를 호출해준다._


이는 AutoCloseable를 구현하는 
Connection, statement, resultSet 등의 멀티 리소스에 모두 적용되기 때문에 
사용이 끝나거나 예외가 발생한 모든 리소스의 `close`를 자동 호출해주는 편리한 기능이라고 볼 수 있다.



---

> - _**try-catch**_
 오류 발생 예상 부분을 `try블록`으로 감싼다.
 발생할 오류와 관련된 Exception을 `catch블록`에서 처리한다.
 `finally블록`은 오류가 발생하든 하지않든 무조건 실행된다.<br/>
 - _**throws**_
 throws는 예외가 발생했을 때, 예외를 호출한 쪽에서 처리하도록 던져주는 것이다.

>- _**java.sql.Statement**_
Statement는 Connection으로 연결한 객체에게 **Query 작업**을 실행하기 위한 객체이다.
정적인 쿼리를 처리할 수 있고, 쿼리를 처리할 때는 반드시 값이 미리 입력되어야만 한다.<br/>
_executeQuery(String sql)_
<br/>
[참고자료 : 📌 아리 코딩 님의 [JDBC] 자바에서 sql문 처리하기 (Statement)](https://aricode.tistory.com/9)

> - _**java.sql.ResultSet**_
ResultSet은 Statement을 통해 실행한 쿼리 결과값을 `ResultSet타입`으로 반환해서 저장할 수 있는 객체이다. 값 저장은 `executeQuery(String sql)`메소드를 통해 할 수 있다. <br/>
   _next()_ 
  _**`ResultSet`**_의 행을 가리키는 커서 이동 <br/>
  _getString()_ 
  `컬럼 index`, `컬럼명`을 통해 _**`ResultSet`**_의 값을 가져올 수 있다. <br/>
_  getByte()_

 
<br/><br/>



---

# JDBC를 이용한 CRUD 처리

![](https://velog.velcdn.com/images/suran-kim/post/fc18d5f0-b2d4-40db-8620-70fc5e4bf00c/image.png)
이제 while문에 `Timestamp`타입의 `create_at`필드를 가져올 코드를 작성한다.


그런데 현재 java에서 시간과 날짜를 대표하는 타입은 `LocalDate타입`이다.
따라서 `.toLocalDate()`메소드를 이용해 LocalDate타입으로 형변환한 뒤 이용하는 것이 좋다.


![](https://velog.velcdn.com/images/suran-kim/post/c79a7669-fcb3-46da-b360-fb22fdaa4cc8/image.png)




_**주의**_
- `name`과 `customerId`는 not null 으로 생성되었기 때문에 null 여부를 체크해줄 필요가 없었지만
`created_at`처럼 null이 될 수 있는 경우에는 **null 여부를 체크해주고 형변환**을 해주어야 한다. 
그렇지 않으면 _NullPointException_ 이 발생할 수 있다.






## SQL문 : 문자열 이용 
이번에는 Where절을 추가해보자.
가장 간단한 방법은 sql 문장을 문자열 조합으로 바꾸는 것이다.

SELECT_SQL라는 var변수를 만들고 executeQuery()에 인자로 넣었던 sql문을 대입한다.

그리고 String 타입의 매개변수를 입력받고 
String타입의 List를 반환하는 findNames 메소드를 새로 생성해서 
Main의 코드를 모두 findNames 메소드로 옮긴다.

그리고 return해줄 List를 담을 그릇인 참조변수도 선언해준다.  

![](https://velog.velcdn.com/images/suran-kim/post/053978cf-452d-499b-a773-7958c37eec23/image.png)

findNames를 호출하면 주어진 name에 해당되는 결과를 찾는 것이다. 


`name`이 "name"인 사람을 찾고싶다면 이런 코드 작성이 가능한데, 
![](https://velog.velcdn.com/images/suran-kim/post/b6afbc18-12dd-4b45-be99-5987c68a7c43/image.png)

다음과 같이 좀 더 포맷팅을 해줄 수 있다.
![](https://velog.velcdn.com/images/suran-kim/post/0bec3689-ca9a-4edd-a8d1-af2d3052bf95/image.png)


> - _**formatted()**_
입력값을 string으로 변환해서 return한다.

---

_% 이 코드는 진행하다가 에러가 발생했는데 나중에 그 이유를 찾아보고싶다. %_
![](https://velog.velcdn.com/images/suran-kim/post/3c649ed4-65c3-4603-b510-f138aca3b44a/image.png)

---


이렇게 코드를 작성해주면 name이 치환되어 실제 `WHERE절`에 들어가게 된다.

_**결과**_
![](https://velog.velcdn.com/images/suran-kim/post/87152e72-eb2c-4d19-9dbd-e0ac41e48238/image.png)



하지만 이 코드에는 큰 문제가 있다.
![](https://velog.velcdn.com/images/suran-kim/post/e643b617-aec4-4acd-ae17-a073f5e86212/image.png)


where절 쿼리를 다음과 같이 준다면? 어떤 결과가 나올까?

![](https://velog.velcdn.com/images/suran-kim/post/a967b73b-aea7-475d-92ab-7d9736a5e28f/image.png)


이것이 그 유명한 _**SQL injection**_이다.
`tester01`의 뒤에 `' OR 'a'='a` 라는 or문을 주입시킨 코드이다.

이로 인해 유저의 권한에 맞는 tester01의 정보만 열람할 수 있어야하는데
SQL injection이 발생해 모든 고객의 정보가 모두 열람된 것이다.


만약 SELECT문을 _formInput_ 으로 받았다면?
어떤 문제가 발생했을까?

이처럼 SQL문을 문자열 조합으로 사용한다면
원치않는 쿼리가 실행되는 문제인
_**SQL injection** 에 매우 취약해진다._

이런 문제점 때문에 `SQL문의 문자열 조합`은 매우 신중하게 사용해야 하고
SQL injection이 발생하지 않도록 고민해서 제어해야 한다.




## SQL문 : prepared Statement (권장)

SQL문의 문자열 조합에서 생길 수 있는 문제를 해결하기 위해서 
`prepared Statement`를 사용할 수 있다.



보통 `Statement`를 사용하면
statement.executeQuery()메소드의 사용 등 매번 쿼리를 수행할 때마다

1. 쿼리를 분석하고 
2. 컴파일하고
3. 실행하는 

3단계를 거친다.


그런데 prepared Statement는 처음 한 번만 위의 3단계를 거치고 
이후에는 case에 담아 _재사용_ 된다. 이것이 컴파일 되기 때문에 쿼리문은 
**처음에 만들어진 쿼리문으로 고정**된다.

따라서 SQL injection이 발생할 우려가 사라진다.

`prepared Statement`는 말그대로 Statement를 미리 만들어두는 것이므로
쿼리를 중간에 바꿀 수 없고, 매번 3단계를 거치지 않아도 되기 때문에 성능상의 이점이 있다.

그런 이유로 가급적이면 prepared Statement를 사용하는 것이 좋다.
_(심지어 동적인 SQL문일지라도 prepared Statement으로 사용할 수 있는 방법이 있다.)_



![](https://velog.velcdn.com/images/suran-kim/post/46156a36-aba2-477a-a077-faa50e4590bb/image.png)
prepared Statement으로 SQL문을 사용할 때는 
치환될 데이터에 대해 따로 포맷팅을 할 필요 없이 `물음표 '?'`를 사용한다.

그리고 prepared Statement에 `?` 에 치환될 값을 전달해준다.


그런데 try문에 auto closerable을 할 수 있는 걸 못넣기 떄문ㅇ?? 무슨 소리야 이건

statement를 `{}` 블록에서 호출해주어야 한다.
statement의 setString()메소드를 호출하는데, 
_**setString()**_는 만약 쿼리문에 여러 변수가 들어왔을 때 

![](https://velog.velcdn.com/images/suran-kim/post/c4b2ac81-f294-4c0c-b555-9b07f32af0aa/image.png)

지정된 인덱스 매개변수를 지정된 문자열 값으로 적어주는 것이다.
prepared Statement를 사용하려면 이렇게 **파라미터를 설정**해주어야 한다.
파라미터 설정 시, 인덱스 1부터 시작한다는 점을 숙지해야 한다.

![](https://velog.velcdn.com/images/suran-kim/post/18abdae5-437c-4fd3-9baa-37f74e80da18/image.png)


`resultSet`을 선언하는 코드의 위치를 `{}`블록 안으로 바꿔줬기 때문에
close를 위한 try-catch문을 따로 만들어주어야 한다.


그래서 코드는 다음과 같아진다. 

![](https://velog.velcdn.com/images/suran-kim/post/e966c553-ce3c-464e-b8d8-e9e9dc985992/image.png)


중간에 

`check the manual that corresponds to your MySQL server version for the right syntax to use near '?' at line 1`라는 오류가 발생했는데 
_(왜 강의를 보고 따라치는데도 오류가 발생하는가)_


스택오버플로우에서 이 글을 보고 해결할 수 있었다. 
https://stackoverflow.com/questions/24917917/right-syntax-to-use-near

statement.executeQuery()의 인자에 String을 넣으면 **바인딩하는 파라미터를 사용하지 않고** 쿼리를 실행한다는 것 같다. 자세히 보니 정말 `executeQuery(SELECT_SQL)` 이라고 
코드를 적어두었다.

_**수정한 코드**_
![](https://velog.velcdn.com/images/suran-kim/post/0b4a83f8-a5dc-4daa-bc7b-1f1afb4cf19e/image.png)

![](https://velog.velcdn.com/images/suran-kim/post/ae10c1a1-aab6-40bb-b38c-ee9bce6529d5/image.png)


문제없이 실행된다.

`tester01' OR 'a'='a`를 SELECT문으로 질의해도 `SQL injection`이 발생하지 않는 것이다.


제대로 된 질의를 한 번 넣어보자. 

![](https://velog.velcdn.com/images/suran-kim/post/8315943d-4897-4d0b-9e3b-a1ad94dad282/image.png)
![](https://velog.velcdn.com/images/suran-kim/post/b76e9a12-c2b7-42d9-9cd1-f29a8d079eb6/image.png)

잘 동작한다.




_**SQL injection이 발생하지 않은 원리**_

왜 `' OR 'a'='a`은 들어가지 않았을까?

![](https://velog.velcdn.com/images/suran-kim/post/85e6d2e3-64a8-456b-943c-05bca9936eec/image.png)

다음과 같이 `' OR 'a'='a`를 인자로 주고 statement의 로그를 찍어보면 이런 결과가 나온다.

![](https://velog.velcdn.com/images/suran-kim/post/1a2a178b-d428-4c4c-a35f-394c368f4ae1/image.png)

그럼 로그에서
statement의 실질적 구현체는 `mysql.cj.jdbc.ClientPreparedStatement`라는 것과, 
select문이 어떻게 만들어졌는지 볼 수 있다.

_문자열이 아니기 때문에 OR절이 들어가지 않는다는 사실을 확인할 수 있다._




---

> _**rf**_
https://docs.microsoft.com/ko-kr/sql/connect/jdbc/reference/setstring-method-sqlserverpreparedstatement?view=sql-server-ver16