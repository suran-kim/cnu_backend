





# 문제

```sql
동물 보호소에 들어온 동물 이름 중 두 번 이상 쓰인 이름과 
해당 이름이 쓰인 횟수를 조회하는 SQL문을 작성해주세요. 
이때 결과는 이름이 없는 동물은 집계에서 제외하며, 결과는 이름 순으로 조회해주세요.

```

<br/>

<br/>

---

# 해결

```sql
SELECT NAME, COUNT(1)
FROM ANIMAL_INS
WHERE NAME IS NOT NULL 
GROUP BY NAME
HAVING COUNT(1) >= 2
ORDER BY NAME;
```



<br/>

<br/>



----



# 이것을 배웠다


## 👉 HAVING절의 사용

- `HAVING`절은 `GROUP BY`절과 함께 사용이 된다.
`HAVING` 절은 `GROUP BY`에서 정의한 소그룹에 `집계함수`를 가지고 조건비교를 할 때 사용한다.

- `WHERE`절과 `HAVING` 절은 비슷해보이지만 에서는 `WHERE`절에서는 `집계함수`를 사용할 수 없다.




<br/>

<br/>
<br/>


> _**rf**_
[DATA ON-AIR: D가이드 - GROUP BY, HAVING 절] (https://dataonair.or.kr/db-tech-reference/d-guide/sql/?mod=document&uid=343)