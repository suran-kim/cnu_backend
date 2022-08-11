>_본 포스팅은 프로그래머스 미니 데브 코스를 공부하며 
학습을 기록하기 위한 목적으로 작성된 글입니다._


데이터 입력 페이지 제작
스프링이 웹 어플리케이션을 어떻게 만들까?

# Spring MVC


## 🍃 Spring MVC 폼 처리

- HTML의 폼은 부트스트랩을 이용해 처리할 수 있다.
![](https://velog.velcdn.com/images/suran-kim/post/9abfdcbd-75e3-46b2-96de-93b49b6b0837/image.png)

- Form 데이터로 받은 것을 Controller로 받는 POST 메소드 생성
- 도메인 모델에 접근하기 위한 서비스


- 클라이언트로부터 컨트롤러가 HTTP 데이터를 받아온다. 
- **컨트롤러**가 요청에 대한 핸들링을 한다. 
서비스들에 접근해서 서비스를 사용한다.
- 실제 도메인로직은 엔티티와 서비스에서 진행

```java
// Controller

@Controller
public class CustomerController {
    // 컨트롤러에서 jsp에 접근하기 위한 서비스 주입
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
    
    // GET메소드
    @GetMapping("/customers/new")
    public String viewNewCustomerPage() {
        return "views/new-customers"; // resources - viesws 폴더 아래에 html파일 생성
    }
    
    // POST메소드
    @PostMapping("/customers/new") // URL은 같지만 HTTP 메소드는 다르다.
        public String addNewCustomer(CreateCustomerRequest createCustomerRequest) { // Form data 매핑을 위한 클래스 생성 customerService.createCustomer(createCustomerRequest.email(), createCustomerRequest.name());
        customerService.createCustomer(createCustomerRequest.email(), createCustomerRequest.name()); // customer생성은 customerService가 한다.(controller가 하지 않음) CreateCustomerRequest는 일종의 DTO.
        return "redirect:/customers"; // 상세페이지 접속하거나 customers페이지 리다이렉트 가능
    }
}   
    
```
```java
// CustomerSevice

public interface CustomerService {
    void createCustomers(List<Customer> customers);

    Customer createCustomer(String email, String name);

    List<Customer> getAllCustomers();
}

```
```java
// CustomerServiceImpl

@Service
public class CustomerServiceImpl implements CustomerService {
	
    …
    
    @Override
    public Customer createCustomer(String email, String name) {
        var customer = new Customer(UUID.randomUUID(), name, email, LocalDateTime.now());
        return customerRepository.insert(customer);
    }
}
```
```java
// CreateCustomerRequest

// email과 name이 일치하면 form data를 객체화 시켜준다.
public record CreateCustomerRequest(String email, String name) {
}
```




_상세페이지 접속 기능 구현 (과제)_
- 컨트롤러와 URL 매핑 
```java
// Controller

@Controller
public class CustomerController {
	…
    // Controller에 매핑하는 메소드
    @GetMapping("/customers/{customerId}") // URL 패스 일부분을 변수화.
    public String findCustomer(@PathVariable("customerId") UUID customerId, Model model){ // @PathVariable으로 매개변수와 customerId 매핑. 이때 형변환 발생. 문자열을 UUID로 자동변환 -> 실패 시 에러
        var maybeCustomer = customerService.getCustomer(customerId);
        if (maybeCustomer.isPresent()) {
            model.addAttribute("customer", maybeCustomer.get()); // model 전달 ( customer 어트리뷰트 이용 -> DB에서 가져온 정보를 템플릿 작성 -> 뷰 출력 )
            return "views/customer-details"; // view
        } else {
            return "views/404";
        }
    }
}    
```


## 🍃 WebApplicationContext
![](https://velog.velcdn.com/images/suran-kim/post/5efaabd3-0392-446b-bcfc-819083b0186a/image.png)
출처 : https://nesoy.github.io/articles/2019-02/Servlet
![](https://velog.velcdn.com/images/suran-kim/post/4fb86dbf-c291-4001-a6a9-fab195abbe72/image.png)
출처 :  https://stackoverflow.com/questions/4223564/servletconfig-vs-servletcontext


![](https://velog.velcdn.com/images/suran-kim/post/f43da8b1-840a-4ecf-870b-35f456e97d48/image.png)
출처 : https://blog.csdn.net/demon7552003/article/details/103603877


- 웹 어플리케이션이 시작될 때 (**ServletContext**가 생성될 때) **rootApplicationContext**가 생성된다. 

### 👉 ApplicationContext

- _**IOC Container (Spring Container)**_
 
   - _**Bean Factory**_ :
     - DI관점
     - 빈(오브젝트)의 생성과 빈의 관계설정 제어 

   
- _**ApplicationContext**_
  - Bean Factory를 상속받아 확장
    - 사실 스프링 컨테이너는 DI 작업보다 더 많은 일을 함.
    - 이에 필요한 여러가지 컨테이너 기능을 추가
    - 별도의 **설정 정보**를 참고하고 IoC를 적용하여 빈의 생성, 관계설정 등의 제어 작업을 총괄
    - 직접 오브젝트를 생성하고 관계를 맺어주는 코드가 없고, 
    그런 생성 정보와 연관관계 정보에 대한 **설정**을 읽어 처리. 
    - Ex) 대표적인 IoC의 설정정보 :     _**@Configuration**과 같은 어노테이션_

  - 스프링이 관리하는 빈들이 담겨 있는 컨테이너.
    - 스프링 안에는 여러 종류의 ApplicationContext 구현체가 존재. 
    - 이들은 모두 `ApplicationContext 인터페이스`를 구현한 객체들
    - WebApplicationContext는 ApplicationContext를 확장한  WebApplicationContext 인터페이스의 구현체. 
    - WebApplicationContext는 스프링 ApplicationContext의 변종이면서 서블릿 컨텍스트와 연관 관계에 있다
  - ApplicationContext는 인터페이스 
    - 실제 스프링 컨테이너는 이 ApplicationContext를 구현한 구현체를 뜻한다.
    - Ex)
       - annotation기반 AnnotationConfigApplicationContext
       - xml 기반
       - groovy 기반 …

- IoC 컨테이너의 역할은 초기에 Bean  객체를 생성하고 DI한 후 최초로 애플리케이션을 기동할 Bean을 제공해주는 것까지이다.

### 👉 WebApplicationContext

![](https://velog.velcdn.com/images/suran-kim/post/1047d4c2-e81c-41bb-bdd6-1a9c1110ee23/image.png)
이미지 출처 : https://www.oreilly.com/library/view/head-first-servlets/9780596516680/ch05s10.html


- 웹 어플리케이션이 시작될 때 (**ServletContext**가 생성될 때) **rootApplicationContext**가 생성된다.
  - rootApplicationContext은 모든 ApplicationContext에 접근 가능하다.
      - rootApplicationContext은 setAttribute()를 통해 servletContext에 들어간다.
      - DispatcherServlet들이 servletContext에 접근해서 rootApplicationContext를 가져온다.
      - DispatcherServlet 내부의 ApplicationContext와 부모-자식 관계 형성
    

<br/>

- _**WebApplicationContext**_
    - Servlet 단위의 ApplicationContext
    - WebApplicationContext = `applicationContext` + `servletContext 접근기능 추가`
      - _WebApplicationContext_ implements _applicationContext_
      - WebApplicationContext는 ApplicationContext에 getServletContext() 메서드가 추가된 인터페이스. 
        - getServletContext() 메서드 
       : 호출 시 서블릿 컨텍스트를 반환
    - **DispatcherServlet**이 직접 사용하는 `컨트롤러`를 포함한 `웹 관련 빈`을 등록하는 데 사용된다.
    - **DispatcherServlet**은 독자적인 WebApplicationContext를 가지고 있고, 모두 동일한 Root WebApplicationContext를 공유한다. (그림참조)
    

- rootApplicationContext
  - 이 모든 Servlet에서 만들어진 ApplicationContext에 접근할 수 있는 Bean?


### 👉 RootApplicationContext와 WebApplicationContext 

![](https://velog.velcdn.com/images/suran-kim/post/c221e245-248a-4ad8-9e5a-9c981d37e172/image.png)
출처 : https://howtodoinjava.com/spring-mvc/contextloaderlistener-vs-dispatcherservlet/


- RootApplicationContext
  - 최상위 ApplicationContext이다.
WebApplicationContext의 부모 Context이며 자식에게 자신의 설정을 공유한다. 
단, 자신은 자식인 WebApplicationContext의 설정에 접근하지 못한다.
  - root-context에 등록되는 빈들은 모든 컨텍스트에서 사용할 수 있다. (공유 가능)
  - service나 dao를 포함한, 웹 환경에 독립적인 빈들을 담아둔다.
  - 서로 다른 servlet-context에서 공유해야 하는 빈들을 등록해놓고 사용할 수 있다.
  - servlet-context 내 빈들을 이용할 수 없다.

 

- WebApplicationContext
  - Servlet 단위의 ApplicationContext이다. 
  RootApplicationContext의 자식 Context이며, 부모인 RootApplicationContext의 설정에 접근할 수 있다.
  - servlet-context에 등록되는 빈들은 해당 컨텍스트에서만 사용할 수 있다.
  - DispatcherServlet이 직접 사용하는 컨트롤러를 포함한 웹 관련 빈을 등록하는 데 사용한다.
독자적인 컨텍스트들을 가지며, root-context 내 빈 사용이 가능하다.
 

![](https://velog.velcdn.com/images/suran-kim/post/77989571-5efc-4273-90b9-913e1a864e61/image.png)
이미지 출처 : https://velog.io/@dongeranguk/DispatcherServlet-%EC%84%A4%EC%A0%95
   
 ![](https://velog.velcdn.com/images/suran-kim/post/787b5b4e-6ec7-43f4-8a3f-984475dc8300/image.png)
  
  
### 👉 RootApplicationContext 생성 과정


![](https://velog.velcdn.com/images/suran-kim/post/5ae26e18-6689-4ea3-8518-00c26f169579/image.png)

- _**ContextLoaderListener**_
  - ServletContextListener를 구현
    - Servlet 상태가 변경될 때 호출되는 Listener
  - WebApplication 전체에서 사용 가능한 WebApplicationContext**(rootApplicationContext)** 생성. 
    - ServletContext에 접근가능 유무

**_1. Web.xml_**

**_2. 코드 기반_**
  
### 👉 동작 방식
웹 환경에서 Spring Application이 동작하는 방식을 살펴보자.

 ![](https://velog.velcdn.com/images/suran-kim/post/66bb02f7-365d-48b1-9272-76c338026617/image.png)

- 서블릿 컨테이너 안에 **웹 어플리케이션**이 만들어진다.
  - **서블릿**, **서블릿 컨텍스트** 존재
  - WebApplicationContext 생성

- DispatcherServlet은 
  - 요청을 받으면 WebApplicationContext에서 등록된 bean들을 가져온다.
  - WebApplicationContext에 **Controller**가 있으면 요청을 위임한다.

- POJO bean 오브젝트
  - 어댑터, 핸들러 등 생성
  
- ApplicationContext.xml  
  - **bean 생성을 위한 메타정보**
  - 과거에는 XML이었지만 현재는 config 메타를 자바로도 생성 가능

- web.xml
  - 서블릿을 만들기 위한 설정 정보
  - 현재 자바코드로도 생성 가능
  
![](https://velog.velcdn.com/images/suran-kim/post/cd4fb532-e316-493c-8929-062521463c67/image.png)

- 프레젠테이션 계층
  - 사용자의 요청에 대한 처리
  - 외부와 인터페이스
  
- 서비스 계층
  - 실제 비즈니스 로직 담당
  
- 데이터 액세스 계층
  - DAO나 **레포지토리**를 이용해 DB에 접근
  - 현재는 레포지토리를 많이 사용


- SpringMVC
![](https://velog.velcdn.com/images/suran-kim/post/2befa431-f1c3-488c-be9f-399a929cbc8d/image.png)
  - SpringMVC모듈은 프레젠테이션 계층에 이용된다.


- ApplicationContext 기준으로 본다면?
![](https://velog.velcdn.com/images/suran-kim/post/5c9cab3d-ee66-48bb-8f84-dff5fb018d1b/image.png)

 
- DispatcherServlet
  - DispatcherServlet은 여러 개 만들어질 수 있다.



- WebApplicationContext  (serveletApplicationContext) 
  - Controller는 해당하는 DispatcherServlet에 매핑되어져 있다.
  - Servlet에서 사용되는 Controller는 DispatcherServlet를 만들 때 전달한 WebApplicationContext(serveletApplicationContext) 라는 IoCcontainer에 등록되어 있다.
  - DispatcherServlet이 사용하는 bean들이 **전부** 등록
    - 컨트롤러
    - 리졸버
    - 핸들러
    - 어댑터
    - 메세지 컨버터 …
   - LoadOnStartUp옵션 `-1`으로 설정 가능
     - WebApplicationContext을 특정 URL 요청이 왔을 때 시작
     - `장점` : 초반에 요청이 적을 때 서버를 빠르게 띄울 수 있다.



- root applicationContext 
   - 여러  Servlet에서 공통으로 사용되는 영역
   - 하나가 생성된다.
   - root applicationContext 밑으로 자식 applicationContext인 serveletApplicationContext가 생성된다.

![](https://velog.velcdn.com/images/suran-kim/post/292e3803-10c8-44be-bbbb-fe376723c1e6/image.png)

이미지 출처 : https://www.definejava.net/11

### 👉 코드

```java
// WebApplicationInitializer으로 서버등록
public class KdtWebApplicationInitializer implements WebApplicationInitializer {
    private static final Logger logger = LoggerFactory.getLogger(KdtWebApplicationInitializer.class);

    // WebapplicationContext(servletApplicationContext) 설정
    @Configuration
    @EnableWebMvc // Spring MVC에 필요한 bean들 자동 생성
    @ComponentScan(basePackages = "org.prgrms.kdt.customer",
      includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = CustomerController.class), // CustomerController만 assign되도록 설정
      useDefaultFilters = false  // 컴포넌트 스캔 시 다른 struct타입으로 annotation한 클래스들이 등록되는 것 방지
    )
    @EnableTransactionManagement
    static class ServletConfig implements WebMvcConfigurer, ApplicationContextAware { … }
    }

    //root applicationContext 설정
    @Configuration
    @ComponentScan(basePackages = "org.prgrms.kdt.customer",
            excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = CustomerController.class) // CustomerController가 아닌 것만 assign되도록 설정
    )
    @EnableTransactionManagement
    static class RootConfig{ … } // webMVC 관련 설정 불필요. AppConfig에서 webMVC설정 제외한 설정 복사해오기

	// onStartup 메소드
    @Override
    public void onStartup(ServletContext servletContext) {
        logger.info("Starting Server...");

        // rootApplicationContext 생성
        var rootApplicationContext = new AnnotationConfigWebApplicationContext();
        rootApplicationContext.register(RootConfig.class); // WebApplicationContext의 Bean 생성을 위한 메타데이터(설정정보) 전달
        var loaderListener = new ContextLoaderListener(rootApplicationContext);
        servletContext.addListener(loaderListener);


        // ServletApplicationContext 생성
        var applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(ServletConfig.class);
        var dispatcherServlet = new DispatcherServlet(applicationContext);// DispatcherServlet 쓰려면 WebApplicationContext 필요
        var servletRegistration = servletContext.addServlet("test", dispatcherServlet);// 테스트 서블릿을 추가할 수 있다.
        servletRegistration.addMapping("/"); // 서블릿 매핑 시 넣었던 URL 패턴 입력
        servletRegistration.setLoadOnStartup(-1);  // setLoadOnStartup 옵션 -1으로 줘서 초기화
    }
}

// 요즘은 컨테이너 자체를 서버로 분리하고 코드베이스도 분리시키는 형태
```



### 👉 추가 정리




- servlet container
  - Web container
  - TOMCAT
  - 서블릿을 관리하는 곳
  - servletContext 생성
  - 웹 환경에서는 main() 메서드 역할을  서블릿 컨테이너가 한다. 
    - 웹 애플리케이션은 동작하는 방식이 다르다. 
      - 독립형 자바 프로그램은 자바 Vm에게 main() 메서드를 가진 클래스의 시작을 요청할 수 있지만 웹에서는 main() 메서드를 호출할 방법이 없다.
      - 브라우저로부터 오는 HTTP 요청을 받음 -> 해당 요청에 매핑되어 있는 서블릿을 실행
      - 서블릿이 일종의 main()의 역할.


- servletContext
  - 하나의 서블릿이 서블릿 컨테이너와 통신하기 위해서 사용되어지는 메서드들을 가지고 있는 클래스
  - 하나의 web application 내에 하나의 servletContext 존재. 
  - web application내에 있는 모든 서블릿들을 **관리**하고, 서블릿끼리 **정보공유의 매개**역할.
    - 개별 서블릿 컨테이너 생성 시 servletContext에 전체 서블릿이 사용가능한 정보가 담긴다.
    - 여러 Servlet에서 접근 가능 == 여러 **DispatcherServlet**에서 접근 가능
  - 서블릿 컨테이너에서 만들어진다

 
 
- DispatcherServlet은 WebApplicationContext를 이용하여 자신을 설정한다.
  - DispatcherServlet도 결국 Servlet이다. 따라서 다른 Servlet과 마찬가지로 자바 설정이나 web.xml에 있는 설정에 따라 정의되어야 하고 매핑되어야 한다. 












# REST(ful) API

REST를 모두 충족하면 RESTful API이다.

- API(Application Programming Interface)
  - 정의 및 프로토콜 집합을 사용하여 두 소프트웨어 구성 요소가 서로 통신할 수 있게 하는 메커니즘
  - 인터페이스는 두 애플리케이션 간의 서비스 계약
    - 이 계약은 요청과 응답을 사용하여 **두 애플리케이션이 서로 통신하는 방법**을 정의한다.
  - API 문서에는 개발자가 이러한 **요청과 응답을 구성하는 방법**에 대한 정보가 들어있다.
  - 다양한 소프트웨어 컴포넌트들과 통신하기 위해 정의된 방법들
  - 통신하기 위해서는 통신 규약(프로토콜)이 필요하다.

- REST(Representational State Transfer)
  - 월드 와이드 웹과 같은 분산 하이퍼미디어 시스템을 위한 **소프트웨어 아키텍처**의 한 형식
  - REST는 클라이언트가 서버 데이터에 액세스하는 데 사용할 수 있는 GET, PUT, DELETE 등의 함수 집합을 정의한다.
  - 클라이언트와 서버는 **HTTP**를 사용하여 데이터를 교환한다.
  - 엄격한 의미 
    - REST는 네트워크 아키텍처 원리의 모음.
    - '네트워크 아키텍처 원리'란 **자원을 정의하고 자원에 대한 주소를 지정**하는 방법
  - 간단한 의미
    - 웹 상의 자료를 **HTTP**위에서 별도의 **전송 계층**(**SOAP**이나 쿠키를 통한 세션 트랙킹) 없이 전송하기 위한 아주 간단한 인터페이스를 말함
  - REST에서는 자원(resource)에 접근할 때 URL로 접근한다. URL은 자원의 위치를 나타내는 일종의 식별자이다.  
   



- SOAP API 
  - 이 API는 단순 객체 접근 프로토콜을 사용한다. 
  - 클라이언트와 서버는 `XML`을 사용하여 메시지를 교환한다. 
  - 과거에 더 많이 사용되었으며 유연성이 떨어지는 API.


- REST API
  - 오늘날 웹에서 볼 수 있는 가장 많이 사용되고 유연한 API. 
  - 클라이언트가 서버에 요청을 `데이터`로 전송한다. 
  - 서버가 클라이언트 입력(데이터)을 사용하여 내부 함수를 시작하고, 출력 데이터를 다시 클라이언트에 반환한다.
  - API의 프로토콜이 REST로 정의된 것이 REST API

- Endpoint
  - API가 서버의 특정 리소스에 대한 요청을 받는 위치
  - 같은 URL들에 대해서도 메소드가 다른 요청을 하게끔 구별하게 해주는 항목이 바로 'Endpoint'이다.
  - API가 서버에서 자원(resource)에 접근할 수 있도록 하는 URL
  - 일반적으로 API에서 Endpoint는 서버에서 리소스의 위치를 제공하는 URL(Uniform Resource Locator)이다.
 
##  REST 아키텍쳐 스타일

- 클라이언트-서버
- 스테이트리스
- 캐시
- 균일한 인터페이스
- 계층화된 시스템

출처: https://www.ics.uci.edu/~fielding/pubs/dissertation/rest_arch_style.htm
 
- 클라이언트-서버 (client-server)
  - 사용자 인터페이스에 대한 관심을 데이터 저장에 대한 관심으로부터 분리함으로써 클라이언트의 이식성과 서버의 규모확정성을 개선한다.


- 스테이트리스 (stateless)
  - 서버가 요청 간에 클라이언트 데이터를 저장하지 않는다.
  - 클라이언트 서버의 통신에 상태가 없다. 
  - 모든 요청에 필요한 모든 정보가 담겨 있어 가시성이 좋다.
  - 요청 실패시 복원이 쉽기 때문에 신뢰성이 좋다. 
  - 상태를 저장할 필요가 없어 규모 확장성이 개선된다.


- 캐시 (cache) 
  -캐시가 가능해야 한다. HTTP가 가진 캐싱 기능이 적용 가능하다. HTTP 프로토콜 표준에서 사용하는 Last-Modified태그나 E-Tag를 이용하면 캐싱 구현이 가능하다.
  

- _**⭐균일한 인터페이스 (uniform interface)⭐**_
  - URI로 지정한 리소스에 대한 조작을 통일되고 한정적인 인터페이스로 수행하는 아키텍처 스타일을 말한다.
    - Richardson Maturity Model


- 계층화된 시스템 (layered system)
  - REST 서버는 다중 계층으로 구성될 수 있으며 보안, 로드 밸런싱, 암호화 계층을 추가해 구조상의 유연성을 둘 수 있고 PROXY, 게이트웨이 같은 네트워크 기반의 중간매체를 사용할 수 있게 한다.
 
 

## Richardson Maturity Model
REST인지 아닌지 판별해주는 모델

![](https://velog.velcdn.com/images/suran-kim/post/9e57eb23-d55f-4eb5-af53-23ecedeeb287/image.png)
이미지 출처 : https://martinfowler.com/articles/richardsonMaturityModel.html


- _**Level0**_
  - 리소스 개념이 없다.
  - SOAP API
    - 일종의 remote procedure call
      - 클라이언트가 서버에서 함수나 프로시저를 완료하면 서버가 출력을 클라이언트로 다시 전송
      - remote procedure이 외부에 있는 메소드 호출
      - URL과 end Point가 **하나**만 존재
      - 요청을 `xml`로 정의 (응답도 `xml`)
      - 무조건 `POST`
  - SOAP 기반으로 만든 HTTP API   
  - HTTP사용에 의의가 있다.    

- _**Level1**_
  - 리소스 개념이 생김
  - 리소스 단위로 여러개의 end Point 존재.
  - 리소스 중심 설계(리소스 단위 URL 설계)


- _**Level2**_
   - HTTP 동사 지원
     - 적절한 HTTP 동사 사용 
   - **Representations** 지원
     - 어떠한 리소스의 특정 시점의 상태를 반영하고 있는 정보 
     - **representation data**와 **representation metadata**로 구성
     - 하나의 정보를 **다양한 형태**로 표현 가능
        - _EX) 예시_
        >  - representation data
             _hello_
          - representation metadata
            _Content-Type: text/json // 콘텐트 타입 설정 가능
            Content-Language: ko // 콘텐트 랭귀지 설정 가능_  




- Level3
   - **HATEOAS** (Hypermedia as the Engine of Application State (hey-dee-us))
   - REST API에 가장 근접하다.
   - 리소스의 연결성
     - `links필드` - 리소스로 할 수 있는 행위를 기술해준다.
   - URL로 균일한 인터페이스 제공
     








   
> _**오류해결**_
  
> _**새로 알게된 용어**_
- DTO(Data Transfer Ovject) == VO
계층(컨트롤러, 뷰, 비즈니스, 퍼시스턴스 등) 간 데이터 전달에 사용하는 데이터 객체
참고자료 : https://genesis8.tistory.com/214
- Attribute 
개체가 가지는 속성

> _**TIP**_
  
> _**더 공부하면 좋을 포스팅**_

  
  
>_**Rf**_
- [심드류 카네기 님의 [Spring] ContextLoaderListener 란? RootApplicationContext과 WebApplicationContext란?](https://tlatmsrud.tistory.com/43)
- [bruteforce 님의 Spring Web MVC에서 사용하는 context들](https://live-everyday.tistory.com/164)
- [KoB 님의 [Spring] ApplicationContext와 WebApplicationContext](https://kingofbackend.tistory.com/78)
- [ykkkk님의 ServletContext, ApplicationContext, WebApplicationContext](https://cyk0825.tistory.com/m/78)
- [yshjft 님의 2022년 4월 20일 TIL](https://velog.io/@yshjft/2022%EB%85%84-4%EC%9B%94-20%EC%9D%BC-TIL)
- [망나니 개발자님의 [Spring] 애플리케이션 컨텍스트(Application Context)와 스프링의 싱글톤(Singleton)](https://mangkyu.tistory.com/category/Java)
- [AWS 공식 사이트 - API란 무엇인가요?](https://aws.amazon.com/ko/what-is/api/)
- [[Web] API 그리고 EndPoint](https://velog.io/@kho5420/Web-API-%EA%B7%B8%EB%A6%AC%EA%B3%A0-EndPoint)
- [What Is an API Endpoint? (And Why Are They So Important?)](https://blog.hubspot.com/website/api-endpoint)