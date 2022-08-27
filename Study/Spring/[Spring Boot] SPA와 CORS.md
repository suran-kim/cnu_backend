
>_본 포스팅은 프로그래머스 미니 데브 코스를 공부하며 
학습을 기록하기 위한 목적으로 작성된 글입니다._

**_학습 목표_** 
SPA의 개념을 이해해보자
react를 이용해 SPA를 구현해보자
CORS 에러를 해결해보자


# SPA 
SPA는 단일 페이지 웹 어플리케이션


![](https://velog.velcdn.com/images/suran-kim/post/fb068100-e157-43d9-855d-dff169eae683/image.png) 
![](https://velog.velcdn.com/images/suran-kim/post/0d7f1008-f6cb-4b2f-9638-113a8dcbf30f/image.png)[이미지 출처](https://poiemaweb.com/js-spa)

SPA는 단일 페이지 웹 어플리케이션(Single-Page Application)이다.
 SPA에서는 동적 랜더링을 서버가 아닌 **브라우저**에서 처리한다.
MPA에서는 매번 페이지가 리로딩됐다. 하지만 SPA에서는 페이지를 한 번만 로드하면 클라이언트가 AJAX 기술을 이용해서 URI 변경없이 클라이언트 백단에서 요청을 보낸다. 이게 무슨 소리인고? 그림과 함께 보면 이해가 쉽다.

 어플리케이션 로드 시 AJAX를 이용해서 대부분의 정적 리소스(html, css, js)를 **한 번만** 읽는다. 그 뒤 어플리케이션 실행 중에는 페이지 갱신에 필요한 데이터만을 **JSON**로 읽어오고 관련된 화면을 변경시킨다.
 
↳ 새로고침이 발생하지 않기 때문에 네이티브 앱과 유사한 경험을 제공한다.

`장점` 
- 개발 영역의 분리가 가능하다. (백엔드 개발자는 뷰 구현에 대한 부담이 덜어짐)
- 서버의 부담이 줄어든다. (클라이언트의 부담이 높아짐)


## SPA VS MPA





_**MPA (일반 웹 애플리케이션)**_
- URL 변경 시 **모든 페이지**가 다시 렌더링 된다.
- 페이지 리로드 시 자바 스크립트 및 CSS를 서버에게 매번 요청한다.
- 레이아웃 처리를 **서버**에서 담당한다.
- 세션 관리 영역 : 서버


 - 매번 페이지가 리로딩 된다.



_**SPA (단일 페이지 웹 어플리케이션)**_
- URL 변경 시 **특정 영역**만 렌더링 된다.
  - 주로 **DOM 조작**을 통해 브라우저에서 렌더링이 이루어진다.
- 자바 스크립트 및 CSS는 최초 로드 시 한 번만 다운받는다.
- 화면을 서버에서 처리하지 않고 **브라우저**에서 처리한다.


- 브라우저에서 랜더링을 한다.
- AJAX를 이용해서 Http 요청을 보낸다. 



## 웹 페이지 로딩 절차 비교 (SPA, MPA)


_**MPA 기반 웹 페이지 로딩 절차**_

세션은 http의 특징 중 하나인 stateless 때문에 만들어졌다. 세션은 웹서비스에 접속한 일정시간동안 유지되는 상태이다. 사용자 세션에는 사용자의 이름, 이메일 등이 보관되는데 http는 들어온 요청이 누구의 요청인지를 구분하기 위해 쿠키를 통해 세션 정보를 전달 받는다. 서버는 쿠키를 이용해 세션이 유지되는 동안 그 요청이 어떤 사용자의 요청인지 구분할 수 있다.


- 세션 정보를 저장하는 것은 **서버**이다.
- 요청에는 쿠키(사용자 정보가 포함된 세션 정보)가 실린다. 이를 통해 사용자 데이터 모델을 찾아 

브라우저 랜더링 처리를 하는 것은 아니다! 
<br/>


_**SPA 기반 웹 페이지 로딩 절차**_
세션으로 처리하는 경우와 세션 없이 처리하는 경우로 나뉜다. 
세션으로 처리하는 경우를 먼저 설명하겠다. 세션에는 메뉴 정보, 사용자 configuration 등도 포함될 수 있지만 그렇게 되면 새로운 데이터를 담을 때마다 세션은 매우 무거워진다. SPA 기반 로딩 절차에서는 사용자 데이터 모델의 일부를 자바 스크립트 쪽에서 관리한다 → 즉, 브라우저의 메모리(**로컬 스토리지**)에 담아둔다. 메모리에 담아두면 리프레시할 때마다 정보가 날아가지만, 로컬 메모리에 담아두면 리로드 시마다 가져올 수 있다 (?) 
- 그래서 메뉴 정보를 로컬스토리지에 담아두고 필요할 때 이용하는 양상
- 변경이 필요한 부분에만 DOM 프로그램을 이용해 DOM 조작.


<br/>

_**라우팅 처리**_
다음은 poiemaweb에서 설명하는 라우팅에 대한 개념이다.

> 라우팅이란 출발지에서 목적지까지의 경로를 결정하는 기능이다.
어플리케이션의 라우팅은 사용자가 태스크를 수행하기 위해 어떤 화면(view)에서 다른 화면으로 화면을 전환하는 내비게이션을 관리하기 위한 기능을 의미한다. 일반적으로 라우팅은 사용자가 요청한 **URL 또는 이벤트**를 해석하고 **새로운 페이지로 전환**하기 위해 필요한 데이터를 서버에 요청하고 페이지를 전환하기 위한 **일련의 행위**를 말한다.<br/> 
브라우저가 화면을 전환하는 경우는 다음과 같다.<br/>
_1. 브라우저의 주소창에 **URL**을 입력하면 해당 페이지로 이동한다.
2. 웹페이지의 **링크**(a 태그)를 클릭하면 해당 페이지로 이동한다.
3. 브라우저의 **뒤로 가기** 또는 **앞으로 가기** 버튼을 클릭하면 사용자 방문 기록(history)의 뒤 또는 앞으로 이동한다. history 관리를 위해서는 각 페이지는 브라우저의 주소창에서 구별할 수 있는 유일한 URL을 소유해야 한다.<br/>_
[(라우팅 설명 출처: poiemaweb)](https://poiemaweb.com/js-spa)




 - _서버 사이드 라우팅 처리_
  ![](https://velog.velcdn.com/images/suran-kim/post/fb068100-e157-43d9-855d-dff169eae683/image.png) [서버 사이드 라우팅 : 이미지 출처](https://poiemaweb.com/js-spa) 
 ![](https://velog.velcdn.com/images/suran-kim/post/a78d5ca8-272d-45b3-8b4f-8e50becc4ed1/image.png) [URI 구조 : 이미지 출처](https://poiemaweb.com/js-spa)

<br/>

화면을 전환하는 경우가 발생하면, 리소스 경로가 URL의 path에 추가되어 주소창에 나타나고 해당 **리소스를 서버에 요청**한다. 서버는 화면을 표시할 수 있는 완전한 리소스를 **클라이언트에게 html**으로 응답해준다. 응답받은 html로 전체 페이지를 다시 렌더링하게 되므로 **새로고침**이 발생한다. 

즉, 요청받는 URL에 따른 리소스를 반환하고, 일반적인 웹사이트에서 사용자의 URL에 해당하는 웹페이지를 반환하는 행위로 볼 수 있다.

`SSR 장점` :  
    - 자바 스크립트의 도움 없이 응답받은 html만으로 렌더링이 가능
    - 각 페이지마다 고유의 URL이 존재하므로 **history 관리** 및 **SEO 대응**에 아무런 문제가 없다.

`SSR 단점` :
    - 요청마다 중복된 리소스를 응답받아야 한다. 
    - 전체 페이지를 다시 렌더링하는 과정에서 새로고침이 발생하여 사용성이 좋지 않다.


 <br/><br/>
 
 - _클라이언트 라우팅 처리(해쉬방식)_
 
 ![](https://velog.velcdn.com/images/suran-kim/post/0d7f1008-f6cb-4b2f-9638-113a8dcbf30f/image.png) [이미지 출처](https://poiemaweb.com/js-spa) 
   
SPA 특성상 클라이언트에서 동적으로 렌더링하므로 URL 변화에 따라 화면 상태를 변경한다.

네비게이션(유저가 웹 사이트 내에서 자연스럽게 이동할 수 있도록 안내하는 시스템)이 클릭되면 hash가 추가된 URI가 주소창에 표시된다. 단, **URL이 동일한 상태에서 hash만 변경**되면 브라우저는 서버에 어떠한 **요청도 하지 않는다**. 즉, URL의 hash는 변경되어도 서버에 새로운 요청을 보내지 않으며 따라서 페이지가 **갱신되지 않는다**. 왜냐하면 hash는 요청을 위한 것이 아니라 웹페이지 내부에서 **이동**하기 위한 것이기 때문이다. 서버에 새로운 요청을 보내지 않기 때문에 페이지 갱신이 일어나지 않지만, 페이지마다 **고유의 논리적 URL**이 존재하므로 history 관리에 아무런 문제가 없다. 

 hash 방식은 URL의 hash가 변경하면 발생하는 이벤트를 사용해 hash의 변경을 감지하고 URL의 hash를 취득해 필요한 ajax 요청을 수행한다.



`해쉬방식 장점` :  
    - HTML5의 History API를 사용할 수 있다.
    - 각 페이지마다 고유의 논리적 URL이 존재하므로 **history 관리**가 가능하다.

`해쉬방식 단점` :
    - url에 불필요한 #이 들어간다
    - SEO 이슈가 존재할 수 있다. 
 -  웹 크롤러는 JavaScript를 실행시키지 않기 때문에 hash 방식으로 만들어진 사이트의 콘텐츠를 수집할 수 없다. (구글은 해시뱅을 일반적인 URL로 변경시켜 문제를 해결)

<br/>  

_(+)_

  - 클라이언트 라우팅 처리 방식에는 해쉬 방식 이외에도 여러 방식이 있다. 
  - 클라이언트 라우팅 처리 방식을 사용하면 서버 쪽에는 직접 뷰를 렌더링하는 코드 없이 JSON이나 XML을 return하는 API 코드만 존재하므로 코드가 간결해진다.


<br/><br/>

---

_CORS를 처리할 수 있는 코드 실습_

1. _proxy 사용_
웹서버에 요청을 보낸다.(동일 호스트이기 때문에 에러발생 x)
웹서버가 요청을 받아서 spring에게 http요청을 보낸다.
proxy가 응답을 전달해준다.(?)
상세페이지로 이동하면 `요청`이 발생한다.

2. _CORS를 지원하는 API 사용_
링크 클릭 시 Response를 보내는 메소드
```java
 // GET메소드
    @GetMapping("/api/v1/customers/{customerId}")// api를 만들 때는 versioning 필요.
    @ResponseBody // List<Customer>를 Http 메세지로 변환하기 위해 필요
    public ResponseEntity<Customer> findCustomers(@PathVariable("customerId") UUID customerId) { // ResponseEntity - spring에서 제공하는 엔티티. http 바디 타입을 제네릭으로 전달한다.
        var customer = customerService.getCustomer(customerId);
        return customer.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); // v -> ResponseEntity.ok(v)
    } // customer가 있으면 200(ok), 없다면 404(notFound)를 한 뒤 빌드 -> return하는 코드.
    // 혹은 error를 throw하고 에러 공통 처리 모듈에서 처리하게 할 수도 있다.
```

 <br/><br/>


# CORS(Cross Origin Resource Sharing)

웹에서는 다른 출처로의 리소스 요청을 제한하는 두 가지 정책이 존재한다. (CORS과 SOP) 그 중 CORS는 클라이언트와 클라이언트 간의 에러이다.

## CORS와 SOP

하나의 웹 애플리케이션은 하나의 호스트에서만 데이터를 가져오지 않는다. 웹이 복잡해지고 다양한 리소스가 필요해지면서 웹 애플리케이션 또한 다양한 호스트에 접근할 필요가 생겼다. 이에 따라 보안성이 중요해지고, 사용자를 악의적인 공격에서 보호하기 위해 브라우저는 **동일한 출처**에서만 리소스를 활용할 수 있도록 허용하는 **SOP(Same-Origin Policy)** 정책을 만들었다.

그러나 앞서 말했듯 다양한 리소스에 접근할 필요성이 생김에 따라, 동일 출처가 아닌 리소스에 접근하는 것을 허용하는 것이 **CORS(Cross Origin Resource Sharing)**정책이다. CORS는 **http 헤더**를 이용해 다른 출처의 리소스에 접근할 수 있도록 해주는 메커니즘을 의미한다.


SOP 정책
 - 웹 애플리케이션은 동일 출처의 리소스만 접근 가능.

 
CORS 정책
  - **http 헤더**를 통해 다른 출처의 리소스에 접근할 수 있도록 해주는 메커니즘.
 
 
## _**origin (출처) **_
 ![](https://velog.velcdn.com/images/suran-kim/post/bc663635-21c4-4c61-97ec-a1b0cd69edbd/image.png)
 이미지 출처 : https://evan-moon.github.io/2020/05/21/about-cors/
 
 - 출처가 같다 : `프로토콜` + `호스트` + `포트넘버` 가 같다.
 
서버의 위치를 의미하는 https://google.com과 같은 URL들은 여러 개의 구성 요소로 이루어져있다. 이때 출처(origin)은 `Protocol`과 `Host`, 그리고 `포트번호`(:80, :443)를 모두 합친 것을 의미한다.  각 웹에서 사용하는 HTTP, HTTPS 프로토콜의 기본 포트 번호가 정해져 있기 때문에 origin 내의 `포트번호`는 생략이 가능하다.

예를 들어 
http://store.company.com/dir/page.html 페이지의 스크립트가 아래와 같은 페이지에서  리소스 접근시 결과는 다음과 같다.

![](https://velog.velcdn.com/images/suran-kim/post/2de3ca88-7a3b-4028-96aa-ef9b8abd0571/image.png)이미지 및 예시 출처 : https://velog.io/@sangmin7648/SOP-CORS%EB%9E%80

이처럼 호스트가 다르면 CORS가 발생한다.


## CORS 동작 방식




예비요청(preflight)이 있느냐 없느냐에 따라 flow가 다름
예비요청을 보낼 때 : **단순 요청**이 아닐 때

 
 
### with preflight (예비요청 O)



- 예비요청(preflight)
    - 요청 : HTTP메소드 OPTIONS에 Origin을 실어 보낸다. (origin은 URL에서의 host가 됨)
    - 응답 : Access-Controll-Allow-Origin이라는 헤더에 **허용되는 출처**가 응답


- 본요청
  - 응답된 허용된 출처와 동일한 경우 main request 진행

![](https://velog.velcdn.com/images/suran-kim/post/9175f02a-9dce-4fe1-8529-132f40b2ee68/image.png)출처 : https://velog.io/@sangmin7648/SOP-CORS%EB%9E%80

### without preflight (예비요청 X)

- 단순 요청 (simple requests)
 - ![](https://velog.velcdn.com/images/suran-kim/post/c57bb3f8-5958-444c-86bb-b9f85572cd76/image.png)출처 : https://developer.mozilla.org/ko/docs/Web/HTTP/CORS <br/><br/>
 - 유저 에이전트 : 브라우저
 - 프론트엔드, 자바스크립트에서 수동으로 설정할 수 있는 헤더의 예시(content-type헤더의 허용값 유의)
 - 예를 들어, application JSON이라고 요청을 하면 단순 요청이 아니게 된다(?)

주의사항!
서버에서 Access-Contrl-Allow-Origin을 주지 않으면 브라우저에서 fail이 뜬다.


## CORS 설정

CORS를 서버에서 서포트하는 것은 쉽다? 스프링 MVC 에서 구성할 때 CORS를 글로벌하게 셋업하는 방법도 있고, Control단위로도 쉽게 설정할 수 있다.
addCorsMappings을 사용해서 


```java
// 서버 단에서 설정가능
@Override
public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")
            .allowedMethods("GET", "POST")
            .allowedOrigins("*");
}
```
```java
// 컨트롤러에서 설정 가능 (어노테이션 사용)
@Controller
@CrossOrigin(origins = "*")
public class CustomerController { ... }
```


<br/><br/>

> _**오류해결**_
- intelliJ 터미널에서 yarn이 실행되지 않는 문제
해결 -> https://wan2.land/posts/2017/05/24/yarn-global-setting/
https://kim-mj.tistory.com/269
- 프록시가 적용되지 않는 문제 
해결 -> 서버를 다시 올린다.
-  findCustomers메소드의 response가 적용되지 않고 404 not found가 발생하는 문제
해결 -> 오타. `/api/v1/customers/{customerId}` 를 `/api/v1/customers{customerId}` 라고 씀
  
> _**새로 알게된 용어**_
- _**세션 **_
대체로 로그인을 하면 만들어진다.
웹 서비스에 접속해서 일정시간동안 머무는 상태.
- **_네이티브 앱_**
모바일 기기에 최적화 된 언어로 개발 된 앱
- **_프로그래시브 웹앱 (PWA)_**
- _**SEO(검색 엔진 최적화) **_
웹사이트가 검색 결과에 더 잘 보이도록 최적화하는 과정 [참고자료]
(https://developer.mozilla.org/ko/docs/Glossary/SEO)
- _**JSON 파싱**_
JSON이란 JavaScript Object Notation으로 데이터를 주고받을때 사용되는 포맷 중 하나.
JSON은 name-value 형태. → **name**(무조건 Sring)-**value**(기본자료형 | 배열 | 객체 ) 
JSON 파싱은 이 **JSON형태**의 문자열을 **이해할 수 있는 자료형**(EX: JavaScript 값이나 객체)으로 뽑아내는 것. [참고자료](https://zeddios.tistory.com/90) 
- _**NPM, yarn**_
JavaScript의 빌드 툴
- _**ajax**_


  
> _**TIP**_
- JACKSON을 사용하려면 디폴트 생성자가 꼭 필요하다.
- 도메인을 막 건들면 안 된다. 도메인의 룰을 유지해야한다. <br/> <br/>
- 
- ![](https://velog.velcdn.com/images/suran-kim/post/35091759-3e43-4169-9037-3e9dad3fc7f7/image.png)(이미지 출처 : [0307kwon 님의 웹은 어떻게 동작할까? - 1. 사용자가 웹페이지를 보기까지](https://velog.io/@0307kwon/%EC%9B%B9%EC%9D%80-%EC%96%B4%EB%96%BB%EA%B2%8C-%EB%8F%99%EC%9E%91%ED%95%A0%EA%B9%8C-1.-%EC%82%AC%EC%9A%A9%EC%9E%90%EA%B0%80-%EC%9B%B9%ED%8E%98%EC%9D%B4%EC%A7%80%EB%A5%BC-%EB%B3%B4%EA%B8%B0%EA%B9%8C%EC%A7%80#-%EB%82%B4%EA%B0%80-%EC%98%A4%ED%95%B4%ED%95%98%EA%B3%A0-%EC%9E%88%EB%8D%98-%EB%B6%80%EB%B6%84) 게시글의 도표와 동일하게 제작한 자료 입니다.)

> _**더 공부하고 싶은 것**_
  - 프로그래시브 웹앱 (PWA)
  - [CORS는 왜 이렇게 우리를 힘들게 하는걸까?](https://evan-moon.github.io/2020/05/21/about-cors/)
  
  
>_**Ref**_
- [0307kwon 님의 웹은 어떻게 동작할까? - 1. 사용자가 웹페이지를 보기까지](https://velog.io/@0307kwon/%EC%9B%B9%EC%9D%80-%EC%96%B4%EB%96%BB%EA%B2%8C-%EB%8F%99%EC%9E%91%ED%95%A0%EA%B9%8C-1.-%EC%82%AC%EC%9A%A9%EC%9E%90%EA%B0%80-%EC%9B%B9%ED%8E%98%EC%9D%B4%EC%A7%80%EB%A5%BC-%EB%B3%B4%EA%B8%B0%EA%B9%8C%EC%A7%80#-%EB%82%B4%EA%B0%80-%EC%98%A4%ED%95%B4%ED%95%98%EA%B3%A0-%EC%9E%88%EB%8D%98-%EB%B6%80%EB%B6%84)
-  [참고자료1](https://yamoo9.github.io/react-master/lecture/rr-spa.html#spa-%E1%84%85%E1%85%A1%E1%86%AB)
- [poiemaweb : Single Page Application & Routing](https://poiemaweb.com/js-spa)
- [sangmin7648님의 SOP, CORS란❓](https://velog.io/@sangmin7648/SOP-CORS%EB%9E%80)
- [yshjft님의 2022년 4월 21일 TIL](https://velog.io/@yshjft/2022%EB%85%84-4%EC%9B%94-21%EC%9D%BC-TIL)