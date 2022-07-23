> _김영한의 스프링 입문 - 코드로 배우는 스프링 부트, 웹 MVC, DB 접근 기술 강의를 공부하며 작성한 글입니다._


# 개요

스프링 웹 개발 방식은 3가지로 분류할 수 있다.

1. 정적 컨텐츠
	- 서버에서 **파일을 그대로** 웹브라우저에 내려주는 것


2. MVC와 템플릿 엔진
	- 서버에서 프로그래밍을 통해 정적 컨텐츠(ex: HTML)를 동적으로 바꿔 내려주는 것
	- MVC 
		-  **모델, 뷰, 컨트롤러**
		- 최근에는 MVC패턴으로 개발하는 경우가 많다. 


3. API
	- 데이터를 직접 전달하는 방식
    - 서버끼리 통신할 때 사용 (서버끼리는 HTML 불필요)
	- JSON 포맷을 통해 클라이언트에게 파일 전달 


<br/>
<br/>

---

# 실습 프로젝트 트리구조
```
└─src
    ├─main
    │  ├─java
    │  │  └─com
    │  │      └─example
    │  │          └─hello_spring
    │  │              │  helloSpringApplication.java
    │  │              │
    │  │              └─Controller
    │  │                      HelloController.java
    │  │
    │  └─resources
    │      │  application.properties
    │      │
    │      ├─static
    │      │      hello-static.html
    │      │      index.html
    │      │
    │      └─templates
    │              hello-template.html
    │              hello.html
    │
    └─test
        └─java
            └─com
                └─example
                    └─hello_spring
                            helloSpringApplicationTests.java


```



<br/>
<br/>

---

# 정적 컨텐츠
스프링 공식 문서에서는 static을 `static 디렉토리`에서 제공한다고 적혀있다. [📌 스프링 공식문서 링크](https://docs.spring.io/spring-boot/docs/current/reference/html/web.html#web.servlet.spring-mvc.static-content)


![스프링 공식문서](https://velog.velcdn.com/images/suran-kim/post/74ed80c5-f207-4761-9b96-4b5676eb0f1a/image.png)


<br/>
<br/>

## 실습

**_hello-static.html_**
```html
<!DOCTYPE html>
<html>
<head>
  <title>static content</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
정적 컨텐츠입니다.
</body>
</html>
```

`static 폴더`에 `hello-static.html` 파일을 생성한다.


![](https://velog.velcdn.com/images/suran-kim/post/21b141b0-fa9d-4474-ad3c-84da925e42ad/image.png)

정적 컨텐츠는 `localhost:8080/hello-static.html`이라는 파일경로를 적어줘야 웹브라우저에서 확인이 가능하다.

정적 컨텐츠는 넣어준 **정적 파일 그대로 웹브라우저에서 반환**이 된다.
그래서 동적 프로그래밍을 할 수는 없다.



<br/>
<br/>




## 원리

![](https://velog.velcdn.com/images/suran-kim/post/76bfa2fc-225c-483e-be5b-6b5851ec61e0/image.png)

									(강의 교안의 참고자료를 직접 따라 그렸습니다.)

1. 웹브라우저에서 `localhost:8080/hello-static.html`을 입력하면 `내장 톰켓 서버`에서 요청을 받고 `스프링 컨테이너`에게 넘긴다.
2. 스프링 컨테이너는 `Controller`를 우선 살펴본다. `hello-static` 와 관련(Mapping) 된  Controller가 없다면 다음 단계로 넘어간다.
3. 스프링 컨테이너는 `resources 폴더` 내부에 있는 `static/hello-static.html`을 찾는다. 있다면 웹브라우저에 반환한다.



<br/>
<br/>




**_hello-static.html_**
![](https://velog.velcdn.com/images/suran-kim/post/634f689a-35b3-495c-8540-d2b31e4fc39e/image.png)

**_웹브라우저에서의 hello-static.html_**
![](https://velog.velcdn.com/images/suran-kim/post/8043c008-ca68-465f-bd74-eb6c676bc9f7/image.png)

두 코드가 동일한 것을 확인 가능.




<br/>
<br/>



---

# MVC와 템플릿 엔진

예전 Model과 View가 분리되어 있지 않던 개발 방식(Model1 방식)과 달리 
요즘에는 MVC 패턴을 이용한 개발이 일반적이다.

_**Model**_
- 화면에 필요한 것을 담는 그릇
- Controller로부터 담겨서 View에게 전달됨


_**Controller**_
- 비즈니스 로직과 관련
- 내부적인 처리 
- 서버단 관련 처리(?)

_**View**_
- 화면과 관련된 일





<br/><br/>

## 실습

_**HelloController.java**_
```java
@Controller
public class HelloController {

    @GetMapping
    public String hello(Model model) {
        model.addAttribute("data", "반가워요?!");
        return "hello";
    }

    @GetMapping("hello-mvc")
    public String helloMvc(@RequestParam("name") String name, Model model){
        model.addAttribute("name", name);
        return "hello-template";
    }

}

```

_**hello-template.html**_
```html
<html xmlns:th="http://www.thymeleaf.org">
<body>
<p th:text="'hello ' + ${name}">hello! world</p>
</body>
</html>
```

- ${키값이름}은 model에서 값을 꺼낸다는 뜻이다.
- template 디렉토리 아래의 html 파일은 `뷰`이다.




<br/>
<br/>




웹브라우저에 Controller에서 정의한 
`localhost:8080/hello-mvc` 을 입력해본다.


![](https://velog.velcdn.com/images/suran-kim/post/d6720140-9c66-4ac2-85dc-094f18bfdcc4/image.png)
에러가 발생한다. 왜지?
![](https://velog.velcdn.com/images/suran-kim/post/05368dc6-cb67-47b9-b3fb-fea934350ff9/image.png)
에러 로그는 다음과 같다.
`Required request parameter 'name' for method parameter type String is not present`
반드시 넘겨줘야할 파라미터가 넘어오지 않았다는 뜻이다.

따라서 웹브라우저에 파라미터를 넘겨준다.
`http GET방식`에서 파라미터를 넘겨주는 방법은 다음과 같다.
localhost:8080/`@GetMapping이름`?`@RequestParam키값`=`입력값`

다음과 같이 파라미터를 넘겨준다.
localhost:8080/`hello-mvc`?`name`=`spring!`

![](https://velog.velcdn.com/images/suran-kim/post/73a5f421-c40a-4c5b-9aa2-d4135ea3034f/image.png)
![](https://velog.velcdn.com/images/suran-kim/post/d7a65e7a-823e-430f-9965-364199896a53/image.png)


웹브라우저에 입력한`name`=`spring!`식이 넘어가면 Controller에서 `name`은 입력값인 `spring`으로 바뀐다. 바뀐 값은 `model`에 담긴다.

`hello-template.html`에서의 `${name}`은 model에서 키 값이 `name`인 것을 꺼내 해당 키의 값으로 치환한다는 뜻이다. 따라서 화면에서는 hello 뒤에 `입력값`이 붙어서 출력된다. 



<br/>
<br/>



## 원리

![](https://velog.velcdn.com/images/suran-kim/post/e51301cf-8ef8-40f8-b840-ed912bb2f683/image.png)

									(강의 교안의 참고자료를 직접 따라 그렸습니다.)
	
1. 웹브라우저에서 `localhost:8080/hello-mvc`을 입력하면 `내장 톰켓 서버`에서 요청을 받고 `스프링 컨테이너`에게 넘긴다.
2. 스프링 컨테이너는 `hello-mvc`메서드가 `HelloController`와 Mapping된 것을 발견하고 해당 메서드를 호출한다. 이때, return시 이름은 `hello-template`, 모델은 name(키값):spring!(밸류)으로 해서 `viewResolver`에게 넘겨준다.
3. `viewResolver`가 동작해서 http에게 응답으로 돌려줄 뷰(`templates/hello-template.html`)를 찾고, `타임리프 템플릿 엔진`에게 넘긴다. 
4. 타임리프 템플릿 엔진이 렌더링을 통해 **Html을 변환**한 뒤 웹 브라우저에 반환한다.



>_정적 컨텐츠와 템플릿 엔진의 차이  _ <br/>
정적 컨텐츠 : Html을 변환하는 과정이 없음
템플릿 엔진 : **Html을 변환**한 뒤 웹 브라우저에 반환



<br/>
<br/>



_**변환 전 hello-template.html**_
![](https://velog.velcdn.com/images/suran-kim/post/17934cbe-62a0-49b4-81d5-66653c9bc32d/image.png)

_**변환 후 hello-template.html**_
![](https://velog.velcdn.com/images/suran-kim/post/33cf76bf-eedc-4ec0-8731-13664755bc4b/image.png)

변환 전과 후의 코드가 다르다. 

<br/>
<br/>




### (+) 타임리프 템플릿의 장점
intellij에서 [Ctrl + Shift + c] 를 통해 Apsolute path를 복사한 뒤 웹 브라우저에 주소를 붙여넣으면 파일을 그대로 전달할 수 있다. 이 방법을 통해 서버를 통하지 않아도 HTML파일의 출력화면을 확인할 수 있다.

![](https://velog.velcdn.com/images/suran-kim/post/716749f5-ce3c-4972-92b5-351f74630f25/image.png)

다만 서버를 통할 때와 서버를 통하지 않을 때 결과로 출력되는 내용은 조금 다르다.
서버를 통하지 않을 때는 hello! empty가 출력되지만
서버를 통해 웹브라우저와 연결한다면 `'hello ' + ${name}` 가 치환되어 출력될 것이다.

![](https://velog.velcdn.com/images/suran-kim/post/68f6f0d6-59a5-4ca7-99a7-f9c76390c5f9/image.png)






<br/>
<br/>


---

# API
API를 쓰는 가장 큰 이유는 데이터 전송 때문이다.

## 실습 1 (문자열 반환)

_**HelloController.java**_
```java
@Controller
public class HelloController {

    @GetMapping
    public String hello (Model model) {
        model.addAttribute("data", "반가워요?!");
        return "hello";
    }

    @GetMapping("hello-mvc")
    public String helloMvc(@RequestParam("name") String name, Model model){
        model.addAttribute("name", name);
        return "hello-template";
    }

    @GetMapping("hello-string")
    @ResponseBody
    public String helloString(@RequestParam("name") String name) {
        return "hello " + name;
    }

}
```

> _**@ResponseBody란? **_
- http에는 `header 부, body 부`가 존재.
- `@ResponseBody`는 body 부에 내용을 직접 넣는다는 의미.





![](https://velog.velcdn.com/images/suran-kim/post/53180ee9-63da-41d7-87b4-8f8b821e5f97/image.png)
![](https://velog.velcdn.com/images/suran-kim/post/757b32cf-2c82-4ccc-9087-4cab9210aebf/image.png)
실제 브라우저에 `hello-stirng`에 밸류값을 줘서 호출해보면 화면은 MVC방식과 비슷하게 나온다. 

하지만 웹브라우저로 전송된 코드에는 `html태그`가 하나도 없다.
`return`으로 반환한 문자가 그대로 출력되는 것이다. 



<br/>
<br/>




## 실습 2 (객체 반환)


_**HelloController.java**_
```java
@Controller
public class HelloController {

    @GetMapping("hello-api")
    @ResponseBody
    public Hello helloApi(@RequestParam("name") String name) {
        Hello hello = new Hello();
        hello.setName(name);
        return hello;
    }

    static class Hello{
        private String name; // 키

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

```


![](https://velog.velcdn.com/images/suran-kim/post/f0acc9e5-fd7f-46cc-981e-6c1ca2c1a111/image.png)
![](https://velog.velcdn.com/images/suran-kim/post/89ee86e1-c938-46ff-be55-b20bbb6771cd/image.png)
API 방식을 통해 객체를 반환하면 브라우저에서는 `JSON`형식의 코드가 출력된다.

`JSON`은 {키:밸류}의 구조를 가지고 있다.
html처럼 열고 닫는 구조가 아니기 때문에 html에 비해 단순하고 간단하다. 
스프링에서는 `@ResponseBody`로 객체를 반환할 때, 기본적으로`JSON`을 반환하도록 세팅되어 있다.



<br/>
<br/>



### _**(+) JAVA Bean 표준 방식( propertie 접근 방식 )**_
static 클래스 Hello의 `name` 필드는 `private`이기 때문에 외부에서 직접 접근할 수 없고 getter, setter를 이용해 접근해야 한다.




<br/>
<br/>


---


## 원리
![](https://velog.velcdn.com/images/suran-kim/post/440ec05a-2608-49c7-a17f-f116c548f8c8/image.png)

								(강의 교안의 참고자료를 직접 따라 그렸습니다.)



1. 웹브라우저에서 `localhost:8080/hello-api`를 입력하면 `내장 톰켓 서버`에서 요청을 받고 `스프링 컨테이너`에게 넘긴다.
2. 스프링 컨테이너는 `hello-api`메서드가 `HelloController`와 Mapping된 것을 발견하고 해당 메서드를 호출한다. `@ResponseBody`가 있으므로 http에 대한 응답(return)에 데이터를 바로 넣는다(**body에 직접 반환**). 단, 데이터가 문자가 아닌 **객체**일 때는 return시 `JSON` 방식으로 만들어서 반환한다. 

       - _** 추가 설명**_ 
          `@ResponseBody`를 확인하면 `HttpMessageConverter`가 동작한다. 
         _ StringHttpMessageConverter_ : 데이터가 단순 문자열일 때 동작
         _ MappingJackson2HttpConverter _: 데이터가 객체일 때 동작. 객체를 Json 형식으로 바꾸고 요청한 서버 or 웹 브라우저에게 반환




> _**템플릿 엔진과 API의 차이**_
- `템플릿 엔진` : 뷰(템플릿)을 조작하는 방식. return 발생 시 `viewResolver` 동작
- `API` : 뷰 없음. 데이터를 그대로 내려준다. return 발생 시 `HttpMessageConverter` 동작



<br/>
<br/>




---

# 요약

> 
**_정적 컨텐츠_** : 파일을 그대로 내려준다.
**_MVC와 템플릿 엔진_** : `뷰리졸버`가 `뷰`를 찾고, `템플릿 엔진`으로`뷰`(HTML)를 랜더링해서 반환
_**API**_ : `HttpMessageConverter`를 통해 `JSON 형식`으로 객체를 반환


> ### _**색인**_<br/>
 _**viewResolver**_
view를 찾아주고 template에 연결시켜주는 기능을 한다. <br/>
_**Jackson, Gson **_
객체를 Json으로 변환시켜주는 라이브러리 



