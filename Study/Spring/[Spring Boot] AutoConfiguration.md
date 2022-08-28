
>_본 포스팅은 프로그래머스 미니 데브 코스를 공부하며 
학습을 기록하기 위한 목적으로 작성된 글입니다._


스프링은 처음 접하면 매우 어렵다.
꾸준히 여러 번 지속적으로 하는 것이 중요하다.



지금까지 했던 스프링 웹어플리케이션을 스프링부트로 변경
그렇다. 지금까지 했던 건 스프링.


어떤 고충을 해결하기 위해 어떤 기술이 나왔는지를 아는 것이 중요하다.




# @SpringBootApplication

- 내장 tomcat(embeded tomcat) 실행 → applicationContext 실행
  - 내장 톰캣을 통해 IDE에서 실행하는 것만으로 서버를 띄울 수가 있다.
- @SpringBootApplication 자체가 configuration이 된다.
<br/>

##  Auto Configuration
    
Bean과 propertie설정을 타입화 -> 자동으로 특정 condition에 맞춰 Bean설정  

Was 셋업, dependency 추가, JDBC 템플릿 Bean, 웹 MVC, 리소스 리졸버, 서버… 자동 설정 ->
   Bean이 자동으로 되는 것과 마찬가지!

- 스프링 부트의 Auto Configuration기능 
  - 필요하지 않은 기능은 exclude 가능
  - `@Conditional`을 이용해서 조건(특정 Class | Bean | 의존성이 있는 경우)에 따른 자동 설정 가능
    - @ConditionalOnClass : 특정 Class 파일이 존재하면 Bean을 등록한다.
    - @ConditionalOnBean : 특정 Bean 이 존재하면 Bean을 등록한다.


<br/>

## application.yml 설정
이전에는 DataSource를 Bean으로 등록해서 사용했었다. But

- `application.yml`에서 **Spring 관련 설정**을 할 수 있다.
  - DataSource 설정 가능
  - 데이터 소스 풀 설정 가능
  - 타임리프 설정 가능

```yaml
        # 스프링 전용 설정
        spring:
          # 데이터 소스 설정
          datasource:
            url: jdbc:mysql://localhost/order_mgmt
            username: root
            password: root1234!
          # 데이터 소스 풀 설정 (디폴트 : 히카리 소스 타입 -> 따로 설정하지 않아도 됨)
          # 타임리프 설정
          thymeleaf:
            view-name: "views/*"
            prefix: "/WEB-INF/" # 뷰 네임과 리졸버 매칭..?
```

<br/>  
  
- `application.yml`에서 **임베디드 서블릿** 관련 설정을 할 수 있다.  
  - servletContext name  지정 가능
  - 포트넘버 지정 가능
  - 주소 지정 가능
```yaml
        server:
         servlet:
          context-path: /kd_spring_order_war_exploded
```
<br/>

-  `application.yml`에서 **logging** 설정을 할 수 있다.  
```yaml
        logging:
          level:
            org:
              springframework: DEBUG
```

<br/><br/><br/>

> _**TIP**_
- 스프링부트는 Configuration을 통해 어플리케이션 구성을 돕는다.
- 스프링부트는 서블릿 name설정을 하지 않으면 기본으로 띄운다 -> http://localhost:8080/customers
- config 패키지를 따로 빼서 componentScan 경로에 추가하면 매우 간단해진다네 <br/>
_**⭐ 이전**_![](https://velog.velcdn.com/images/suran-kim/post/63af8c28-a065-40c6-8cc6-a6f0641615f2/image.png) _**⭐ 이후**_ ![](https://velog.velcdn.com/images/suran-kim/post/da105327-6eec-434d-87d1-a1ee0b987992/image.png) <br/>
- 패키지
![](https://velog.velcdn.com/images/suran-kim/post/0918e28a-8c2b-4a2a-b37a-7fc5d7b296dd/image.png)


  

  
>_**Rf**_
- [yshjft 님의 2022년 04월 22일 TIL](https://velog.io/@yshjft/2022%EB%85%84-04%EC%9B%94-22%EC%9D%BC-TIL)

