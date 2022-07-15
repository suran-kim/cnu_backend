>_본 포스팅은 프로그래머스 미니 데브 코스를 공부하며 
학습을 기록하기 위한 목적으로 작성된 글입니다._

- [1. 실습에 필요한 테이블 정의](#1---------------)
- [2. INSERT](#2-insert)
    + [👉 IS NULL](#---is-null)
    + [👉 IS NOT NULL](#---is-not-null)
- [3. DELETE](#3-delete)
  * [💿 DELETE FROM vs TRUNCATE](#---delete-from-vs-truncate)
    + [👉 Safe update mode 와 PK](#---safe-update-mode---pk)
- [4 UPDATE](#4-update)


---
<br/>

# 1. 실습에 필요한 테이블 정의



먼저, `CREATE문`으로 
실습에 필요한 `prod.vital테이블`과 `prod.alert테이블`을 생성한다.

<br/>

```sql
CREATE TABLE prod.vital(
	user_id int NOT NULL,
    vital_id int,
    date timestamp NOT NULL,
    weight int NOT NULL,
    primary key(vital_id)
);
```
- `PK`는 `vital_id`

```sql
CREATE TABLE prod.alert (
	alert_id int,
    vital_id int,
    alert_type varchar(32),
    date timestamp,
    user_id int,
    primary key(alert_id)
);

```

- `PK`는 `alert_id`

<br/>
<br/>

---

# 2. INSERT

INSERT는 **레코드를 추가하는 명령어**이다.

>_ INSERT를 사용하는 구체적인 예로 신규회원 정보 저장이 있다.<br/>
사이트에 신규 회원가입이 발생했을 때, <br/>개발자는 신규 회원정보 데이터를 저장하기 위해 
INSERT 명령어를 사용해 데이터를 추가할 수 있을 것이다._

<br/>

```sql
INSERT INTO prod.vital(user_id, vital_id, date, weight) VALUES(100, 1, '2020-01-01', 75);
INSERT INTO prod.vital(user_id, vital_id, date, weight) VALUES(100, 1, '2020-01-02', 78);
INSERT INTO prod.vital(user_id, vital_id, date, weight) VALUES(100, 1, '2020-01-01', 90);
INSERT INTO prod.vital(user_id, vital_id, date, weight) VALUES(100, 1, '2020-01-02', 95);
INSERT INTO prod.vital(user_id, vital_id, date, weight) VALUES(999, 5, '2020-01-02', -1);  -- weight 필드에 음수값이 들어갔다
INSERT INTO prod.vital(user_id, vital_id, date, weight) VALUES(999, 5, '2020-01-02', 10);  -- primery key로 지정된 필드의 값이 중복된다.
```
- INTSERT의 형식은 위 코드와 같다. 입력한 필드 값을 가진 레코드가 하나씩 추가된다.

- 단, 6번 째 코드는 에러가 출력된다. RDBMS가 PK의 유일성을 보장하기 때문이다.


<br/>

<br/>


```sql
INSERT INTO prod.vital(user_id, vital_id, date, wight) VALUES(1, 4, '2020-01-02', 101);
INSERT INTO prod.vital(user_id, vital_id, date, wight) VALUES(2, NULL, '2020-01-04', 100);
INSERT INTO prod.vital(user_id, vital_id, date, wight) VALUES(3, NULL, '2020-01-04', 101);   
```

- 추가하는 레코드의 순서가 _테이블을 정의할 때의 필드 순서_와 동일하다면 
INSERT문에서 필드명 명시를 생략할 수도 있다.


<br/><br/>

### 👉 IS NULL

- **특정 필드의 값이 NULL인 레코드만 찾고싶을 때**가 있다. 그런데 위 질의로 추가한 2, 3번째 레코드를 찾기 위해 다음과 같은 질의를 작성하면 출력되는 결과는 이것이다.
<br/>



```sql
SELECT * FROM prod.alert WHERE vital_id = NULL;
```


![](https://velog.velcdn.com/images/suran-kim/post/9a54b8bc-772a-4eb4-be9b-54eadc16d726/image.png)

<br/>


- 분명 INSERT문으로 추가한 레코드가 검색되지 않는 이유가 뭘까? 
정답은 _NULL 연산 시 사용해야하는 키워드_에 있다. 
NULL의 연산은 '=' 연산자가 아닌 `IS NULL` 연산으로 진행해야 한다.

<br/><br/>

### 👉 IS NOT NULL


- **NULL이 아닌 값을 찾고싶을 때**도 '!=' 연산자가 아니라 `IS NOT NULL` 연산을 사용하면 된다.



<br/>

```sql
SELECT * FROM prod.alert WHERE vital_id IS NOT NULL;
```


![](https://velog.velcdn.com/images/suran-kim/post/724511b1-16be-4435-9758-01f562769eb9/image.png)



<br/>

<br/>
<br/>
<br/>

---

# 3. DELETE

DELETE는 테이블에서 **조건에 따라 레코드를 삭제하는 명령어**이다.
단, 모든 레코드를 삭제하더라도 테이블은 계속 존재한다.
<br/>
<br/>

## 💿 DELETE FROM vs TRUNCATE

<br/>

`차이점`

- **DELETE FROM**은 조건을 주고 융통성있게 특정 레코드를 삭제할 수 있다.

- **TRUNCATE**는 조건 없이 _모든 레코드_를 삭제한다. 속도가 빠르지만 
트랜잭션 사용시 롤백이 불가능하다.


<br/>

`공통점` 

- 두 명령어로 모든 레코드를 삭제해도 테이블은 계속 존재한다.

<br/>
<br/>
<br/>

_예제_

```sql
DELETE FROM prod.vital WHERE weight <= 0;
```
- 위 예제에서는
`DELETE` 조건으로 `weight필드`_가 0보다 작거나 같은 경우만 삭제_ 를 걸었다.
그럼 INSERT 예제에서 `prod.vital 테이블`에 추가했던 5번째 레코드인 
weight값으로 -1이 들어간 레코드가 삭제된다. (아래 코드 참조)


<br/>

```sql
INSERT INTO prod.vital(user_id, vital_id, date, wight) VALUES(999, 5, '2020-01-02', -1); 
-- 이 레코드는 삭제된다.
```

<br/>
<br/>

### 👉 Safe update mode 와 PK

추가로, 사실 위 예제의
`DELETE FROM prod.vital WHERE weight <= 0;` 코드는 MySQL에서 직접 실행시키면 에러가 발생한다. 
<br/>

MySQL의 기본 동작 모드인 safe update mode에서는 
`DELETE`, `UPDATE`를 할 때 `WHERE절(조건절)`에 반드시 `PK값(키값)`을 이용해야한다. 
게다가 **하나의 레코드만 DELETE, UPDATE하도록 설정**이 되어있기 때문에 
다수의 레코드가 `DELETE`, `UPDATE`하는 sql질의를 실행한다면
아예 해당 코드의 실행을 막아버린다.


<br/>

따라서 질의를 실행하기 위해서는 `vital_id(PK)`를 사용한 다음과 같은 코드를 사용해야 한다.



```sql
DELETE FROM prod.vital WHERE vital_id = 5;
```

<br/>
<br/>

만약 safe mode를 해제하고 싶다면 다음 코드를 실행하면 된다.

```sql

SET SQL_SAFE_UPDATES = 0; 		-- 0 : sefe update mode 해제 , 1: safe update mode 설정

```

<br/>

👉 [워크벤치로 해제하는 방법] (https://velog.io/@suran-kim/MySQL-safe-mode해제)


<br/>

<br/>

---

# 4 UPDATE

UPDATE는 **이미 존재하는 특정 레코드의 필드 값을 수정하는 명령어**이다. 


<br/>

예제를 통해 알아보자. 
다음은 `vital_id (PK)`가 4인 레코드의 `weight`를 92로 변경하는 질의이다.

<br/>

```sql
UPDATE prod.vital
SET weight = 92
WHERE vital_id = 4;
```

- 만약 WHERE로 조건을 주지 않는다면 `prod.vital테이블` 의 모든 `weight필드`의 값이 92로 바뀐다.

- 바로 이런 경우에 `PK`가 유용하게 쓰인다. 
`PK`는 테이블에 존재하는 특정 레코드를 유일하게 지칭할 수 있기 때문이다.

<br/>




![](https://velog.velcdn.com/images/suran-kim/post/8756a2cd-55e5-4b1a-8ba3-7a7a21ffe68d/image.png)


