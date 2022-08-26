참고하면 좋은 것

SQL참조 안내서 7.2. ALTER DATABASE
SQL참조 안내서 7.69. FLASHBACK TABLE


# 백업
- 여러 유형의 장애로부터 데이터베이스를 보호한다.
  - MTBT를 증가시키고 MTBR를 감소

- 시스템 장애 발생 시 **복원**을 하거나 시스템 **작동을 유지**시키기 위한 절차 또는 기법
- 데이터베이스를 복사해서 가지고 있다가 손상 발생 시 복원에 활용할 수 있게 하는 것.
관리자는 시스템 장애 시 발생할 손실을 최소화하고 복구 가능한 상태로 DB를 운용해야 한다.
   - 최소 한 달에 한 번 **DB 전체 백업**
   - 하루에 한 번씩 **Export 백업** 권장
   - (+)복사 중에는 성능이 떨어지거나 제약이 발생하기 때문에 너무 자주 받을 수는 없다

- 데이터베이스 관리자는 백업에 대한 정책을 수립하고 꼭 필요한 데이터를 최소한의 양으로 백업한다.
   - 백업은 DBA의 주요 역할 중 가장 주의를 기울여야 하는 작업이다.
 
<BR/> 

- 백업솔루션 
EX) 베리타스 넷백업 - 어플라이언스(소프트웨어 + 하드웨어) 
   -  설정한 시간에 지정한 데이터베이스에 대해 백업
   -  백업을 위해서는 DB의 데이터를 **복사하기 전**과 **후**에 DB쪽에 명령(SQL)을 실행해야 한다. 정해진 **절차**를 따라 복사한 데이터만 복구 시에 사용할 수 있다.
   (명령 없이 복사한 데이터는 복구 시에 해당 데이터를 이용할 수 없다)
   - 백업 소프트웨어는 최초 세팅만 해놓으면 백업 작업을 자동화할 수 있다.
     - 백업 절차와 SQL문에 대한 쉘 스크립트를 작성해서 백업 소프트웨어에 저장한다.
     - **백업 스크립트**를 작성할 수 있어야 한다.

<BR/> 

## 💻 여러가지 유형의 장애
- 사용자로 인한 장애 및 해결
  -  실수로 데이터를 삭제한 경우 : 백업한 데이터베이스 필요

## 💻 백업과 리커버리 관련 티베로 동작 방식
### 📼 **Transaction Durability**
  - 메모리에만 존재하는 상태가 아니라 **파일에 Write**된 상태. 
데이터 변경 이후  반드시 commit해야 영속성을 확보할 수 있다. 
   - 변경 이력인 **redo log**의 영속성을 보장하는 것이다. 이력정보를 메모리 -> 파일로 쓰는 작업으로 영속성을 보장한다.(그래서 데이터 블럭의 영속성을 보장한다는 뜻이 아니다.)
    - commit한 데이터의 경우
     - 파일이 손상되지 않고 전원이 꺼졌다 (메모리 날아감) -> Recoverable 보장
     - 데이터 파일이 손상됐다. -> x
   
### 📼 **logging**
   - redo로그 정말 중요하다.
   - redo로그 **저장 대상 범위**
     - 피지컬 로깅
       - 수정이 일어날 때마다 해당 block을 통째로 남긴다. 
     - 로지컬 로깅
       - 업데이트 및 delete 변경이력을 log에 남기는 방법
       - 그럴 때는 통째로 redolog에 저장 기본적으로는 로지컬 로깅이지만 어쩔 때 피지컬 로깅으로 통째로 남느냐? -> 온라인 백업 시. **백업 플래그**가 켜져 있을 때는 그 값을 보고 통째로 리두 로그 쪽에 저장하게 되어있다.
     - Physiological Logging
       - 위 둘의 장점을 합한 형태
     

     
### 📼 **Database(controlfile, redolog file, datafile) 동기화 방식**
 - TSN 
      - 백업 발생 시 0부터 시작해서 지속적으로 숫자 증가
      - 데이터베이스의 버전, 커밋 버전
      - 트랜잭션이 **commit**(변경 확정)될 때 TSN이 생성된다.
      - TSN은 모든 파일안에 값이 세팅되어 있다. 그 값을 서로 비교해서 동기화를 한다. 백업 복구 시에도 중요한 역할을 한다.
      
  - 체크포인트
      - 메모리에 있는 **모든 변경된 블럭**을 **디스크**에 쓰는 작업이다. 
      - 모든 데이터 접근 작업은 메모리에서 처리하게 되어있다. 
      블럭이 메모리에 올라옴 -> DML작업으로 변경된 블럭(dirty블럭) -> 디스크에 있는 데이터 파일에 반영되어야 하는데 이 반영 작업은 동기적으로 즉시 실행되는 것이 아니다. 즉, **sql에 대해 비동기적**이다. (sql문이 실행되어도 즉시 디스크에 반영되지 않는다.) 디스크에 올리는 작업은 **체크포인트** 시점에 실행된다. 변경된 블럭을 메모리에 가지고 있다가 나중에 반영하는 이유는 매번 동기화 -> 빈번한 디스크 접근 작업 -> 서버의 속도가 느려지기 때문이다. 따라서 디스크에 쓰는 작업을 **모아서 한 번에** 쓰도록 한다. 
      - 체크포인트는 티베로에서 자동 진행된다. 그러나 수동으로 즉시 진행할 수도 있다.
      - 체크포인트 발생상황(?)
        - 기본적으로 commit한 대상에 대해서는 영속성을 보장하기 때문에 안전하다. 
        
        
## 💻 백업 종류와 전략 

![](https://velog.velcdn.com/images/suran-kim/post/1d6ddcc0-6636-42cd-a5eb-67def6181ff7/image.png)

### 📼 논리적 / 물리적 백업
- 논리적인 백업
  - tbExport
    - 논리적인 저장 단위로 백업을 받을 수 있다. (특정 테이블 하나를 추출할 수도 있다.) 
    - .dat파일은 마이그레이션 용도로 사용할 수도 있음
- 물리적인 백업
  - file copy 
    - 절차를 따라서 데이터를 저장한다.
 
 
![](https://velog.velcdn.com/images/suran-kim/post/0c03d356-9c69-42f9-878c-c307727204f0/image.png)
굵은 선을 따라서 백업을 해야함 
 
### 📼 Offline backup(NOARCHIVELOG)
- **NOARCHIVELOG** 모드에서의 백업
  - (Offline Backup / Cold Backup)
  - **redo log**를 아카이브 파일로 보관하지 않는 방식

- NOARCHIVELOG(오프라인 백업)모드에서는  redolog를 저장하지 않는다. 그 상태에서 백업을 받는다면 closed(파일을 open하는 주체는 티베로 인스턴스(TSM, 로그 버퍼...). 그것을 close한다-> tbdown) -> ?? 
- consistent : 정상적인 shutdown 후의 백업
DB가 닫힌 상태가 됐을 때 redo log, data file, control 파일 간에는 일관성이 있어야 한다. **control 파일**에 명시된 파일 정보와 **실제 파일** 간의 정보가 일치해야 하는 것이다. (각 파일의 TSN(티베로 시스템 넘버)가 일치하는 상태) 
- 티베로 인스턴스를 **정상 종료**할 때 일관성있는 상태가 된다. 
(immiediate옵션으로 tbdown할 때) 



### 📼 Online backup(ARCHIVELOG)
- **ARCHIVELOG** 모드에서의 백업
  - (Online Backup / Hot Backup)  
  - redo log를 아카이브 파일로 보관하는 방식
  

  - 서버를 닫지 않은 open 상태에서 **DB파일 복사** 가능 - > 복구 시 사용 가능
`abnormal tbdown`과 같은 **비정상 종료 시에도 복구 가능**. 
inconsistent 상태에서도 일관성 있게 복구 가능


- 보통은 아카이브 모드를 사용한다.

- 노아카이브 모드는 백업하기 위해 서버를 다운시켜야한다. -> 이용자가 이용할 수 없음
아카이브 모드 -> 24시간 운영. onpe, inconsistent상태에서 백업


### 📼 ARCHIVELOG MODE 변경
_**온라인 백업**을 위해서는 ARCHIVELOG MODE를 변경해야 한다._

 
 - < 아카이브 로그 모드 변경 작업 순서 >
  1. `Tibero 종료`
```sql
	$ tbdown
```
  2. 파라미터 파일에 LOG_ARCHIVE_DEST 설정
```sql
    $ vi $TB_HOME/config/$TB_SID.tip

    $ cat $TB_HOME/config/$TB_SID.tip
    ---------파라미터 파일 내용----------
    DB_NAME=tibero
    LISTENER_PORT=8629
    CONTROL_FILES="/tibero/tbdata/tibero/c1.ctl","/tibero/tbdata/tibero/c2.ctl"
    DB_CREATE_FILE_DEST=/tibero/tbdata/tibero
    LOG_ARCHIVE_DEST=/tibero/tbdata/tibero/arch  -- LOG_ARCHIVE_DEST
    MAX_SESSION_COUNT=20
    TOTAL_SHM_SIZE=1G
    MEMORY_TARGET=2G
```


  3. `Tibero mount 모드` 기동
```sql
	$ tbboot mount
```

  4. 아카이브로그 모드 변경 쿼리 수행
```sql
	$ tbsql sys/tibero
	SQL> ALTER DATABASE ARCHIVELOG;
```
  
  5. `Tibero normal 모드` 기동
```sql
	$ tbdown
    $ tbboot
```

6. 아카이브 모드 확인
   - 티베로는 온라인 리두로그와 **내용**이 똑같은 아카이브 파일을 자동으로 만들어준다. **파일 자체가 동일한 것은 아니다**. 만약 온라인 리두로그 파일의 크기가 100mb이고, 내용만의 크기는 50mb라면 아카이브 파일은 50mb가 된다. 
```sql
    $ tbsql sys/tibero
    SQL> SELECT LOG_MODE FROM V$DATABASE;

    LOG_MODE
    ------------
    ARCHIVELOG
```

7. 아카이브 로그 생성하여 파일 확인
     -  `alter system switch logfile`
        - **수동 로그 스위치** 시키는 명령어
        -  가득 차기 전에 수동으로 로그 스위치 -> 자동 스위치된 로그 파일보다 크기가 작음
        - 아카이브 파일에 대한 정보는 **컨트롤 파일**에서 읽어오는 것이다. 
```sql
    SQL> ARCHIVE LOG LIST;

    NAME                            VALUE
    ------------------------------- ------------------------------------------------
    Database log mode               Archive Mode
    Archive destination             /tibero/tbdata/tibero/arch/
    Oldest online log sequence      4
    Next log sequence to archive    6
    Current log sequence            6


    SQL> ALTER SYSTEM SWITCH LOGFILE;  -- 로그 스위치

    System altered.

    SQL> ARCHIVE LOG LIST;   

    NAME                            VALUE
    ------------------------------- ------------------------------------------------
    Database log mode               Archive Mode
    Archive destination             /tibero/tbdata/tibero/arch/
    Oldest online log sequence      5
    Next log sequence to archive    7
    Current log sequence            7


    SQL> COL NAME FOR A60
    SQL> SELECT SEQUENCE#, NAME FROM V$ARCHIVED_LOG;  -- 아카이브 로그 조회

     SEQUENCE# NAME
    ---------- ------------------------------------------------------------
             5 /tibero/tbdata/tibero/arch/log-t0-r0-s5.arc
             6 /tibero/tbdata/tibero/arch/log-t0-r0-s6.arc
			-- t0 : 스레드 번호
            -- r0 : 리두로그 리셋 시 증가함
            -- s6 : 시퀀스 번호
    
    
    
    SQL> col name for a60
    SQL> select sequence#, name from v$archive_dest_files; -- 아카이브 로그 조회2

    SEQUENCE# NAME
    ---------- ------------------------------------------------------------
             6 /tibero/tbdata/tibero/arch/log-t0-r0-s5.arc
             7 /tibero/tbdata/tibero/arch/log-t0-r0-s6.arc            
```

<br/>


_**아카이브 모드 확인**_
![](https://velog.velcdn.com/images/suran-kim/post/a2c6dd3e-d89d-48ee-b71e-a55ba23d0e1f/image.png)
![](https://velog.velcdn.com/images/suran-kim/post/e1115af8-6bf8-404b-a9b3-3409ba857d6a/image.png)


<br/>


설명 안한 부분도 다 읽어봐야 함
<br/><br/>

## 💻 Backup 수행 방법

### 📼 _** 티베로 종료 후 백업 (Offline Backup)**_

- `MOUNT모드` 또는 `OPEN 모드`에서 `v$datafile`, `v$logfile` 뷰를 통해서 백업할 파일 정보 조회. 
```sql

    SQL> select name from v$datafile;

    NAME
    -----------------------------------------------
    /tibero/tbdata/tibero/system001.dtf
    /tibero/tbdata/tibero/undo001.dtf
    /tibero/tbdata/tibero/usr001.dtf
    /tibero/tbdata/tibero/syssub001.dtf
    /tibero/tbdata/tibero/USERS01.dtf



    SQL> select GROUP#, member from v$logfile;

        GROUP# MEMBER
    ---------- -----------------------------------
             0 /tibero/tbdata/tibero/log01.log
             1 /tibero/tbdata/tibero/log11.log
             2 /tibero/tbdata/tibero/log21.log



    SQL> SELECT NAME FROM V$CONTROLFILE;

    NAME
    ----------------------------------------------------
    /tibero/tbdata/tibero/c1.ctl
    /tibero/tbdata/tibero/c2.ctl
         

```

- 티베로 인스턴스 종료
  - `tbdown [immediate]` -> 반드시 **티베로 정상종료**

- Copy 명령을 이용해 datafile, logfile, controlfile, tip file등을 백업한다.
`ARCHIVELOG 모드`에서는 archive 파일도 백업한다.

```sql
      $ cp /tibero/tbdata/tibero/*.dtf          /tibero/s/off_backup
      $ cp /tibero/tbdata/tibero/*.log          /tibero/s/off_backup
      $ cp /tibero/tbdata/tibero/*.ctl          /tibero/s/off_backup
      $ cp /tibero/tbdata/tibero/arch/*.arc     /tibero/s/off_backup
      $ cp /tibero/tibero7/config/tibero.tip    /tibero/s/off_backup
      $ cp /tibero/tbdata/tibero/.passwd        /tibero/s/off_backup
```

<br/>

_**오프라인 백업데이터 이용해서 데이터베이스 복원**_

1. 티베로 인스턴스 종료 
    - `tbdown abort` 가능
    - normal종료 아니어도 상관없다. 지금 사용하는 데이터는 깨져도 상관없음
```sql
        tbdown abort
```


2. 기존 데이터베이스 **삭제**
```sql
        $ rm /tibero/tbdata/tibero/*.dtf      
        $ rm /tibero/tbdata/tibero/*.log      
        $ rm /tibero/tbdata/tibero/*.ctl      
        $ rm /tibero/tbdata/tibero/arch/*.arc 
        $ rm /tibero/tibero7/config/tibero.tip
        $ rm /tibero/tbdata/tibero/.passwd    
```

3. 백업 데이터베이스 리스토어
    - 백업 시와 반대방향이 되도록 문장 작성
```sql
        cp /tibero/s/off_backup/*.dtf       /tibero/tbdata/tibero         
        cp /tibero/s/off_backup/*.log       /tibero/tbdata/tibero          
        cp /tibero/s/off_backup/*.ctl       /tibero/tbdata/tibero         
        cp /tibero/s/off_backup/*.arc       /tibero/tbdata/tibero/arch     
        cp /tibero/s/off_backup/tibero.tip  /tibero/tibero7/config   
        cp /tibero/s/off_backup/.passwd     /tibero/tbdata/tibero        

```
  

4. 티베로 인스턴스 시작 
```sql
        tbboot 
```
<br/>

### 📼 **_쉘 파일 만들기_**

 - 오프라인에서 데이터를 **백업**하는 쉘 파일 생성
 
```sql
    $ vi off_backup.sh

    -------- 오프라인에서 데이터를 백업하는 쉘 파일 --------
    #!/bin/sh

    echo "TIBERO OFFLINE BACKUP DATABASE : /tibero/s/off_backup2"

    ### TIBERO INSTACE SHUTDOWN
    tbdown immediate

    ### COPY DATABASE
    cp /tibero/tbdata/tibero/*.dtf          /tibero/s/off_backup2
    cp /tibero/tbdata/tibero/*.log          /tibero/s/off_backup2
    cp /tibero/tbdata/tibero/*.ctl          /tibero/s/off_backup2
    cp /tibero/tbdata/tibero/arch/*.arc     /tibero/s/off_backup2
    cp /tibero/tibero7/config/tibero.tip    /tibero/s/off_backup2
    cp /tibero/tbdata/tibero/.passwd        /tibero/s/off_backup2

    ### TIBERO INSTANCE START
    tbboot

    --------------------------------------------------

    $ chmod u+x off_backup.sh  
    $ ./off_backup.sh          -- 백업 실행

 ```
 
 <br/>
 

-  오프라인에서 백업데이터를 이용해서 DB를 **회복**시키는 쉘 파일 생성

```sql
    $ vi recover_using_offback.sh       

    ---- 백업 데이터를 이용해서 db를 원복시키는 쉘 파일 ----

    #!/bin/sh
    echo "TIBERO DATABASE RECOVER USING OFFLINE BACKUP DATABASE !"

    ### TIBERO INSTACE SHUTDOWN
    tbdown abort

    ### DELECTE DATABASE
    rm /tibero/tbdata/tibero/*.dtf
    rm /tibero/tbdata/tibero/*.log
    rm /tibero/tbdata/tibero/*.ctl
    rm /tibero/tbdata/tibero/arch/*.arc
    rm /tibero/tibero7/config/tibero.tip
    rm /tibero/tbdata/tibero/.passwd

    ### RESTORE DATABASE
    cp /tibero/s/off_backup/*.dtf       /tibero/tbdata/tibero
    cp /tibero/s/off_backup/*.log       /tibero/tbdata/tibero
    cp /tibero/s/off_backup/*.ctl       /tibero/tbdata/tibero
    cp /tibero/s/off_backup/*.arc       /tibero/tbdata/tibero/arch
    cp /tibero/s/off_backup/tibero.tip  /tibero/tibero7/config
    cp /tibero/s/off_backup/.passwd     /tibero/tbdata/tibero

    ### TIBERO INSTANCE START
    $ tbboot

    ---------------------------------------------------

    $ ls -al off_backup.sh
    -rw-r--r-- 1 tibero dba 818 Aug 25 11:39 off_backup.sh

    $ chmod u+x off_backup.sh  -- 파일 모드를 변경하여 실행 가능하도록 설정

    $ ls -al off_backup.sh
    -rwxr--r-- 1 tibero dba 818 Aug 25 11:39 off_backup.sh

    $ ./off_backup.sh          -- 회복 실행

    TIBERO DATABASE RECOVER USING OFFLINE BACKUP DATABASE !
```
<br/>


### 📼 _** 티베로 운영 중 백업 (Online Backup)**_

- 쿼리작성 : 티베로 데이터베이스에 온라인 백업 시작을 알린다.
```sql
    SQL> ALTER TABLESPACE 테이블스페이스명 BEGIN BACKUP;

    -- begin backup 시 백업 플래그가 켜진다. 
    -- redolog file을 이미지 로깅 방식으로 저장하게 된다. 
    -- -> 변경 사항에 대한 log의 양이 늘어나기 때문에 최대한 빨리 끝내야 한다. 
```

- OS명령으로 해당 테이블 스페이스의 데이터파일 복사
```sql
    SQL> !cp /data01/tibero/system001.tbf /backup/tibero/system001.tbf_backup 

    -- 느낌표를 붙이면 tbsql외부의 명령어를 사용할 수 있다.
    -- 실제 환경에서는 백업 소프트웨어를 이용
```
- 쿼리작성 : 티베로 데이터베이스에 온라인 백업 종료를 알림
```sql
    SQL> ALTER TABLESPACE 테이블스페이스명 END BACKUP;
```




### 📼 _** 티베로 Begin backup 이후 발생 내용**_


`begin backup`을 실행한 뒤 되도록이면 테이블스페이스에 연결된 데이터파일에 대한 데이터 변경 작업을 하지 않는 것이 좋다. 

빠르게 `end backup`을 하지 않으면?

이미지 log가 많이 발생한다.
서버가 전체적으로 느려진다.
log가 가득차서 log switch가 계속 발생 -> 디스크가 계속찬다.
online log switch가 진행되지 않는다 
모든게 멈춘다.


## 💻 부트 과정별 복구 recover 작업

- 부트 과정별 복구
  - nomount모드 : 컨트롤파일 손상 시 create control 쿼리 사용해서 생성
  - mount모드 : alter database recover(미디어 복구 명령) 사용 가능
  - 이 사이에 : redolog file 재생성
  - open모드 : 복구작업 불가


## 💻 Online Backup
Online Backup은 인스턴스가 데이터를 open하여 사용하는 중에 backup을 수행하는 것이다.

![](https://velog.velcdn.com/images/suran-kim/post/9ddd486f-a008-4fe8-8970-ad98abf87ad9/image.png)



### 📼 1. **Datafile 백업**
```
- 보통 테이블 스페이스의 백업플래그를 켜고 -> 백업 -> 백업플래그 종료를 하게 된다.
  이는 redolog 발생량을 최소화하는 방법이다.
- 반면 모든 테이블 스페이스의 백업플래그를 한 번에 켜는 방법도 있다.
  장 : 명령 한 번으로 사용 가능 / 단 : 이미지 로깅 발생
- 모든 작업이 끝나면 log switch를 수행한다
```

- 데이터 파일 백업   
  - 백업장소 만들기
  - 모든** 테이블 스페이스**의 데이터 파일에 대해 **백업플래그 켜기**

```sql
		$ mkdir /tibero/s/on_backup
        
		SQL> ALTER DATABASE BEGIN BACKUP;
```
  
  - 모든 데이터 파일 백업하기
```sql
		SQL> !cp /tibero/tbdata/tibero/*.dtf     /tibero/s/on_backup
```
  - 모든 **테이블 스페이스**의 데이터 파일에 대해 **백업플래그 끄기**
```sql
		SQL> ALTER DATABASE END BACKUP;  
```
  - 백업 플래그 끄기 수행 이력이 담긴 리두 로그를 아카이브 하기

```sql
		SQL> ALTER SYSTEM SWITCH LOGFILE;
```   

   
- 백업된 데이터파일 조회


```sql
		-- 백업 플래그가 모두 꺼졌는지 조회
        SQL> ALTER SESSION SET NLS_DATE_FORMAT='YYYY/MM/DD HH24:MI:SS';

        Session altered.

        SQL> COL TIME FOR A25
        SQL> SELECT * FROM V$BACKUP;  -- 백업 플래그 종료 확인

             FILE# STATUS        CHANGE# TIME
        ---------- ---------- ---------- -------------------------
                 0 NOT ACTIVE          0
                 1 NOT ACTIVE          0
                 2 NOT ACTIVE          0
                 3 NOT ACTIVE          0
                 4 NOT ACTIVE          0



        SQL> !ls -al /tibero/s/on_backup/*.dtf  -- 백업된 데이터파일 조회
        -rwxrwx--- 1 root vboxsf  52428800 Aug 25 14:14 /tibero/s/on_backup/syssub001.dtf
        -rwxrwx--- 1 root vboxsf 171966464 Aug 25 14:14 /tibero/s/on_backup/system001.dtf
        -rwxrwx--- 1 root vboxsf 104857600 Aug 25 14:14 /tibero/s/on_backup/temp001.dtf
        -rwxrwx--- 1 root vboxsf 209715200 Aug 25 14:14 /tibero/s/on_backup/undo001.dtf
        -rwxrwx--- 1 root vboxsf 136839168 Aug 25 14:14 /tibero/s/on_backup/USERS01.dtf
        -rwxrwx--- 1 root vboxsf  52428800 Aug 25 14:14 /tibero/s/on_backup/usr001.dtf

```


### 📼 2.  **Controlfile 백업**
- **CREATE문**으로 컨트롤 파일 새로 생성하는 방식으로 복구

- 백업장소
    - 기존 데이터파일 백업과 같은 경로 : /tibero/s/on_backup
- 백업 명령 실행
```sql
	-- 컨트롤 파일 백업 쿼리
    SQL> ALTER DATABASE BACKUP CONTROLFILE TO TRACE AS '/tibero/s/on_backup/crectl.sql'
    REUSE         -- 동일 파일 존재 시 기존 파일을 덮어쓴다. 
    NORESETLOGS; -- 로그를 리셋하지 않는다.
```
- 백업된 파일 조회
```sql
    $ ls -al /tibero/s/on_backup/crectl.sql
    $ cat    /tibero/s/on_backup/crectl.sql
```


### 📼 3. **Logfile 백업**
- **log switch **실행
- 백업 장소
     - 기존 데이터파일, 컨트롤파일 백업과 같은 경로 : /tibero/s/on_backup   


- 온라인 리두 로그를 아카이브 수행하여 **아카이브 로그 파일**을 백업받는다.
```sql
	-- 로그 스위치 수행
    SQL> ALTER SYSTEM SWITCH LOLGFILE;
    SQL> !cp /tibero/tbdata/tibero/arch/*.arc    /tibero/s/on_backup
```

- 백업된 파일 조회
```sql
    SQL> !ls -al /tibero/s/on_backup/*.arc
```


### 📼 4.  **기타(.passwd , tip 백업 )**
- 파라미터 파일
```sql
    SQL> !cp /tibero/tibero7/config/tibero.tip    /tibero/s/on_backup
```
  
- 패스워드 파일
```sql
    SQL> !cp /tibero/tbdata/tibero/.passwd        /tibero/s/on_backup
```

- 백업된 파일 조회
```sql
    SQL> !ls -al /tibero/s/on_backup/*.tip
    SQL> !ls -al /tibero/s/on_backup/.passwd
```


- 전체 백업 파일 조회

```sql
    SQL> !ls -al /tibero/s/on_backup
    total 712617
    drwxrwx--- 1 root vboxsf      4096 Aug 25 14:44 .
    drwxrwx--- 1 root vboxsf      4096 Aug 25 14:09 ..
    -rwxrwx--- 1 root vboxsf      3898 Aug 25 14:31 crectl.sql
    -rwxrwx--- 1 root vboxsf   1058304 Aug 25 14:40 log-t0-r0-s10.arc
    -rwxrwx--- 1 root vboxsf    175104 Aug 25 14:40 log-t0-r0-s11.arc
    -rwxrwx--- 1 root vboxsf    119296 Aug 25 14:40 log-t0-r0-s6.arc
    -rwxrwx--- 1 root vboxsf     35840 Aug 25 14:40 log-t0-r0-s7.arc
    -rwxrwx--- 1 root vboxsf     19968 Aug 25 14:40 log-t0-r0-s8.arc
    -rwxrwx--- 1 root vboxsf     51712 Aug 25 14:40 log-t0-r0-s9.arc
    -rwxrwx--- 1 root vboxsf        44 Aug 25 14:44 .passwd
    -rwxrwx--- 1 root vboxsf  52428800 Aug 25 14:23 syssub001.dtf
    -rwxrwx--- 1 root vboxsf 171966464 Aug 25 14:23 system001.dtf
    -rwxrwx--- 1 root vboxsf 104857600 Aug 25 14:23 temp001.dtf
    -rwxrwx--- 1 root vboxsf       252 Aug 25 14:43 tibero.tip
    -rwxrwx--- 1 root vboxsf 209715200 Aug 25 14:23 undo001.dtf
    -rwxrwx--- 1 root vboxsf 136839168 Aug 25 14:23 USERS01.dtf
    -rwxrwx--- 1 root vboxsf  52428800 Aug 25 14:23 usr001.dtf

```

![](https://velog.velcdn.com/images/suran-kim/post/8d189f61-20d5-4291-8dce-c72d700d14bf/image.png)



## 💻 백업 파일을 이용한 datafile 완전 복구

1) (장애유발) Datafile 삭제
− Datafile을 삭제한 후 TIBERO 비정상 종료
```sql

SQL> SELECT NAME FROM V$DATAFILE;

NAME
--------------------------------------------------------------------------------
/tibero/tbdata/tibero/system001.dtf
/tibero/tbdata/tibero/undo001.dtf
/tibero/tbdata/tibero/usr001.dtf
/tibero/tbdata/tibero/syssub001.dtf
/tibero/tbdata/tibero/USERS01.dtf

5 rows selected.

SQL> !rm /tibero/tbdata/tibero/usr001.dtf  -- 데이터파일 삭제
SQL> q
Disconnected.

$ tbdown abnormal -- 서버 강제 종료

```
2) **mount 모드** 기동
 − 티베로 시작시, MOUNT 모드로 기동됨( 에러메시지 발생 )
```sql
```
3) (mount 모드) 데이터 파일 조회
− open faild 가 발생한 데이터 파일을 조회함 
- **RECOVER_FILE 딕셔너리** : 컨트롤파일을 기준으로, 실제 파일과 컨트롤파일 내용의 차이점을 표시해준다.
```sql
      SQL> COL TIME FOR A10
      SQL> COL ERROR FOR A30
      SQL> SELECT * FROM V$RECOVER_FILE;

           FILE# ONLINE  ERROR                             CHANGE# TIME
      ---------- ------- ------------------------------ ---------- ----------
               2 ONLINE  open failed                             0



      SQL> SELECT NAME FROM V$DATAFILE WHERE FILE#=2;

      NAME
      --------------------------------------------------------------------------------
      /tibero/tbdata/tibero/usr001.dtf


```
4) (mount 모드) 백업 파일 넣기
− 장애 이전에 백업하였던 해당 데이터파일 "usr001.dtf" 을 넣음
- 백업받을 파일? 검색하는 과정 필요
- 가장 최근에 백업한 파일 넣어야함
```sql
      SQL>  !cp /tibero/s/off_backup/usr001.dtf      /tibero/tbdata/tibero/usr001.dtf


      SQL> SELECT * FROM V$RECOVER_FILE;

           FILE# ONLINE  ERROR                             CHANGE# TIME
      ---------- ------- ------------------------------ ---------- ----------
               2 ONLINE  file restored                       64990 2022/08/25

```
5) (mount 모드) 복구 명령 수행
− 미디어 복구 명령 실행함.
   - 데이터파일 마지막 백업 시점 이후의 변경이력 적용 -> 최신상태로 적용? -> 복구???
- 복구를 자동으로 수행한다.
```sql
      SQL> ALTER DATABASE RECOVER AUTOMATIC DATABASE;

      Database altered.


      SQL>  SELECT * FROM V$RECOVER_FILE;

      0 row selected.


```
6) 데이터베이스 오픈
− tbdown, tbboot 명령으로 재기동하여, 인스턴스가 NORMAL mode 로 구동되는지 확인한다.
```sql
```

컨트롤파일
파일이름 경로 마지막 갱신시간 (tsn) 을 가지고 있음

## 💻 온라인 백업파일을 이용한 전체 복구
- 장애 : 모든 데이터 파일, 컨트롤 파일, 리두로그, 아카이브로그 파일 삭제 됨
- 복구 : 온라인 백업 파일 이용하여 복구하기


1) (장애유발) 모든 파일 삭제
− 모든 파일 삭제한 후 TIBERO 비정상 종료

```sql
  rm /tibero/tbdata/tibero/*.dtf
  rm /tibero/tbdata/tibero/*.log
  rm /tibero/tbdata/tibero/*.ctl
  rm /tibero/tbdata/tibero/arch/*.arc
  rm /tibero/tbdata/tibero/.passwd


  -- 비정상 종료
  tbdown abnormal
```


2) 온라인 백업파일 이용
− 백업 파일 리스토어(복구)

1. 데이터파일 복구
```sql
    -- 백업 폴더에서 파일 가져오기
    $ cp /tibero/s/on_backup/*.dtf          /tibero/tbdata/tibero
    $ cp /tibero/s/on_backup/crectl.sql     /tibero/tbdata/tibero
    $ cp /tibero/s/on_backup/*.arc          /tibero/tbdata/tibero/arch
    $ cp /tibero/s/on_backup/.passwd        /tibero/tbdata/tibero
    
    -- 복사한 파일 확인
    $ ls -al        
    
    total 711176
    drwxr-xr-x 4 tibero dba       180 Aug 25 16:44 .
    drwxr-xr-x 3 tibero dba        20 Aug 23 10:25 ..
    drwxr-xr-x 2 tibero dba       152 Aug 25 16:43 arch
    -rwxr-x--- 1 tibero dba      3898 Aug 25 16:43 crectl.sql -- 쿼리로 생성해야함
    drwx------ 3 tibero dba        17 Aug 23 10:27 java
    -rwxr-x--- 1 tibero dba        44 Aug 25 16:44 .passwd
    -rwxr-x--- 1 tibero dba  52428800 Aug 25 16:42 syssub001.dtf
    -rwxr-x--- 1 tibero dba 171966464 Aug 25 16:42 system001.dtf
    -rwxr-x--- 1 tibero dba 104857600 Aug 25 16:42 temp001.dtf
    -rwxr-x--- 1 tibero dba 209715200 Aug 25 16:42 undo001.dtf
    -rwxr-x--- 1 tibero dba 136839168 Aug 25 16:42 USERS01.dtf
    -rwxr-x--- 1 tibero dba  52428800 Aug 25 16:42 usr001.dtf
     -- 데이터파일만 존재. 컨트롤파일과 리두로그 파일은 없다.
    
```

2. 컨트롤파일 복구
- 데이터베이스 복구 시 두 가지 옵션이 있다.
  - `noresetlogs` : control file을 재생성할 필요가 없는 경우 사용하는 옵션. 온라인 리두 로그에 접근해서 컨트롤 파일을 만들 때 집어넣는다. 온라인 리두 로그 파일이 존재하지 않으면 실패한다.
  - `resetlogs` :  **control file 재성성** 시 사용하는 옵션. 어차피 리두 로그가 있어도 리두 로그를 리셋하고, 리두로그가 없다면 리두 로그 파일을 새로 생성하기 때문에 리두 로그가 없어도 컨트롤 파일이 만들어진다.
```sql
      -- nomount모드 부트
      $ tbboot nomount
      
      $ cat /tibero/tbdata/tibero/crectl.sql  -- 컨트롤파일 내용 확인
      
      -- 이때, 컨트롤파일에서 RESETLOGS case의 CREATE문을 사용한다.
      
	  $ cp /tibero/tbdata/tibero/crectl.sql  /tibero/tbdata/tibero/crectl_resetlogs.sql
	  $ vi /tibero/tbdata/tibero/crectl_resetlogs.sql  -- RESETLOGS옵션을 사용하도록 noresetlogs 옵션을 지우고 저장
	  
      $ tbsql sys/tibero
	  SQL> @/tibero/tbdata/tibero/crectl_resetlogs.sql  
      
      -- Control File created.
      ...
      SQL> -- ALTER TABLESPACE TEMP ADD TEMPFILE '/tibero/tbdata/tibero/temp001.dtf'
      ...
      
      
      -- mount모드 부트 가능
      $ tbdown
      $ tbboot mount
```

3. 리두로그파일 복구
- 온라인 리두 로그 재생성 시점 : 마운트 모드 -> 노말 모드 사이


```sql
	  $ tbsql sys/tibero
      SQL> ALTER DATABASE RECOVER AUTOMATIC DATABASE; -- 미디어 복구 명령
      -- 데이터파일 마지막 백업 시점 이후의 변경이력 적용 -> 최신상태로 적용

	  -- 아래는 온라인 리두 로그가 없기 때문에 발생하는 에러이다. 
      -- 아카이브 로그만 이용해서 복구 -> 데이터 파일의 마지막 변경 시점 이후에 발생한 변경 내역이 온라인 리두 로그에 있다? 
      TBR-1072: Current online log file (thread 0 seq -1) is outdated.
      Need a log with seq 12 to recover from TSN 69070.
      Recovery done until 2022/08/25 14:40:17 TSN 69061.  
      -- 69061까지 recover되었다. -> 69061 변경내역까지만 데이터 파일에 적용했다.
      Recovery is incomplete and resetlogs is required.

      
      $ tbboot resetlogs -- resetlogs 모드로 부트

      Tibero 7

      TmaxTibero Corporation Copyright (c) 2020-. All rights reserved.
      Tibero instance started up (NORMAL RESETLOGS mode). 
      -- 결과적으로는 노말모드 부트. 그러나 과정에서 온라인 리두 로그를 새로 생성했다.
      

      $ ls -al


      total 1012460
      drwxr-xr-x 4 tibero dba       287 Aug 25 17:01 .
      drwxr-xr-x 3 tibero dba        20 Aug 23 10:25 ..
      drwxr-xr-x 2 tibero dba       180 Aug 25 17:01 arch
      -rw------- 1 tibero dba  75612160 Aug 25 17:02 c1.ctl
      -rw------- 1 tibero dba  75612160 Aug 25 17:02 c2.ctl
      -rwxr-x--- 1 tibero dba      3068 Aug 25 16:55 crectl_resetlogs.sql
      -rwxr-x--- 1 tibero dba      3898 Aug 25 16:43 crectl.sql
      drwx------ 3 tibero dba        17 Aug 23 10:27 java
      -rw------- 1 tibero dba  52428800 Aug 25 17:01 log01.log -- tbboot resetlog 하는 순간 생성됨
      -rw------- 1 tibero dba  52428800 Aug 25 17:01 log11.log
      -rw------- 1 tibero dba  52428800 Aug 25 17:01 log21.log
      -rwxr-x--- 1 tibero dba        44 Aug 25 16:44 .passwd
      -rwxr-x--- 1 tibero dba  52428800 Aug 25 17:01 syssub001.dtf
      -rwxr-x--- 1 tibero dba 171966464 Aug 25 17:01 system001.dtf
      -rwxr-x--- 1 tibero dba 104857600 Aug 25 16:42 temp001.dtf
      -rwxr-x--- 1 tibero dba 209715200 Aug 25 17:01 undo001.dtf
      -rwxr-x--- 1 tibero dba 136839168 Aug 25 17:01 USERS01.dtf
      -rwxr-x--- 1 tibero dba  52428800 Aug 25 17:01 usr001.dtf

```

4. 데이터베이스 오픈 이후 tempfile 추가하기
- **TEMP 테이블 스페이스**는 존재하지만 연결된 데이터파일은 없는 상태.
이유 : 컨트롤 파일이 재생성 되었기 때문에 
그래서 temp 테이블스페이스와 tempfile을 연결해줘야 한다.
- 기존 정보를 활용하는 게 좋다.
TEMPFILE추가 방법은 컨트롤 파일 스크립트에 나와있다.

```sql

    SQL> SELECT FILE_ID, TABLESPACE_NAME FROM DBA_TEMP_FILES;
    0 row selected.


    SQL> SELECT NAME FROM V$TABLESPACE;

    NAME
    --------------------------------------------------------------------------------
    SYSTEM
    UNDO
    TEMP  -- 테이블 스페이스는 존재하지만 연결된 데이터 파일이 없는 상태
    USR
    SYSSUB
    USERS


    -- TEMPFILE 연결
    SQL> ALTER TABLESPACE TEMP ADD TEMPFILE '/tibero/tbdata/tibero/temp001.dtf' size 10m reuse;
```
복구 끝

<br/>

5. 전체 백업하기

- `redolog reset`을 하게 되면 리셋 이전과 이후는 단절된다. 그래서 리셋을 했으면 **전체 백업**을 다시 해줘야 한다. 전체 백업을 받지 않고 다시 장애가 발생했을 때 리셋 이전 로그를 사용해서 복구하는 것은 불가능하다.
- 만약 로그 리셋을 하지 않고 복구했다면 전체 백업을 즉시 해주지 않아도 된다.
- 오프라인 백업 필요

```sql
$ cat ./off_backup2.sh
$ ./off_backup2.sh  -- 백업 파일 실행

---- 백업 파일 내용 -------------
#!/bin/sh

echo "TIBERO OFFLINE BACKUP DATABASE : /tibero/s/off_backup2"

### RM FILES
rm /tibero/s/off_backup2/*
rm /tibero/s/off_backup2/.passwd
ls -al /tibero/s/off_backup2/*

### TIBERO INSTACE SHUTDOWN
tbdown immediate

### COPY DATABASE
cp /tibero/tbdata/tibero/*.dtf          /tibero/s/off_backup2
cp /tibero/tbdata/tibero/*.log          /tibero/s/off_backup2
cp /tibero/tbdata/tibero/*.ctl          /tibero/s/off_backup2
cp /tibero/tbdata/tibero/arch/*.arc     /tibero/s/off_backup2
cp /tibero/tibero7/config/tibero.tip    /tibero/s/off_backup2
cp /tibero/tbdata/tibero/.passwd        /tibero/s/off_backup2

### TIBERO INSTANCE START
tbboot
-----------------------------------
```



> _이미지 자료 출처_ 
- 충남대학교 티베로 DB 엔지니어링 교육 강사 자료
- 티베로 공식문서