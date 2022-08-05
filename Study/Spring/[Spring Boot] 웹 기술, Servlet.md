>_본 포스팅은 프로그래머스 미니 데브 코스를 공부하며 
학습을 기록하기 위한 목적으로 작성된 글입니다._

# WEB
- 웹은 월드와이드 웹의 줄임말이다.
- 인터넷에 연결된 컴퓨터를 통해 사람들이 정보를 공유할 수 있는 전세계적인 정보 공간이다.


- 팀 버너스 리가 웹 브라우저와 웹 서버를 창시 _(천재)_
- 웹 기술의 근간 (웹의 구성)
  - HTTP 프로토콜
  - URI 
  - HTML
  ![](https://velog.velcdn.com/images/suran-kim/post/a4f0c453-80bc-425d-8974-5102f2d5001c/image.png)

  
## URI

- _**Uniform Resource Identifier**_
  - 웹 상의 정보(리소스)를 식별할 수 있는 식별자. 
  - 자원에 접근할 수 있는 유일한 주소.


- _**URI 구조 **_
  - `http://blog.example.com/entries/1`
  **URI SCHEME** - http
  **호스트명** - blog.example.com
  **패스**   -     /entries/1 <br/><br/>
  - `http://ranran:pass@blog.example.com:8080/search?q=test&debug=true`
  **URI SCHEME** - http
  **사용자** - ranran:pass
  **호스트명** - blog.example.com
  **포트번호** - 8080
  **패스**   -     /entries/1 
  **쿼리 문자열(스트링)** - q=test&debug=true
    - **쿼리 파라미터** - q=test
  <br/><br/>
  
  
### 상대경로와 절대경로
  
  - _**절대경로**_
  `/` 로 시작 
  
  - _**상대경로**_
   `/` - 루트
  `./` - 현재 위치
  `../` - 현재 위치의 상단 폴더 <br/>
  상대경로는`현재 위치한 곳`을 기준으로 한 `그곳의 위치`
  배포 시에는 상대경로를 사용하는 것이 좋다. 
    
 
  폴더일 수도 파일일 수도 
  특정 페이지일 수도 리소스일 수도
  
  
### URI에 사용할 수 있는 문자(ASCII)



ASCII 문자가 아닌 문자를 사용할 경우 _encode와_ _decode_ 처리가 필요하다.



## HTTP
- _**HyperText Transfer Protocol**_
  - **하이퍼텍스트**를 교환하기 위해 만들어진 **통신 규약** (하이퍼 텍스트 전송 프로토콜)
  - **클라이언트 서버 프로토콜** 
    - 대표적인 클라이언트(고객)은 **웹 브라우저**.
    _(그러나 서버 간 HTTP 요청에서는 클라이언트가 서버일 수도 있다)_
    - 클라이언트가 **요청**을 보내면 서버가 **응답**한다.
    - HTTP는 **동기형 프로토콜** (요청 && 응답이 한 세트. 서버에서 응답이 올 때까지 대기)

  -  HTTP는 **URI로 조작대상을 지정**해서, URI를 통해 가져온 리소스를 조작한다.


---

- _**요청**_ 
클라이언트(웹 브라우저)가 OS자원을 이용해서 네트워크를 타고 호스트 서버에 요청을 보낸다. 

- _**응답**_ 
호스트 서버가 네트워크를 타고 OS의 커널에 보낸 **응답(HTML)**을 웹브라우저가 사용자의 화면에 렌더링해준다. 


---

###  HTTP 특징
   
- TCP/IP 기반 
- 요청/응답형 프로토콜
- 동기형 프로토콜
- 스테이트리스
  - 이전 요청에 대한 상태를 서버가 기억하지 않는다.
_  (토큰, 캐시 등을 매번 전달해야 한다.)_
  
  
<br/>

### HTTP Messege Format

 _**HTTP Messege Format**_
![](https://velog.velcdn.com/images/suran-kim/post/49a6a60a-1da9-42f6-975b-849d3d588ac8/image.png)
이미지 출처 : https://developer.mozilla.org/en-US/docs/Web/HTTP/Messages

- `헤더`와 `바디`로 구성
  
<br/>

_**HTTP Headers**_
- Request header - 요청 시 키, 밸류를 콜론으로 구분
- Response header - 응답 시 키, 밸류를 콜론으로 구분
- General header - 공통으로 가지는 헤더
- Respresentation header - 리소스를 나타내는 부분
<br/>

_**HTTP Body**_
- 바이너리 or 텍스트( XML / HTML / JSON )


### HTTP Method
리소스의 특정 행위를 취할 때 


## HTML
- _**HTML (Hyper Text Markup Language) **_
  - _Hyper Text + Markup _
  - **Hyper Text** - 초월적인 텍스트: 다른 정보와 연결성을 가진다.
  - **Markup** - 문단에서의 중요한 의미를 구조적으로 표시할 수 있도록 만든 언어

즉, HTML이란 **연결성을 가지고 있는 마크업 언어**이다.

## 웹의 기술적 특징 2가지

- _**Hypermedia System (하이퍼 미디어 시스템)**_
텍스트, 이미지, 음성 등 다양한 미디어들로 구성된 시스템

- _**Distributed System (분산 시스템)**_
중앙집중 시스템 : 한 대의 컴퓨터가 모든 프로세스를 실행
분산 시스템 : 여러 대의 컴퓨터에 서버를 분산시킨다.
웹은 전세계에 배치된 서버에 전 세계의 브라우저가 접근할 수 있는 분산 시스템이다.


# Web Application Architecture

- _**아키텍처 (Architecture)**_
  - 웹, 어플리케이션 시스템에 대한 구성과 동작 원리
  - **시스템 구성 환경**을 설명하는 설계도
_  (시스템에 대한 논리적 이해를 돕는 다이어그램)_
![](https://velog.velcdn.com/images/suran-kim/post/21c485b7-0d94-402b-8e7e-3a811b5070f0/image.png)
출처 : https://litslink.com/blog/web-application-architecture
     
    유저가 브라우저에 요청하면 브라우저 안에서 Frontend기술이 동작한다. 
     브라우저가 URL을 입력하고 리소스를 요청하면 앱에 대한 로직을 가지고 있는 웹 서버가
     특정 리소스를 특정 OS의 file system에 접근해서 HTTP 프로토콜을 이용해 응답을 보낸다.
     브라우저가 응답받은 리소스를 확인하고 헤더의 스크립트 코드 확인, 
     HTML에 의한 랜더링이 시작되면서 CSS에 의해 화면이 그려진다.
     그렇게 랜더링 결과를 사용자가 볼 수 있게 된다.
     (+ HTTP 프로토콜은 TCP / IP 위에 있다. 네트워크는 모두 TCP / IP 로 연결됨)



- _**아키텍처 자세히 뜯어보기**_
![](https://velog.velcdn.com/images/suran-kim/post/45061607-97a7-4c7c-9bdf-c3b6302172e2/image.png)
출처 : https://litslink.com/blog/web-application-architecture

  - _**DNS**_
    - Domain Name System
    - **도메인 이름**을 통해 IP를 가져오는 시스템이다.
  URI에 IP를 적지 않고 도메인 이름을 적는다.
  그럼 DNS가  도메인 이름에 해당하는 IP를 검색해주고 
  해당하는 IP로 HTTP 요청이 날아가게 된다.
  _👉 웹사이트를 위한 전화번호부_ <br/><br/>
  
  
  - _**Load Balancer**_
    - 트래픽을 여러 대의 서버로 **분산**해주는 기술이다  (   _어플리케이션의 수평적 확장_ ).
     👉 들어오는 요청을 복제/미러링 된 수많은 애플리케이션 서버 중의 하나로 연결, 서버의 응답을 클라이언트로 보낸다. 모든 서버는 특정 요청을 같은 방식으로 처리한다. 
     👉 로드 밸런서는 서버에 과부하가 걸리지 않도록 들어오는 요청을 적절히 분배해준다.
       
       
       <br/><br/>
    
  - _**Web App Servers(WAS)**_
    - 사용자 요청이 들어오면 **핵심 비즈니스 로직**을 실행하고, 그 결과를 **HTML**에 담아 브라우저로 전송하는 역할을 한다.
    👉 이 과정에서 데이터베이스, 캐싱 계층, 잡 큐, 검색 서비스, 기타 마이크로 서비스, 데이터/로그 큐 등 다양한 백엔드 인프라와 데이터를 주고 받아야 한다. <br/><br/>
  
  - _**Databases**_
    - **데이터** 구조를 정의하고, 새로운 데이터를 삽입하고, 데이터를 찾고, 데이터를 수정하거나 삭제하고, 데이터로 연산을 수행하는 역할을 한다.
    👉 대부분은 **WAS**와 직접 통신한다.
    👉 각 백엔드 서비스는 애플리케이션의 다른 영역과 분리된 자신만의 데이터베이스를 갖고 있을 수도 있다
    👉 NoSQL (Non-SQL): 대규모 웹 애플리케이션에 의해 생성되는 많은 양의 데이터를 처리하기 위해 등장한 최신 데이터베이스 기술의 집합.
    _( NoSQL를 왜 사용하는가? - SQL에서 파생된 기술 대부분은 수평적 확장이 어려우며 특정 지점에 도달하면 수직적 확장만이 가능하기 때문이다.)_ 
     👉 OLTP (Online Transaction P)<br/><br/>
   
    
  
  - _**Caching Service**_
    - 정보를 거의 O(1) 시간 안에 찾을 수 있는 키-값 데이터 저장소를 제공한다.
    👉 자원이 많이 소모되는 연산을 반복하지 않고 캐시에서 가져와서 효율이 상승한다. 
     👉 똑같은 리소스에 대한 요청에 더 빠른 응답을 줄 수도 있다.
    👉 _**Ex)** 데이터베이스의 쿼리 결과, 외부 서비스 호출 결과, 주어진 URL의 HTML 등을 캐시에 저장._<br/><br/>
    
  - _**Job Queue (optional) & Job Severs **_
    - Job Queue 아키텍쳐  - **비동기적인 작업**을 가능하게 하는 아키텍쳐. 두 개의 컴포넌트로 구성된다. 
     1. Job Queue 
      👉 애플리케이션이 정기적인 일정이나 사용자에 의해 발생한 작업(바로 응답할 수 없는 요청)을 실행할 필요가 생기면, 해당 작업을 큐에 추가한다
      👉 제일 간단한 FIFO 큐를 사용하지만 애플리케이션 규모에 따라 우선순위 큐가 필요해지기도 한다. 
      2. Job Severs 
      👉 잡 큐를 가져와서 할 일이 있는지 확인하고, 있다면 큐에서 잡을 뽑아내서 실행한다 <br/><br/>
  
  - _**Full-Text Search Service (optional)**_
    - 사용자가 텍스트(쿼리)를 입력을 하면 가장 ‘관련 있는’ 결과를 보여주는 기능
    👉 쿼리 키워드를 포함하는 문서를 빠르게 찾기 위해 _역 인덱스(inverted index)_를 활용 <br/><br/>
  
  
  - _**Services**_
     - 앱에서 (별도의 애플리케이션으로) 분리해서 운영하기 위한 별도의 **웹 서버**
    👉  MSA(마이크로 서비스 아키텍쳐)처럼 구성 가능 
    👉 외부에는 노출되지 않지만, 앱이나 다른 서비스와는 연동된다. <br/><br/>
  
  - _**Data Warehouse**_
    - 거의 모든 앱은 특정 규모에 도달하면 데이터를 제어, 저장, 분석하기 위해 **데이터 파이프라인**을 사용한다. 전형적인 파이프라인의 주요 3단계를 살펴보자. 
    <br/>
    1. 사용자 상호작용으로 발생한 데이터를 데이터 **"firehose"**에 전달한다. 
    👉 데이터를 받아들이고 처리할 수 있는 스트리밍 인터페이스를 제공
    👉 가공되지 않은 원시 데이터는 변형되거나(transformed) 추가 정보와 함께(augmented) 다른 firehose로 전달
    👉 사용되는 기술 : AWS Kinesis, Kafka
  
  
    2. 원시 데이터와 최종 데이터 모두 **클라우드 스토리지**에 저장된다.
    👉 AWS Kinesis는 원시 데이터를 AWS의 클라우드 스토리지(S3)에 저장할 수 있도록 매우 쉽게 사용할 수 있는 ‘firehose’로 불리는 설정을 제공한다.
    3. 변형/추가된 데이터는 종종 분석을 위해 **데이터 웨어하우스(DW)**에서 로드된다.
     👉 사용되는 기술 : Oracle + 독점적인 데이터 웨어하우스 기술(규모가 큰 기업), AWS Redshift(스타트업) <br/><br/>
     
    
  
  - _**클라우드 스토리지**_
    - 데이터를 웹 서버의 파일 스토리지가 아닌 클라우드 스토리지에 저장한다. 
    👉 인터넷을 통해 데이터를 저장, 접근, 공유할 수 있는 단순하고 확장성 있는 방법(출처 : AWS)<br/>👉 로컬 파일 시스템에 저장할 수 있는 거의 모든 것(비디오, 사진, 오디오 데이터, CSS, 자바스크립트, 사용자 데이터 등)을 **RESTful API**를 사용하고 **HTTP**를 통해 클라우드에 저장하고 접근할 수 있다.<br/>👉 사용되는 기술 : AWS의 S3<br/><br/>
  - _**CDN**_
    - Content Delivery Network
    - 웹을 이용해서 HTML, CSS, 자바스크립트, 이미지 같은 **정적인 데이터**를 1개의 원본(origin) 서버를 사용하는 것보다 더 빠르게 제공하기 위한 기술이다.<br/>👉 콘텐츠를 전 세계의 많은 **엣지(edge) 서버**에 분산시키고 저장하는 방식으로 동작한다.<br/>👉 사용자는 데이터를 원본 서버 대신 사용자와 가장 가까운 엣지 서버에서 다운로드한다.<br/>👉 Ex ) 한국에 있는 사용자가 뉴욕에 있는 원본 서버의 웹페이지에 접근하면 정적인 데이터를 가져오기 위해 매우 느린 HTTP 요청을 하는 대신 일본에 있는 CDN 엣지 서버로부터 빠르게 다운로드 할 수 있음.<br/>👉  일반적인 웹 어플리케이션은 CSS, 자바스크립트, 이미지, 비디오 및 다른 정적인 데이터를 제공하기 위해 항상 CDN을 사용해야 한다.



# Web Server vs WAS

차이점 - 동적 컨텐츠를 지원하는가?

![](https://velog.velcdn.com/images/suran-kim/post/608b163a-bef8-40c5-bc7a-8950045ba53a/image.png)
이미지 출처 : http://setgetweb.com/p/WAS9/ae/was3532.html


## Web Server

- Http Web Sever를 줄여서 Web Server라고 한다.
- **Http 프로토콜**을 지원하는 서버.
- **정적 컨텐츠**(HTML, CSS, JS, 이미지 등)를 제공하는 서버.


- 클라이언트에서 요청이 왔을 때 **가장 앞단에서** 요청에 대한 처리를 한다.
- DB에 접근하지 않는다. Web Server는 static 컨텐츠를 포스팅하기 위해 사용한다.


- public하게 노출이 되어도 된다.

## WAS

- Web Application Server
- **동적 컨텐츠(DB 조회, 비즈니스 로직)**를 제공하기 위한 어플리케이션 서버.
- Web Server 역할 + 동적 컨텐츠 
_(주의 : WAS가 Web Server를 포함하는 것은 아니다!!!)_
 _(WAS는 정적, 동적 처리 둘 다 가능하지만 정적 처리를 WAS가 하게 되면 부하가 많이 걸려서 좋지 않다.)_


- **JSP, Servlet 구동 환경** 제공
- 컨테이너, 웹 컨테이너, 서블릿 컨테이너라고도 부른다.
_( JSP, servlet을 실행시킬 수 있는 소프트웨어 == **컨테이너** )_


- 클라이언트에서 요청이 왔을 때 Web Server의 **뒷단에서** 요청에 대한 처리를 한다
- DB와 연동이 된다.


- public하게 노출되면 안 된다. (Ex : 인증, 고객 정보 등) 




![](https://velog.velcdn.com/images/suran-kim/post/40d57a8e-ee1f-47e5-ad44-b2c9a4717b10/image.png)
이미지 출처 : https://gmlwjd9405.github.io/2018/10/27/webserver-vs-was.html





      - Web Server에서 Http 요청이 발생한다 -> 
      - Web Container의 Servlet을 구동시킨다 ->
      - DB Connection이 일어난다 -> 
      - 응답을 이용한 JSP페이지 or JSON를 생성해서 응답을 전달한다



      - 웹 어플리케이션을 만들면 빌드 후 WAS에 deploy한다.
      - WAS는 별도의 서버로 구동한다. (Ex: Tomcat, jetty)
      - 물리적인 서버에 설치하고 WAS를 구동시킨다. (?)
      - WAS를 .war 로 빌드하고 deploy -> WAS가 load된다. (?)
      - 핵심 : Tomcat등의 WAS에 어플리케이션을 load한다.
      
      + 국내 상용 WAS : 티맥스의 제우스 



# Servlet

![](https://velog.velcdn.com/images/suran-kim/post/90e4d8d7-51b3-4c58-8440-b465decb341f/image.png)


 동적 웹 페이지(Dynamic Web Page)를 만들 때 사용되는 자바 기반의 웹 애플리케이션 프로그래밍 기술. 
- 웹을 만들때는 다양한 요청(Request)과 응답(Response)이 존재하고 이런 요청과 응답에는 규칙이 존재한다.
- **서블릿**은 이런 웹 요청과 응답의 흐름을 간단한 **메서드 호출**만으로 체계적으로 다룰 수 있게 해주는 기술이다.
- **WAS 내부**에서 동작하고, WAS 내부에서 **클라이언트(브라우저)의 요청**을 받아 필요한 **Service 기능을 호출**해주는 **Component**가 바로 서블릿.
<br/>

_**서블릿 특징**_

- 클라이언트의 요청(Request)에 대해 동적으로 작동하는 웹 애플리케이션 컴포넌트
- HTML을 사용하여 응답(Response) 한다.
- JAVA의 스레드를 이용하여 동작한다.
- MVC 패턴에서의 컨트롤러로 이용된다.
- HTTP 프로토콜 서비스를 지원하는 javax.servlet.http.HttpServlet 클래스를 상속받는다.
- UDP보다 속도가 느리다.
- 단점 - HTML 변경 시 Servlet을 재 컴파일해야 한다.







_**서블릿 사용 흐름**_
- 자바의 Servlet은 인터페이스이다. (Servlet의 구현체 : Http Servlet)
- 실제 개발 시 **Http Servlet**을 확장해서 로직을 작성하게 된다.
- Http Servlet를 상속 받아서 메소드를 구현하면 WAS가 해당 메소드를 호출할 수 있다. 
- 클라이언트에서 요청 -> WAS가 메소드 호출 -> 서블릿을 상속받아 구현한 메소드가 실행 
- 즉, 서블릿은 웹 상의 **요청과 응답**에 대한 일종의 **프레임워크**
, WAS 개발과 Web 개발의 충돌 방지 (?)
WAS의 비즈니스 로직과 요청, 응답 처리 작업을 분리
 


_**서블릿 사용법**_
_**1. Container에 서블릿 등록**_
- Http Servlet을 상속받아서 구현한 서블릿 인스턴스를 new 로 생성하지 않고 **Container**에 등록한다.
- web.xml에 로직을 구현한 서블릿이 등록된다. (등록되는 거 내가 직접 하는 거? -> yes)
- WAS에 url-pattern과 맞는 요청(request)가 오면 서블릿의 메소드 호출

_**2. JSP 호출 **_
- `RequestDispatcher` 인스턴스를 이용해서 **동적인 JSP 페이지**를 제공할 수 있다.
- 서블릿이 JSP를 만들면 **WAS가 JSP를 읽어서 컴파일** 후 랜더링으로 만든 **HTML 페이지**를 응답으로 보내게 된다.



## MVC Pattern in Servlet

MVC 패턴이란?
- Model, View, Controller
- 각각의 컴포넌트들이 해야하는 관심사를 분리하는 것이다.
- 웹 어플리케이션을 만들 때 기능을 **화면, 로직, 데이터 저장**으로 분리


서블릿에서의 MVC 패턴이란? 
- _**Model** - 자바 객체 또는 Pojo로 데이터를 담고있다_
- _**View** - 모델이 담고 있는 데이터를 시각적으로 보여주는 역할_
- _**Controller** - 모델과 뷰를 연결하기 위한 **매개체**. 사용자의 입력/요청을 받아 모델의 상태를 변경해주고 그에 따른 뷰를 업데이트 해준다._
![](https://velog.velcdn.com/images/suran-kim/post/2da2d234-3163-4eb7-ac95-ed53388c52bf/image.png)
이미지 출처 : https://www.javatpoint.com/MVC-in-jsp

_(Spring에서만 MVC 패턴을 지원하는 게 아니다~)_




## Servlet의 Life Cycle
![](https://velog.velcdn.com/images/suran-kim/post/dddaa06e-8227-4b42-9b0b-cdf588a6f60e/image.png)
이미지 출처 : https://gmlwjd9405.github.io/2018/10/27/webserver-vs-was.html

_Servlet의 Life Cycle 핵심_

1. **init()**
2. **service()**
3. **doGet()** 혹은 **doPost()**

<br/>

_Web Service Architecture_
- Web Server는 웹 브라우저 클라이언트로부터 HTTP 요청을 받는다.
- Web Server(Proxy)는 클라이언트의 요청(Request)를 WAS에 보낸다.
- WAS(컨테이너)는 관련된 Servlet을 메모리에 올린다.
- WAS는 web.xml을 참조하여 해당 Servlet에 대한 Thread를 생성한다. (Thread Pool 이용)
- HttpServletRequest와 HttpServletResponse 객체를 생성하여 Servlet에 전달한다.
  _(주의! Thread가 매번 Servlet 인스턴스를 만드는 게 아니다!)_
  _(Servlet 인스턴스는 단 한 번 init() 되고 서버가 닫힐 때 제거된다.)_
  - Servlet의 **init()**이 실행된다.
  - Thread는 Servlet의 **service()** 메소드를 호출한다.
  - service() 메서드는 요청에 맞게 **doGet()** 또는 **doPost()** 메서드를 호출한다.
  _(매 요청마다 doGet() 또는 doPost() 호출)_
  - protected doGet(HttpServletRequest request, HttpServletResponse response)
- doGet() 또는 doPost() 메서드는 인자에 맞게 생성된 적절한 동적 페이지를 Response 객체에 담아 WAS에 전달한다.
- WAS는 Response 객체를 HttpResponse 형태로 바꾸어 Web Server에 전달한다.
- 생성된 Thread를 종료하고, HttpServletRequest와 HttpServletResponse 객체를 제거한다.



**_알아두자!_**
- 매 요청마다 **Thread**가 만들어져서 호출되는 것이다.
  - 매 요청마다 Servlet 객체(인스턴스)가 만들어지는 게 아니다!!
- 멀티 스레드 환경이기 때문에 필드 단위 접근 주의!
  - 여러 클라이언트가 필드 값에 대한 요청을 한다면 기존 요청과 새로운 요청들이 뒤섞일 수 있다.
  - 서블릿에서는 무조건 메소드 안에서 변수를 만들고 접근하도록 하자.  


## Servlet 코드


- `spring-boot-starter-web` 의존성 필요


### web.xml으로 서버를 등록하는 방법
💡 _요청하는 순간 init 호출_


1. web.xml 생성
- [src] - [main] - [webapp 패키지 생성] - [WEB-INF 패키지 생성] - [**web.xml** 생성]


```xml
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
         http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">

    <servlet>
        <servlet-name>test</servlet-name>  <!-- 서블릿 이름 -->
        <servlet-class>org.prgrms.kdt.servlet.TestServlet</servlet-class> <!-- 서블릿 클래스 -->
    </servlet>
    <servlet-mapping>
        <servlet-name>test</servlet-name>
        <url-pattern>/*</url-pattern> <!-- 매핑될 URL : 하위의 모든 경로을 서블릿에 매핑시킨다. -->
    </servlet-mapping>
    <servlet>
        <servlet-name>order</servlet-name>  <!-- 여러 개의 서블릿을 매핑 가능 -->
        <servlet-class>org.prgrms.kdt.servlet.OrderServlet</servlet-class> <!-- 서블릿 클래스 -->
    </servlet>
    <servlet-mapping>
        <servlet-name>order</servlet-name>
        <url-pattern>/orders/*</url-pattern> <!-- 매핑될 URL : orders URL 패턴 하위의 모든 경로을 서블릿에 매핑시킨다. -->
    </servlet-mapping>
</web-app>
```


2. 서버 배포 (WAS에 deploy)
- intellij의 [edit Configuration] - [Add new Configuration] - [Tomcat Server] - [local]
- 톰캣 파일 압축 풀고 Configuration에 경로 설정해주기
- Deployment에 프로젝트 넣어주기
  - [Fix] 
  - [Project Setting - Artifacts] - [add] - [Web Application : Exploded] - [From Module]
- Facet 세팅
  - [Project Setting - Facet] - [Web] - [add] - [web.xml 추가]  
- out 폴더 생성
  - 실행하는 순간 build된 파일이 옮겨지고 deploy되는 구조. (자바 파일이? out이?)
  ![web.xml경로](https://velog.velcdn.com/images/suran-kim/post/cd87fbda-66fb-4b5c-ac0e-87863554cab2/image.png)



```java
// 서블릿 

public class TestServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(TestServlet.class);

    @Override
    public void init() throws ServletException {
        super.init();  // 서버 구동 확인 : init() 이 잘 호출되는지?
        logger.info("Init Servlet");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { // 요청, 응답에 대한 파라미터 전달
        var requestURI = req.getRequestURI();
        logger.info("Got Request from {}", requestURI); // 요청이 올 때마다 로그 출력

        var writer = resp.getWriter();
        writer.println("Hello Servlet!");
    }

}


// 로그 

// 2022-08-05 20:40:20.105  INFO   --- [nio-8080-exec-1] org.prgrms.kdt.servlet.TestServlet       : Init Servlet
// 2022-08-05 20:40:20.121  INFO   --- [nio-8080-exec-1] org.prgrms.kdt.servlet.TestServlet       : Got Request from /kd_spring_order_war_exploded/
// 2022-08-05 20:40:20.240  INFO   --- [nio-8080-exec-4] org.prgrms.kdt.servlet.TestServlet       : Got Request from /kd_spring_order_war_exploded/

// 웹 페이지 URL에 입력하고 enter를 치면 HTTP GET 요청이 발생한다.

```
  
  



### 어노테이션을 이용하는 방법
💡 _요청을 받기 전에 미리 init _
  - @WebServlet (value = "`패스(경로)`", loadOnStartup = `1`)
    - loadOnStartup = `-1` : 기본값. 요청을 받을 때 init
    - loadOnStartup = `-1` : 요청을 받기 전에 init

web.xml이 없으면 tomcat을 구동시켜도
WAS는 deploy가 없어도 모른다.?
WAS에게 알려주기 위한 어노테이션

```java
// 서블릿

@WebServlet(value = "/*", loadOnStartup = -1) 
public class TestServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(TestServlet.class);

    @Override
    public void init() throws ServletException {
        super.init();  // 서버 구동 확인 : init() 이 잘 호출되는지?
        logger.info("Init Servlet");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { // 요청, 응답에 대한 파라미터 전달
        var requestURI = req.getRequestURI();
        logger.info("Got Request from {}", requestURI); // 요청이 올 때마다 로그 출력

        var writer = resp.getWriter();
        writer.println("Hello Servlet!");
    }

}
```

### spring 제공 인터페이스 구현


- 서블릿 3.0부터 지원
- WAS가 클래스 패스에 있는 모든 클래스를 스캔
  - WebApplicationInitializer (인터페이스)의 구현체가 있으면? -> 실행한다. 
  - 구현체를 인스턴스화시키고 onStartup 메소드 호출 


- web.xml은 선언적 접근
- WAS에 서버가 올라가기 전 선행 / 후행 코드를 작성해야할 필요가 있을 때 사용할 수 있는 방법




_**원리**_
- WebApplicationInitializer는 스프링에서 제공
- SpringServletContainerInitializer는 서블릿에서 제공
  - ServletContainerInitializer를 상속받는다.
  - WebApplicationInitializer를 찾는다.
  - WebApplicationInitializer으로 onStartup 메소드를 호출한다.


그래서 ServletContainerInitializer를 직접 구현해도 되지만
WebApplicationInitializer을 구현해서 처리할 수도 있는 것이다.

- 스프링 MVC 프로젝트에서는 대체로 WebApplicationInitializer을 구현한다.
- 서블릿이 Initializer될 때 서블릿이 호출하는 클래스

```java

public class KdtWebApplicationInitializer implements WebApplicationInitializer {
    private static final Logger logger = LoggerFactory.getLogger(KdtWebApplicationInitializer.class);
    // servletContext가 생성된다. WAS가 접근 가능
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        logger.info("Staring Server...");
        var servletRegistration = servletContext.addServlet("test", new TestServlet());// 테스트 서블릿을 추가할 수 있다.
        servletRegistration.addMapping("/*"); // 서블릿 매핑 시 넣었던 URL 패턴 입력
        servletRegistration.setLoadOnStartup(1);
    }
}

// 로그
// 2022-08-05 21:16:20.953  INFO   --- [on(3)-127.0.0.1] o.p.k.s.KdtWebApplicationInitializer     : Staring Server...
// 2022-08-05 21:16:20.994  INFO   --- [on(3)-127.0.0.1] org.prgrms.kdt.servlet.TestServlet       : Init Servlet
// [2022-08-05 09:16:21,013] Artifact kd-spring-order:war exploded: Artifact is deployed successfully
// [2022-08-05 09:16:21,013] Artifact kd-spring-order:war exploded: Deploy took 2,797 milliseconds
// 2022-08-05 21:16:21.347  INFO   --- [nio-8080-exec-1] org.prgrms.kdt.servlet.TestServlet       : Got Request from /kd_spring_order_war_exploded/
// 2022-08-05 21:16:21.478  INFO   --- [nio-8080-exec-4] org.prgrms.kdt.servlet.TestServlet       : Got Request from /kd_spring_order_war_exploded/
```


우리가 한 방식은 톰캣을 서버에 미리 설치해놓고 톰캣 구동 -> 톰캣에 war를 빌드하는 방식 
remote면 war로 말아서 deploy
앞으로는 embedded Tomcat을 쓸 것이다.
legacy 어플리케이션에서는 WAS로 배포하기도 
모던 아키텍쳐에서는 embedded Tomcat / embedded Survlet으로 말아서 jar로 배포하는 경우가 많다.



  <br/><br/><br/><br/><br/><br/><br/><br/>
  
> _**오류해결**_
  
> _**새로 알게된 용어**_
- _**토큰**_ : 인증 처리 시 필요
- _**REST란? “Representational State Transfer” 의 약자**_ <br/> 자원을 이름(자원의 표현)으로 구분하여 해당 자원의 상태(정보)를 주고 받는 모든 것을 의미한다.<br/> 출처 : https://gmlwjd9405.github.io/2018/09/21/rest-and-restful.html
- _**수직확장과 수평확장**_
수평확장(Horizental Scaling), Scale Out - 장비를 추가해서 확장하는 방식 (서버 추가 ) <br/>수직확장(Vertical Scaling), Scale Up - 기존 하드웨어를 보다 높은 사양으로 업그레이드하는 것<br/>출처 : https://tech.gluesys.com/blog/2020/02/17/storage_3_intro.html
- _**JSP (JavaServer Pages )**_
Java 언어를 기반으로 하는 Server Side 스크립트 언어.
HTML 코드에 JAVA 코드를 넣어 동적 웹페이지를 생성하는 웹 어플리케이션 도구.
JSP를 통해 정적인 HTML과 동적으로 생성된 contents(HTTP 요청 파라미터)를 혼합하여 사용할 수 있다. 즉, 사용자가 입력한 contents에 맞게 동적인 웹 페이지를 생성한다
https://gmlwjd9405.github.io/2018/11/03/jsp.html
출처: https://javacpro.tistory.com/43, https://gmlwjd9405.github.io/2018/11/03/jsp.html
- _**POJO**_
 본래 자바의 장점을 살리는 '오래된' 방식의 '순수한' 자바객체
 즉, 특정 '기술'에 종속되어 동작하는 것이 아닌 순수한 자바 객체를 의미한다.
 출처 : https://siyoon210.tistory.com/120
  
> _**TIP**_
  - 기본은 80포트
  - TCP는 비동기형 프로토콜이다.
  - 브라우저가 URI를 통해 특정 리소스에 접근하면 HTTP프로토콜을 타고 서버에 있는 하이퍼 텍스트 리소스를 브라우저가 받아와서 랜더링한다. 하이퍼 텍스트 리소스에 링크가 있다면 또 다른 HTTP 프로토콜을 타고 또 다른 서버에 있는 리소스를 … 그런 흐름이다.
  - URL은 URI의 서브셋이지만 URI와 URL의 가장 큰 차이점은 다음과 같다. <br/> **URI**는 식별하고, **URL**은 위치를 가리킨다.<br/>출처 : https://www.charlezz.com/?p=44767
  - Java Servlet은 현재 Jakarta Servlet이라는 이름으로 불린다.
  - 서블릿의 동작 - 웹 브라우저에서 서버에 요청(Request)을 하면 Request(일반적으로 Http)에 맞는 동작을 수행하고 웹 브라우저에 **HTTP형식**으로 응답(Response)한다. 결과 : JSP 페이지. <br/>JDBC에서 underline 작업에 대해서 몰라도 connection을 통해 DB를 조작할 수 있었던 것처럼<br/>서블릿을 이용하면 WAS에서 실제 호출되는 과정을 몰라도 웹 브라우저의 요청과 응답에 대한 조작이 가능해진다.<br/>
  - 하나의 요청은 하나의 스레드 안에서 돌게 된다.
  

  
> _**더 공부하면 좋을 포스팅**_
-   [Mdn 문서 - HTTP 개요](https://developer.mozilla.org/ko/docs/Web/HTTP/Overview)
-  [LITSLINK - 웹 어플리케이션 아키텍쳐](https://litslink.com/blog/web-application-architecture)
- [HeeJeong Kwon 님의 [Web] Web Server와 WAS의 차이와 웹 서비스 구조](https://gmlwjd9405.github.io/2018/10/27/webserver-vs-was.html)
- [HeeJeong Kwon 님의 [Web] JSP란 (Java Server Pages)](https://gmlwjd9405.github.io/2018/11/03/jsp.html)
- [siyoon210 님의 POJO - (Plain Old Java Object)란 뭘까?](https://siyoon210.tistory.com/120)
  
>_**Rf**_
- [shaking 님의 절대경로와 상대경로](https://88240.tistory.com/122)
-   [LITSLINK - 웹 어플리케이션 아키텍쳐](https://litslink.com/blog/web-application-architecture)
- [Bok Jiho님의 웹 아키텍처의 구성 요소와 구조](https://bokjiho.medium.com/%EC%9B%B9-%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98%EC%9D%98-%EA%B5%AC%EC%84%B1-%EC%9A%94%EC%86%8C%EC%99%80-%EA%B5%AC%EC%A1%B0-694e994f798) 
- [[번역] 웹 아키텍쳐 입문](https://blog.rhostem.com/posts/2018-07-22-web-architecture-101)
- [김백개발자 님의 WAS, Tomcat 그리고 Jetty](https://baek-kim-dev.site/119)
- [코딩 팩토리 님의 [Web] 서블릿(Servlet)이란 무엇인가? 서블릿 총정리](https://coding-factory.tistory.com/742)
-  [HeeJeong Kwon 님의 [Web] Web Server와 WAS의 차이와 웹 서비스 구조](https://gmlwjd9405.github.io/2018/10/27/webserver-vs-was.html)
  
  
  