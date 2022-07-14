
>_본 포스팅은 프로그래머스 미니 데브 코스를 공부하며 
학습을 기록하기 위한 목적으로 작성된 글입니다._


## 1. 데이터베이스 선택 명령어 
SQL은 일종의 분류체계처럼 2계층으로 이루어져있다. 
상위 계층에는 **데이터베이스(스키마)**가 있고 하위 계층에는 **테이블**이 있다.
그래서 데이터의 성격에 맞게 데이터베이스들을 관리할 수 있다.

<br/>


```sql
SHOW DATABASES;
USE 데이터베이스 이름;
```
<br/>

`SHOW DATABASES;` 는 전체 데이터베이스를 보여주는 명령어다.
`USE 데이터베이스 이름;` 은 앞으로 테이블 생성은 명시한 데이터베이스에서 하겠다는 의미다.


<br/>

```sql
SHOW TABLES;
```
`USE`로 데이터베이스를 명시해준 뒤 `SHOW TABLES;` 명령어를 사용하면 명시해준 데이터베이스에 존재하는 테이블들을 볼 수 있다.

<BR/><BR/>

---


## 2. SELECT문

### 💿 기본 구조

<br/>

```sql
SELECT 필드이름1, 필드이름2, …
FROM 테이블이름
WHERE 선택조건
GROUP BY 필드이름1, 필드이름2, …
ORDER BY 필드이름 [ASC|DESC] -- 필드 이름 대신에 숫자 사용 가능
LIMIT N;
```

<br/>


- `LIMIT` 는 결과로 출력되는 레코드의 수를 결정해준다.
예를 들어 `LIMIT 10;`이라는 질의를 입력했다면 결과로 출력되는 레코드는 10개가 된다.

<BR/>
<BR/>


### 💿 DISTINCT

<br/>

```sql
SELECT DISTINCT 필드이름
FROM 테이블명
```
<br/>

* `DISTINCT` 는 중복이 아닌 값을 보여준다.
어떤 값이 사용 가능한지 쉽게 확인하고 싶을 경우에 사용한다.

- DISTINCT는 한 필드에만 적용되지 않는다. 
뒤따라오는 모든 필드에 적용된다.




<BR/>
<BR/>

### 💿 COUNT()

<br/>

```sql
SELECT COUNT(*)
FROM 테이블명
```

<br/>


* SELECT된 필드의 레코드 수를 카운트한다.
WHERE문이 있다면 WHERE문의 조건을 만족하는 특정 레코드의 수를 카운트한다. 
`COUNT()` 내부의 값이 NULL만 아니라면 어떤 값을 넣든 `COUNT()`는 동일하게 동작한다.

<BR/>
<BR/>

_예제_
"`COUNT()` 내부의 값이 NULL만 아니라면 어떤 값을 넣든 `COUNT()`는 동일하게 동작한다."
이게 대체 무슨 말일까? 예제를 통해 학습해보았다.


![](https://velog.velcdn.com/images/suran-kim/post/01cf8826-c89c-4d26-aa6c-578a21397d38/image.png)
_prod.count_test_ 테이블의 _value_ 필드가 있다고 하자. 
테이블에 대한 다음 다섯 가지의 질의가 있다. 

```sql
SELECT COUNT(1) 
FROM prod.count_test;

SELECT COUNT(0) 
FROM prod.count_test;

SELECT COUNT(NULL) 
FROM prod.count_test;

SELECT COUNT(value) 
FROM prod.count_test;

SELECT COUNT(DISTINCT value) 
FROM prod.count_test;
```

<BR/>
각 질의에 대한 결과는 어떻게 될까?
<BR/>
<BR/>
<BR/>

```sql
SELECT COUNT(1) 
FROM prod.count_test;

SELECT COUNT(0) 
FROM prod.count_test;
```
<BR/>

`COUNT()`는 기본적으로 SELECT된 레코드의 수를 센다. 
`COUNT()`가 수를 세는 방식은 `COUNT()`의 인자에 따라 달라진다.
`COUNT()`의 인자가 NULL이 아닌 경우에는 무조건 1씩 카운트한다.
NULL인 경우에는? 아예 카운트하지 않는다.

두 질의에서는 `COUNT` 인자가 NULL값이 아니기 때문에 
_value_ 필드의 모든 레코드를 한 번씩 카운트한다.

따라서, 결과는 7이다.
<BR/>

```sql
SELECT COUNT(NULL) 
FROM prod.count_test;
```
<BR/>

이 질의에서는 `COUNT` 인자가 NULL값이다. 
이 경우에는 `COUNT` 함수에 항상 NULL이 들어와 카운트를 하지 않는다.

따라서, 결과는 0이다. 

<BR/>

```sql
SELECT COUNT(value) 
FROM prod.count_test;
```
<BR/>

이 질의에서는 컬럼 값을 `COUNT`의 인자로 줬다. 
이 경우 컬럼의 레코드에서 _NULL이 아닌 경우_만 카운트한다.

따라서, 결과는 6이다.


<BR/>

```sql
SELECT COUNT(DISTINCT value) 
FROM prod.count_test;
```
<BR/>

이 질의에서는 `DISTSINCT`를 먼저 하고 그 다음 `COUNT`를 했다. 
`DISTSINCT` 를 수행했을 때 결과는 NULL, 1, 0, 4, 3 이 된다.
그 다음 `COUNT`를 수행하면 NULL값이 빠진 1, 0, 4, 3 레코드만 카운트한다.

따라서, 결과는 4이다.



<BR/>
<BR/>

### 💿 CASE WHEN 

<br/>


```sql
CASE
WHEN 조건1 THEN 값1
WHEN 조건2 THEN 값2
ELSE 값3
END 필드이름
```

<br/>


- CASE WHEN은 필드들을 가지고 적당한 기준으로 새 필드를 만들어낸다.

- WHEN은 적어도 한 개 이상 있어야 한다.

- CASE로 열고 END로 닫는다.

<BR/>

<BR/>

_예제_
```sql
SELECT channel_id, CASE
WHEN channel_id in (1, 5, 6) THEN 'Social-Media'
WHEN channel_id in (2, 4) THEN 'Search-Engine'
ELSE 'Something-Else'
END channel_type
FROM prod.session;
```
<BR/>

위 예제는 
prod 데이터베이스의 `session`이라는 테이블의 `channel_id`라는 필드에서  
`channel_id`가 1, 5, 6이면 `'Social-Media'`라는 값을 사용하고
`channel_id`가 2, 4이면 `'Search-Engine'`라는 값을 사용하고
아니면 `'Something-Else'`라는 값을 사용하라는 질의가 된다.

<BR/>

위 질의의 결과로 두 개의 필드가 나타난다. 
int 타입의 `channel_id`와 Varchar타입의 `channel_type`이다.

<BR/>

_결과_

![](https://velog.velcdn.com/images/suran-kim/post/caffaf0e-4ee6-49a4-901c-f70e019206d5/image.png)



<BR/>
<BR/>

---
## 3. WHERE

<BR/>

WHERE는 조건문이다.
WHERE에서 사용할 수 있는 다양한 오퍼레이터들이 있다.
다음 오퍼레이터들은 `CASE WHEN` 사이에서도 사용 가능하다.


<BR/>
<BR/>


### 💿 IN

<br/>


```sql
WHERE channel_id IN (1, 2)
-- 위 코드는 다음과 동일하다.
WHERE channel_id = 1 OR channel_id = 2
```

<br/>

* IN은 OR와 비슷한 연산을 한다.
위 연산은 `channel_id`가 1이나 2인 레코드를 출력한다.

<br/>


```sql
WHERE channel_id NOT IN (1, 2)
```

<br/>

* 위 연산은 `channel_id`가 1이나 2이 아닌 레코드를 출력한다.


<BR/>
<BR/>

### 💿 LIKE


<br/>

```sql
WHERE channel LIKE 'A%'  --'A*' channel이름이 A로 시작하는 모든 레코드 

WHERE channel LIKE '%수%'  --'*수*' channel이름에 수가 들어있는 모든 레코드

WHERE channel NOT LIKE '%C%'  --'*C*' channel이름에 C가 들어있지 않은 모든 레코드 

```

<br/>

- `LIKE`는 문자열 매칭을 할 때 사용되는 연산이다. 

- 대소문자 구분을 하지 않는다.

<br/>
<br/>



### 💿 BETWEEN


<br/>

```sql
SELECT DISTINCT 필드이름
FROM 테이블명
```

<br/>

- `BETWEEN` 은 날짜 범위에 사용할 수 있다.

<br/>
<br/>

### 💿 STRING Functions



<br/>

```sql
LEFT(str, N)
REPLACE(str, exp1, exp2)
UPPER(str)
LOWER(str)
LENGTH(str)
LPAD
RPAD
SUBSTRING --필드를 붙여 새로운 함수를 만든다.
CONCAT
```



*  SQL에서는 다양한 String 함수가 지원된다.




<br/>
<br/>

_예시_
```sql
SELECT
	LENGTH(channel),
    UPPER(channel),
    LOWER(channel),
    LEFT(channel, 4), -- 앞에서부터 4글자만 출력
    RPAD(channel, 15, '-') -- 오른쪽에 최대 15글자까지 '-'를 패딩한다
FROM channel;
```

<br/>

_출력결과_
![](https://velog.velcdn.com/images/suran-kim/post/c198d50a-b5bd-4ad3-ae4e-0d1222890000/image.png)

<br/>
<br/>



---

## 4. ORDER BY

<br/>


`ORDER BY`는 `SELECT` 로 가져온 값에 순서를 준다.
`ORDER BY` 의 디폴트 순서는 `오름차순(ASC)`이다.
오름차순은 가장 작은 값이 먼저 나온다.

만약 TIMESTAMP 타입의 필드를 오래된 순으로 정렬하고 싶다면
`오름차순(ASC)`을 사용해야 한다.

반대로 최신순으로 정렬하고싶다면 `내림차순(DESC)`를 사용한다.

<br/>


### 💿 여러 개의 필드를 사용한 정렬 


<br/>

```sql
ORDER BY 1 DESC, 2, 3;
```
<br/>


- 위 질의의 뜻은 다음과 같다.
`SELECT`문에서 사용된 첫 번째 필드 값을 기준으로 내림차순 정렬하고, <BR/>
첫 번째 필드값에서 같은 레코드가 나오면 
두 번째 필드값을 기준으로 오름차순 정렬한다. <BR/>
두 번째 필드값에서도 같은 레코드가 나오면 
세 번째 필드값을 기준으로 오름차순 정렬한다.

<br/>

_예제1_


```sql
SELECT id, channel_id
FROM session
ORDER BY 1 DESC;
```

<br/>

_결과_
![](https://velog.velcdn.com/images/suran-kim/post/cb020b33-b5cc-44a9-b417-061fde89ea55/image.png)


<br/>


_예제2_

```sql
SELECT id, channel_id
FROM session
ORDER BY 2 DESC;
```

<br/>


_결과_

![](https://velog.velcdn.com/images/suran-kim/post/d119bf70-4d06-42ec-b08e-270e92ac05a6/image.png)


<br/>



_예제3_

```sql
SELECT id, channel_id
FROM session
ORDER BY 2 DESC, 1;
```

<br/>


_결과_
![](https://velog.velcdn.com/images/suran-kim/post/53f1caeb-3ac1-496c-9884-14c99830d91d/image.png)
<br/>

_예제4_

```sql
SELECT id, channel_id
FROM session
ORDER BY 2 DESC, 1 DESC;
```


<br/>


_결과_
![](https://velog.velcdn.com/images/suran-kim/post/96cfbeb2-d2f9-4891-b1ac-f114e348c5e3/image.png)

<br/>
<br/>


### 💿 NULL 값의 정렬


<br/>

```sql
NULL
```

<br/>

- NULL값의 정렬은 RDBMS마다 다르다.

- MySQL의 경우에는 
오름차순일 때 NULL값이 처음에 위치하고
내림차순일 때 NULL값이 마지막에 위치한다.

<br/>
<br/>




## 5. GROUP BY
<br/>

SELECT된 레코드를 **특정 필드의 값**을 기준으로 그룹핑하고,
여러 집계함수 연산을 할 수 있게 해준다. 
ORDER BY와 마찬가지로 필드 이름을 사용하거나, 
필드 일련번호를 사용해도 된다. 

<br/>

실습에 사용되는 테이블은 다음과 같다

2만개의 레코드가 들어있는 session테이블과 channel테이블이다.

![](https://velog.velcdn.com/images/suran-kim/post/a2b28af5-dc84-4d88-a94e-de80ca63633f/image.png)
![](https://velog.velcdn.com/images/suran-kim/post/ad534330-9489-408c-9a47-5179660fb185/image.png)



<br/>
<br/>
<br/>

---

### 💿 월별 세션 수


<br/>


```sql
SELECT
	LEFT(created, 7) AS mon,
    COUNT(1) AS session_count
FROM session
GROUP BY 1 -- GROUP BY mon, GROUP BY LEFT (created, 7)
ORDER BY 1;
```

- 위 질의는 `LEFT()`함수로 `created`필드에서  처음 7글자만 뽑아 `mon`이라는 새로운 필드 결과를 생성한다.
- `GROUP BY`로 인해 `mon` 필드의 값이 같은 레코드들이 같은 그룹으로 묶인다.
- `ORDER BY`로 `SELECT` 의 첫 번째 값을 기준으로 정렬한다.
이때, 디폴트 값인 오름차순(ASE)으로 정렬된다. 시간의 오름차순은 오래된 날짜 -> 최신순이다.   


<br/>

_결과_

![](https://velog.velcdn.com/images/suran-kim/post/9252a5ac-d501-4ad6-8649-549f8e363853/image.png)



<br/>

---


### 💿 가장 많이 사용된 채널은?

'우선 가장 많이 사용되었다'의 기준을 정해야한다. 
두 가지 기준이 있을 수 있다.
- 세션 수
- 사용자의 수


<br/>

```sql
SELECT
	channel_id,
    COUNT(1) AS session_count,
    COUNT(DISTINCT user_id) AS user_count
FROM session
GROUP BY 1		-- GROUP BY channel_id
ORDER BY 2 DESC; -- ORDER BY session_count DESC
```

<br/>

- `SELECT`문에서 `COUNT()`를 두 번 했다.
_이 채널에서 발생한 세선의 수 카운트
이 채널을 사용한 유니크한 user_id 카운트
_
<br/>

_결과_
![](https://velog.velcdn.com/images/suran-kim/post/c57fd545-25d1-432d-87ba-a5f24182441e/image.png)


만일 `DISTICT`를 사용하지 않는다면 다음과 같은 결과가 나온다.

![](https://velog.velcdn.com/images/suran-kim/post/32237a4d-e1d3-471a-9c75-90489fd743b7/image.png)




<br/>

---

### 💿 가장 많은 세션을 만들어낸 사용자 ID



<br/>

```sql
SELECT
	user_id,
    COUNT(1) AS session_count	
FROM session
GROUP BY 1 			-- GROUP BY user_id
ORDER BY 2 DESC		-- ORDER BY count DESC
LIMIT 1;
```

<br/>

_결과_

![](https://velog.velcdn.com/images/suran-kim/post/dfe62a6e-e895-49a1-a3aa-6fc4475f3500/image.png)



<br/>


---


### 💿 월별, 채널별 유니크한 사용자 수

MAU(Monthly Active User)는 트래픽 지표에서 매우 중요하다.
MAU에는 시간정보, 사용자 정보가 필요하다.

`월별 세션 수` 실습과 비슷하지만
이번에는 `channel_id` 가 아닌 `channel`의 이름으로 계산해보는 실습을 했다.

그러기 위해서는 두 개의 테이블을 `JOIN` 해야한다.

<br/>

```sql
SELECT 
	s.id, s.user_id, s.created, s.channel_id, c.channel
FROM session s
JOIN channel c ON c.id = s.channel_id;
```

- 위 질의는 INNER JOIN의 예시다.
양 쪽 테이블에서 조건을 만족하는 레코드들이 있는 경우에만 SELECT가 된다(_교집합_).

- JOIN이란 서로 다른 테이블에 존재하는 레코드들을 특정 조건을 바탕으로 병합하여
더 완전한 정보를 가질 수 있도록 하는 연산이다.

- `ON` 뒤에는 병합 조건을 설정해준다.

<br/>

<br/>

이제 월별 채널별 유니크한 사용자 수를 알아보자.

<br/>


_예시_

```sql
SELECT 
	LEFT(s.created, 7) AS mon,
    c.channel,
    COUNT(DISTINCT user_id) AS MAU
FROM session s JOIN channel c ON s.channel_id = c.id 
GROUP BY 1, 2
ORDER BY 1 DESC, 2;
```
<br/>

- 월별, 채널별이기 때문에 `GROUP BY`를 두 개의 필드에 적용한다.
`LEFT(s.created, 7)`와 ` c.channel`이 같은 레코드를 가지는 경우를 하나로 묶고
그 안에서 중복되지 않는 `user_id`만 카운트해서  MAU라는 필드로 만든다.

 - 최종 결과를 `mon`를 기준으로 내림차순하고,  
 중복되는 경우 `c.channel`을 기준으로 오름차순 정렬한다.
 즉, 최신 순부터 정렬되면서 채널이름은 ABC순으로 정렬하게 된다.
 
 - (+)_주의할 점_
 `COUNT`의 동작을 잘 이해하자. 만일 `DISTINCT`를 사용하지 않고 그냥 `user_id`만 카운트한다면 결과는 _사용자 수_가 아니라 _session의 수_를 카운트한 것이 된다.
 
 <br/>
 
 
 _결과_
 
![](https://velog.velcdn.com/images/suran-kim/post/531f3bea-c5eb-4370-ac93-00b2941ab96b/image.png)




_결과2 (DINSTINCT를 하지않았을 때)_
![](https://velog.velcdn.com/images/suran-kim/post/1df6d50b-920d-438d-8a8f-934e7b1001ed/image.png)




