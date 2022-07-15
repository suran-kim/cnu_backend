>_본 포스팅은 프로그래머스 미니 데브 코스를 공부하며 
학습을 기록하기 위한 목적으로 작성된 글입니다._

- [1. 트랜잭션이란?](#1--------)
- [2. 트랜잭션 문법](#2--------)
- [3. commit모드 - Autocommit](#3-commit-----autocommit)
  * [💿 Autocommit true](#---autocommit-true)
  * [💿 Autocommit false](#---autocommit-false)
- [4. 트랜잭션에서의 DELETE vs TRUNCATE](#4---------delete-vs-truncate)
  * [💿 DELETE](#---delete)
  * [💿 TRUNCATE](#---truncate)
- [5. 정리](#5---)

<small><i><a href='http://ecotrust-canada.github.io/markdown-toc/'>Table of contents generated with markdown-toc</a></i></small>


# 1. 트랜잭션이란?

트랙잭션은 `원자성`을 지키며 실행되어야하는 SQL문들을
하나의 작업으로 묶어 처리하는 방법이다.

`원자성`이란 트랜잭션의 안전한 실행을 위해 만족해야하는
`ACID 특성` 중 Atomicity에 해당한다. `원자성`은 트랜잭션과 관련된 일은 모두 실행되거나, 
모두 실행되지 않아야 하는 것을 보장하는 특성이다.

트랜잭션에서는 트랜잭션과 관련된 SQL들이 다같이 성공해야하고, 다 같이 실패해야한다.
SQL문 중 하나라도 실패하면 다시 트랜잭션 실행 전의 원상태로 돌아가야 한다.

DML, DDL 명령어 사용 도중 테이블을 수정하거나 레코드를 추가하는 등의 `테이블 변경사항`이 있는 경우 트랜잭션이 필요할 때가 있다.

그러나 DML 중에서도 `읽기작업`만 하는 `select문`을 사용하는 경우에는 트랜잭션을 사용할 이유가 없다.





<br/>
<br/>

은행 계좌 이체는 트랜잭션을 설명하기 좋은 예제이다.
>  _계좌 이체는 인출과 입금의 두 과정으로 이루어진다.<br/><br/> A와 B는 거래하기로 했다.
A는 돈을 입금했고 
B는 계좌를 보며 기다리고 있다.
그런데 오류로 인해
A의 계좌에서 인출만 이뤄지고
B의 계좌로 입금되지 않았다.<br/>
돈이 휘발된 것이다.<br/>_


위와 같은 일을 발생시키지 않기 위해서
트랜잭션은 전부 성공하거나, 전부 실패해야 한다.





<br/><br/>

---


# 2. 트랜잭션 문법


트랜잭션을 사용하려면
SQL문을 `BEGIN`과 `END(COMMIT)`으로 감싼다.

이렇게 트랜잭션을 사용할 경우
`END(COMMIT)`이 실행되기 전까지는 
변경내용이 DB에 반영되지 않는다.

하나의 트랜잭션은 `COMMIT` 과 `ROLLBACK`을 만나면 끝난다.



<br/>

_트랜잭션 기본 문법_

```sql
BEGIN;  -- START TRANSACTION 
 A의 계좌에서 인출
 B의 계좌로 입금
END;  -- COMMIT
```
- 트랜잭션이 성공하면 성공적으로 `COMMIT`이 된다. 
`COMMIT` 전까지는 작업내역이 나 이외의 다른 사람들에게 보이지 않는다.

- 만약 `BEGIN` 전의 상태로 돌아가고 싶다면 `ROLLBACK`을 실행한다.

- 관계형 데이터베이스는 모두 트랜잭션을 지원한다. 그러나 동작은
`Autocommit모드`에 따라 달라진다.

<br/>


<br/>

<br/>


<br/>



---

# 3. commit모드 - Autocommit 

Autocommit은 자동으로 `COMMIT`을 해주는 모드이다.
Autocommit이 가지는 값은 boolean값이며, 
어떤 MySQL 클라이언트를 사용하느냐에 따라 기본값이 다르다.
MySQL Workbench의 경우에는 기본값이 `True`값이다.


<br/>


_autocommit 상태를 알아보는 코드_

```sql
SHOW VARIABLES LIKE 'AUTOCOMMIT';
```
- VALUE가 `ON`이면 Autocommit값이  `true`인 상태이다.
- VALUE가 `Off`이면 Autocommit값이  `false`인 상태이다.


<br/>




## 💿 Autocommit true



DDL, 혹은 DML 사용 시 자동으로 `COMMIT`이 된다. 
테이블의 수정과 변경 사항이 DB에 바로 적용된다.
즉시 반영을 피하고 싶을 때 `트랜잭션`을 사용할 수 있다.

트랜잭션을 시작할 때
`BIGIN`과 `START TRANSACTION` 키워드를 사용하고,

트랜잭션을 끝낼 때
`END`와 `COMMIT` 키워드를 사용한다.

모든 변경사항은 DB에 바로 `COMMIT`되며,
트랜잭션을 하는 순간에만 즉시 반영이 되지 않는다.


<br/>


_autocommit 값을 설정하는 코드_
```sql
SET autocommit = 1; -- autocommit = 0은 false
```
- `SET autocommit = 1`은 Autocommit값을 `true`로 설정


<br/>
<br/>
<br/>




<br/>


## 💿 Autocommit false

DDL, 혹은 DML 사용 시 변경사항이 자동으로 `COMMIT`되지 않고,
태아블의 수정과 변경사항은 명시적으로 `COMMIT`을 실행해야 DB에 반영된다.

모든 `쓰기작업`은 명시적으로 `ROLLBACK`이나 `COMMIT`을 해야 반영된다.
`COMMIT` 이전에는 데이터가 변경된 것처럼 보여도 **DB에는 반영되지 않는다**.

모든 것을 명시적으로 `COMMIT`해야 반영되는 구조이기 때문에 트랜잭션이라는 개념이 없다.
따라서 `BIGIN` 도 사용하지 않는다. 
만일 원 상태로 돌아가고 싶다면 `ROLLBACK`을 사용한다.



<br/>

_autocommit 값을 설정하는 코드_
```sql
SET autocommit = 1; -- autocommit = 0은 false
```



- `SET autocommit = 0`은 Autocommit값을 `false`로 설정

<br/>

<br/>

<br/>
<br/>

---

# 4. 트랜잭션에서의 DELETE vs TRUNCATE


## 💿 DELETE
_**형식**_
```sql 
DELETE FROM 테이블 이름;
```

<br/>

_**장점 **_
- WHERE를 사용한 특정 레코드 삭제가 가능하다.

_**단점 **_

- 속도가 느리다.
<br/>
<br/>

## 💿 TRUNCATE 
_**형식**_
```sql 
TRUNCATE 테이블 이름;
```

<br/>

_**장점 **_
- 전체 테이블 내용을 삭제할 때 속도가 빠르다.

_**단점 **_

- `WHERE`을 지원하지 않는다. 
- `TRANSACTION`을 지원하지 않는다. 
즉, 트랜잭션 안에서`TRUNCATE`를 사용하면 `TRUNCATE`는 트랜잭션에 상관없이
테이블의 내용을 모두 삭제한다.






<br/>

<br/>

---

# 5. 정리 

 트랜잭션은 `원자성`이 필요한 sql문을 하나의 작업으로 묶어서 처리하는 방법이다.
따라서 트랜잭션은 모두 성공하거나, 모두 실패해야 한다.

트랜잭션이 시작할 때 `BEGIN(START TRANSACTION)`으로 묶고
트랜잭션이 끝날 때 `END(COMMIT)`으로 감싼다.

그런데 `TRANSACTION`이라는 개념은 `Autocommit true`인 상태에서 더 의미가 있다.
`Autocommit false`인 상태에서는 모든 `쓰기작업`이 명시적인 `COMMIT 혹은 ROLLBACK`을 해야 DB에 반영되기 때문에 `BIGIN` 도 사용하지 않는다. 

추가로, 테이블의 요소를 삭제하는 키워드인 `TRUNCATE`는 트랜잭션을 지원하지 않기 때문에 트랜잭션 내부에서 `TRUNCATE`를 사용하더라도 `TRUNCATE`는 트랜잭션에 관계없이 테이블의 내용을 모두 삭제한다.


<br/>
<br/>
