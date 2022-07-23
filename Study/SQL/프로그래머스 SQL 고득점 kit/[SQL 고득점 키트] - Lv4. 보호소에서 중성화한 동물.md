
# 문제

```
보호소에서 중성화 수술을 거친 동물 정보를 알아보려 합니다. 
보호소에 들어올 당시에는 중성화되지 않았지만, 보호소를 나갈 당시에는 중성화된 동물의 
아이디와 생물 종, 이름을 조회하는 아이디 순으로 조회하는 SQL 문을 작성해주세요.

```

<br/>

<br/>

---

# 해결

```sql
SELECT O.ANIMAL_ID, O.ANIMAL_TYPE, O.NAME
FROM ANIMAL_INS I  LEFT JOIN ANIMAL_OUTS O USING(ANIMAL_ID)
WHERE I.SEX_UPON_INTAKE LIKE 'Intact%' AND O.SEX_UPON_OUTCOME NOT LIKE 'Intact%'
ORDER BY 1;
```



<br/>

<br/>



----



# 이것을 배웠다


## 👉 Like

```sql
SELECT 테이블 이름 LIKE '매칭조건' 
```
- `LIKE`는 `SELECT`절에서 특정 문자를 검색할 때 사용할 수 있는 연산이다.

- 매칭 조건에 `%`, `_`와 같은 `와일드카드`를 사용할 수 있다.
`%`는 0개 이상의 문자를 대신 표현할 수 있고
`_`는 1개의 문자를 대신한다.




<BR/>
<BR/>
<BR/>

---

> _**rf**_
[[MYSQL] LIKE 함수 - 특정 문자 검색하기] (https://bramhyun.tistory.com/16)