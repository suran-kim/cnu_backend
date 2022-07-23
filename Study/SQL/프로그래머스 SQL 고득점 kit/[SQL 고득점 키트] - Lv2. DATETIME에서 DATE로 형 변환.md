

# 문제

```
ANIMAL_INS 테이블에 등록된 모든 레코드에 대해, 
각 동물의 아이디와 이름, 들어온 날짜1를 조회하는 SQL문을 작성해주세요. 
이때 결과는 아이디 순으로 조회해야 합니다.

```

<br/>

<br/>

---

# 해결

```sql
SELECT ANIMAL_ID, NAME, DATE_FORMAT(DATETIME, '%Y-%m-%d')
FROM ANIMAL_INS
ORDER BY ANIMAL_ID;
```



<br/>

<br/>



----



# 이것을 배웠다


## 👉 DATE_FORMAT
```sql
DATE_FORMAT(DATE, '날짜 형식')
```
- `DATE_FORMAT`은 `DATE`의 형식을 바꿀 수 있는 함수이다.

- 나는 `DATE`타입과 `DATETIME`의 차이점을 모르고 있었는데, 이 문제를 풀면서 학습했다.
`DATE`타입은 시간을 포함하지 않는 `'YYYY-MM-DD'` 형식이고
`DATETIME`타입은 시간을 포함하는 `'YYYY-MM-DD HH:MM:SS'` 형식이다.




<br/>

<br/>
<br/>

---


> rf
https://murra.tistory.com/157
https://devjhs.tistory.com/89
http://www.tcpschool.com/mysql/mysql_datatype_dateTime