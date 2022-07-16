>_본 포스팅은 프로그래머스 미니 데브 코스를 공부하며 
학습을 기록하기 위한 목적으로 작성된 글입니다._

# View란?

자주 select하는 쿼리가 있을 때, 해당 `select쿼리`로 만들어진
`가상 테이블`에 이름을 주고 그것을 사용하여 질의하는 것이다.

`가상 테이블`에 이름을 주면 그것이 `View`로서 데이터베이스 단에 저장된다.
주의해야할 점은 `View`는 `테이블`로서 저장되는 것이 아니라는 것이다.

`view`는 `select쿼리`가 view라는 이름으로 저장되는 것이다.
따라서 view가 사용될 때마다 `select`가 실행된다.



view도 마치 **테이블처럼 특정 데이터베이스 아래에 저장**할 수 있다.


<br/>
<br/>
<br/>


## 💿 View 생성방법



_view 형식_
```sql
CREATE OR REPLACE VIEW 뷰이름 AS SELECT 질의 쿼리;
```


<br/>


_예제_
```sql
-- 자주 사용하는 쿼리
SELECT s.id, s.user_id, s.created, s.channel_id, c.channel
FROM session s
JOIN channel c ON c.id = s.channel_id;
```

- 아래 코드는 위의 select문 쿼리를 view로 생성하는 코드이다. 

<br/>


```sql
CREATE OR REPLACE VIEW test.suranSessionDetails AS
SELECT s.id, s.user_id, s.created, s.channel_id, c.channel
FROM session s
JOIN channel c ON c.id = s.channel_id;
```
- 이제 `test.suranSessionDetails`를 가상테이블처럼 사용할 수 있다.

<br/>

<br/>


```sql
SELECT * 
FROM test.suranSessionDetails
WHERE id = 7;
```



![](https://velog.velcdn.com/images/suran-kim/post/54be07c2-6329-4b25-a5d3-65bdb1f0f712/image.png)

- sql문이 간결해진다.


<br/>
<br/>

---

# Trigger란?

`Trigger`는 특정 테이블에 레코드 추가/ 삭제 / 갱신 작업이
벌어지기 직전이나 직후에 어떤 작업을 `자동 수행`하게 하는 기능이다.

`트리거`를 사용하면 데이터의 입력, 수정, 삭제 시 로그를 기록하여 테이블의 `변경내역`을 추적할 수 있다.
 <br/>

**_TRIGGER사용 시 지정해야할 것_**
-  TRIGGER는 테이블을 대상으로 실행되기 때문에 대상 `테이블`을 지정한다.
- `INSERT`, `DELETE`, `UPDATE` 중 어떤 작업이 발생할 때 트리거를 수행하는지 결정한다.
-  트리거를 `작업 전`에 수행해야하는지, `작업 이후`에 수행해야하는지 수행 시점을 결정한다.

 <br/>

**_트리거가 생성하는 임시 테이블_**

 트리거에는 `INSERT`, `DELETE`, `UPDATE` 작업이 수행되면 임시로 사용되는 `시스템 테이블`이 있다. 
 - **NEW** : `INSERT`,  `UPDATE` 작업이 수행되면 테이블에 들어올 새로운 데이터를 잠깐 저장한다. 그 뒤, 테이블에서 입력과 변경이 발생한다. 
 - **OLD** : `DELETE`, `UPDATE` 작업이 수행되면 테이블의 예전 값이 잠깐 저장된다. 그 뒤, 테이블에서 입력과 변경이 발생한다.
 
 
 <br/> <br/>


_⚠ 주의 _
다른 DBMS에서는 `뷰`에 대한 트리거를 지원하기도 하지만
MySQL에서는 뷰에 대한 트리거를 지원하지 않는다.

<br/>
  <br/>


## 💿 Trigger 문법


```sql
CREATE TRIGGER 트리거이름
{BEFORE | AFTER} {INSERT | UPDATE | DELETE} ON 테이블이름 
FOR EACH ROW  -- 다수의 레코드가 동시에 변경되는 경우 레코드별 트리거 호출
트리거 로직;

```


<br/>


_예시_
```sql
-- 변경사항 로그를 저장하기 위한 테이블 suranNameGenderAudit 생성.
DROP TABLE IF EXISTS test.suranNameGenderAudit;
CREATE TABLE test.suranNameGenderAudit (
	name varchar(16),
    gender enum('Male', 'Female'),
    modified timestamp
);

-- 테이블의 내용이 UPDATE 되기 직전에 실행되는 트리거 eforeUpdateSuranNameGender 생성. 
DROP TRIGGER IF EXISTS test.beforeUpdateSuranNameGender;
CREATE TRIGGER test.beforeUpdateSuranNameGender
	BEFORE UPDATE ON test.suRan_name_gender
    FOR EACH ROW 
INSERT INTO test.suranNameGenderAudit
SET name =  OLD.name, -- 바뀌기 전 이름
	gender = OLD.gender,
	modified = NOW();
    
    
-- test.suRan_name_gender테이블의 내용 변경    
UPDATE test.suRan_name_gender
SET name = 'Benjamin'
WHERE name = 'Ben';
    
```


<br/>



<br/>
<br/>

---

# 정리

view는 자주 쓰는 select쿼리를 마치 테이블처럼 DB에 등록하고, 쉽게 사용할 수 있게 해주는 방법이다. 
view도 테이블처럼 특정 데이터베이스 아래에 저장할 수 있고, 
뷰 생성 시에는 `CREATE OR REPLACE VIEW 뷰이름 AS SELECT 질의 쿼리;` 를 사용한다.

`트리거`는 테이블과 관련된 이벤트(`INSERT, DELETE, UPDATE`)에 반응해 자동 실행되는 작업이다.





> _**Ref**_
https://title-developer.tistory.com/146

