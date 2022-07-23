
# 문제

```
보호소에서는 몇 시에 입양이 가장 활발하게 일어나는지 알아보려 합니다. 
09:00부터 19:59까지, 각 시간대별로 입양이 몇 건이나 발생했는지 조회하는 SQL문을 작성해주세요. 
이때 결과는 시간대 순으로 정렬해야 합니다.

```

<br/>

<br/>

---

# 해결

```sql
SELECT HOUR(DATETIME) 'HOUR', COUNT(1)
FROM ANIMAL_OUTS
GROUP BY HOUR
HAVING HOUR BETWEEN 9 AND 20
ORDER BY 1;
```



<br/>

<br/>



----



# 이것을 배웠다


## 👉 BETWEEN
```sql
BETWEEN 조건 A AND 조건 B
```
- `BETWEEN`은 WHERE절에서 `범위`를 정하고 싶을 때 사용한다.
(이 문제에서는 HAVING절에 사용했다.)
`조건 A`와 `조건 B` 사이의 값을 조회한다.


<br/>

<br/>
<br/>

## 👉 헷갈렸던 것🤔
```sql
-- 오답 코드
SELECT HOUR(DATETIME), COUNT(1)
FROM ANIMAL_OUTS
GROUP BY HOUR
HAVING HOUR BETWEEN 9 AND 20
ORDER BY 1;
```
처음에 내가 작성한 코드는 위와 같다. 

-  `GROUP BY` 사용 시 SELECT문에 있는 모든 컬럼은 `집계함수`가 되거나 `GRUOP BY`절에 나타나야 한다. 만약 `GROUP BY` 절을 사용하면서 SELECT문에 `집계함수`를 사용하지 않거나 `GROUP BY절에 언급되지 않은 컬럼`이 존재하면 오류가 발생하게 된다. 그래서 SELECT절의 `HOUR(DATETIME)`에 `'HOUR'`라는 별명을 붙여줘야 쿼리가 제대로 실행된다.


<BR/>
<BR/>
<BR/>

---



> _**rf**_
[[HOW]SQL에서 BETWEEN A AND B 조건식 사용하는 방법.  오라클(ORACLE)
] (https://tiboy.tistory.com/565)