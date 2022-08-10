>_본 포스팅은 프로그래머스 미니 데브 코스를 공부하며 
학습을 기록하기 위한 목적으로 작성된 글입니다._


데이터 입력 페이지 제작
스프링이 웹 어플리케이션을 어떻게 만들까?

# Spring MVC


## Spring MVC 폼 처리

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
```java



```
```java



```

## WebApplicationContext
![](https://velog.velcdn.com/images/suran-kim/post/5efaabd3-0392-446b-bcfc-819083b0186a/image.png)
출처 : https://nesoy.github.io/articles/2019-02/Servlet
![](https://velog.velcdn.com/images/suran-kim/post/4fb86dbf-c291-4001-a6a9-fab195abbe72/image.png)
출처 :  https://stackoverflow.com/questions/4223564/servletconfig-vs-servletcontext


![](https://velog.velcdn.com/images/suran-kim/post/f43da8b1-840a-4ecf-870b-35f456e97d48/image.png)
출처 : https://blog.csdn.net/demon7552003/article/details/103603877


- 웹 어플리케이션이 시작될 때 (**ServletContext**가 생성될 때) **rootApplicationContext**가 생성된다. 

### ApplicationContext

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

### WebApplicationContext

![](https://velog.velcdn.com/images/suran-kim/post/1047d4c2-e81c-41bb-bdd6-1a9c1110ee23/image.png)
이미지 출처 : https://www.oreilly.com/library/view/head-first-servlets/9780596516680/ch05s10.html

- _**WebApplicationContext**_
    - Servlet 단위의 ApplicationContext
    - WebApplicationContext = `applicationContext` + `servletContext 접근기능 추가`
      - _WebApplicationContext_ implements _applicationContext_
      - WebApplicationContext는 ApplicationContext에 getServletContext() 메서드가 추가된 인터페이스. 
        - getServletContext() 메서드 
       : 호출 시 서블릿 컨텍스트를 반환
    - **DispatcherServlet**이 직접 사용하는 `컨트롤러`를 포함한 `웹 관련 빈`을 등록하는 데 사용된다.
    - **DispatcherServlet**은 독자적인 WebApplicationContext를 가지고 있고, 모두 동일한 Root WebApplicationContext를 공유한다. (그림참조)
    




### RootApplicationContext와 WebApplicationContext 

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
  
  
### 동작 방식

 
  
  
### 추가 정리

- 웹 어플리케이션이 시작될 때 (**ServletContext**가 생성될 때) **rootApplicationContext**가 생성된다.
    - rootApplicationContext은 setAttribute()를 통해 servletContext에 들어간다.
    - DispatcherServlet들이 servletContext에 접근해서 rootApplicationContext를 가져온다.
    - DispatcherServlet 내부의 ApplicationContext와 부모-자식 관계 형성


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



# REST API




   
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
[심드류 카네기 님의 [Spring] ContextLoaderListener 란? RootApplicationContext과 WebApplicationContext란?](https://tlatmsrud.tistory.com/43)
[bruteforce 님의 Spring Web MVC에서 사용하는 context들](https://live-everyday.tistory.com/164)
[KoB 님의 [Spring] ApplicationContext와 WebApplicationContext](https://kingofbackend.tistory.com/78)
[ykkkk님의 ServletContext, ApplicationContext, WebApplicationContext](https://cyk0825.tistory.com/m/78)
[yshjft 님의 2022년 4월 20일 TIL](https://velog.io/@yshjft/2022%EB%85%84-4%EC%9B%94-20%EC%9D%BC-TIL)
[망나니 개발자님의 [Spring] 애플리케이션 컨텍스트(Application Context)와 스프링의 싱글톤(Singleton)](https://mangkyu.tistory.com/category/Java)