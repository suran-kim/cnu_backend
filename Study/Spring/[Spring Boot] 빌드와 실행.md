> _김영한의 스프링 입문 - 코드로 배우는 스프링 부트, 웹 MVC, DB 접근 기술 강의를 공부하며 작성한 글입니다._

# 목표
스프링부트를 이용해 프로젝트를 실행가능한 `jar`파일로 빌드하고 웹에서 `jar`파일을 실행해본다. 


<br/>
<br/>

# 빌드하기


![](https://velog.velcdn.com/images/suran-kim/post/4f4af1af-8047-496d-be66-768bcf32dc11/image.png)
- 터미널에서 `dir` 명령어 실행
- 윈도우: `gradlew.bat` 파일의 위치 확인 
mac: `gradlew` 파일의 위치 확인


<br/>

![](https://velog.velcdn.com/images/suran-kim/post/5663a3aa-a155-4b91-82f8-d1f78e1184a4/image.png)
- `./gradlew.bat build` 명령어 실행


<br/>

![](https://velog.velcdn.com/images/suran-kim/post/cdcddcf3-f90f-43ec-b2c7-515eccb27757/image.png)

- build 폴더에 생성된 파일들 확인 가능

<br/>

![](https://velog.velcdn.com/images/suran-kim/post/ac3c2a4e-5491-49c2-b611-b8f374722d8a/image.png)

- `cd build/libs` 명령어를 이용해서 build폴더 내부의 lib폴더로 이동해보면 생성된 `jar`파일 확인가능


<br/>
<br/>

## 👉 빌드 재수행

![](https://velog.velcdn.com/images/suran-kim/post/3f932280-85bb-4b0d-ad6b-89f7a4cef34c/image.png)

- 혹시 빌드 과정에서 실수가 발생하거나 빌드를 다시 수행해야하는 경우 `gradlew 폴더`가 존재하는 디렉토리로 돌아가서`./gradlew.bat clean build` 혹은 `./gradlew.bat clean` 명령어를 실행한다. 그럼 build 폴더 자체가 사라진다.


---

# 실행하기


![](https://velog.velcdn.com/images/suran-kim/post/ba1bed81-d005-4ac9-83f4-3a17db3e8293/image.png)


- `java -jar .jar파일명` 키워드로 실행할 수 있다.

![](https://velog.velcdn.com/images/suran-kim/post/7d6f898c-fd94-4891-a47f-733c8f59b6f9/image.png)

- `localhost:8080`에 들어가보면 소스코드의 내용이 홈페이지에 적용된 것을 볼 수 있다.

- 서버 배포 시 빌드된`jar`파일을 복사해서 서버에 넣고, `java -jar .jar파일명`  키워드로 실행시킨다. 이후 서버에서도 Spring이 동작한다.


<br/>

