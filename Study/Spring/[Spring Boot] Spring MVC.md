>_본 포스팅은 프로그래머스 미니 데브 코스를 공부하며 
학습을 기록하기 위한 목적으로 작성된 글입니다._

# Spring MVC
Spring MVC는 어떻게 웹 어플리케이션의 개발을 도와줄까? 

## DispatchServlet
![](https://velog.velcdn.com/images/suran-kim/post/57cfa1bd-ba7b-439c-8bd4-fd6f2bfa9805/image.png)
이미지 출처 : https://mossgreen.github.io/Servlet-Containers-and-Spring-Framework


_**요약**_
- dispatch: 보내다.
- **DispatchServlet**이란 HTTP 프로토콜로 들어오는 모든 요청을 가장 먼저 받아 적합한 컨트롤러에 위임해주는 **프론트 컨트롤러(Front Controller)이다.**
- _**Front Controller Pattern**_ 을 사용한다.
_Front Controller _
   -  주로 서블릿 컨테이너의 제일 앞에서 서버로 들어오는 클라이언트의 모든 요청을 받아 처리해주는 컨트롤러. 
   -  MVC 구조에서 함께 사용되는 디자인 패턴

_**DispatchServlet 장점**_
- web.xml의 역할 축소로 편리한 이용 가능
  - 과거에는 **모든 서블릿**을 URL 매핑하기 위해 web.xml에 전부 등록해야했다.
  - 이제 DispatcherServlet이 해당 어플리케이션으로 들어오는 **모든 요청을 핸들링**해주고 **공통 작업**을 처리한다.
   - **컨트롤러 구현**만 하면 DispatcherServlet이 적절한 컨트롤러에 위임을 해주는 구조.
 


_**DispatchServlet Flow**_
- 클라이언트로부터 어떠한 요청이 오면 **서블릿 컨테이너**(Ex: _Tomcat_ )가 요청을 받는다.
- 제일 앞단의 중앙 집중용 컨트롤러(**프론트 컨트롤러 == DispatchServlet**)가 모든 요청을 받는다. 
- 디스패처 서블릿은 **공통적인 작업**을 먼저 처리한 뒤에, 해당 요청을 처리해야 하는 컨트롤러를 찾아서 **작업을 위임**하고 응답을 받아 **뷰**를 생성한다.
- 하위 컨트롤러에서 발생한 예외는 **Front Controller**가 처리한다.


## Spring MVC 처리 흐름



1. DispatcherServlet의 HTTP 요청 접수
2. DispatcherServlet에서 컨트롤러로 HTTP 요청 위임
3. 컨트롤러의 모델 생성과 정보 등록
4. 컨트롤러의 결과 리턴: 모델과 뷰
5. DispatcherServlet의 뷰 호출과 (6) 모델 참조
6. HTTP 응답 돌려주기





_**자세한 흐름**_

![](https://velog.velcdn.com/images/suran-kim/post/44aa0873-a908-4445-9319-e80c1ad95570/image.png)

출처 : https://terasolunaorg.github.io/guideline/public_review/Overview/SpringMVCOverview.html


1. 클라이언트의 요청( Http )을 DispatchServlet이 받는다.
   - 서블릿 컨텍스트(웹 컨텍스트)를 지나 스프링 컨텍스트에서 디스패처 서블릿이 가장 먼저 요청을 받게된다.
2. 요청 정보를 통해 요청을 위임할 컨트롤러를 찾는다.
3. 요청을 컨트롤러로 위임할 핸들러 어댑터를 찾아서 전달한다. 
4. 핸들러 어댑터가 컨트롤러로 HTTP 요청을 위임한다.
5. 비즈니스 로직을 처리한다.
   - 컨트롤러가 모델을 생성하거나 서비스를 호출한다. 
   - 서비스에서 실제 엔티티를 만들고, 엔티티로 비즈니스 로직 처리, 컨트롤러에게 결과 반환. 
6. 컨트롤러가 반환값을 반환한다.
   - 컨트롤러가 화면에 전달할 모델을 만든다. 모델을 가지고 뷰를 만들어줌.
   - 모델과 뷰  return
7. HandlerAdapter가 반환값을 처리한다.
   - 디스패처 서블릿에  모델과 뷰  return
   - 디스패처 서블릿이 뷰에 모델 전달. 뷰가 컨텐츠(랜더링 결과)를 만들어준다.
   
8. 서버의 응답(HTTP)을 클라이언트로 반환한다.
   - 디스패쳐 서블릿이 모델과 뷰(XML, JSON, JSP 등일 수 있음)로
HTTP 응답을 만들어서 바디에 담아 클라이언트에게 전달한다.






Front Controller에 등록된 다른 컨트롤러를 호출해서 로직 처리를 위임 -> 응답을 받아 뷰 생성

- 우리가 작성하는 것은 서블릿이 아니라 컨트롤러(하위 컨트롤러).
- 스프링이 우리가 구현한 컨트롤러를 호출


- 클라이언트로부터 요청을 받으면 validation체크, 로직 처리, JSP에 모델을 던지거나 MVC에서 중복해서 컨트롤하는 것들을 template화 시켜두고 필요한 부분만 수정 




### 1. DispatcherServlet의 HTTP 요청 접수 
- 서블릿 등록 시 web.xml, WebApplicationInitializer(스프링 제공 방법)을 이용할 수 있었다. 
 
- WebApplicationInitializer의 구현체를 사용하면 WebApplicationContext를 만들어서 등록할 수 있다. 
서블릿을 만들어서 연결시켜준다.
패스 경로를 등록시켜준다.(app아래의 요청은 모두 dispatcher서블릿으로 가도록)

- 단 하나의 서블릿
서블릿을 여러 개 만들 수는 있 지 만
스프링은 하나의 서블릿 하위에 컨트롤러를 등록하는 방식으로 어플리케이션을 만들게 해준다.

### 2. DispatcherServlet에서 컨트롤러로 HTTP 요청 위임
- 스프링에서 **컨트롤러 == 핸들러**
- 요청이 전달되는 방법은 해당 컨트롤러 오브젝트의 **메소드를 호출**하는 방법 뿐이다.


-  _**핸들러 매핑 전략**_
   - Handler Mapping에서 특정 Controller를 찾을 수 있어야 함
   - 사용자 요청의 URL을 기준으로 어떤 핸들러(컨트롤러)에게 작업을 위임할 지 결정 
     - URL 정보, 파라미터 정보, Http 메소드 등을 참고해서 매핑 결정

  

  - _**핸들러 어답터 전략**_
    - Handler Mapping을 통해 찾은 Handler를 실행할 수 있는 Handler Adapter가 필요
    - 제각각 다른 메소드와 포맷을 가진 컨트롤러를 DispatcherServlet이 호출할 수 있도록 해준다.
    - 매핑 대상 Controller의 파라미터로 변환해서 request를 처리하도록 보낸다. 

    

-   _**@RequestMapping**_
    - 애노테이션 기반의 컨트롤러 
    - RequestMappingHandlerMapping (가장 기본적으로 쓰이는 핸들러 매핑)  
    - RequestMappingHandlerAdapter (가장 기본적으로 쓰이는 핸들러 어답터)  
 
### 3. DispatcherServlet의 뷰 호출과 모델 참조







> _**오류해결**_
  
> _**새로 알게된 용어**_
  - validation 체크 : 유효성 체크 
> _**TIP**_
  
> _**더 공부하면 좋을 포스팅**_
  
>_**Rf**_
- [망나니개발자 님의 [Spring] Dispatcher-Servlet(디스패처 서블릿)이란? 디스패처 서블릿의 개념과 동작 과정](https://mangkyu.tistory.com/18)
- [연로그 님의 [스프링 MVC] 핸들러 매핑, 핸들러 어댑터](https://yeonyeon.tistory.com/112)