>_본 포스팅은 프로그래머스 미니 데브 코스를 공부하며 
학습을 기록하기 위한 목적으로 작성된 글입니다._


데이터 입력 페이지 제작
스프링이 웹 어플리케이션을 어떻게 만들까?

# Spring MVC


## Spring MVC 폼 처리

- HTML의 폼은 부트스트랩을 이용해 처리할 수 있다.
![](https://velog.velcdn.com/images/suran-kim/post/9abfdcbd-75e3-46b2-96de-93b49b6b0837/image.png)

- Form 데이터로 받은 것을 Controller로 받는 POST 메소드 생성
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
    public String addNewCustomer(CreateCustomerRequest createCustomerRequest) // Form data 매핑을 위한 클래스 생성
}    
    
```
```java
// CustomerSevice



```

```java
// CreateCustomerRequest


```


# REST API