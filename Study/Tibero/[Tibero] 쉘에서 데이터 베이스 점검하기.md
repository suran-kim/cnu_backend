


![](https://velog.velcdn.com/images/suran-kim/post/da62b334-95d9-497b-98b3-59406428f3e4/image.png)

# DB 점검

딕셔너리 뷰를 통해 생성한 데이터베이스를 점검해보자.

서비스 포트는 **리스너 포트**를 의미한다.

각 항목에 대한 쿼리를 작성할 수 있어야 한다.
이름 앞에 `V$`이 붙은 뷰 딕셔너리는 동적 뷰이다. `V$`가 붙은 뷰는 티베로 인스턴스가 실행된 뒤에도 값을 동적으로 변경할 수 있다.(?)

_EX)_
 - `V$LOG` : 로그 그룹의 정보를 조회하는 뷰
 - `V$LOGFILE` : 로그 파일의 정보를 조회하는 뷰








### **인스턴스 이름 조회**
```sql
SQL> DESC V$INSTANCE

COLUMN_NAME                              TYPE               CONSTRAINT
---------------------------------------- ------------------ --------------------
INSTANCE_NUMBER                          NUMBER
INSTANCE_NAME                            VARCHAR(40)
DB_NAME                                  VARCHAR(40)
HOST_NAME                                VARCHAR(63)
PARALLEL                                 VARCHAR(3)
THREAD#                                  NUMBER
VERSION                                  VARCHAR(8)
STARTUP_TIME                             DATE
STATUS                                   VARCHAR(16)
SHUTDOWN_PENDING                         VARCHAR(4)
TIP_FILE                                 VARCHAR(256)


SQL> SELECT INSTANCE_NAME FROM V$INSTANCE;

INSTANCE_NAME
----------------------------------------
tibero

```
<br/>


### **데이터베이스 이름 조회**
```sql
SQL> SELECT DB_NAME FROM V$INSTANCE;

DB_NAME
----------------------------------------
tibero

```

<br/>


### **리스너 포트 점검 **

```sql
SQL> DESC v$PARAMETERS

COLUMN_NAME                              TYPE               CONSTRAINT
---------------------------------------- ------------------ --------------------
NAME                                     VARCHAR(64)
VALUE                                    VARCHAR(1024)
DFLT_VALUE                               VARCHAR(1024)



SQL> SELECT VALUE FROM V$PARAMETERS WHERE NAME='LISTENER_PORT';

VALUE
--------------------------------------------------------------------------------
8629
```
<br/>



### **캐릭터셋 점검** 

```sql
SQL> DESC DATABASE_PROPERTIES

COLUMN_NAME                              TYPE               CONSTRAINT
---------------------------------------- ------------------ --------------------
NAME                                     VARCHAR(128)
VALUE                                    VARCHAR(80)
COMMENT_STR                              VARCHAR(4000)

SQL> SELECT VALUE FROM DATABASE_PROPERTIES WHERE NAME='NLS_CHARACTERSET';

VALUE
--------------------------------------------------------------------------------
MSWIN949
```
<br/>


### **현재 리두로그 사이즈 점검**

```sql
SQL> DESC V$LOG;

COLUMN_NAME                              TYPE               CONSTRAINT
---------------------------------------- ------------------ --------------------
THREAD#                                  NUMBER
GROUP#                                   NUMBER
SEQUENCE#                                NUMBER
BYTES                                    NUMBER
MEMBERS                                  NUMBER
ARCHIVED                                 VARCHAR(3)
STATUS                                   VARCHAR(8)
FIRST_CHANGE#                            NUMBER
FIRST_TIME                               DATE

SQL> SELECT AVG(BYTES)/1024/1024 FROM V$LOG;

AVG(BYTES)/1024/1024
--------------------
                  50

```
<br/>


### **리두로그 그룹갯수**
```sql
SQL> SELECT COUNT(*) FROM V$LOG;

  COUNT(*)
----------
         3
```


### **리두로그 그룹당 멤버 갯수**
```sql
SQL> SELECT AVG(MEMBERS) FROM V$LOG;

AVG(MEMBERS)
------------
           1
           
그룹당 멤버 수 어차피 똑같음 -> 그래서 (그룹 수/멤버 수의 합) 으로 평균내는 거임           
```
<br/>


### **리두로그 파일 경로 (V$LOGFILE)**
```sql
SQL> DESC V$LOGFILE

COLUMN_NAME                              TYPE               CONSTRAINT
---------------------------------------- ------------------ --------------------
GROUP#                                   NUMBER
STATUS                                   VARCHAR(7)
TYPE                                     CHAR(6)
MEMBER                                   VARCHAR(256)


SQL> SELECT MEMBER FROM V$LOGFILE WHERE GROUP#=0;

MEMBER
--------------------------------------------------------------------------------
/tibero/tbdata/tibero/log01.log
```

현재는 0번 그룹의 멤버 하나만 조회 됨
근데 삼중화 되면 조회가 주르륵 될 듯

<br/>

### **시스템 테이블 스페이스 크기**

```sql
SQL> DESC DBA_DATA_FILES;

COLUMN_NAME                              TYPE               CONSTRAINT
---------------------------------------- ------------------ --------------------
FILE_NAME                                VARCHAR(256)
FILE_ID                                  NUMBER
TABLESPACE_NAME                          VARCHAR(128)
BYTES                                    NUMBER
BLOCKS                                   NUMBER
STATUS                                   CHAR(9)
RELATIVE_FNO                             NUMBER
AUTOEXTENSIBLE                           VARCHAR(3)
MAXBYTES                                 NUMBER
MAXBLOCKS                                NUMBER
INCREMENT_BY                             NUMBER

SQL> SELECT SUM(BYTES)/1024/1024 FROM DBA_DATA_FILES WHERE TABLESPACE_NAME='SYSTEM';

SUM(BYTES)/1024/1024
--------------------
                 164
```
<br/>


### **언두 테이블 스페이스**
```sql
SQL> SELECT SUM(BYTES)/1024/1024 FROM DBA_DATA_FILES WHERE TABLESPACE_NAME='UNDO';

SUM(BYTES)/1024/1024
--------------------
                 200


```
<br/>



### **TEMP 테이블 스페이스 크기(DBA_TEMP_FILES)**
```sql

SQL> SELECT SUM(BYTES)/1024/1024 FROM DBA_TEMP_FILES WHERE TABLESPACE_NAME='TEMP';

SUM(BYTES)/1024/1024
--------------------
            100.0625


```
<br/>


### **CONTROLFILE경로와 이름**

```sql
SQL>  DESC V$CONTROLFILE

COLUMN_NAME                              TYPE               CONSTRAINT
---------------------------------------- ------------------ --------------------
STATUS                                   NUMBER
NAME                                     VARCHAR(256)
BLKSIZE                                  NUMBER
BLOCKS                                   NUMBER

SQL> SELECT NAME FROM V$CONTROLFILE;

NAME
--------------------------------------------------------------------------------
/tibero/tbdata/tibero/c1.ctl
/tibero/tbdata/tibero/c2.ctl

```
<br/>

<br/>


### **모든 데이터 파일 경로** 
 - _파일이름_ 을 조회하면 경로와 이름이 모두 포함되어있다.
 
```sql
SQL> DESC DBA_DATA_FILES

COLUMN_NAME                              TYPE               CONSTRAINT
---------------------------------------- ------------------ --------------------
FILE_NAME                                VARCHAR(256)
FILE_ID                                  NUMBER
TABLESPACE_NAME                          VARCHAR(128)
BYTES                                    NUMBER
BLOCKS                                   NUMBER
STATUS                                   CHAR(9)
RELATIVE_FNO                             NUMBER
AUTOEXTENSIBLE                           VARCHAR(3)
MAXBYTES                                 NUMBER
MAXBLOCKS                                NUMBER
INCREMENT_BY                             NUMBER


SQL> SELECT FILE_NAME FROM DBA_DATA_FILES;

FILE_NAME
--------------------------------------------------------------------------------
/tibero/tbdata/tibero/system001.dtf
/tibero/tbdata/tibero/undo001.dtf
/tibero/tbdata/tibero/usr001.dtf
/tibero/tbdata/tibero/syssub001.dtf

 ```
<br/>

 
### **패스워드 파일 경로** 

```sql

SQL> DESC V$PARAMETERS

COLUMN_NAME                              TYPE               CONSTRAINT
---------------------------------------- ------------------ --------------------
NAME                                     VARCHAR(64)
VALUE                                    VARCHAR(1024)
DFLT_VALUE                               VARCHAR(1024)

SQL> SELECT VALUE FROM V$PARAMETERS WHERE NAME='DB_CREATE_FILE_DEST';

VALUE
--------------------------------------------------------------------------------
/tibero/tbdata/tibero/



```

<br/>


### **티베로 전체 메모리 크기**

```sql
SQL>  SELECT VALUE FROM V$PARAMETERS WHERE NAME='MEMORY_TARGET';

VALUE
--------------------------------------------------------------------------------
2147483648



SQL> SELECT VALUE/1024/1024 FROM V$PARAMETERS WHERE NAME='MEMORY_TARGET';

VALUE/1024/1024
---------------
           2048


```
<br/>


### **공유메모리 조회**
```sql
SQL> SELECT VALUE/1024/1024 FROM V$PARAMETERS WHERE NAME='TOTAL_SHM_SIZE';

VALUE/1024/1024
---------------
           1024

1 row selected.

```
<br/>



### 데이터 베이스 오픈 모드 
```sql
SQL> DESC V$DATABASE;

COLUMN_NAME                              TYPE               CONSTRAINT
---------------------------------------- ------------------ --------------------
DBID                                     NUMBER
NAME                                     VARCHAR(40)
DB_CREATE_DATE                           DATE
CF_CREATE_DATE                           DATE
CURRENT_TSN                              NUMBER
OPEN_MODE                                VARCHAR(10)
RESETLOG_TSN                             NUMBER
RESETLOG_DATE                            DATE
PREV_RESETLOG_TSN                        NUMBER
PREV_RESETLOG_DATE                       DATE
LOG_MODE                                 VARCHAR(12)
CKPT_TSN                                 NUMBER
CKPT_DATE                                DATE
CPU_NAME                                 VARCHAR(32)
PLATFORM_NAME                            VARCHAR(32)
CPU_MODEL                                VARCHAR(256)
OS_UPTIME                                VARCHAR(256)

SQL> SELECT OPEN_MODE FROM V$DATABASE;

OPEN_MODE
----------
READ WRITE
```
<br/>


### (리두 로그 관련) 로그 모드

```sql
SQL> SELECT LOG_MODE FROM V$DATABASE;

LOG_MODE
------------
NOARCHIVELOG
```
<br/>


### 테이블 스페이스 목록

```sql
SQL> DESC DBA_DATA_FILES

COLUMN_NAME                              TYPE               CONSTRAINT
---------------------------------------- ------------------ --------------------
FILE_NAME                                VARCHAR(256)
FILE_ID                                  NUMBER
TABLESPACE_NAME                          VARCHAR(128)
BYTES                                    NUMBER
BLOCKS                                   NUMBER
STATUS                                   CHAR(9)
RELATIVE_FNO                             NUMBER
AUTOEXTENSIBLE                           VARCHAR(3)
MAXBYTES                                 NUMBER
MAXBLOCKS                                NUMBER
INCREMENT_BY                             NUMBER



SQL> SELECT TABLESPACE_NAME FROM DBA_DATA_FILES;

TABLESPACE_NAME
--------------------------------------------------------------------------------
SYSTEM
UNDO
USR
SYSSUB
USR
SYSTEM

```
<br/>



### USER 테이블 스페이스에 연결된 데이터 파일 목록

```sql

SQL> SELECT FILE_NAME FROM DBA_DATA_FILES WHERE TABLESPACE_NAME = 'USR';

FILE_NAME
--------------------------------------------------------------------------------

/tibero/tbdata/tibero/usr001.dtf

/tibero/tbdata/tibero/usr002.dtf
```

<br/>

<br/>
