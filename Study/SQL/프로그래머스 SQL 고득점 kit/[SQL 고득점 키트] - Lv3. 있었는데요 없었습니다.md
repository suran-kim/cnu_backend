# 문제
```sql
관리자의 실수로 일부 동물의 입양일이 잘못 입력되었습니다. 
보호 시작일보다 입양일이 더 빠른 동물의 아이디와 이름을 조회하는 SQL문을 작성해주세요. 
이때 결과는 보호 시작일이 빠른 순으로 조회해야합니다.
```
<BR/>

---

# 해결

```sql
SELECT I.ANIMAL_ID, O.NAME
FROM ANIMAL_INS I JOIN ANIMAL_OUTS O USING(ANIMAL_ID)
WHERE I.DATETIME > O.DATETIME 
ORDER BY I.DATETIME;
```


<BR/>
<BR/>

---

# 이것을 배웠다
## 👉 USING

`INNER JOIN`에서 병합조건으로 사용하는 `컬럼명`이 같을 경우 
`USING`절을 이용하면 조인 조건을 간단하게 적을 수 있다.

```sql
SELECT I.ANIMAL_ID, O.NAME
FROM ANIMAL_INS I JOIN ANIMAL_OUTS O ON I.ANIMAL_ID = O.ANIMAL_ID
WHERE I.DATETIME > O.DATETIME 
ORDER BY I.DATETIME;

-- 둘은 동일하다.

SELECT I.ANIMAL_ID, O.NAME
FROM ANIMAL_INS I JOIN ANIMAL_OUTS O USING(ANIMAL_ID)
WHERE I.DATETIME > O.DATETIME 
ORDER BY I.DATETIME;
```


<BR/>
<BR/>
<BR/>


## 👉 헷갈렸던 것🤔
```sql
-- 오답 코드
SELECT I.ANIMAL_ID, O.NAME, I.DATETIME, O.DATETIME
FROM ANIMAL_INS I JOIN ANIMAL_OUTS O USING(ANIMAL_ID)
WHERE I.DATETIME < O.DATETIME  --  보호시작일보다 입양일이 늦은 동물이 조회됨
ORDER BY I.DATETIME DESC;
```
처음에 내가 작성한 코드는 위와 같다. 두 가지 문제가 보인다.

1.  일단 문제 조건인 `보호 시작일이 빠른 순으로 조회`를 잘못 이해해서 `ORDER BY`절에 `내림차순 조회`를 했다. 이러면 보호시작일이 최근인 순으로 조회하게 된다. 

2.  DATETIME 타입의 비교연산에 대해 잘못 이해하고 있었던 것 같다. 
`I.DATETIME < O.DATETIME` 으로 연산하면 보호 시작일보다 입양일이 늦은 동물이 조회된다.
DATETIME 타입 비교연산에서는 최신 날짜가 작고, 오래된 날짜가 큰 것으로 간주되는 모양이다.
DATETIME 타입에서 `ASC`는 오래된 순 정렬이고 `DESC`가 최신순 정렬이라 헷갈렸다. 

<BR/>
<BR/>
<BR/>

---

> rf
https://devjhs.tistory.com/409