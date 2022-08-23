
Tibero 관리자 안내서의 7.4 데이터베이스 링크
Tibero SQL 참조 안내서 7.25절 CREATE DATABASE LINK
[TIBERO_DBLINK 구성가이드(Tibero to Oracle_UNIX)](https://technet.tmaxsoft.com/ko/front/technology/viewTechnology.do?cmProductCode=0301&paging.page=2&kss_seq=KSSQ-20170820-000001)
[Tibero DB Link (Tibero To Oracle) - Local 방식](https://technet.tmaxsoft.com/ko/front/technology/viewTechnology.do?cmProductCode=0301&paging.page=8&kss_seq=KSSQ-20140416-000046)
[Tibero DB Link (Tibero To Oracle) - Listener 방식](https://technet.tmaxsoft.com/ko/front/technology/viewTechnology.do?cmProductCode=0301&paging.page=9&kss_seq=KSSQ-20140416-000045)
[DB Link 연결 가이드 문서](https://technet.tmaxsoft.com/ko/front/technology/viewTechnology.do?cmProductCode=0301&paging.page=9&kss_seq=KSSQ-20140103-000008)
[Tibero DB Link (Tibero To Tibero)](https://technet.tmaxsoft.com/ko/front/technology/viewTechnology.do?cmProductCode=0301&paging.page=8&kss_seq=KSSQ-20140512-000009)

# 데이터베이스 링크
데이터베이스 링크는 원격 데이터베이스의 데이터에 로컬 데이터베이스의 데이터에 접근하는 것처럼 접근할 수 있는 방법을 제공한다. 


![DB링크](https://velog.velcdn.com/images/suran-kim/post/8ab2922c-fdc7-414e-ba87-63b1dc28f221/image.png)
이미지 출처 : http://haisins.epac.to/wordpress/?p=4246



어떤 회사에 서로 다른 데이터베이스가 존재하고, 각각의 DB의 테이블에는 데이터가 들어있다. 사용자(클라이언트)는 Oracle Server에 접속해서 쿼리를 실행한다. 만약 사용자가 MSSQL DB Server에 접근하고싶다면 그곳에 접속해서 쿼리를 실행해야 한다. 
 오라클 서버에 사원(사원이름, 부서번호) 테이블이 있고, MS Server에 부서(부서번호, 부서명) 테이블이 있다면 각 DB에 접속해서 가져온 정보를 조합해야할 것이다.

**데이터베이스 링크**를 사용하면 클라이언트는 하나의 DB Server에만 접속한다. DB끼리 연결되기 때문에 사용자는 하나의 DB에만 연결한 상태로도 정보를 한꺼번에 볼 수 있게 된다. 

Oracle 서버에게 질의를 던지면 오라클 서버가 MS서버로 쿼리를 전달하고, 결과 역시 오라클 서버를 거쳐 클라이언트에게 질의 결과가 전달된다.

두 DB의 사이에 존재하는 **GateWay**는 쿼리 변형 및 포맷 변경 기능을 수행한다.
GateWay실행파일은 로컬 DB의 바이너리 파일이 가지고 있다. 바이너리 파일은 여러개가 존재하는데, 그 중 원격 DB에 특화된 Gateway를 선택해서 사용한다. (Gateway를 설정하고 실행하는 작업은 별도로 진행된다.) 

GateWay와 원격 DB 사이에는 **클라이언트 라이브러리**가 들어간다. 그림의 예시에서는 MSSQL 클라이언트 라이브러리가 들어가게 된다. 게이트웨이는 1차 DB회사에서 제작한 것이기 때문에 2차 DB와 직접적으로 소통할 수 없다. 따라서 2차 DB회사에서 만들어서 배포하는 클라이언트 라이브러리가 필요하다. 게이트웨이와 2차 DB의 연결을 위해서는 **클라이언트 라이브러리**의 설정이 필요하기 때문에 서로 다른 벤더 사의 DB를 연결할 때는 Gateway의 설정이 조금 더 복잡해진다.

트랜잭션은 로컬 DB에서 하는 것이 기본적이다. 그런데 데이터베이스 링크를 사용하면 원격 DB와 로컬 DB의 질의를 하나의 트랜잭션으로 묶어 분산 트랜잭션을 처리할 수 있게 된다.(?)

대부분의 DBMS는 데이터베이스 링크 기능을 지원한다.


## 데이터 링크 장점
장점
- 분산된 데이터를 다루기 편리하다
실제 업무에서는 데이터베이스가 여러 개 존재하고 각 DB마다 업무에 대한 데이터가 저장되어 있다. 
- 데이터베이스에 한번만 접속해도 Link를 통해 다른 데이터베이스에 간편하게 접속할 수 있다.


단점
- 장애요소가 굉장히 많다. 여러 요소들이 복합되어 있기 때문에 문제 발생 시 원인을 투명하게 밝히기가 어렵다.
- 안정성 부분이 취약하기 때문에 데이터 처리의 안정성이 강조가 되는 경우에는 DB Link 사용을 지양한다.(ex: 은행)

_DB Link는 일반적으로 평상시 온라인 서비스를 위해 사용하지 않고 관리자가 **오프라인 배치작업 **성격의 작업을 할 때 사용한다. _


### 데이터베이스 링크 생성, 제거


#### _Public DBLink_
- 데이터베이스 링크를 생성한 사용자 외의 다른 사용자들도 데이터베이스 링크를 이용할 수 있다. 

- _권한_
public DBLink를 생성하기 위해서는 **create public database link 권한**이 있어야 한다. `tbdsn.tbr` 파일에 Using절 이후에 오는 연결할 데이터베이스에 대한 **연결 정보**가 저장되어 있어야 한다. <br/>
public DBLink를 제거하기 위해서는 **drop public database link 권한**이 있어야 한다.
```sql
-- 생성
create public database link DBLINK이름 using '연결할 데이터베이스 이름';

-- 삭제
drop public database link DBLINK이름;
```



로컬 DB는 원격DB의 `tbdsn.tbr`파일(리스닝 포트정보, DB이름, )을 보고 접속한다. DB세션이 만들어지고 
마치 로컬 DB가 원격 DB의 클라이언트인 것처럼 동작하는 것이다. 

원격 DB는 구동만 하고있으면 되고 따로 데이터베이스 링크 관련 설정을 할 필요가 없다. 모든 설정은 로컬 DB에서 진행한다.


#### _Private DBLink_
데이터베이스 링크를 **생성한 사용자**만 데이터베이스 링크를 사용할 수 있다. 삭제도 생성한 사용자만 할 수 있다.



### 원격 데이터베이스 연결
원격 DB의 연결에 사용하는 계정을 설정하는 데는 두 가지 방법이 있다.
- 지정한 계정
- 현재 연결된 계정

### 게이트웨이
로컬 DB는 질의를 게이트웨이에 전달한다. 게이트웨이는 원격 DB의 인터페이스 드라이버 라이브러리를 통해 질의의 포맷을 변형한다. 원격 DB에 접속하는 주체는 **게이트웨이**이다.

_**DBMS 벤더별 게이트웨이**_


_**게이트웨이 프로세스 생성 방식**_
게이트웨이는 연결할 DBMS가 제공하는 라이브러리가 필요하다. 
게이트웨이와 원격 DB 사이에 인터페이스 드라이버 라이브러리가 들어간다. 게이트웨이 프로세스는 해당 데이터베이스 링크를 사용하는 세션이 닫힐 때 종료된다. 여기서 세션이란 로컬 DB에 접속한 클라이언트의 세션을 의미한다. 하지만 설정에 따라 세션이 닫혀도 종료되지 않도록 할 수 있다?

로컬 DB의 `tbdsn.tbr파일`에 작성한다.
```sql
ora_dblink=(
            (GATEWAY=(PROGRAM=gw4orcl)  -- 오라클용 게이트웨이의 이름
                     (TARGET=orcl) -- 오라클 접속정보의 이름 (세션?)
                     (TX_MODE=GLOBAL))
)
```


_**게이트웨이 관련 디렉터리 구조**_
이런 디렉토리를 만들어줘야 한다.
tbgw.cfg도 vi로 작성해줘야 한다.

_**게이트웨이 설정(ORACLE, ...)**_

게이트웨이에 9999번으로 접속해서 쿼리를 전달할 수 있게 된다.

_**게이트웨이 설정(MSserver, ...)**_

## 데이터베이스 링크 사용
데이터베이스 링크를 통해 접근할 수 있는 **원격 DB객체**의 종류
- 테이블
- 뷰
- 시퀀스

단, 제약사항 존재!
사용자가 직접 접속하는 것이 아니라 로컬 DB를 거쳐서 접근하기 때문에 제약사향이 존재한다. 왜냐하면 게이트웨이는 다른 부분을 맞춰주는 중간 다리역할을 하는 것일 뿐이기 때문에 모든 부분을 

\

나머지 셀프 스터디

## 데이터페이스 링크 정보 조회
딕셔너리 뷰 소개


#### V$DBLIK
모니터링 가능


```sql
SQL> select * from employee@remote_tibero; -- 원격 DB에 존재하는 테이블
-- remote_tibero는 원격 DB에 접속할 수 있도록 해주는 DBLink?
```

티베로 SQL 참조 안내서 7.25절 참고

퍼블릭 

```sql
CREATE PUBLIC DATABASE LINK public_remote
  CONNECT TO tibero IDENTIFIED BY tmax USING 'remote_tibero'; -- 원격 DB에 접속하기 위한 유저명과 패스워드 / USING절 뒤에 로컬 DB의 접속정보 이름(tbdsn.tbr파일의 커넥트 아이덴티파이어)
```


## 데이터 링크 실습

### Tibero to Tibero(TtoT)
같은 DB 간의 데이터베이스 링크에서는 **GateWay가 생략**된다. 1차 DB와 2차 DB의 포맷을 맞추기 위한 것이 gateway이기 때문에 두 DB가 동일한 제품이라면 로컬 DB의 바이너리 파일에서 게이트웨이를 설치할 필요가 없다. 따라서 구성이 간단해진다. 

물론 같은 종류의 DB라 하더라도 원격 DB에 질의 쿼리를 실행하기 위해서는 데이터 링크를 사용할 때의 문법에 맞게 쿼리를 작성해야 한다.

1. _네트워크 설정_
`tbdsn.tbr`파일에 원격 DB에 접속하기 위한 정보를 추가로 작성해야 한다. 



2. _네트워크 접속확인_
```sql
Tibero Server#1 에서 Server#2로 접속 가능여부 TEST.
tbsql (server#2의 계정ID)/password@alias (tbdsn.tbr에서 설정한alias) 
예시> $tbsql tibero/tmax@Tibero2
```
- 로컬 DB인 티베로에 있는 tbSQL을 이용해 원격 DB에 접속해본다. 문제없이 연결된다면 둘 사이에 방화벽 등의 네트워크 상의 장애물이 없는 것이다. 이는 DBLink와 상관없이 가능한 단계로 접속 정보가 일치하고 문제가 없다는 것을 미리 확인해보는 것이다.  단순히 tbsql을 사용해서 접속정보가 맞는지 확인해본 것이기 때문에 로컬 DB와 무관하다(?)
<br/>

3. _DBLink 생성 및 사용_
 - 먼저 로컬 DB에 접속해서 DB Link를 생성해야 한다.
 - _DB Link 문법_
 create database link Link명 connect to userid identified by 'password' using 'tbdsn에 설정한alias'
 - DBLink 사용
   테이블, 뷰, 시퀀스 가능?
   select * from Tibero#2테이블명@DB_LINK명
SQL> select * from v$instance@tlink

- tbsql이 로컬 DB server에 질의를 전달한다. DB server가 질의를 파싱하는 과정에서 `@`를 발견하면 원격 DB에서 객체를 가져오기위한 실행계획을 만든다.

Target DB(원격DB)는 

![](https://velog.velcdn.com/images/suran-kim/post/d2fa59a4-a285-4ae2-991b-7fdd505b3053/image.png)
![](https://velog.velcdn.com/images/suran-kim/post/a748f2c6-a3da-46d0-ae7d-833e37785ea4/image.png)
![](https://velog.velcdn.com/images/suran-kim/post/7878b18c-f905-4593-b114-73d8aacbd329/image.png)
로컬 DB에는 아무 것도 없고 원격 DB에는 edu 스키마(EMP 테이블 존재)가 있다.

tibero 유저와 edu유저
tlink 객체를 통해 Tibero 인스턴스에 연결된다?
어떤 걸 어떻게 만드는지? 기본적인 위치 구분은 정확히 할 수 있어야 한다.

<BR/><BR/><BR/>

_원격 DB의 테이블을 로컬 DB에 복사하기_
![](https://velog.velcdn.com/images/suran-kim/post/c9c00d61-9a9e-4f66-9b6c-c27773521c19/image.png)

1. 한 번에 작업하기 
- 테이블의 구조를 가져와서 CREAT해야 한다.
- AS
SELECT 대상 테이블로부터 구조정보를 가져와서 동일한 테이블을 만들어준다. 
대상 테이블은 기본으로 로컬 DB가 설정되어있으나 원격 DB로 설정하는 것이 가능하다.
CREATE TABLE `이름` AS `SELECT * FROM 원본 테이블`;
- 데이터와 객체(테이블)을 따로 가져오려면 WHERE절로 조건을 준다. DATA를 가져올 때는 INSERT SELECT쿼리를 사용해서 데이터를 가져온다. 
<BR/><BR/><BR/>

![](https://velog.velcdn.com/images/suran-kim/post/a372a33c-1703-4c4a-81a9-7276be6b6b22/image.png)

2. 둘로 나누어서 작업하기 

```sql
-- 테이블(오브젝트)만 가져오기
CREATE TABLE DEPT AS 
SELECT * 
FROM DEPT@TLINK 
WHERE ROWNUM < 1;
-- ROWNUM이 1 미만인 경우는 존재하지 않는다.
```
```sql
-- 데이터 가져오기
INSERT INTO DEPT
SELECT *
FROM DEPT@TLINK;

-- 커밋
```

- 왜 굳이 둘로 나누어서 작업을 할까? 데이터의 양이 많은 경우 복사에 매우 오랜 시간이 걸리기 때문이다. 테이블만 생성하는 작업은 데이터를 가져오는 것보다 훨씬 더 빠르게 완료되기 때문에 테이블을 미리 만들어두고 그 다음에 데이터를 가져오는 것이다. 데이터를 가져올 때도 보통 한꺼번에 모든 데이터를 가져오지 않고 **WHERE절**로 조건을 주고 데이터를 나누어서 가져온다. 

<BR/>

### Tibero to Oracle(TtoO)
오라클 인스턴트 파일 다운로드 필요


_**게이트 웨이 파일**_ 
- 바이너리 파일의 위치는 버전에 따라 다르다
- Oracle 클라이언트 라이브러리는 **인터페이스 드라이버이**다. 게이트웨이가 이를 사용해서 Oracle DB에 접속한다. 티베로에 포함되어 있지 않기 때문에 개별 설치 필요
  - 로컬방식 : DB Link를 사용할 경우 Gateway프로세스가 가동하는 방식
    로컬 방식을 사용한다면 동일 서버에 존재해야한다.
  - 리스너 방식 : 게이트 웨이를 미리 기동해서 리스닝하는 방식
    리스너 방식을 사용한다면 어디에 있어도 상관 없다.

<순서에 대해 정확히 알고 있어야 한다.>

1. profile 설정
   - 오라클 클라이언트가 사용하는 환경변수 세팅
```
export TBGW_HOME=$TB_HOME/client/gateway
export ORACLE_HOME=<Oracle Home>
export LD_LIBRARY_PATH=$ORACLE_HOME/lib:$LD_LIBRARY_PATH
export ORACLE_SID=<Oracle SID>  
export PATH=$ORACLE_HOME/bin:$PATH
```
2. 오라클 라이브러리 관련 권한 변경
  - 서로 다른 사용자에 대한 권한 설정
3. 게이트웨이 바이너리 복사
  - 환경변수가 가리키는 디렉토리를 만들어야 한다.
4. Network Alias [공통]
   - 게이트 웨이에 대한 정보
   

   - 티베로 인스턴스가 게이트웨이에 접속하기 위해 사용한다.


5. 게이트웨이 환경설정
```sql
gw_local=(
(GATEWAY=
(PROGRAM=/home/tibero/gateway/gw4orcl) -- 게이트웨이의 위치
(TARGET=orcl)
(TX_MODE=GLOBAL)
  )
)
```
6. DB Link 생성 및 확인 [공통]
- nestat으로 리스닝 확인?

클라이언트 설치 시에 세팅해야하는 파일 편집해야 하는 파일들이 다음과 같다
게이트웨이 시작 전 ldd
게이트웨이 시작하는 부분
오라클 11g instant클라이언트

<br/>


1. 게이트웨이와 Oracle DB의 연결
- 오라클 클라이언트 설치
- 게이트웨이 세팅해서 올리기
  - `gw4orcl` 파일 복사해서 디렉토리 세팅
    
2. Tibero DB
- 티베로에 접속해서 DB Link 객체 생성
- 


