

## Load Balancer 용어
- VPC (Virtual Private Cloud)
Public Cloud 상에서 논리적으로 완전히 분리된 **고객 전용 네트워크**를 제공하는 서비스로, 최대 /16의 IP 네트워크 공간 사용 가능 (IP 대역: RFC 1918)


- VPC 제공 여부에 따른 플랫폼 용어(Classic / VPC)
  - Classic 환경
퍼블릭 클라우드에서 고객 전용 사설 네트워크인 VPC(Virtual Private Cloud)를 제공하지 않는 플랫폼
  - VPC 환경
퍼블릭 클라우드에서 고객 전용 사설 네트워크인 VPC(Virtual Private Cloud)를 제공하는 플랫폼


- Load Balancer (LB)
서버의 성능과 부하량을 고려해 네트워크 트래픽을 **다수의 서버로 분산**시켜 주는 서비스


- Load Balancing
병렬로 운영되는 기기 사이에서 부하가 균등하게 되도록 하는 행위

- Target Group
**요청을 처리할 대상**에 대한 집합



- Subnet
어떤 기관에 소속된 네트워크이지만 따로 분리되어 있는 한 부분으로 인식될 수 있는 네트워크


- Health Check
  - Health Check
Auto Scaling Group의 가상 서버에 주기적인 상태 확인을 수행하여 **상태가 비정상**인 가상 서버를 식별하도록 체크하는 행위

  - Health Check Type
Health Check의 유형으로 **서버**와 **Load Balancer** 가운데 선택 가능. Auto Scaling Group 설정에서 Load Balancer 이름을 지정한 경우에는 Health Check 유형도 Load Balancer로 설정하며, 이런 경우 Auto Scaling은 Load Balancer Health Check 방식과 기준에 따라 서버의 상태를 판단


- 서버 부하 분산 방식

  - Least Connection
**가장 적은 수의 연결**이 이루어진 서버로 요청을 할당하는 서버 부하 분산 방식

  - Round Robin
지정된 서버들에 대해 **공평하게 순차적**으로 요청을 전달하는 서버 부하 분산 방식

  - Source Internet Protocol Hash (Source IP Hash)
**클라이언트의 Source IP 정보를 해시한 결과**로 로드밸런싱을 실행하는 서버 부하 분산 방식

- HyperText Transfer Protocol (HTTP)
**웹 서버**와 사용자의 **인터넷 브라우저** 사이에 문서를 전송하기 위해 사용되는 **통신 규약**


- Secure Socket Layer (SSL)
전자 상거래에서 신용 카드 번호 전송과 같이 **보안 관련 트랜잭션** 제공 시 사용되는 **웹용 암호화** 기술


- Transmission Control Protocol (TCP)
인터넷상의 컴퓨터들 사이에서 **데이터를 메시지 형태**로 보내기 위해 **IP**와 함께 사용되는 **프로토콜**로, IP 데이터를 전달하는 동안 **데이터 패킷을 추적 관리**하는 기능 수행

- Transport Layer Security (TLS)
인터넷상에서 통신 시 주고받는 데이터를 보호하기 위한 표준화된 **암호화 프로토콜**

- User Datagram Protocol (UDP)
인터넷 프로토콜 스위트의 주요 프로토콜 중 하나로 IETF의 RFC 768로 표준으로 정의되어 있으며, TCP와 함께 **데이터그램**으로 알려진 단문 메시지를 교환하는 용도로 사용

- Public IP Service
Public IP가 필요한 경우, 고객이 지정한 서버에 Public IP를 할당해 주는 서비스

- Private Internet Protocol Address (Private IP Address)
Private Subnet의 사설 IP 주소 영역에 포함되도록 할당하는 IP 주소로, 네트워크 주소를 포함하여 초기 10개의 IP는 관리용으로 예약되며, 네트워크 주소와 CIDR 형식의 서브넷 bit를 입력

- Sub Account
대표 계정(메인 계정) 하위에 생성하여 업무 역할별로 권한 관리를 하도록 지정할 수 있는 서브 계정

- User Created
 클라우드 플랫폼의 Sub Account에서 계정 사용자가 임의로 생성한 정책



## IP
- `IP(Internet Protocol)` 란 인터넷에 연결되어 있는 모든 장치들(컴퓨터, 서버 장비, 스마트폰 등)을 식별할 수 있도록 각각의 장비에게 부여되는 고유 주소이다.


### IPv4, IPv6
IP주소는 `IPv4`, `IPv6` 2가지 종류가 있으며, 일반적으로 말하는 IP 주소는 `IPv4` 주소를 의미한다. 

- IPv4
  - IPv4는 IP version 4의 약자로 전 세계적으로 사용된 첫 번째 인터넷 프로토콜
  - 주소는 32비트 방식으로, 8비트씩 4자리로 되어 있음. 각 자리는 온점으로 구분
  - IPv4는 0 ~ 2^32 (약 42억 9천)개의 주소를 가질 수 있는데, 전 세계적으로 인터넷 사용자 수가 급증하면서 IPv4 주소가 고갈될 위기 발생
  
- IPv6
  - `IPv6`는 IP version 6의 약자로, `IPv4`의 주소체계를 128비트 크기로 확장한 차세대 인터넷 프로토콜 주소
  - 주소는 128비트 방식으로, 16비트씩 8자리로 되어 있음. 각 자리는 콜론으로 구분

### 고정 IP, 유동 IP
- 고정 IP
  - `고정 IP`는 변하지 않고 컴퓨터에 **고정**적으로 부여된 IP
  - 한번 부여되면 IP 반납을 하기 전까지는 다른 장비에 부여할 수 없는 고유의 IP
  - 보안성이 우수하기 때문에 보안이 필요한 업체나 기관에서 사용
  
- 유동 IP 
  - `유동 IP`는 변하는 IP
  - 인터넷 사용자 모두에게 고정 IP를 부여해 주기는 힘들다
  - 일정한 주기 또는 사용자들이 인터넷에 접속하는 매 순간마다 사용하고 있지 않은 IP 주소를 임시로 발급해 주는 IP
  - 대부분의 사용자는 유동 IP를 사용함

### 공인 IP, 사설 IP

- 공인 IP
  - IP 주소는 임의로 부여할 수 없고 ICANN이라는 기관이 국가별로 사용할 IP 대역을 관리한다.
우리나라는 한국인터넷진흥원(KISA)에서 국내 IP 주소들을 관리한다. 
  - 이것을 ISP(Internet Service Provider의 약자로 KT, LG, SKT와 같이 인터넷을 제공하는 통신업체)가 부여받는다.
  - 이용자들은 ISP에 가입해 IP를 제공받아 인터넷을 사용하게 된다.
이렇게 발급받은 IP를 `공인 IP`라고 한다.

- 사설 IP
  - 공유기를 사용한 인터넷 접속 환경일 경우 공유기까지는 `공인 IP`를 할당한다.
  - 공유기에 연결되어 있는 가정이나 회사의 각 네트워크 기기에는 `사설 IP`를 할당한다.
  - 사설 IP는 어떤 네트워크 안에서 내부적으로 사용되는 고유한 주소

- 공인 IP와 사설 IP 비교
  - `공인 IP`는 전 세계에서 유일하지만, `사설 IP`는 하나의 네트워크 안에서 유일하다.
  - `공인 IP`는 외부, 내부 상관없이 해당 IP에 접속할 수 있지만 `사설 IP`는 내부에서만 접근이 가능하다.


## 서브넷 
- 서브넷 등장배경 
흔히 사용되는 IPv4 주소 체계는 클래스를 나누어 IP를 할당한다. 어떤 기관에 A클래스를 할당하면 16,777,214개의 호스트를 할당할 수 있다. 이 기관이 100개의 호스트만 할당한다고 해도 나머지 호스트는 사용되지 않고 낭비된다. **서브넷(subnet)**은 이처럼 호스트가 낭비되는 일을 방지하기 위해 등장하게 되었다.


- 서브넷
  - IP 주소에서 네트워크 영역을 부분적으로 나눈 부분 네트워크
  - 간단히 설명하자면 IP주소를 잘게 쪼개서 사용하는 것.
  - 일반 인터넷과 같은 기능을 수행하는 것은 맞지만 IP주소를 나눠서 쓰는 것과 같다.
  - 네트워크 장치들의 수에 따라 효율적으로 사용할 수 있다.

- 서브넷 마스크
  - 서브넷을 만들 때 사용된다
  - IP 주소 체계의 Network ID와 Host ID를 분리하는 역할

- 서브넷팅(subnetting)
  - 서브넷팅은 IP 주소 낭비를 방지하기 위해 원본 네트워크를 여러개의 서브넷으로 분리하는 과정을 뜻한다
  - 서브넷팅은 서브넷 마스크의 bit 수를 증가시키는 것이라고 생각하면 이해가 편함 
  - 서브넷마스크의 bit수를 1씩 증가시키면 할당할 수 있는 네트워크가 2배수로 증가하고 호스트 수는 2배수로 감소한다



## 방화벽
- 수신/발신 네트워크 트래픽을 모니터링하고 제어하는 네트워크 보안 시스템. 관리자가 설정해 놓은 보안 규칙(ACL, Access Control List)에 따라 허가되지 않은 외부 접속 시도를 차단하여 내부 정보 자산을 보호한다.

- 외부 네트워크와 내부 네트워크의 경계선에 위치한다.


![](https://velog.velcdn.com/images/suran-kim/post/fa46aed8-84e1-4de0-96ba-86baa8fd67d3/image.png)

- 방화벽 주요 기능
   1. 접근제어(Access Control)
      - 들어오고 나가는 패킷에 대해 허용/차단
      - 구현 방법에 따라 '패킷 필터링 방식'과 '프록시 방식'으로 나뉨

    2. 감사 및 로깅(Auditing/Logging)
      - 트래픽에 대한 접속 정보 기록

  3. 인증(Authentication)
      - 여러 인증방법(메시지 인증, 사용자 인증, 클라이언트 인증)을 사용하여 접속하는 호스트에 대한 정당성 여부 검사
  4. 데이터 암호화(Data Encryption)
      - 방화벽에서 다른 방화벽까지 전송되는 데이터를 암호화해서 보냄
  5. 네트워크 주소 변환(NAT, Network Address Translation)
     - IP 패킷의 TCP/UDP 포트 숫자와 소스 및 목적지의 IP 주소 등을 재기록하면서 라우터를 통해 네트워크 트래픽을 주고 받음


## 가상화
- 가상화
  - 가상화란 가상화를 관리하는 소프트웨어(주로 Hypervisor)를 사용하여 하나의 물리적 머신에서 가상 머신(VM)을 만드는 프로세스
  - Hypervisor는 가상화 층을 구현하여 물리적 머신의 컴퓨팅 리소스로부터 가상 환경을 분리하고 가상 머신(VM)을 생성
  - Hypervisor는 필요에 따라 각 VM에 cpu와 메모리 및 스토리지와 같은 컴퓨팅 리소스를 할당
  
- 가상화 장점
  - Server Consolidation: 물리적인 서버의 개수를 줄여 1개의 서버로 통합함으로써 서버의 전력 및 냉각 비용, 하드웨어 공간 비용 등을 줄일 수 있다.
  - Isolation: 기능에 맞게 여러 개의 머신으로 분리하여 Failures나 Security Leaks 등에 더욱 잘 대처할 수 있다.
  - Efficiency: 컴퓨팅 자원의 사용을 최대화하고 보다 쉽게 관리 할 수 있다.
  - Flexibility: 한 서버의 데이터를 마이그레이션하기에 용이해진다. <br/><br/>  
  - (+)그 외에도 가상화를 통해 동일한 머신에서 다양한 유형의 앱, 데스크탑 및 운영체제 실행이 가능하거나 새로운 어플리케이션을 프로비저닝 하는데 걸리는 시간을 줄일 수 있다
  


- 클라우드 컴퓨팅
  - 클라우드 컴퓨팅은 인터넷을 통해 공유 컴퓨팅 리소스, 소프트웨어 또는 데이터를 제공하는 방식

> _**출처**_  
- [네이버 클라우드 Load Balancer 용어사전](https://guide.ncloud-docs.com/docs/loadbalancer-glossary)
- [매일의 공부 기록](https://study-recording.tistory.com/7)
- [[네트워크] 서브넷,  서브넷마스크, 서브넷팅이란? | 서브넷팅 예제](https://code-lab1.tistory.com/34)
- [서브넷과 서브넷 마스크에 대한 설명](https://dany-it.tistory.com/102)
- [[네트워크] 방화벽(Firewall)](https://change1212.tistory.com/m/22)
- [[Server] 가상화(Virtualization)란? (1/2)
출처: https://mangkyu.tistory.com/86 [MangKyu's Diary:티스토리]](https://mangkyu.tistory.com/86)
- 이미지 출처 : [위키백과](https://ko.wikipedia.org/wiki/%EB%B0%A9%ED%99%94%EB%B2%BD_%28%EB%84%A4%ED%8A%B8%EC%9B%8C%ED%82%B9%29)

> _**이전에 공부했던 관련 내용**_
- [아이티센 클라우드 basic](https://velog.io/@suran-kim/%EC%95%84%EC%9D%B4%ED%8B%B0%EC%84%BC-%EB%A9%98%ED%86%A0%EB%A7%81-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EB%A9%98%ED%86%A0%EB%A7%81-%EC%84%A4%EB%AA%85%ED%9A%8C)
- [[springBoot] 웹 기술, Servlet](https://velog.io/@suran-kim/springBoot-%EC%9B%B9-%EA%B8%B0%EC%88%A0-Servlet#http)

> _**더 공부할 내용**_
- [[네트워크] CIDR 범위 쉽게 계산하는 방법](https://kim-dragon.tistory.com/154)
