>_본 포스팅은 프로그래머스 미니 데브 코스를 공부하며 
학습을 기록하기 위한 목적으로 작성된 글입니다._


- [1. JOIN은?](#1-join--)
- [2. JOIN 시 주의할 점](#2-join--------)
- [3. JOIN 문법](#3-join---)
  * [💿 실습에 사용할 테이블](#--------------)
  * [💿 INNER JOIN *](#---inner-join--)
  * [💿 LEFT JOIN *](#---left-join--)
  * [💿 RIGHT JOIN](#---right-join)
  * [💿 FULL JOIN (OUTER JOIN)](#---full-join--outer-join-)
    + [👉 UNION을 사용하는 경우](#---union---------)
    + [👉 UNION ALL을 사용하는 경우](#---union-all---------)
  * [💿 CROSS JOIN (카티전 프로덕트)](#---cross-join-----------)
  * [💿 SELF JOIN](#---self-join)

<small><i><a href='http://ecotrust-canada.github.io/markdown-toc/'>Table of contents generated with markdown-toc</a></i></small>



# 1. JOIN은?



JOIN이란 스타 스키마 모델을 사용한 RDBMS에서 
**각 테이블을 병합해서 완전한 정보를 얻기위해 사용하는 연산**이다. 


`스타 스키마 모델`은 저장공간의 낭비가 덜하고
특정 엔티티의 레코드를 다른 테이블에 `의존성`없이 쉽게 업데이트할 수 있다.

의존성이란 무엇인가?
_객체 지향 프로그래밍(OOP)에서 `의존성` 이 있다는 것은 **클래스 간 의존 관계**가 있다는 것이다. 
즉, 한 클래스가 바뀌면 다른 클래스도 영향을 받는 것이다. _
`스타 스키마 모델`에서는 그런 `객체(테이블)`간 영향 없이 특정 테이블의 레코드를 업데이트할 수 있다.

그러나 `스타 스키마`를 이용한 DB일 경우 
여러 테이블에 정보가 분산되어 있을 확률이 높으므로 
완전한 정보를 얻기 위해서는 `JOIN 연산`이 필요하다. 

<br/>

_JOIN이란?_
JOIN은 양쪽의 결과를 모두 가진 새로운 가상의 테이블을 만들어서 `SELECT` 해주는 것이다.
<br/>

 JOIN의 방식에 따라 

- 어떤 레코드가 선택되는가?

- 어떤 필드가 채워지는가?

두 가지가 결정된다.

<br/><br/>
<br/>




# 2. JOIN 시 주의할 점

- `중복 레코드`가 없어야 한다.

- JOIN 대상 테이블에 `Primary Key`가 제대로 정의되어 있어야 한다.
- 어느 테이블을 `FROM절`에 기본으로 사용할 것인지 결정해야 한다.
- `JOIN 테이블 간 관계`를 명확하게 정의해야한다.
	 
    - One to one *
    - One to many (ex: `주문 테이블`과 `주문 물품 테이블들` )
    - Many to one
    - Many to many



<br/>

<br/>

<br/>
<br/>


# 3. JOIN 문법



_JOIN 기본 문법_
```sql
SELECT A.*, B.*
FROM table1 A
[INNER | LEFT | RIGHT | CROSS ] JOIN table2 B ON A.number = B.id_number
WHERE table1의 조건;
```


- JOIN 앞의 키워드를 사용하지 않으면 디폴트로 `INNER JOIN` 을 사용한다.

- JOIN에서 WHERE 조건문은 보통 `FROM절 테이블`의 선택조건이다.


<br/>

```sql
SELECT A.*, B.*
FROM table1 A
[INNER | LEFT | RIGHT | CROSS ] JOIN table2 B ON A.number = B.id_number
WHERE A.timestamp >= '2019-01-01';
```
- 위 코드에서는 table1 의 `timestamp`가 2019-01-01 이상인 레코드만 JOIN대상이 된다.

<br/>
<br/>

다음 사진을 참고하며 JOIN의 종류를 학습해보자.

![](https://velog.velcdn.com/images/suran-kim/post/c4c806a0-5fde-44ca-b226-328ff9c8a6d0/image.png)


[이미지 출처] https://theartofpostgresql.com/blog/2019-09-sql-joins/


<br/>
<br/>

## 💿 실습에 사용할 테이블

_vital 테이블_
![](https://velog.velcdn.com/images/suran-kim/post/5c9b5ebd-eb8f-4e8d-8a80-c76f116a8b47/image.png)


- PK는 `vitalID` 


<BR/>

_alert 테이블_
![](https://velog.velcdn.com/images/suran-kim/post/f2be6956-bcca-41b2-8f21-bb7e4e688f02/image.png)

- PK는 `alertID`

<br/><br/><br/><br/>

## 💿 INNER JOIN *

**양쪽에서 매칭되는 레코드만 리턴**한다.
(매칭된다 ->  `ON 뒤에 제시된 병합조건`이 일치하는 레코드를 의미한다.)

매칭이 안 되는 레코드는 JOIN 대상이 아니기에 출력하지 않는다. 
따라서 `INNER JOIN`은 **필드가 모두 채워진 상태**로 리턴한다.

_(대부분의 JOIN은 `INNER JOIN`이다.)_

<br/>

```sql
SELECT * FROM vital v
JOIN alert a ON v.vitalID = a.vitalID;
```

<br/>


![](https://velog.velcdn.com/images/suran-kim/post/c1b4fad7-88c8-47f9-8944-8e3911b45abb/image.png)


<br/>
<br/>



## 💿 LEFT JOIN *


**왼쪽 테이블에 있는 레코드를 전부 리턴**한다.
오른쪽 테이블에서 넘어오는 레코드는 매칭되는 것만 필드가 채워져서 리턴한다.
**매칭이 안 되는 필드는 NULL**로 나온다.

FROM절이 `LEFT JOIN`에서의 왼쪽 테이블이다.


<br/>

```sql
SELECT * FROM vital v
LEFT JOIN alert a ON v.vitalID = a.vitalID;
```

<br/>

![](https://velog.velcdn.com/images/suran-kim/post/dfe028eb-bc83-48c8-a721-f478560fec46/image.png)



<br/>


<br/>
<br/>



    
## 💿 RIGHT JOIN

**오른쪽 테이블에 있는 레코드를 전부 리턴**한다.
왼쪽 테이블에서 넘어오는 레코드는 매칭되는 것만 필드가 채워져서 리턴한다.
**매칭이 안 되는 필드는 NULL**로 나온다.

`RIGHT JOIN`은 순서만 바꾸면  `LEFT JOIN`으로 적용할 수 있다. 

<br/>

```sql
SELECT * FROM vital v
RIGHT JOIN alert a ON v.vitalID = a.vitalID;
```

<br/>

![](https://velog.velcdn.com/images/suran-kim/post/e5791166-8c92-47a2-9eb8-abf0ad491618/image.png)

<br/>
<br/>

_(+) LEFT JOIN과 RIGHT JOIN_

```sql
SELECT * FROM alert a
RIGHT JOIN vital v ON v.vitalID = a.vitalID;
```
- 위 코드에서 `FROM절`에 오는 테이블의 순서만 바꾸면 `LEFT JOIN`의 예제와 동일한 결과를 리턴한다.

<br/>

![](https://velog.velcdn.com/images/suran-kim/post/9f5bfa32-3792-42b2-b7ee-f4c2d2b0d035/image.png)

<br/>
<br/>

## 💿 FULL JOIN (OUTER JOIN)

**양쪽 테이블의 모든 레코드를 리턴**한다.
양쪽이 모두 매칭되는 경우에만 모든 필드들이 채워진 레코드가 리턴되기 때문에
매칭이 안 되어 **NULL값으로 나타나는 레코드가 양쪽 모두**에서 보일 수 있다.


<br/>

### 👉 UNION을 사용하는 경우

```sql
SELECT * FROM vital v
LEFT JOIN alert a ON v.vitalID = a.vitalID
UNION
SELECT * FROM vital v
RIGHT JOIN alert a ON v.vitalID = a.vitalID;
```

- MySQL에서는 FULL JOIN을 지원하지 않기 때문에
`LEFT JOIN`과 `RIGHT JOIN`을 `UNION`하는 형태로 FULL JOIN을 흉내낸다.

- 두 개의 SELECT 결과를 가져오기 때문에 `LEFT JOIN`과 `RIGHT JOIN`에서 발생한 동일한 레코드가 있을 수 있다.
`UNION` 연산은 그렇게 양쪽에서 return되는 **중복 레코드를 없앤 결과를 보여준다**.


<br/>

![](https://velog.velcdn.com/images/suran-kim/post/d5665c18-678f-4994-82f1-c398f7528293/image.png)

<br/>

### 👉 UNION ALL을 사용하는 경우

```sql
SELECT * FROM vital v
LEFT JOIN alert a ON v.vitalID = a.vitalID
UNION ALL
SELECT * FROM vital v
RIGHT JOIN alert a ON v.vitalID = a.vitalID;
```

- `UNION ALL`은 중복 레코드를 없애지 않는다.



<br/>

![](https://velog.velcdn.com/images/suran-kim/post/6c317f84-1a1c-4c64-aca6-85741a321f5b/image.png)



<br/>
<br/>



## 💿 CROSS JOIN (카티전 프로덕트)

**모든 가능한 조합에 대해서 병합**한다.
그래서 JOIN 조건이 없다.

테이블 두 개가 각각 `A`개, `B`개의 레코드를 가지고 있다고 할 때,
두 테이블을 `CROSS JOIN`하면 결과로 출력되는 레코드의 수는 `A * B` 개이다.


<br/>

```sql
SELECT * FROM vital v
CROSS JOIN alert a;  -- JOIN 병합 조건이 없다.
```

- 결과 테이블을 보면
왼쪽 레코드 하나(`vitalID = 1`)에 오른쪽 레코드 3개를 전부 매칭하여 리턴했다. 
왼쪽 다음 레코드 하나(`vitalID = 2`)에 오른쪽 레코드 3개를 전부 매칭하여 리턴했다. 
그것이 왼쪽 레코드의 레코드 수만큼 반복된다.
그래서 출력되는 레코드의 수는 `3 * 4` 이다.


<br/>

![](https://velog.velcdn.com/images/suran-kim/post/6ccf8cb2-8992-4774-b350-59de0a8b6b06/image.png)


<br/>
<br/>



## 💿 SELF JOIN

**같은 테이블끼리 JOIN**한다.
동일한 테이블의 alias (as)를 다르게 해서 자기 자신과 JOIN한다.
JOIN 조건을 어떻게 주는지에 따라 유용하게 쓰일 수 있다.

<br/>

```sql
SELECT * FROM vital v1
JOIN vital v2 ON v1.vitalID = v2.vitalID;
```




<br/>

![](https://velog.velcdn.com/images/suran-kim/post/d47787ba-56eb-4b60-8ed0-c727b9fd6c58/image.png)


<br/>
<br/>
    

    