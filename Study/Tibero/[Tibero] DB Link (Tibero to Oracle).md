Oracle Instant Client Downloads for Linux x86-64 (64-bit)
file://10.188.191.33/tibero%EA%B5%90%EC%9C%A1%EC%9E%90%EB%A3%8C/0823/

오라클 쪽은 신경쓸 필요가 없다. 이미 구동 중이고 리스너가 떠있다. 언제든지 접속해서 쿼리를 실행할 수 있다.
왼쪽 로컬 티베로만 세팅하면 된다.![](https://velog.velcdn.com/images/suran-kim/post/b228a9d0-238d-4648-ba80-c1208810f6f4/image.png)

모든 요청은 sql을 통해서만 진행된다. Client는 `tbsql`
sql이 인스턴스에게 전달 -> 게이트 웨이에게 전달 -> 오라클 클라이언트에게 전달

세 가지 설정이 되어있어야 원활한 연결 가능

1. Tibero Instance
1번에서 2번이 원활하게 진행되기 위해서는 tbdsn.tbr이 설정되어있어야한다. 인스턴스가 이 정보를 보고 게이트 웨이를 식별해서 쪽으로 전달한다.
게이트웨이와 동일한 포트로 접속을 시도
DSN : (yanghoon) -  DBLink 생성 시 USING절에 들어가는 이름과 일치해야한다. 그럼 조회 시 HOST 항목에 뜨게 된다. (게이트웨이에 접속하기 위한 접속정보) 
![](https://velog.velcdn.com/images/suran-kim/post/91e12744-1124-444c-9484-a8d49eb7dbf9/image.png)


2. TBGW
tbgw.cfg는 게이트웨이의 구동과 관련. 게이트웨이가 시작할 때 cfg파일의 내용을 보고 구동된다. 게이트웨이가 9998을 리스닝하도록 하라 -> 게이트웨이는 9998을 리스닝한다. 

3. 오라클 인스턴트 클라이언트
tnsnames.ora
오라클 클라이언트 접속 정보 -> tbdsn.tbr 파일에서 사용된다. 서로 정보가 일치해야함?
클라이언트가 오라클을 식별하기 위해 사용된다. 티베로에서 사용하는 게 아니라 oracle인스턴트 클라이언트가 사용한다 (다운받은 압축파일)
HOST 의 ip가 오라클의 IP임
디렉토리의 파일을 설정해야??
인스턴트 클라이언트가 서버에 접속할 수 있다.


빨간 표시는 중요하기 때문에 표시된 거임


_**시작시작**_
게이트웨이 기동에 필요한 디렉토리 생성
```
mkdir /tibero/tbgateway/
mkdir /tibero/tbgateway/oracle
mkdir /tibero/tbgateway/oracle/config
mkdir /tibero/tbgateway/oracle/log
vi /home/tibero5/tbgateway/oracle/config/tbgw.cfg
```

게이트웨이 파일
gw4orcl: 오라클용 티베로 게이트웨이. 이것이 오라클 클라이언트 인스턴트를 사용해서 오라클과 ..뭘 함 그 과정에서 오라클의 환경변수가 필요하기 때문에 ~/.bash_profile에 내용 추가
 bash_profile 누가 쓰냐? 
   게이트웨이
   오라클 클라이언트
   
   ![](https://velog.velcdn.com/images/suran-kim/post/555100dd-8006-4964-9f37-e000ddee2477/image.png)
   
   
tnsnames.ora
게이트 웨이가 오라클에 접속하기 위한 접속 정보


   
   .so파일을 이용해서 동작하는데, 리눅스의 특정 경로에 들어있기도 하고
   ldd 명령어 : 리눅스 명령어. 점검해보는 명령어
   
   
   
   오라클 홈 디렉토리 수정...
   
   
   리눅스의 프로세스 아이디 pid
   
   ![](https://velog.velcdn.com/images/suran-kim/post/6448d6ca-ca0a-44d2-9d0e-e5b94821f5ec/image.png)
![](https://velog.velcdn.com/images/suran-kim/post/d7aa8c47-6d78-4b75-9b28-134598caaaed/image.png)
![](https://velog.velcdn.com/images/suran-kim/post/d18c22aa-7415-4f82-a4bd-94d34b841776/image.png)


**TIBERO tbsql테스트**의 의미 (2번 - 3번 까지 구간에 대한 테스트)
TBGW와 OracleClient의 
게이트웨이와 오라클에 문제가 없다는 것을 의미 
여기가 문제없이 진행되면 이제 남은 세팅은 1번과 2번 사이 밖에 없다?
데이터베이스 생성 전에 진행하는 테스트

게이트웨이 접속정보 조회
```

MOF=(
   (GATEWAY=(LISTENER=(HOST=localhost)(PORT=9998))
            (TARGET=ORCL)
            (TX_MODE=GLOBAL)
   )
)
[tibero@T1:/tibero/tbgateway]$ tbsql edu/edu@MOF
```

게이트웨이는 오라클과 접속하기 때문에 결국은 오라클과 접속하게 된다.
quit는 게이트웨이와 sql의 접속 끊기




< tbsql <--> gateway <--> oracle 구간 테스트 >

================================================
### tbsql <--> gateway(X) <--> oracle(X) 일때 
```sql
[tibero@T1:/tibero/tbgateway]$ tbsql edu/edu@MOF

tbSQL 7

TmaxTibero Corporation Copyright (c) 2020-. All rights reserved.

TBR-2131: Generic I/O error.
```
================================================
### tbsql <--> gateway(O) <--> oracle(X) 일때 
```sql
[tibero@T1:/tibero/tbgateway]$ tbsql edu/edu@MOF

tbSQL 7

TmaxTibero Corporation Copyright (c) 2020-. All rights reserved.

TBR-130092:(ORA-12541) TNS:no listener
```
================================================
### tbsql <--> gateway(O) <--> oracle(O) 일때 
```sql
[tibero@T1:/tibero/tbgateway]$ tbsql edu/edu@MOF

tbSQL 7

TmaxTibero Corporation Copyright (c) 2020-. All rights reserved.

Connected to ORACLE GATEWAY using MOF.
```
이제 테스트가 종료됐다. Exit
주의사항 : dblink생성은 로컬에서 진행한다. 접속만 확인하는 거지 여기서 뭘 하려고 하면 안된다.


##
로컬 티베로에 접속한다.
sys유저로 접속해서 connect, resource 권한을 부여해주고
dblink 생성 권한도 부여

테이블 조회 `LS TABLE`




이후에 한 건 이건 리스너 실습

티베로 입장에서는 같은 곳에 떠있는 것...?
```sql
ORCL = ##
	(DESCRIPTION =
			(ADDRESS = (PROTOCOL = TCP)(HOST = 192.168.41.143)(PORT = 1521))
			(CONNECT_DATA =
			(SERVER = DEDICATED)
			(SERVICE_NAME = orcl)
			)
	)
    
    
-- ORCL = DBLINK 명(ex : select * from dual@oralink)
## HOST = Oracle 서버의 IP
## PORT = Oracle 서버의 Port
## SERVICE_NAME = 오라클 서버의 Service 명

```
```sql
MOF = (
 	  (GATEWAY =
			(LISTENER = (HOST=192.168.41.142)(PORT=9998)) 
 	  (TARGET=ORCL)
 	  (TX_MODE=GLOBAL)
 	)
)


-- MOF = Oralce 서버로 접근되는 Alias 명
## HOST = TIBERO 서버의 IP, gw4orcl 이 기동되어야 하는 서버의 IP
## PORT = tbgw.cfg에 명시된 Port를 통해서 tnsnames.ora에 전달됨
## TARGET = DBLINK 이름, tnsnames.ora와 연결됨
```



> TIP
- netstat -tlpn 네트워크상태 확인

\\10.188.191.33\tibero교육자료