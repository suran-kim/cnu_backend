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

<br/>

_**DispatchServlet 장점**_
- web.xml의 역할 축소로 편리한 이용 가능
  - 과거에는 **모든 서블릿**을 URL 매핑하기 위해 web.xml에 전부 등록해야했다.
  - 이제 DispatcherServlet이 해당 어플리케이션으로 들어오는 **모든 요청을 핸들링**해주고 **공통 작업**을 처리한다.
   - **컨트롤러 구현**만 하면 DispatcherServlet이 적절한 컨트롤러에 위임을 해주는 구조.
 
<br/>

_**DispatchServlet Flow**_
- 클라이언트로부터 어떠한 요청이 오면 **서블릿 컨테이너**(Ex: _Tomcat_ )가 요청을 받는다.
- 제일 앞단의 중앙 집중용 컨트롤러(**프론트 컨트롤러 == DispatchServlet**)가 모든 요청을 받는다. 
- 디스패처 서블릿은 **공통적인 작업**을 먼저 처리한 뒤에, 해당 요청을 처리해야 하는 컨트롤러를 찾아서 **작업을 위임**하고 응답을 받아 **뷰**를 생성한다.
- 하위 컨트롤러에서 발생한 예외는 **Front Controller**가 처리한다.

<br/>

알아두자!
- 클라이언트로부터 요청을 받으면 validation체크, 로직 처리, JSP에 모델을 던지거나 MVC에서 중복해서 컨트롤하는 것들을 template화 시켜두고 필요한 부분만 수정해서 사용하는 것이다. 


<br/><br/>

## Spring MVC 처리 흐름



1. DispatcherServlet의 HTTP 요청 접수
2. DispatcherServlet에서 컨트롤러로 HTTP 요청 위임
3. 컨트롤러의 모델 생성과 정보 등록
4. 컨트롤러의 결과 리턴: 모델과 뷰
5. DispatcherServlet의 뷰 호출과 모델 참조
6. HTTP 응답 돌려주기

<br/><br/>



_**자세한 흐름**_

![](https://velog.velcdn.com/images/suran-kim/post/b682d14e-a5f6-48a4-829b-5322e3c98c0e/image.png)


출처 : https://terasolunaorg.github.io/guideline/public_review/Overview/SpringMVCOverview.html


1. DispatcherServlet은 요청을 받는다.
2. DispatcherServlet 요청 처리를 수행하는 Controller의 선택을 HandlerMapping에게 위임한다. HandlerMapping는 요청 URL에 매핑되는 Controller를 선택하고 (Choose Handler）Controller를 DispatcherServlet으로 반환한다. **(핸들러 매핑전략)**
3. DispatcherServlet는 HandlerAdapter에게 Controller의 비즈니스 로직 처리의 실행을 위임한다. **(핸들러 어답터 전략)**
4. HandlerAdapter는 Controller의 비즈니스 로직 처리를 호출한다.
5. Controller는 비즈니스 로직을 실행하고, 반환값으로 Model(뷰에 전달할 목적)을 생성한다. 뷰의 논리 이름은 HandlerAdapter->DispatcherServlet에게 반환한다.
6. DispatcherServlet은 뷰 이름과 model을 ViewResolver에게 전달한다. ViewResolver는 뷰 이름에 매핑되는 View를 DispatcherServlet에게 반환한다.
    - 뷰는 `XML`, `JSON`, `JSP` 등일 수 있다.
7. DispatcherServlet은 반환된 View에 렌더링 처리를 위임한다.
8. View는 Model이 가진 정보를 렌더링하고 응답을 반환한다.
    - 뷰가 직접 반환하는 건 아님
    - DispatcherServlet이 response HTTP 바디부에 응답을 담아 보내는 것. 

<br/>

---

Front Controller에 등록된 다른 컨트롤러를 호출해서 로직 처리를 위임 -> 응답을 받아 뷰 생성

- 우리가 작성하는 것은 서블릿이 아니라 컨트롤러(하위 컨트롤러).
- 스프링이 우리가 구현한 컨트롤러를 호출

<br/><br/>

---





### 1. DispatcherServlet의 HTTP 요청 접수 
- 서블릿 등록 시 web.xml, WebApplicationInitializer(스프링 제공 방법)을 이용할 수 있었다. 
 
- WebApplicationInitializer의 구현체를 사용하면 WebApplicationContext를 만들어서 등록할 수 있다. 
서블릿을 만들어서 연결시켜준다.
패스 경로를 등록시켜준다.(app아래의 요청은 모두 dispatcher서블릿으로 가도록)

- 단 하나의 서블릿
서블릿을 여러 개 만들 수는 있 지 만
스프링은 하나의 서블릿 하위에 컨트롤러를 등록하는 방식으로 어플리케이션을 만들게 해준다.

<br/><br/>

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
    - (HTTP 서블릿 요청의) 매핑 대상 Controller의 메소드에 맞는 파라미터로 변환해서 request를 처리하도록 보낸다(컨트롤러 메소드 호출). 

    

-   _**@RequestMapping**_
    - 애노테이션 기반의 컨트롤러 
    - RequestMappingHandlerMapping (가장 기본적으로 쓰이는 핸들러 매핑)  
    - RequestMappingHandlerAdapter (가장 기본적으로 쓰이는 핸들러 어답터)  

<br/><br/>

### 3. DispatcherServlet의 뷰 호출과 모델 참조

![](https://velog.velcdn.com/images/suran-kim/post/65b778aa-1573-4309-97d2-cc0de3cb9cfc/image.png)
이미지 출처 : https://mossgreen.github.io/Spring-Certification-Spring-MVC/

- 뷰 리졸버 
  - DispatcherServlet 내부에 등록되어 있다.
  - 컨트롤러가 return한 `뷰 이름`에 매칭되는 뷰를 return한다. 
  - chain형태로 되어있어 매칭되는 뷰를 찾을 때까지 체인에서 뷰를 찾는다.
  - 매칭된 뷰에게 DispatcherServlet이 model을 넘긴다.
  - 뷰가 렌더링한 결과값을 DispatcherServlet이 response 바디에 실어 클라이언트에게 응답
   
<br/>

- _알아두자!_
  - 그림에 보이는 InternalResourceViewResolver는 체인에서 가장 하위의 뷰 리졸버.
  - 뷰는 `XML`, `JSON`, `JSP` 등일 수 있는데, InternalResourceViewResolver는 `JSP`를 처리하기 위해 쓰이는 뷰 리졸버.
  - 디폴트 뷰 리졸버는 **ContentNegotiatingViewResolver**이다.


<br/><br/>


## Static Resource 처리 
- Dispatcher Servlet이 요청을 Controller로 넘겨주는 방식은 효율적. 
- 그러나 Dispatcher Servlet이 모든 요청을 처리함 -> 이미지나 HTML/CSS/JavaScript 등과 같은 정적 파일에 대한 요청마저 모두 가로챔
- 정적자원(Static Resources)을 불러오지 못하는 상황 발생

_**해결방법(영역 분리)**_
- 애플리케이션에 대한 요청을 탐색하고 없으면 정적 자원에 대한 요청으로 처리
- Dispatcher Servlet이 요청을 처리할 컨트롤러를 먼저 찾고, 요청에 대한 컨트롤러를 찾을 수 없는 경우에, 2차적으로 설정된 **자원(Resource) 경로**를 탐색하여 자원을 탐색
- 장점
  - 효율적인 리소스 관리를 지원
  - 추후 확장이 용이함

<br/><br/>

---

### resourceHandler

실습 코드
[scr - main - webapp - resources]에 이미지 리소스 추가

![](https://velog.velcdn.com/images/suran-kim/post/c7d721e4-fb42-4265-a33a-5a4e67aa5cec/image.png)


- 스프링에서 정적 리소스를 가져오기(정적 리소스 호스팅) 위한  **resource handler**를 제공
이미지를 resource handler로 다운받아보자.


- @Configuration 클래스에서 addResourceHandlers로 리소스 핸들러 추가 가능


<br/><br/>

_**resourceResolver(중요)**_
- 스프링 4.1부터 추가 
- 디폴트로 설정된 리졸버는 pathResolver
- _resourceChain_
  - resourceChain을 **True**로 설정하면 pathResolver(디폴트)는 최하위에 위치
  - 리소스 요청(registry.addResourceHandler)과 실제 리소스 위치(registry.addResourceLocations)의 정보로부터 특정 resource를 매핑해줄 resolver를 설정 가능
  - Ex) 최신 버전을 찾아주는 resolver, 웹 .jar를 찾아주는 resolver …
_(리졸버가 resource와 매핑할 수 있는 단서는 인코딩 정보 등 다양 )_
```java
    @EnableWebMvc // Spring MVC에 필요한 bean들 자동 생성
    @Configuration
    @ComponentScan(basePackages = "org.prgrms.kdt.customer")
    @EnableTransactionManagement
    static class AppConfig implements WebMvcConfigurer {
		
        …
        
        // ResourceHandler 추가
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/resources/**") // resource에 대한 전체 요청 발생 시
                    .addResourceLocations("/resources/") // resource에 대한 위치 설정 - resources 하위로 매핑
                    .setCachePeriod(60) // 리소스에 캐시 설정 가능
                    .resourceChain(true) // 리졸버 체인 설정 :
                    .addResolver(new EncodedResourceResolver()); // Request Header의 Accept-Encoding 정보를 보고 resource를 gzip형태로 매핑?
        }
```

<br/><br/>

## View 처리 기술

_**알아두자**_
- 백엔드는 API 서버 > 뷰 렌더링(뷰 처리기술)
  - HTML으로 **렌더링 X**
  - JSON(정보의 덩어리 == resource)와 같은 API return
  - JSON를 이용한 렌더링은 react, nodeJ, viewJS, JavaScript 등을 이용해서 처리
  - 뷰 랜더링은 거의 하지 않는 추세. 

<br/><br/>

### JSP

- `jstl` 의존성 필요 
  - java server page standard tag library
  - JSP에서 조건문, 반복문을 태그로 실행 가능
  - 요즘 JSP 많이 안씁니다 ^^ Just good to know 다른 거 공부하세요

- 뷰 처리에 이용하는 비율
  - JSON > thymeleaf 

- URL 매핑은 개별 메소드 단위

<br/><br/>

### Thymeleaf
- `spring-boot-starter-thymeleaf` 의존성 필요

  - java Template 엔진
  - 확장자로 HTML 사용
  - 가독성, 표현식에 있어 JSP보다 우위




![](https://velog.velcdn.com/images/suran-kim/post/9a5af1b0-e0e0-4194-8700-46877a7886e1/image.png)






- view와 jsp를 나누어서 처리하기 위한 디렉토리 분리 

```java
// ApplicationInitializer

    @EnableWebMvc // Spring MVC에 필요한 bean들 자동 생성
    @Configuration
    @ComponentScan(basePackages = "org.prgrms.kdt.customer")
    @EnableTransactionManagement
    static class AppConfig implements WebMvcConfigurer, ApplicationContextAware {
    
        // 특정 viewResolver setup
        @Override
        public void configureViewResolvers(ViewResolverRegistry registry) {
            registry.jsp().viewNames("jsp/*");

            // thymeleafViewResolver
            var springResourceTemplateResolver = new SpringResourceTemplateResolver();
            springResourceTemplateResolver.setApplicationContext(applicationContext); // springResourceTemplateResolver는 ApplicationContext필요
            springResourceTemplateResolver.setPrefix("/WEB-INF/"); // Prefix 설정
            springResourceTemplateResolver.setSuffix(".html"); // Suffix 설정

            var springTemplateEngine = new SpringTemplateEngine(); // TemplateEngine 선언

            springTemplateEngine.setTemplateResolver(springResourceTemplateResolver); // TemplateEngine에 TemplateResolver필요
            var thymeleafViewResolver = new ThymeleafViewResolver();
            thymeleafViewResolver.setTemplateEngine(springTemplateEngine); // thymeleafViewResolver에는 TemplateEngine필요
            thymeleafViewResolver.setOrder(1); // 순서 설정
            thymeleafViewResolver.setViewNames(new String[]{"views/*"}); // 뷰 이름 설정
            registry.viewResolver(thymeleafViewResolver);
        }
   
    	@Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        }
    }
```

```java
// Controller

@Controller
public class CustomerController {
    // 컨트롤러에서 jsp에 접근하기 위한 서비스 주입
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

1. ModelAndView를 리턴하는 방법
//  GET메소드
//  @GetMapping("/customers")
    @RequestMapping(value = "/customers", method = RequestMethod.GET) // URL 매핑은 개별 메소드 단위
    public ModelAndView findCustomers() {
        var allCustomers = customerService.getAllCustomers();

        // 뷰 이름을 views로 줌 -> HTML을 타고 Thymeleaf의 viewresolver를 타게 된다.
        return new ModelAndView("views/customers",
                Map.of("serverTime", LocalDateTime.now(),
                        "customers", allCustomers));
    }
    
2. 모델을 주는 방법
// GET메소드
    @GetMapping("/customers")// URL 매핑은 개별 메소드 단위
    public ModelAndView findCustomers(Model model) {
        var allCustomers = customerService.getAllCustomers();
        model.addAttribute("serverTime", LocalDateTime.now());
        model.addAttribute("customers", allCustomers);

        return new "views/customers";
    }
    
    // 파라미터로 정의해두면 핸들러가 알아서 모델을 넣어준다.
    
}

```

- 여러 개의 메세지 리졸버를 등록하고 뷰 이름에 매핑되는 리졸버를 찾는 방법?


<br/><br/>


_**Thymeleaf Expression**_


  - `${OGNL}` 
    - 변수 식
    - OGNL (Object-Graph Navigation Language) 표현식
    - 객체의 속성에 값을 가져오고 설정하는데 사용
    - context는 `Model`
    - EX)
  	```
    <p>Today is: <span th:text="${today}">13 february 2011</span>.</p>
	
    ==> ctx.getVariable("today");
    ```

  - `#{코드}`
    - 메시지 식

  - `@{링크}` 
  	- 링크 식
    - / 로 시작하는 패스는 자동으로 애플리케이션 컨텍스트 네임이 앞에 붙음
    - EX)
  	```
    <img th:src="@{/resources/image.png}" class="img-fluid">
    ```
  - `*{OGNL}`
    - 선택 변수 식
    - th:object로 선택한 객체에 한해서 필드에 접근
	```
     <tbody>
        <tr th:each="customer: ${customers}" th:object="${customer}" >
            <td th:text="${customer.customerId}"></td>
            <td th:text="${customer.name}"></td>
            <td th:text="${customer.email}"></td>
            <td th:text="${customer.createdAt}"></td>
            <td th:text="${customer.lastLoginAt}"></td>
        </tr>
    </tbody>
    
    -- 선택변수식 적용
    
    <tbody>
        <tr th:each="customer: ${customers}" th:object="${customer}" >
            <td th:text="*{customerId}"></td>
            <td th:text="*{name}"></td>
            <td th:text="*{email}"></td>
            <td th:text="*{createdAt}"></td>
            <td th:text="*{lastLoginAt}"></td>
        </tr>
    </tbody>
    ```



<br/><br/>




> _**오류해결**_
- 뷰 처리 시 HTTP 상태 500 – 내부 서버 오류가 발생
     1.`절대 URI인 [http://java.sun.com/jsp/jstl/core]을(를), web.xml 또는 이 애플리케이션과 함께 배치된 JAR 파일 내에서 찾을 수 없습니다.`
   2.`Request processing failed; nested exception is org.springframework.jdbc.CannotGetJdbcConnectionException: Failed to obtain JDBC Connection; nested exception is com.mysql.cj.jdbc.exceptions.CommunicationsException: Communications link failure`
   _**해결**_  @Controller에서 생성한 GET메소드의 return값인 ModelAndView key값 오타. 
   ccustomer(x) -> customer(o)
  
> _**새로 알게된 용어**_
  - validation 체크 : 유효성 체크 
  - _**정적 리소스(Static Resource)**_
  클라이언트로 부터 요청이 들어왔을 때 요청에 대한 리소스가 이미 만들어져 있는 것.


> _**TIP**_
- `gzip --keep --best -r src/main/webapp/resources` 
gzip으로 파일을 압축해주는 gzip 명령어를 윈도우 환경에서 사용하려면 intellij의 터미널 설정을 git shell로 바꿔주어야 한다. 👉 [git shell로 터미널 바꾸는 방법](https://dev-coco.tistory.com/79)
  
> _**더 공부하면 좋을 포스팅**_
  
>_**Rf**_
- [망나니개발자 님의 [Spring] Dispatcher-Servlet(디스패처 서블릿)이란? 디스패처 서블릿의 개념과 동작 과정](https://mangkyu.tistory.com/18)
- [연로그 님의 [스프링 MVC] 핸들러 매핑, 핸들러 어댑터](https://yeonyeon.tistory.com/112)
- [2.2. Spring MVC 아키텍처 개요 ](https://terasolunaorg.github.io/guideline/public_review/Overview/SpringMVCOverview.html)
- [Moss GU 님의 Spring MVC in Spring Professional Certification](https://mossgreen.github.io/Spring-Certification-Spring-MVC/)