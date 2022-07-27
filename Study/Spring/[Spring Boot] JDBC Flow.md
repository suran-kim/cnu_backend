>_본 포스팅은 프로그래머스 미니 데브 코스를 공부하며 
학습을 기록하기 위한 목적으로 작성된 글입니다._


# **JDBC**
- 자바 어플리케이션과 데이터베이스의 브릿지 역할
- 영속성 레이어를 위해 존재한 최초의 컴포넌트
- 1987년도부터 JDK에 포함되어있었다.


JDBC 인터페이스는 크게 두 가지로 나뉜다.
- JDBC API
- JDBC DB Driver

**JDBC DB Driver**는 _DBMS bender사(Oracle, MySql...)_ 에서 개발하고 배포한다.

**JDBC API**를 이용하면 DBMS의 종류에 관계없이 SQL문을 실행하고 처리할 수 있다.
백엔드 엔지니어는 JDBC API를 이용해서 JDBC DB Driver와 연결하고 쿼리에 대한 요청을 할 수 있다.


JDBC Driver Model에는 네 가지 종류가 있다.

1. Type I : JDBC- ODBC Bridge
2. Type II: Native APT- Partly Java Driver
3. Type III: Network Protocol- Fully Java Driver
4. **Type IV: Thin Driver- Fully Java Driver**

그 중에서 우리가 주로 사용하게 되는 것은 4번째 타입의 JDBC Driver Model이다.

_MySQL에서 지원하는 드라이버는 JDBC 4.2 스펙에 의해 구현된다?_



# JDBC Flow


- DriverManager 를 통해서 커넥션 객체를 받아온다.
- Connection을 통해서 Statement를 가져온다.
- Statement를 통해서 쿼리를 실행해서 `ResultSet`을 가져오거나 `update`를 실행한다.
- 데이터베이스 커넥션을 **종료**한다.

위와 같은 JDBC Flow를 따라 
Api 요청이 오면 DB와 커넥션하고, 
쿼리 실행 후 ResultSet으로 엔티티를 구성하고 
화면에 전달하거나 서비스를 실행하고
커넥션을 종료하게 된다.

**_주의사항_**
- `ResultSet`을 가져오지 않고 DML문(update)을 실행시킬 때는
실행 결과의 ROW 개수만 받아올 수 있다.
- 문제 상황 발생 시 커넥션은 무조건 닫아야 한다.

