>_본 포스팅은 프로그래머스 미니 데브 코스를 공부하며 
학습을 기록하기 위한 목적으로 작성된 글입니다._


# JDBC

![](https://velog.velcdn.com/images/suran-kim/post/6be146d3-560e-4050-b64d-7a6726ec6a88/image.png)
출처: https://brownbears.tistory.com/289

## DataSource

커넥션을 관리하는 주체
_(JDBC에서는 드라이버 매니저 외에 DataSource를 이용해서 커넥션을 연결 가능)_

<br/><br/>

### 🍃 DataBase Connection Pool (DBCP)

- DataBase Connection Pool(DBCP)
매번 커넥션을 생성 -> close하면 많은 자원이 소모된다.

- 커넥션 풀은 커넥션을 미리 만들어서 풀에 저장. 
필요 시마다 가져와서 사용한 뒤 반환하는 방법이다.

- 커넥션 풀은 **DataSource**가 관리하고 있다.


- 커넥션 풀의 **close**는
실제 반환 (x) 커넥션 풀에 반납 (o) 이다.



pom.xml에 `spring-boot-starter-jdbc` 의존성을 추가하면 **HikariCP**를 포함한 많은 라이브러리들이 추가된다.

<br/>

#### Simple Driver DataSource

- 매번 커넥션을 data manager를 통해 가져온다.
- 테스트용.

<br/>

#### HikariCP
- 톰캣 2.0부터 HikariCP를 사용.
- 2012년도 경에 개발된 매우 가볍고 빠른 JDBC 커넥션 풀


<br/>

### 🍃 DataSource를 이용한 CRUD



> _**비교 포인트 **_
`Driver manager`를 이용한 CRUD코드를 작성했을 때는 
`getConnection()`에 url, user, password를 입력해야했다.
**HikariCP**를 이용하면 Connection Pool에서 Connection을 가져오기 때문에
Dependency injection을 사용해서 구현체를 바꿔 동작할 수 있다.

```java

@Repository // 컴포넌트 대상이 되기 위해 @Repository 추가
public class CustomerJDBCRepository implements CusotomerRepository {

    private static final Logger logger = LoggerFactory.getLogger(JdbcCustomerRepository.class);

    private final DataSource dataSource;

    // 생성자 주입을 통해 주입
    public CustomerJDBCRepository(DataSource datasource) {
        this.dataSource = datasource;
    }


    // SELECT
    @Override
    public List<Customer> findAll() {
        List<Customer> allCustomers = new ArrayList<>();
        try (
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement("select * from customers");
                var resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                mapToCustomer(allCustomers, resultSet);
            }
        } catch (SQLException throwable) {
            logger.error("Got error while closing connection", throwable);
            throw new RuntimeException(throwable); // RuntimeException으로 반환
        }

        return allCustomers;
    }

    // SELECT
    @Override
    public Optional<Customer> findById(UUID customerId) {
        List<Customer> allCustomers = new ArrayList<>();
        try (
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement("select * from customers WHERE customer_id = UUID_TO_BIN(?)");
        ) {
            statement.setBytes(1, customerId.toString().getBytes());
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    mapToCustomer(allCustomers, resultSet);
                }
            }
        } catch (SQLException throwable) {
            logger.error("Got error while closing connection", throwable);
            throw new RuntimeException(throwable); // RuntimeException으로 반환
        }
        return allCustomers.stream().findFirst();
    }

    // SELECT
    @Override
    public Optional<Customer> findByName(String name) {
        List<Customer> allCustomers = new ArrayList<>();
        try (
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement("select * from customers WHERE name = ? ");
        ) {
            statement.setString(1, name);
            logger.info("statement -> {}", statement);
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    mapToCustomer(allCustomers, resultSet);
                }
            }
        } catch (SQLException throwable) {
            logger.error("Got error while closing connection", throwable);
            throw new RuntimeException(throwable); // RuntimeException으로 반환
        }
        return allCustomers.stream().findFirst();
    }

    // SELECT
    @Override
    public Optional<Customer> findByEmail(String email) {
        List<Customer> allCustomers = new ArrayList<>();
        try (
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement("select * from customers WHERE email = ? ");
        ) {
            statement.setString(1, email);
            logger.info("statement -> {}", statement);
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    mapToCustomer(allCustomers, resultSet);
                }
            }
        } catch (SQLException throwable) {
            logger.error("Got error while closing connection", throwable);
            throw new RuntimeException(throwable); // RuntimeException으로 반환
        }
        return allCustomers.stream().findFirst();
    }

    // INSERT
    @Override
    public Customer insert(Customer customer) {
        try (
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement("INSERT INTO customers(customer_id, name, email, created_at) VALUES (UUID_TO_BIN(?), ?, ?, ?)");
        ) {
            statement.setBytes(1, customer.getCustomerId().toString().getBytes());
            statement.setString(2, customer.getName());
            statement.setString(3, customer.getEmail());
            statement.setTimestamp(4, Timestamp.valueOf(customer.getCreatedAt()));
            var executeUpdate = statement.executeUpdate();
            if (executeUpdate != 1) { // 추가 여부 확인
                throw new RuntimeException("Nothing was inserted");
            }
            return customer;
        } catch (SQLException throwable) {
            logger.error("Got error while closing connection", throwable);
            throw new RuntimeException(throwable); // RuntimeException으로 반환
        }
    }

    // DELETE
    @Override
    public void deleteAll() {
        try (
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement("DELETE FROM customers");
        ) {
           statement.executeUpdate();
        } catch (SQLException throwable) {
            logger.error("Got error while closing connection", throwable);
            throw new RuntimeException(throwable); // RuntimeException으로 반환
        }
    }

    // UPDATE
    @Override
    public Customer update(Customer customer) {
        try (
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement("UPDATE customers SET name = ?, email = ?, last_login_at = ? WHERE customer_id = UUID_TO_BIN(?)");
        ) {
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getEmail());
            statement.setTimestamp(3, customer.getLastLoginAt() != null ? Timestamp.valueOf(customer.getLastLoginAt()) : null);
            statement.setBytes(4, customer.getCustomerId().toString().getBytes());
            var executeUpdate = statement.executeUpdate();
            if (executeUpdate != 1) {  // 업데이트 여부 확인
                throw new RuntimeException("Nothing was updated");
            }
            return customer;
        } catch (SQLException throwable) {
            logger.error("Got error while closing connection", throwable);
            throw new RuntimeException(throwable); // RuntimeException으로 반환
        }
    }

    // 테이블의 행을 select해서 List에 추가하는 메소드
    private void mapToCustomer(List<Customer> allCustomers, ResultSet resultSet) throws SQLException {
        var customerName = resultSet.getString("name");
        var email = resultSet.getString("email");
        var customerId = toUUID(resultSet.getBytes("customer_id"));
        var lastLoginAt = resultSet.getTimestamp("last_login_at") != null ?
                resultSet.getTimestamp("last_login_at").toLocalDateTime() : null;
        var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        allCustomers.add(new Customer(customerId, customerName, email, lastLoginAt, createdAt));
    }


    static UUID toUUID(byte[] bytes){
        var byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }
}


```

<br/><br/>

---

## Jdbc Template
- DataSource 사용 시 **connection 생성**과 **예외처리 부분**이 반복된다.
  스프링에서는 이렇게 반복되는 코드와 변경되는 부분을 Jdbc Template을 이용하여 제거할 수 있다.

- template callback 패턴을 이용한다.

- **dataSource** 필요


<br/>

### 🍃 Jdbc Template를 이용한 CRUD


<br/>

```java
import javax.sql.DataSource;


@Repository // 컴포넌트 대상이 되기 위해 @Repository 추가
public class CustomerJDBCRepository implements CusotomerRepository {

    private static final Logger logger = LoggerFactory.getLogger(JdbcCustomerRepository.class);

    private final DataSource dataSource;

    private final JdbcTemplate jdbcTemplate;

    //
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


    public CustomerJDBCRepository(DataSource datasource, JdbcTemplate jdbcTemplate) {
        this.dataSource = datasource;
        this.jdbcTemplate = jdbcTemplate;
    }


    // COUNT
    @Override
    public int count() {
        return jdbcTemplate.queryForObject("select count(*) from customers", Integer.class); // count()함수의 return타입을 설정 가능
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
            // jdbcTemplate.queryForObject(sql문, RowMapper, sql문에 치환될 파라미터 값) return 단일객체
            return Optional.ofNullable(jdbcTemplate.queryForObject("select * from customers WHERE customer_id = UUID_TO_BIN(?)",
                    customerRowMapper,
                    customerId.toString().getBytes())); // '?' 에 들어가는 파라미터 호출 가능
        } catch (EmptyResultDataAccessException e) {
            logger.error("Got empty result", e);
            return Optional.empty();
        }
    }

    // SELECT
    @Override
    public Optional<Customer> findByName(String name) {
        List<Customer> allCustomers = new ArrayList<>();
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("select * from customers WHERE name = ?",
                    customerRowMapper,
                    name));
        } catch (EmptyResultDataAccessException e) {
            logger.error("Got empty result", e);
            return Optional.empty();
        }


    }

    // SELECT
    @Override
    public Optional<Customer> findByEmail(String email) {
        List<Customer> allCustomers = new ArrayList<>();
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("select * from customers WHERE email = ?",
                    customerRowMapper,
                    email));
        } catch (EmptyResultDataAccessException e) {
            logger.error("Got empty result", e);
            return Optional.empty();
        }
    }

    // INSERT
    @Override
    public Customer insert(Customer customer) {
        // jdbcTemplate.update(sql문, sql문에 치환될 파라미터 값) return 단일객체
        var update = jdbcTemplate.update("INSERT INTO customers(customer_id, name, email, created_at) VALUES (UUID_TO_BIN(?), ?, ?, ?)",
                customer.getCustomerId().toString().getBytes(),
                customer.getName(),
                customer.getEmail(),
                Timestamp.valueOf(customer.getCreatedAt()));
        if (update != 1) { // 추가 여부 확인
            throw new RuntimeException("Nothing was inserted");
        }
        return customer;
    }

    // UPDATE
    @Override
    public Customer update(Customer customer) {
        var update = jdbcTemplate.update("UPDATE customers SET name = ?, email = ?, last_login_at = ? WHERE customer_id = UUID_TO_BIN(?)",
                customer.getName(),
                customer.getEmail(),
                customer.getLastLoginAt() != null ? Timestamp.valueOf(customer.getLastLoginAt()) : null,
                customer.getCustomerId().toString().getBytes()
        );
        if (update != 1) {  // 업데이트 여부 확인
            throw new RuntimeException("Nothing was updated");
        }
        return customer;
    }

    // DELETE
    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM customers");
    }

    static UUID toUUID(byte[] bytes) {
        var byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }
}
```

<br/><br/>

## Test Code

### 🍃 Datasource 사용

```java
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource; 

…

@SpringJUnitConfig
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 인스턴스가 하나만 생성
class CustomerJDBCRepositoryTest {

    @Configuration
    @ComponentScan(
            basePackages = {"org.prgrms.kdt.customer"}
    )

    static class Config {
        @Bean
        public DataSource dataSource() {
            var dataSource = DataSourceBuilder.create()
                    .url("jdbc:mysql://localhost/order_mgmt")
                    .username("root")
                    .password("root1234!")
                    .type(HikariDataSource.class) // (기본) HikariDataSource가 pool에 10개의 connection을 채워넣는다.
                    .build();
            dataSource.setMaximumPoolSize(1000); // connection 사이즈를 1000으로 설정
            dataSource.setMinimumIdle(100); // 기본 connection을 100개로 설정
            return dataSource;
        }}

    @Autowired
    CustomerJDBCRepository customerJDBCRepository;

    @Autowired
    DataSource dataSource; // 등록된 bean

    Customer newCustomer;

    @BeforeAll
    void setup() {
        newCustomer = new Customer(UUID.randomUUID(), "test-user", "test1-user@gmail.com", LocalDateTime.now());
        customerJDBCRepository.deleteAll();
    }


    @Test
    @Order(1)
    public void testHikariConnectionPool() {
        assertThat(dataSource.getClass().getName(), is("com.zaxxer.hikari.HikariDataSource"));
    }

    @Test
    @Order(2)
    @DisplayName("고객을 추가할 수 있다.")
    public void testInsert() {
        customerJDBCRepository.insert(newCustomer);

        var retrievedCustomer = customerJDBCRepository.findById(newCustomer.getCustomerId());
        assertThat(retrievedCustomer.isEmpty(), is(false));
        assertThat(retrievedCustomer.get(), samePropertyValuesAs(newCustomer));

    }

    @Test
    @Order(3)
    @DisplayName("전체 고객을 조회할 수 있다.")
    public void testFindAll() {
        var customers = customerJDBCRepository.findAll();
        assertThat(customers.isEmpty(), is(false));
    }


    @Test
    @Order(4)
    @DisplayName("이름으로 고객을 조회할 수 있다.")
    public void testFindByName() {
        var customer = customerJDBCRepository.findByName(newCustomer.getName());
        assertThat(customer.isEmpty(), is(false));

        var unknown = customerJDBCRepository.findByName("unknown-user"); // 알 수 없는 고객 조회
        assertThat(unknown.isEmpty(), is(true));

    }

    @Test
    @Order(5)
    @DisplayName("이메일로 고객을 조회할 수 있다.")
    public void testFindByEmail() {
        var customer = customerJDBCRepository.findByName(newCustomer.getEmail());
        assertThat(customer.isEmpty(), is(false));

        var unknown = customerJDBCRepository.findByName("unknown-user@gmail.com"); // 알 수 없는 고객 조회
        assertThat(unknown.isEmpty(), is(true));

    }


    @Test
    @Order(6)
    @DisplayName("고객을 수정할 수 있다.")
    public void testUpdate() {
        newCustomer.changeName("updated-user");
        customerJDBCRepository.update(newCustomer);

        var all = customerJDBCRepository.findAll();
        assertThat(all, hasSize(1));
        assertThat(all, everyItem(samePropertyValuesAs(newCustomer)));

        // 전체 데이터의 정확성 테스트
        var retrievedCustomer = customerJDBCRepository.findById(newCustomer.getCustomerId());
        assertThat(retrievedCustomer.isEmpty(), is(false));
        assertThat(retrievedCustomer.get(), samePropertyValuesAs(newCustomer));
    }
}

```

<br/><br/>

### 🍃 Templeate 사용


```java
    static class Config {
        @Bean
        public DataSource dataSource() {
            var dataSource = DataSourceBuilder.create()
                    .url("jdbc:mysql://localhost/order_mgmt")
                    .username("root")
                    .password("root1234!")
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
```

<br/><br/>

### 🍃 테스트의 순서 설정

 테스트 코드는 테스트 코드에 나열한 순서대로 실행되지 않는다.
 

- _**@TestMethodOrder()**_
테스트의 순서를 정해주는 어노테이션
  - _**@Order()**_
  
      - 숫자로 테스트 코드의 실행 순서 표기
    
 
<br/>

 
 
```java
@SpringJUnitConfig
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // @Order를 보장
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 인스턴스가 하나만 생성
class CustomerJDBCRepositoryTest {
	
    @Test
    @Order(1)
   	테스트 코드
    
    …
    
    @Test
    @Order(n)
   	테스트 코드

}
    
```

<br/>

 ### 🍃 @TestInstance
 
- 테스트 인스턴스의 **생성 단위**를 변경하기 위해 사용하는 어노테이션
- JUnit은 설정된 테스트 단위로 테스트 객체(테스트 인스턴스)를 만든다.
- 테스트 인스턴스는 기본적으로 **메소드 단위 생명주기**이다. 
- @TestIntance는 **메소드끼리 영향을** 주는 테스트 케이스를 테스트할 때 사용한다.
- `@TestInstance(Lifecycle.PER_CLASS)` 를 선언한 클래스는 클래스 단위 생명주기를 가진다.


- _클래스 단위 인스턴스 장점_
   - `@BeforeAll` 이나 `@AfterAll` 메서드가 정적 메서드가 아니어도 된다.
   - `@Nested` 클래스에서 `@BeforeAll` 이나 `@AfterAll` 메서드를 사용할 수 있다.



<br/><br/>

> _**새로 알게 된 용어**_
- _callback 함수_
  1. 다른 함수의 인자로써 이용되는 함수.
  2. 어떤 이벤트에 의해 호출되어지는 함수.
출처: https://satisfactoryplace.tistory.com/18 [만족:티스토리]




> 
**_코드 작성 시 팁_**
- 어떤 필드에 `final` 키워드가 적합한지 고민해봐야 한다.
_(어떤 필드의 값이 변하지 않을 것인지?)_
- setter는 만들지 않는다 
_(setter 역할의 메소드를 따로 정의)_
- domain클래스 생성 시 정의된 비즈니스룰을 잘 작성하는 게 중요하다. 
- 항상 Optional 사용을 고려하라. 

  

> _**rf**_
[더 공부해보면 좋을 자료 (@TestMethodOrder)]
(https://effortguy.tistory.com/120)
[참고한 블로그: yshjft님의 벨로그](https://velog.io/@yshjft/2022%EB%85%84-4%EC%9B%94-13%EC%9D%BC-TIL#testinstance)
[[Spring Boot] JUnit 5 (5) - 테스트 인스턴스 (@TestInstance)](https://awayday.github.io/2017-11-12/junit5-05/)
[JUnit 5 (5)](https://awayday.github.io/2017-11-12/junit5-05/)