# 문제

```
입양을 간 동물 중, 보호 기간이 가장 길었던 동물 두 마리의 
아이디와 이름을 조회하는 SQL문을 작성해주세요. 
이때 결과는 보호 기간이 긴 순으로 조회해야 합니다.

```

<br/>

<br/>

---

# 해결

```sql
SELECT I.ANIMAL_ID, I.NAME
FROM ANIMAL_OUTS O JOIN ANIMAL_INS I USING(ANIMAL_ID)
ORDER BY DATEDIFF(I.DATETIME, O.DATETIME)
LIMIT 2;
```



<br/>

<br/>



----



# 이것을 배웠다


## 👉 DATEDIFF
```sql
DATEDIFF(구분자, 날짜1, 날짜2)
```
- `DATEDIFF`는 날짜 간 차이를 구할 때 사용하는 함수이다.
`구분자`에 어떤 키워드를 넣느냐에 따라 `년도`, `분기`, `일`, `주`의 차이를 구할 수도 있다.



<br/>

<br/>
<br/>

---


> rf
https://mirwebma.tistory.com/178