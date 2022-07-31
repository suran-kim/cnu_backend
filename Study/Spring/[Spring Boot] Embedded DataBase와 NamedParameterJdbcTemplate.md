>_본 포스팅은 프로그래머스 미니 데브 코스를 공부하며 
학습을 기록하기 위한 목적으로 작성된 글입니다._


# JDBC


## DB 연동 통합 테스트 코드

테스트가 외부환경(Database)에 영향을 받으면 테스트 자동화가 어려워진다.
스프링에서는 이를 해결하기 위해 **Embedded Database**를 제공해준다.
테스트 시 일반적으로 Embedded Database를 사용한다.

<br/><br/>

### Embedded Database
- pom.xml에 **H2** 의존성 추가
- Embedded Database 자체가 DataSource를 return해준다.
- 그런데 H2에서는 특정 bender 사의 function (ex: _UUID_TO_BIN_ )을 지원하지 않는다.
<br/>
_해결 방법_
  1. H2 대신 **Embedded Mysql**를 사용한다.
  2. SQL문을 표준 ANSI에 맞춰 작성한다.
  
  <br/>

#### _Embedded Database 를 이용한 테스트 코드_

```java
@Configuration
    @ComponentScan(
            basePackages = {"org.prgrms.kdt.customer"}
    )

    static class Config {

        @Bean
        public DataSource dataSource() {
            return new EmbeddedDatabaseBuilder()
                    .generateUniqueName(true)
                    .setType(H2) // H2 EmbeddedDatabase 구동
                    .setScriptEncoding("UTF-8")
                    .ignoreFailedDrops(true)
                    .addScript("schema.sql") // resource 폴더에 schema.sql 파일 생성해서 테이블 생성 가능
                    .build();
        }

        // JdbcTemplate 사용을 위한 Bean 설정
        @Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }
    }

```


<br/>

### Embedded Mysql
- wix에서 만든 오픈소스


- pom.xml에 **wix-embedded-mysql** 의존성 추가
- scope는 test로 설정해준다. -> `<scope>test</scope>`
- _EmbeddedDatabaseBuilder_ 를 사용할 수 없다.


- 테스트가 실행되면 Embedded Mysql이 구동 -> 특정 버전에 맞는 데이터 베이스 구동

#### _Embedded Mysql 를 이용한 테스트 코드_

```java
	@Configuration
    @ComponentScan(
            basePackages = {"org.prgrms.kdt.customer"}
    )

    static class Config {

        @Bean
        public DataSource dataSource() {
            var dataSource = DataSourceBuilder.create()
                    .url("jdbc:mysql://localhost:2215/test-order_mgmt") // setup()메소드에서 port를 2215로 설정해주었기 때문에 localhost:2215로 변경
                    .username("test")                                   //                 schema이름도 설정해준대로 변경
                    .password("test1234!")
                    .type(HikariDataSource.class) // (기본) HikariDataSource가 pool에 10개의 connection을 채워넣는다.
                    .build();
            dataSource.setMaximumPoolSize(1000); // connection 사이즈를 1000으로 설정
            dataSource.setMinimumIdle(100); // 기본 connection을 100개로 설정
            return dataSource;
        }

        // JdbcTemplate 사용을 위한 Bean 설정
        @Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }
    }

    @Autowired
    CustomerJDBCRepository customerJDBCRepository;

    @Autowired
    DataSource dataSource; // 등록된 bean

    Customer newCustomer;

    EmbeddedMysql embeddedMysql;

    // 실제 DB구동 시작
    @BeforeAll
    void setup() {
        newCustomer = new Customer(UUID.randomUUID(), "test-user", "test1-user@gmail.com", LocalDateTime.now());
        var mysqlConfig = aMysqldConfig(v8_0_11) // aMysqldConfig이 Builder처럼 동작한다. -> db 생성?
                .withCharset(UTF8)
                .withPort(2215) // DB가 떠있는 것을 방지하기 위한 임의 포트 설정
                .withUser("test", "test1234!")
                .withTimeZone("Asia/Seoul") // 타임존 설정 가능
                .build();
        // config 전달 -> EmbeddedMysql 생성
       embeddedMysql = anEmbeddedMysql(mysqlConfig)
                .addSchema("test-order_mgmt", classPathScript("schema.sql")) // 스키마 추가
                .start(); // 서버 시작

        // embedded 사용 시 deleteAll() 사용 필요 x. DB가 오르내리면서 데이터가 리셋됨
    }

    @AfterAll
    void cleanup() {
        embeddedMysql.stop(); // EmbeddedMysql 멈춤
    }


```

<br/><br/>

## NamedParameterJdbcTemplate


- JdbcTemplate 종류 중 하나
- **이름 기반의 파라미터**를 설정할 수 있다.     _(`?`는 **인덱스 기반** palce holder )_
- 파라미터 표시는 `?` 에서 `:parameter_name`으로 변경된다. 


- **장점** - 순서 상관없이 key로 mapping되기 때문에 인덱스에 대한 고민 불필요.

<br/>

### NamedParameterJdbcTemplate를 이용한 CRUD

```java
@Repository // 컴포넌트 대상이 되기 위해 @Repository 추가
public class CustomerNamedJDBCRepository implements CusotomerRepository {

    private static final Logger logger = LoggerFactory.getLogger(JdbcCustomerRepository.class);

    // private final JdbcTemplate jdbcTemplate; ->
    private final NamedParameterJdbcTemplate jdbcTemplate;


    private static RowMapper<Customer> customerRowMapper = (resultSet, i) -> {
        // RowNum의 이름없는 메소드 구현: (resultSet, 인덱스) return Customer
        var customerName = resultSet.getString("name");
        var email = resultSet.getString("email");
        var customerId = toUUID(resultSet.getBytes("customer_id"));
        var lastLoginAt = resultSet.getTimestamp("last_login_at") != null ?
                resultSet.getTimestamp("last_login_at").toLocalDateTime() : null;
        var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        return new Customer(customerId, customerName, email, lastLoginAt, createdAt);
    };


    public CustomerNamedJDBCRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    // COUNT
    @Override
    public int count() {
        // jdbcTemplate.queryForObject(sql문, Map<String, ?>, class) retrun 단일 객체
        return jdbcTemplate.queryForObject("select count(*) from customers", Collections.emptyMap(), Integer.class); // count()함수의 return타입을 설정 가능

    }


    // SELECT
    @Override
    public List<Customer> findAll() {
        // jdbcTemplate.query(sql문, RowMapper) return List<>
        return jdbcTemplate.query("select * from customers", customerRowMapper);
    }

    // SELECT
    @Override
    public Optional<Customer> findById(UUID customerId) {
        try {
            // jdbcTemplate.queryForObject(sql문, sql문에 치환될 파라미터 값, RowMapper) return 단일객체
            return Optional.ofNullable(jdbcTemplate.queryForObject("select * from customers WHERE customer_id = UUID_TO_BIN(:customer_id)",
                    Collections.singletonMap("customerId", customerId.toString().getBytes()), // customerId.toString().getBytes()))
                    customerRowMapper));
        } catch (EmptyResultDataAccessException e) {
            logger.error("Got empty result", e);
            return Optional.empty();
        }
    }

    // SELECT
    @Override
    public Optional<Customer> findByName(String name) {
        try {
            // jdbcTemplate.queryForObject(sql문, sql문에 치환될 파라미터 값, RowMapper) return 단일객체
            return Optional.ofNullable(jdbcTemplate.queryForObject("select * from customers WHERE name = :name",
                    Collections.singletonMap("name", name), // customerId.toString().getBytes()))
                    customerRowMapper));
        } catch (EmptyResultDataAccessException e) {
            logger.error("Got empty result", e);
            return Optional.empty();
        }
    }

    // SELECT
    @Override
    public Optional<Customer> findByEmail(String email) {
        try {
            // jdbcTemplate.queryForObject(sql문, sql문에 치환될 파라미터 값, RowMapper) return 단일객체
            return Optional.ofNullable(jdbcTemplate.queryForObject("select * from customers WHERE email = :email",
                    Collections.singletonMap("email", email), // customerId.toString().getBytes()))
                    customerRowMapper));
        } catch (EmptyResultDataAccessException e) {
            logger.error("Got empty result", e);
            return Optional.empty();
        }
    }

    // INSERT
    @Override
    public Customer insert(Customer customer) {
        // 테이블에 추가할 row 생성
        HashMap<String, Object> paramMap = paraMap(customer);
        // jdbcTemplate.update(sql문, sql문에 치환될 파라미터 map<String, ?>) return 단일객체
        var update = jdbcTemplate.update("INSERT INTO customers(customer_id, name, email, created_at) VALUES (UUID_TO_BIN(:customerId), :name, :email, :createdAt)", // 파라미터 값을 이름으로 준다. 이름 == Map의 key값
                paramMap);

        if (update != 1) { // 추가 여부 확인
            throw new RuntimeException("Nothing was inserted");
        }
        return customer;
    }

    // UPDATE
    @Override
    public Customer update(Customer customer) {
        // 테이블에 추가할 row 생성
        HashMap<String, Object> paramMap = paraMap(customer);
        // jdbcTemplate.update(sql문, sql문에 치환될 파라미터 map<String, ?>) return 단일객체
        var update = jdbcTemplate.update("UPDATE customers SET name = :name, email = :email, last_login_at = :last_login_at WHERE customer_id = :customer_id)", // 파라미터 값을 이름으로 준다. 이름 == Map의 key값
                paramMap
        );
        if (update != 1) {  // 업데이트 여부 확인
            throw new RuntimeException("Nothing was updated");
        }
        return customer;
    }


    // 각 INSERT, UPDATE에서 중복되는 row 값 설정 부분을 메소드로 구현
    private HashMap<String, Object> paraMap(Customer customer) {
        return new HashMap<>() {{
            put("customerId", customer.getCustomerId().toString().getBytes());
            put("name", customer.getName());
            put("email", customer.getEmail());
            put("createdAt", Timestamp.valueOf(customer.getCreatedAt()));
            put("lastLoginAt", customer.getLastLoginAt() != null ?  Timestamp.valueOf(customer.getLastLoginAt()) : null );
        }};
    }


    // DELETE
    @Override
    public void deleteAll() {
        // jdbcTemplate.update(sql문, map<String, ?>) return 단일객체
        jdbcTemplate.update("DELETE FROM customers", Collections.emptyMap());
    }

    static UUID toUUID(byte[] bytes) {
        var byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }
}

```


<br/><br/>

### NamedParameterJdbcTemplate 테스트 코드
```java
    @Configuration
    @ComponentScan(
            basePackages = {"org.prgrms.kdt.customer"}
    )

    static class Config {

        @Bean
        public DataSource dataSource() {
            var dataSource = DataSourceBuilder.create()
                    .url("jdbc:mysql://localhost:2215/test-order_mgmt") // setup()메소드에서 port를 2215로 설정해주었기 때문에 localhost:2215로 변경
                    .username("test")                                   //                 schema이름도 설정해준대로 변경
                    .password("test1234!")
                    .type(HikariDataSource.class) // (기본) HikariDataSource가 pool에 10개의 connection을 채워넣는다.
                    .build();
            dataSource.setMaximumPoolSize(1000); // connection 사이즈를 1000으로 설정
            dataSource.setMinimumIdle(100); // 기본 connection을 100개로 설정
            return dataSource;
        }

        // JdbcTemplate 사용을 위한 Bean 설정
        @Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }
    }

    // JdbcTemplate을 주입받는 NamedParameterJdbcTemplate 설정
    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(JdbcTemplate jdbcTemplate) {
        return new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    // CustomerJDBCRepository -> CustomerNamedJDBCRepository
    @Autowired
    CustomerNamedJDBCRepository customerJDBCRepository;
```

<br/><br/>

## DataAccessException

### SQLException
- JDBC 사용 시 발생할 수 있는 예외
- Vendor사 별로 작성한 vendorcode를 통해 SQLException의 종류를 파악해야 한다 -> 예외처리 어려움

<br/>

### DataAccessException
- SQLException를 타입화시킨 것.
- 보편적인 예외들을 **추상화**한 것이다.

```java
    @Test
    @Order(2)
    @DisplayName("고객을 추가할 수 있다.")
    public void testInsert() {
        try {
            customerJDBCRepository.insert(newCustomer);
        } catch (BadSqlGrammarException e) {
            logger.error("Got BadSqlGrmmarException error code -> {}", e.getSQLException().getErrorCode(), e);
        }
        var retrievedCustomer = customerJDBCRepository.findById(newCustomer.getCustomerId());
        assertThat(retrievedCustomer.isEmpty(), is(false));
        assertThat(retrievedCustomer.get(), samePropertyValuesAs(newCustomer));
    }
```

<br/><br/>

## 트랜잭션 처리
- 트랜잭션이란 논리적 작업의 한 단위이다.
- 여러 연산(INSERT, UPDATE 등)을 


<br/>


> _**오류**_
- **Embedded Mysql** - Wix가 `MySQL 8.0`이상에서 windows를 지원하지 않는 문제 -> `mysql5.7`로 테스트
이전 코드의 수정 필요 (수강생 해결공유)
```sql
-- Mysql 8 이상
WHERE uuid = UUID_TO_BIN('77dea2ad-3c8c-40c6-a278-7cf1a1ac9384');
-- 이전버전
WHERE uuid = UNHEX(REPLACE('77dea2ad-3c8c-40c6-a278-7cf1a1ac9384', '-', ''));
```


> **_TIP_**
  - 최대한 function을 사용하지 않고 sql문을 작성한다면 H2만으로 TEST 코드 실행이 가능하다.
UUID를 활용한 문자열 저장 등을 활용하자.
  - 스테이징이나 환경에 접속정보 넣지 않기 (?) 
  - 실제 DB가 아닌 Embedded Database에서 쿼리, 레포지토리 동작 테스트코드를 작성하는 습관을 기르자
  - NamedParameterJdbcTemplate 내부에는 JdbcTemplate이 들어있다. 
  NamedParameterJdbcTemplate를 이용한 CRUD 작성 시 update()메소드에 빈 Map을 인자로 주는 작업이 귀찮다면 그냥 JdbcTemplate의 update()를 사용해도 된다.
  (👉 _JdbcTemplate.getJdbcTemplate().update()_ )
  - 엔티티를 Map으로 치환하는 방법은 다양하다. 더 찾아서 공부하면 좋을 듯
  
  
> _**추가 공부**_
- EmbeddedDatabaseBuilder의 `generateUniqueName(boolean flag)` 메소드의 기능? (리더 답변)
  - 하나의 JVM안에서 여러 어플리케이션 컨텍스트가 만들어질때 **개별로 embedded database를 할당**하기 위해 사용
  - 대체로 embedded database는 테스트에서 사용되기 때문에, 테스트 시 동시에 여러 개의 컨텍스트가 만들어지면서 테스트가 되는 환경에서 서로 독립적인 db를 가지게 하기 위함


> _**rf**_
- [yshjft님의 2022년 4월 14일 TIL](https://velog.io/@yshjft/2022%EB%85%84-4%EC%9B%94-14%EC%9D%BC-TIL#db-%EC%97%B0%EB%8F%99-%ED%86%B5%ED%95%A9%ED%85%8C%EC%8A%A4%ED%8A%B8-%EC%BD%94%EB%93%9C-%EC%9E%91%EC%84%B1%ED%95%98%EA%B8%B0)