





# 문제

```
아직 입양을 못 간 동물 중, 가장 오래 보호소에 있었던 동물 3마리의 
이름과 보호 시작일을 조회하는 SQL문을 작성해주세요. 
이때 결과는 보호 시작일 순으로 조회해야 합니다.

```

<br/>

<br/>

---

# 해결

```sql
SELECT I.NAME, I.DATETIME
FROM ANIMAL_INS I LEFT JOIN ANIMAL_OUTS O ON USING(ANIMAL_ID)
WHERE O.ANIMAL_ID IS NULL
ORDER BY 2
LIMIT 3;
```



<br/>

<br/>



----



# 이것을 배웠다



## 👉 헷갈렸던 것🤔
```sql
-- 오답 코드
SELECT I.NAME, I.DATETIME
FROM ANIMAL_INS I LEFT JOIN ANIMAL_OUTS O ON USING(ANIMAL_ID)
WHERE I.ANIMAL_ID IS NULL -- 이 부분이 틀렸다
ORDER BY 2
LIMIT 3;
```
처음에 내가 작성한 코드는 위와 같다. 

- `WHERE`절 조건을 `I.ANIMAL_ID IS NULL`로 주었다.
이러면 `FROM`절에 별칭이 `I`인 테이블을 `LEFT JOIN`해준 이유가 없어진다. 
`LEFT JOIN`을 한 이유는 별칭이 `O`인 테이블에 `ANIMAL_ID`가 존재하지 않는, 
`아직 입양을 못간 동물`을 결과로 가져오기 위해서였기 때문이다. 

<BR/>
<BR/>
<BR/>

---

