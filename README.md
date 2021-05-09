# Study_Actual-Spring-Data-JPA
### 인프런 실전! 스프링 데이터 JPA (김영한)
https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%EB%8D%B0%EC%9D%B4%ED%84%B0-JPA-%EC%8B%A4%EC%A0%84
-----

## [Settings]
#### Project Name
* Study_Actual-Spring-Data-JPA
#### java
* zulu jdk 11
#### gradle
* IDEA gradle wrapper
#### Spring boot
* 2.4.5
* [스프링 부트 의존성 버전 정보](https://docs.spring.io/spring-boot/docs/current/reference/html/appendix-dependency-versions.html#dependency-versions)
#### H2
* 1.4.200
#### Connection pool
* HikariCP
#### Log
* SLF4J, LogBack
-----

## [환경설정]

### IDEA 설정
* IDEA 기본 빌드 설정
  * 빌드 옵션을 기본 `Gradle`이 아닌 `IDEA`로 변경 (`Gradle` 사용하면 조금 느릴 수 있음)
  * `,` + `;` 단축키로 프로젝트 설정 진입
  * `Build, Execution, Deployment` > `Build Tools` > `Gradle` 경로
    * `Build and run using`, `Run tests using`을 모두 `Gradle (Defalut)` > `Intellij IDEA`로 변경
* `Lombok` 설정
  * 롬복 플러그인 사용을 위해 애너테이션 설정
  * `Build, Execution, Deployment` > `Compiler` > `Annotation Processors` 경로
    * `Enable annotation processing` 설정 선택

### H2 설정
* `brew install h2`
  * `brew` 패키지로 설치하면 터미널에서 h2 명령어로 사용 가능
* 설정명은 `Generic H2 (Server)`으로 설정 (`Generic H2 (Embedded)`도 됨)
* 최초에 JDBC URL을 `jdbc:h2:~/datajpa`으로 설정하여 접속 (이렇게 하면 원격이 아닌 파일로 접근하게 됨)
  * 접속 시도를 아예 하지 않으면 `connection` 및 `test connection` 에러 발생
  * 그 후부터 접속할 때 URL을 `jdbc:h2:tcp://localhost/~/datajpa`으로 설정하여 접속
    * 최초 URL은 파일로 접속하는 방식으로 잠금처리 되어 여러 곳에서 동시에 접속(접근)할 수 없음
* root 경로에 datajpa.mv.db 파일 생성 여부 확인

### application.properties 파일 설정
* `application.properties` 파일 확장자를 `yml`로 변경
* ~~~
  spring:
    datasource:
      url: jdbc:h2:tcp://localhost/~/datajpa
      username: sa
      password:
      driver-class-name: org.h2.Driver

    jpa:
      hibernate:
        ddl-auto: create
      properties:
        hibernate:
    #     show_sql: true
        format_sql: true

  logging.level:
    org.hibernate.SQL: debug
  #  org.hibernate.type: trace
  ~~~

### p6spy 설정
* 쿼리와 파라미터를 동시에 볼 수 있게 도와주는 외부 라이브러리 설정
  * [p6spy 저장소 참조](https://github.com/gavlyukovskiy/spring-boot-data-source-decorator)
* `build.gradle` 설정
  * `implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.5.7`
* ~~~
  2021-05-08 16:45:27.772  INFO 37512 --- [           main] p6spy                                    : #1620459927772 | took 0ms | statement | connection 3| url jdbc:h2:tcp://localhost/~/datajpa
  insert into member (user_name, id) values (?, ?)
  insert into member (user_name, id) values ('memberA', 1);
  ~~~

### JPA 설정
* `Project Structure`에서 `Facets` 경로
  * `JPA`를 `Study_Actual-Spring-Data-JPA.main` 경로 설정 추가

### Java Config 설정
* `@EnableJpaRepositories(basePackages = "com.jaenyeong.study_actualspringdatajpa.repository")` 설정 생략
  * 해당 설정을 스프링 부트가 대신해 주기 때문에 생략 가능
  * 위치가 달라져 `@SpringBootApplication` 애너테이션 하위가 아닌 경우 설정 필요

## 도메인(엔티티)
* 멤버
* 팀

### 연관관계 로딩 확인
* `*ToOne` 관계는 기본적으로 EAGER(즉시 로딩)이기 때문에 LAZY(지연 로딩)으로 변경

## Spring Data JPA
* `Spring Data JPA`가 로딩 시점에 `JpaRepository` 인터페이스를 구현한 `repository interface`의 구현체(프록시)를 만들어 줌
  * `find`, `save` 등 이미 대다수의 기본 기능이 구현되어 있음
* `@Repository` 애너테이션 생략 가능
  * `@Repository`는 컴포넌트 스캔 기능, 발생된 예외(JPA)를 스프링 예외로 변환해주는 기능을 포함

### `Repository` 인터페이스 분석
* `Spring Data` 패키지
  * `Repository`
    * 기본 `Repository` 마커 인터페이스
  * `CrudRepository`
    * `Repository` 인터페이스 구현
  * `PagingAndSortingRepository`
    * `CrudRepository` 인터페이스 구현
* `Spring Data JPA` 패키지
  * `JpaRepository`
    * `PagingAndSortingRepository` 인터페이스 구현

### 주요 메서드
* `save`, `delete`, `findById` 등 주요 공통 메서드 제공
  * `JpaRepository`는 대부분의 공통 메서드 제공

### 쿼리 메서드
* [문서 참조](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation)
* 메서드 네이밍에 따라 `Spring Data JPA`가 쿼리 메서드를 구현해 줌
  * 파라미터 수에 따라 메서드명이 한없이 길어질 수 있음
  * 일반적으로 파라미터를 `2~3개`까지만 사용하는 쿼리 메서드 구현
* 엔티티의 필드명이 변경되면 반드시 쿼리 메서드명도 함께 변경해야 함
  * 변경하지 않으면 애플리케이션을 시작할 때 에러 발생

### `JPA Named Query`
* 정적 쿼리
  * 네임드 쿼리는 정적 쿼리이기 때문에 애플리케이션 로딩 시점에 쿼리 오타 등 에러를 잡아 준다는 장점이 있음
  * 기존 `createQuery`를 통해 쿼리를 작성하는 방식은 쿼리 오타 등 에러는 런타임에 확인 가능
* 하지만 네임드 쿼리는 실무에서 잘 사용하지 않는 경우가 많음
  * 대신 `@Query` 애너테이션으로 `repository`에 쿼리(`JPQL`)를 직접 선언(정의)하는 방식으로 사용
* 네임드 쿼리 호출
  * ~~~
    @Query(name = "Member.findByUserName")
    List<Member> findByUserName(@Param("userName") final String userName);
    ~~~
  * JPQL에 네임드 파라미터 바인딩을 위하여 `@Param` 사용
* 위 `findByUserName`은 네임드 쿼리를 호출하지 않고도 사용 가능
  * ~~~
    List<Member> findByUserName(@Param("userName") final String userName);
    ~~~
  1. Spring Data JPA가 해당 클래스에 네임드 쿼리를 찾아 실행
  2. 네임드 쿼리가 없을 시 쿼리 메서드 작명 규칙으로 쿼리 생성하여 실행

### `@Query` 애너테이션으로 `repository`에 쿼리 정의
* ~~~
  @Query("select m from Member m where m.userName = :userName and m.age = :age")
  List<Member> findUserByQuery(@Param("userName") final String userName, @Param("age") final int age);
  ~~~
* 애플리케이션 로딩 시점에 쿼리 오타 등 에러를 잡아 줌

### `@Query`, 값, DTO 조회
* 값을 조회할 때
  * ~~~
    @Query("select m.userName from Member m")
    List<String> findUserNameList();
    ~~~
* `DTO`를 통해 조회할 때 `new` 키워드를 사용해야 함
  * ~~~
    @Query("select new com.jaenyeong.study_actualspringdatajpa.dto.MemberDto(m.id, m.userName, t.name) from Member m join m.team t")
    List<MemberDto> findUsersForMemberDto();
    ~~~

### 파라미터 바인딩
* 위치 기반
  * `select m from Member m where m.userName = ?0`
  * 실무에서 거의 사용되지 않음 (가독성, 유지보수 때문)
    * 위치(순서)가 변경되면 버그가 생길 확률이 높음
* 이름 기반
  * `select m from Member m where m.userName = :userName`

#### 컬렉션 파라미터 바인딩
~~~
@Query("select m from Member m where m.username in :userNames")
List<Member> findByNames(@Param("userNames") Collection<String> userNames);
~~~

### 반환 타입
* 다건 조회 시 컬렉션 반환
  * `List<Member> findByUsername(String name);`
  * 데이터가 없는 경우 `null`이 아닌 빈 리스트를 반환
* 단건 조회 시
  * 객체 반환
    * `Member findByUsername(String name);`
  * `Optional<T>` 반환
    * `Optional<Member> findByUsername(String name);`

#### 단건 조회 시 데이터가 없는 경우
* `JPA`에서는 예외를 발생 시킴
* `Spring Data JPA`에서는 `null`, 'empty List' 등을 반환하여 처리

#### 단건 조회 시 데이터가 다수인 경우
* `JPA`에서 발생한 `NonUniqueException` 예외를 `IncorrectResultSizeDataAccessException`으로 변경
  * 하위 DB에서 발생된 예외를 `Spring` 레이어에서 추상화된 예외로 변경

### 순수 `JPA` 페이징과 정렬
* 직접 구현할 때는 페이징 계산 로직도 따로 추가해야 함 
* 다른 DB 벤더의 쿼리 또한 무관
  * `application.yml`에서 다른 DB 벤더 설정
    * ~~~
      spring:
        jpa:      
          properties:
            hibernate:
              dialect: org.hibernate.dialect.Oracle10gDialect
      ~~~

### `Spring Data JPA` 페이징과 정렬
* 정렬
  * `org.springframework.data.domain.Sort`
* 페이징
  * `org.springframework.data.domain.Pageable`

#### 특별한 반환 타입
* 반환 타입을 기준으로 쿼리가 결정됨
  * 추가 `count` 쿼리 결과를 포함하는 페이징
    * `org.springframework.data.domain.Page`
    * 콘텐츠 쿼리와 토탈 카운트 쿼리 모두 전송
  * 추가 `count` 쿼리 없이 다음 페이지만 확인 가능 (내부적으로 `limit + 1` 조회)
    * `org.springframework.data.domain.Slice`
    * 콘텐츠 쿼리만 전송 (토탈 카운트 쿼리 X)
  * 추가 `count` 쿼리 없이 결과만 반환
    * `List` 자바 컬렉션

#### `totalCount`
* 실무에서는 조인 등 복잡한 쿼리 상황(방대한 데이터의 양)으로 인해 성능 등 이슈 존재
* 그대로 쿼리를 호출하면 `count` 쿼리에서도 조인을 함
  * ~~~
    @Query(value = “select m from Member m left join m.team t”)
    Page<Member> findMemberAllCountBy(Pageable pageable);
    ~~~
* 이때 `count` 쿼리를 분리할 수 있음
  * ~~~
    @Query(value = “select m from Member m left join m.team t”, countQuery = “select count(m.userName) from Member m”)
    Page<Member> findMemberAllCountBy(Pageable pageable);
    ~~~

#### 복잡한 정렬
* 실무에서 복잡한 정렬 조건이 발생하면 `PageRequest`에 `Sort.by(...)` 만으로 정렬이 어려울 수 있음
  * 이때는 정렬을 빼고 `@Query`에 정렬 `JPQL`을 직접 작성하는 것이 나을 수 있음
