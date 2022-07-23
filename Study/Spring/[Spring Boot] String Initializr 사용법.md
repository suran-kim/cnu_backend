>_본 포스팅은 프로그래머스 미니 데브 코스를 공부하며 
학습을 기록하기 위한 목적으로 작성된 글입니다._

# String Initializr의 사용방법

Maven/Gradle에 기반한 프로젝트를 만들고
pom.xml 이나 build.gradle을 통해 dependency를 직접 수정해 본다.


springBoot의 등장
원형이 되는 pom파일을 만들고 -> 아티팩트를 만들고 매번 프로젝트마다 만들었는데
springBoot가 나온 이후부터는 이렇게 매번 설정할 필요가 없어졌다.
springBoot를 통해 어플리케이션 프로젝트를 제작할 수 있다.

두 가지 방법이 있다
1. SpringBootCLI
2. Spring Initializ

SpringBoot의 Starter를 이용해서 프로젝트가 손쉽게 구성된다.


<br/>
<br/>

## 💿 1. SpringBootCLI

공식 문서에 따라 설치할 수 있다.
scoop을 통해서 간단하게 설치
커맨드라인에 `spring`을 입력하면 사용가능한 커맨드 목록이 나타난다. 

<br/>


![](https://velog.velcdn.com/images/suran-kim/post/dd76fd13-ef3e-4bc4-abc1-2f4a13fa736f/image.png)

run을 이용한 간단한 groovy script작성 및 실행
서버 실행을 직접 할 수 있다.


```java

```
springBootCLI에서
모양이 쉘에 들어갈 수 있음?
spring 쉘이므로 
spring이 인지하는 명령어를 바로 입력 가능

help:사용가능한 커맨드 목록
version: spring Shell의 버전 확인
init : 스프링부트 프로젝트를 다운받아서 저장 



쉘에 들어가서 해도 되고
`spring init` 명령어로 바로 실행할 수도 있다.
`spring init` 에는 옵션을 여러가지 넣을 수 있다


```
// 스프링부트 프로젝트를 오버라이드
spring init --force 
```



```java
// 빌드를 maven으로 하고, java버전을 16으로, 
// coordinate정보(그룹아이디, 아티팩트 아이디, 프로젝트 네임)을 옵션으로 설정
spring init --build maven -j 16 -g 그룹 아이디 - a 아티팩트 아이디 -n 프로젝트 네임
``` 
이를 zip파일로 만들고 싶지 않을 때는 `-x kdt_spring_demo` 키워드를 추가하면 된다.

```
spring init --build maven -j 16 -g 그룹 아이디 - a 아티팩트 아이디 -n 프로젝트 네임 -x 프로젝트 네임
```


![](https://velog.velcdn.com/images/suran-kim/post/ba868103-587b-4a60-b27c-1d530e36e69a/image.png)

![](https://velog.velcdn.com/images/suran-kim/post/1a1c29ea-ff67-438a-9c70-d2aa4d17ce87/image.png)

Using service at https://start.spring.io
이 커맨드는 실제로는 위 사이트의 기능을 이용하는 것이다.

```
// 방금 만든 프로젝트를 idea에서 열기
idea 프로젝트 이름.zip
```
![](https://velog.velcdn.com/images/suran-kim/post/4e9dd465-377c-4f64-95f7-88991ede5b3f/image.png)

프로젝트를 열면 maven dependency가 알아서 다운받아주고 처리된 다음 maven 폴더구조가 보인다.

initializr 제작 시 아무 옵션도 주지 않았다면 pom.xml에 기본적으로 springBoot와 관련된 `start`와 `test` 아티팩트 두 가지만 들어가게 된다.

```xml
    <dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
    <dependencies>      
```

initializr에서 `ADD DEPENDENCIES`를 이용한 옵션 선택을 하면, 해당 아티팩트의 기능을 바로 사용할 수 있다. 예를 들어 `JDBC API`를 추가하면 JDBC starter가 추가되면서 JDBC를 바로 이용할 수 있게 되는 것이다. 추후 공부 예정

만일 웹 프로젝트를 제작한 다면 `JDBC API`와 `Spring Web` Dependency를 추가할 필요가 있을 것이다. 그럼 spring initializr에서 web과 JDBC를 사용할 수 있는 프로젝트를 만들어준다.

<br/>

shell에서 Dependency 목록을 볼 수도 있다.
```
// 사용가능한 명령어 목록 출력
spring help init

// Dependency 목록 출력
spring init --list
```
![](https://velog.velcdn.com/images/suran-kim/post/e706bed3-2267-4eee-a998-9d575a5a3f51/image.png)


## 💿 2. Spring Initializr를 이용한 파일 다운

📌 https://start.spring.io
실질적으로 커맨드라인도 이 홈페이지를 통해 다운받는 거임



## 💿 3. InteliJ를 이용한 프로젝트 생성

inteliJ를 통해서도 프로젝트 생성이 가능하다. 
[NEW - Spring initializr]

![](https://velog.velcdn.com/images/suran-kim/post/e910a9b5-e101-4e4a-a8c9-3e25f5230918/image.png)
![](https://velog.velcdn.com/images/suran-kim/post/48542591-34bc-43c2-98f3-b1dc98f4a13a/image.png)


```xml
<!-- pom.xml에 작성된 web과 jdbc의존성 확인가능-->
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```
추후 더 심화 학습 예정

<br/>
<br/>

---

# 정리
maven, gradle 프로젝트 생성 방법, spring initializr를 이용한 다양한 애플리케이션 제작 방식을 학습했다.