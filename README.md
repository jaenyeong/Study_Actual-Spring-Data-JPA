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

### 벌크성 수정 쿼리
* `@Modifying` 애너테이션 태깅하면 `executeUpdate();` 실행하여 수정 여부를 알려줌
  * 태깅을 하지 않으면 `getResultList();` 등을 실행하게 되어 예외 발생
    * `org.hibernate.hql.internal.QueryExecutionRequestException: Not supported for DML operations`
* 벌크 연산을 실행하고 나서 영속성 컨텍스트 초기화
  * `@Modifying(clearAutomatically = true)`
    * 기본 값은 `false`
  * 실행하지 않으면 영속성 컨텍스트 안에 예전 데이터가 남아 데이터 부정합 발생할 수 있음
    * 영속성 컨텍스트에 캐싱된 데이터가 없을 때 하는 것이 좋고 벌크 연산 실행 직후 초기화 할 것

### `@EntityGraph`
* `JPA` 표준 스펙
  * `fetch join`을 편리하게 사용할 수 있음
  * 따라서 지연 로딩과 `fetch join`을 명확히 이해해야 함
  * 복잡한 쿼리라면 `JPQL`, 간단한 쿼리라면 `@EntityGraph`를 통해 `fetch join` 처리하기도 함
* 지연 로딩
  * 영속성 컨텍스트가 비어져 있을 때 사용하지 않는 연관관계 객체는 프록시로 대체
* 지연 로딩을 하게 되면 `N + 1` 문제 발생할 수 있음
  * 연관관계 데이터 수를 기준으로 쿼리가 발생함
  * 이를 해결하기 위해 `JPQL`을 사용해야 하는 것이 번거롭기 때문에 `EntityGraph`를 사용
* `@EntityGraph`는 내부적으로 `fetch join`을 사용하는 것과 같음
* ~~~
  @Override
  @EntityGraph(attributePaths = {"team"})
  List<Member> findAll();
  ~~~
* `@NamedEntityGraph`
  * ~~~
    @NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("team"))
    public class Member {
    }
    ~~~
  * ~~~
    public interface MemberRepository extends JpaRepository<Member, Long> {
        @EntityGraph()
        List<Member> findEntityGraphByUserName(@Param("userName") final String userName);
    }
    ~~~

### JPA Hint & Lock
* JPA Hint
  * JPA 구현체(일반적으로 하이버네이트)에게 제공하는 쿼리 힌트
  * `@QueryHint`는 `JPA`가 아닌 `Hibernate` 구현체가 제공하는 기능
  * `@QueryHints`는 `JPA`에서 구현체에게 힌트를 넘기는 기능
* `JPA`의 변경감지(dirty checking) 기능의 단점
  * 기능이 동작하기 위해선 원본 데이터와 수정본 데이터를 가지고 있어야 함
  * 따라서 `readOnly` 속성의 쿼리를 실행시키려 해도 두가지 종류의 데이터를 갖고 있어 비효율적
  * 이때 `readOnly` 속성 힌트를 사용하여 최적화
* 사용 형태
  * ~~~
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUserName(final String userName);
    ~~~
  * `@QueryHint(name = "org.hibernate.readOnly", value = "true")` 형태는 문자열을 넘겨 받음
    * 이는 `JPA`로부터 편하게 넘겨 받기 위한 것으로 보임
* Lock
  * `JPA`에서 Optimistic lock(낙관적 락), Pessimistic lock(비관적 락) 등 기능 제공
  * LockModeType은 `javax.persistence` 패키지
  * DB 벤더에 따라 동작 방식이 달라짐
  * 쿼리 형태
    ~~~
    select member0_.member_id as member_i1_0_, member0_.age as age2_0_, member0_.team_id as team_id4_0_, member0_.user_name as user_nam3_0_ from member member0_ where member0_.user_name='member1' for update;
    ~~~
  * 실시간 트래픽이 많은 서비스에서는 가급적 비관적 락을 걸지 않는 것을 권함
    * 낙관적 락(실제 잠금이 아닌 버저닝을 활용한 방식) 사용하거나 잠금 처리를 우회할 수 있는 방식을 고려
    * 하지만 결제와 같은 기능은 비관적 락을 활용하기도 함

### 사용자 정의 리포지토리 구현
* `Spring Data JPA` 리포지토리는 인터페이스만 정의, 구현체는 스프링이 자동 생성
* 리포지토리를 커스터마이징 하려면 인터페이스를 직접구현해야 하기 때문에 구현할 기능이 너무 많음
* 인터페이스의 메서드를 직접 구현?
  * `JPA` 직접 사용 `EntityManager`
  * 스프링 `JDBC Template` 사용
  * `MyBatis` 사용
  * `DB Connection` 직접 사용
  * `QueryDSL` 사용

#### 커스텀 리포지토리
* 커스텀 리포지토리는 `QueryDSL`, `JDBC Template` 등을 사용할 때 자주 사용
  * 별도(임의)의 리포지토리를 인터페이스가 아닌 클래스로 생성(구현), 빈으로 등록하여 직접 사용해도 무방
    * 다만 이때는 `Spring Data JPA`와 무관하게 작동
* 명명 규칙(관례)
  * `상속시킬 리포지토리 인터페이스명` + `Impl` (`MemberRepository` + `Impl`)
    * 스프링 데이터 JPA가 인식해서 스프링 빈으로 등록
    * 최신 버전에서는 `CustomMemberRepsitoryImpl`로 명명해도 동작은 함
  * `Impl` 대신 다른 이름으로 명명할 때 설정
    * `xml` 설정
      ~~~
      <repositories base-package="com.jaenyeong.study_actualspringdatajpa.repository" repository-impl-postfix="Impl" />
      ~~~
    * `Java Config` 설정
      ~~~
      @EnableJpaRepositories(basePackages = "com.jaenyeong.study_actualspringdatajpa.repository", repositoryImplementationPostfix = "Impl")
      ~~~

### Auditing
* 엔티티 생성, 변경 시 다음 사항을 추적하려면?
  * 등록일
  * 수정일
  * 등록자
  * 수정자

#### 순수 `JPA`에서 처리
* `Base Entity`
  ~~~
  package com.jaenyeong.study_actualspringdatajpa.entity;
  
  import javax.persistence.Column;
  import javax.persistence.MappedSuperclass;
  import javax.persistence.PrePersist;
  import javax.persistence.PreUpdate;
  import java.time.LocalDateTime;
  
  @MappedSuperclass
  @Getter
  public abstract class JpaBaseEntity {
      @Column(updatable = false)
      private LocalDateTime createdDate;
      private LocalDateTime updatedDate;
      
      @PrePersist
      public void prePersist() {
          LocalDateTime now = LocalDateTime.now();
          createdDate = now;
          updatedDate = now;
      }
      
      @PreUpdate
      public void preUpdate() {
          updatedDate = LocalDateTime.now();
      }
  }
  ~~~

* `Entity` 클래스에서 상속
  ~~~
  public class Member extends JpaBaseEntity {
  }
  ~~~

#### `Spring Data JPA`에서 처리
* 설정
  * `@EnableJpaAuditing` 스프링 부트 설정 클래스에 적용
    * 적용하지 않으면 작동하지 않음
    ~~~
    @EnableJpaAuditing
    @SpringBootApplication
    public class StudyActualSpringDataJpaApplication {
    }
    ~~~
* `Base Entity`
  * `@EntityListeners(AuditingEntityListener.class)` 엔티티 클래스에 적용
  ~~~
  package com.jaenyeong.study_actualspringdatajpa.entity;

  import lombok.Getter;
  import org.springframework.data.annotation.CreatedBy;
  import org.springframework.data.annotation.CreatedDate;
  import org.springframework.data.annotation.LastModifiedBy;
  import org.springframework.data.annotation.LastModifiedDate;
  import org.springframework.data.jpa.domain.support.AuditingEntityListener;
  
  import javax.persistence.Column;
  import javax.persistence.EntityListeners;
  import javax.persistence.MappedSuperclass;
  import java.time.LocalDateTime;
  
  @EntityListeners(AuditingEntityListener.class)
  @MappedSuperclass
  @Getter
  public abstract class BaseEntity {
      @CreatedDate
      @Column(updatable = false)
      private LocalDateTime createdDate;
  
      @LastModifiedDate
      private LocalDateTime lastModifiedDate;
  
      @CreatedBy
      @Column(updatable = false)
      private String createdBy;
    
      @LastModifiedBy
      private String lastModifiedBy;
  }
  ~~~
* `Entity` 클래스에서 상속
  ~~~
  public class Member extends BaseEntity {
  }
  ~~~
* 등록자, 수정자를 등록해주는 빈 등록
  * 실무에서는 세션 정보나, 스프링 시큐리티 로그인 정보에서 ID를 받아 처리
  ~~~
  @Bean
  public AuditorAware<String> auditorProvider() {
      return () -> Optional.of(UUID.randomUUID().toString());
  }
  ~~~
* 실무에서는 등록일, 수정일은 있지만 등록자, 수정자가 없는 경우로 인해 분리해 사용
  ~~~
   public class BaseTimeEntity {
        @CreatedDate
        @Column(updatable = false)
        private LocalDateTime createdDate;
  
        @LastModifiedDate
        private LocalDateTime lastModifiedDate;
   }
  
   public class BaseEntity extends BaseTimeEntity {
       @CreatedBy
       @Column(updatable = false)
       private String createdBy;
  
       @LastModifiedBy
       private String lastModifiedBy;
   }
  ~~~
* 최초 저장 시점에 등록일, 등록자 뿐 아니라 수정일, 수정자도 같이 저장
  * 이를 통해 `null` 방지와 변경 여부 확인 가능하여 유지보수 관점에서 편리 
  * 수정을 저장하지 않으려면 `@EnableJpaAuditing(modifyOnCreate = false)` 태깅
* 전체 적용
  * `@EntityListeners(AuditingEntityListener.class)` 생략, `orm.xml` 파일에 설정
  * `META-INF/orm.xml` 파일 설정
    ~~~
    <?xml version=“1.0” encoding="UTF-8”?>
    <entity-mappings xmlns=“http://xmlns.jcp.org/xml/ns/persistence/orm”
                     xmlns:xsi=“http://www.w3.org/2001/XMLSchema-instance”
                     xsi:schemaLocation=“http://xmlns.jcp.org/xml/ns/persistence/orm http://xmlns.jcp.org/xml/ns/persistence/orm_2_2.xsd”
                     version=“2.2">
        <persistence-unit-metadata>
            <persistence-unit-defaults>
                <entity-listeners>
                    <entity-listener class="org.springframework.data.jpa.domain.support.AuditingEntityListener”/>
                </entity-listeners>
            </persistence-unit-defaults>
        </persistence-unit-metadata>
    </entity-mappings>
    ~~~

### Web 확장

#### 도메인 클래스 컨버터
* 사용
  ~~~
  package com.jaenyeong.study_actualspringdatajpa.controller;
  
  import com.jaenyeong.study_actualspringdatajpa.entity.Member;
  import com.jaenyeong.study_actualspringdatajpa.repository.MemberRepository;
  import lombok.RequiredArgsConstructor;
  import org.springframework.web.bind.annotation.GetMapping;
  import org.springframework.web.bind.annotation.PathVariable;
  import org.springframework.web.bind.annotation.RestController;
  
  import javax.annotation.PostConstruct;
  
  @RestController
  @RequiredArgsConstructor
  public class MemberController {
  private final MemberRepository memberRepository;
  
      @PostConstruct
      public void init() {
          memberRepository.save(new Member("member1", 10));
      }
  
      @GetMapping("/members/v1/{id}")
      public String findMemberV1(@PathVariable("id") final Long id) {
          return memberRepository.findById(id)
              .orElseGet(() -> new Member("Not Found", 99))
              .getUserName();
      }
  
      @GetMapping("/members/v2/{id}")
      public String findMemberV2(@PathVariable("id") final Member member) {
          return member.getUserName();
      }
  }
  ~~~
* `HTTP` 요청은 회원 아이디를 받지만, 도메인 클래스 컨버터가 중간에 작동하여 회원 엔티티 객체를 반환
  * 도메인 클래스 컨버터도 마찬가지로 리포지토리를 사용해 엔티티를 찾음
* 실무에서는 이렇게 간단한 경우가 없어 자주 사용되진 않음
  * 도메인 클래스 컨버터로 엔티티를 파라미터로 받으면, 이 엔티티는 단순 조회용으로만 사용할 것
  * 트랜잭션이 없는 범위에서 엔티티를 조회했으므로, 엔티티를 변경해도 DB에 반영되지 않음

#### 페이징과 정렬
* `Pageable` 인터페이스로 파라미터 받음
  * 해당 인터페이스는 실제로 `org.springframework.data.domain.PageRequest` 객체 생성하여 파라미터 바인딩 처리
* 요청 예시 (`/members?page=0&size=3&sort=id,desc&sort=username,desc`)
  * `localhost:8080/members?page=0&size=3&sort=id,desc&sort=userName,desc`
    * page: 현재 페이지, 0부터 시작
    * size: 한 페이지에 노출할 데이터 건수
    * sort: 정렬 조건을 정의
      * 예) 정렬 속성, 정렬 속성...(`ASC` | `DESC`), 정렬 방향을 변경하고 싶으면 sort 파라미터 추가 (`asc` 생략 가능)

##### 기본 설정
* 전체(글로벌: 스프링 부트) `application.yml` 파일에 설정
  ~~~
  spring:
    data:
      web:
        pageable:
          # 기본 페이지 사이즈
          default-page-size: 10
          # 최대 페이지 사이즈
          max-page-size: 2000
  ~~~
* 개별(메서드) 설정
  ~~~
  @GetMapping("/members")
  public Page<Member> list(@PageableDefault(size = 5, sort = "userName", direction = Sort.Direction.DESC) final Pageable pageable) {
      return memberRepository.findAll(pageable);
  }
  ~~~

##### 접두사
* 페이징 정보가 둘 이상이면 접두사로 구분
* `@Qualifier` 애너테이션에 접두사 추가 `{접두사명}_xxx`
  * 요청 예시
    * `/members?member_page=0&order_page=1`과 같은 형태로 요청
    ~~~
    @GetMapping("/members/v2")
    public Page<Member> list(@Qualifier("member") final Pageable memberPageable, @Qualifier("order") final Pageable orderPageable) {
        return memberRepository.findAll(memberPageable);
    }
    ~~~

##### `Page` 내용을 `DTO`로 변환
* `API` 등과 같이 외부에 엔티티를 그대로 노출하지 말고 `DTO`로 변환하여 반환할 것
* `Page`는 `map()`을 통해 데이터 변환 기능 제공
  ~~~
  @GetMapping("/members/v3")
  public Page<MemberDto> memberDtoList(final Pageable pageable) {
      return memberRepository.findAll(pageable)
          .map(MemberDto::new);
  }
  ~~~

##### `Page` 1부터 시작
* `Spring Data`는 기본적으로 `Page`를 0부터 시작
* 1부터 시작하려면?
  1. 첫번째 방법은 직접 구현하여 처리
     * `Pageable`, `Page`를 파라미터와 응답 값으로 사용하지 않고, 직접 클래스를 만들어서 처리
     * `PageRequest(Pageable 구현체)`를 생성해서 리포지토리에 전달
     * 응답값 또한 `Page` 대신 직접 만들어 제공
  2. 두번째 방법은 `application.yml` 파일에서 설정
     ~~~
     spring:
       data:
         web:
           one-indexed-parameters: true
     ~~~
     * 해당 방법은 웹에서 `page` 파라미터를 `-1`로 설정할 뿐 나머지 데이터는 `0` 부터 시작하는 것으로 계산된 것을 그대로 사용
     * 따라서 응답값 `page`에 모두 `0` 페이지 인덱스를 사용하는데 한계가 있음

### `Spring Data JPA` 분석

#### `Spring Data JPA` 구현체 분석
* `Spring Data JPA`가 제공하는 공통 인터페이스 구현체
  * `org.springframework.data.jpa.repository.support.SimpleJpaRepository`
  * `@Repository` 역할
    * 스프링 빈을 컴포넌트로 등록
    * `JPA`, `JDBC` 등 영속성 계층 예외를 스프링에서 사용 가능한(추상화 된) 예외로 변경 시켜줌
* `@Transactional`
  * `Spring Data JPA`의 모든 기능(데이터 처리)은 트랜잭션 내에서 시작
  * `service` 계층 트랜잭션 안에서 호출되면 이어서 실행, 트랜잭션이 없었다면 트랜잭션을 새로 시작
    * 따라서 트랜잭션 없이도 `repository`의 트랜잭션으로 데이터 처리가 가능
  * `repository`에 `@Transactional(readOnly = true)` 태깅되어 있음
    * 필요에 따라 트랜잭션 오버라이딩
    * `@Transactional(readOnly = true)`는 `flush`를 생략, 약간의 성능 향상을 얻을 수 있음
* `save`
  * 새로운 엔티티면 저장(`persist`)하고 아니면 병합(`merge`)
    * `merge`의 단점은 DB `select`를 한 번 수행하는 것
    * `merge`는 데이터 변경 목적이 아닌 준영속 상태 엔티티를 영속 상태로 변환할 목적으로 사용해야 함

#### 새로운 엔티티를 구별하는 방법
* 새로운 엔티티를 판단하는 기본 전략
  * 식별자가 객체일 때 `null`로 판단
  * 식별자가 자바 기본 프리미티브 타입(`long`)일 때 `0`으로 판단
  * `Persistable` 인터페이스를 구현해서 판단 로직 변경 가능
    * 해당 인터페이스를 구현하는 경우 `isNew()` 메서드를 통해 새로운 엔티티인지 판단
    ~~~
    package org.springframework.data.domain;
    public interface Persistable<ID> {
        ID getId();
        boolean isNew();
    }
    ~~~

> 식별자 생성 전략이 `@GenerateValue`인 경우
> * `save` 시점에 식별자가 없기 때문에 새로운 엔티티로 식별하여 저장
> 
> 식별자 생성 전략 없이 `@Id`만 사용해 직접 할당하는 경우
> * 식별자가 있는 상태로 `save` 호출, `merge`가 호출됨
> * 하지만 `merge`는  DB `select`를 호출해 값을 읽고 데이터 유무를 판단하여 처리하기 때문에 비효율적
> * 따라서 `Persistable`를 사용해 새로운 엔티티인지 확인하는 것이 효과적
> * 엔티티 등록 시간(`@CreatedDate`)을 조합하면 편리하게 사용 가능
>   * 값의 유무로 새로운 엔티티인지 판단함

### 나머지 기능

#### Specifications (명세)
* 도메인 주도 설계 (`Domain Driven Design`)에서 SPECIFICATION(명세)라는 개념 소개
* `Spring Data JPA`는 `JPA Criteria`를 활용해서 이 개념을 사용할 수 있도록 지원
  * 실무에서 `JPA Criteria`는 가급적 사용하지 않을 것을 권함

##### 술어(`predicate`)
* 참 또는 거짓으로 평가
* AND OR 같은 연산자로 조합해서 다양한 검색조건을 쉽게 생성(컴포지트 패턴)
* `Spring Data JPA`는 `org.springframework.data.jpa.domain.Specification` 클래스로 정의

##### 사용 방법
* `JpaSpecificationExecutor` 인터페이스 상속
  ~~~
  public interface MemberRepository extends JpaRepository<Member, Long>, JpaSpecificationExecutor<Member> {
  }
  ~~~
* `JpaSpecificationExecutor` 인터페이스
  ~~~
  public interface JpaSpecificationExecutor<T> {
      Optional<T> findOne(@Nullable Specification<T> spec);
      List<T> findAll(Specification<T> spec);
      Page<T> findAll(Specification<T> spec, Pageable pageable);
      List<T> findAll(Specification<T> spec, Sort sort);
      long count(Specification<T> spec);
  }
  ~~~
* `Specification`을 파라미터로 받아서 검색 조건으로 사용
* MemberSpec 명세 정의 코드
  ~~~
  public final class MemberSpec {

      public static Specification<Member> teamName(final String teamName) {
          return (root, query, builder) -> {
              if (!StringUtils.hasText(teamName)) {
                  return null;
              }
    
              // 팀을 회원과 조인
              final Join<Member, Team> t = root.join("team", JoinType.INNER);
              return builder.equal(t.get("name"), teamName);
          };
      }

      public static Specification<Member> username(final String userName) {
          return (root, query, builder) -> builder.equal(root.get("userName"), userName);
      }
  }
  ~~~
* 사용 예시
  ~~~
  @Test
  @DisplayName("명세 테스트")
  void specification() throws Exception {
      // Arrange
      final Team teamA = new Team("teamA");
      teamRepository.save(teamA);

      final Member member1 = new Member("member1", 11, teamA);
      final Member member2 = new Member("member2", 12, teamA);
      memberRepository.save(member1);
      memberRepository.save(member2);

      em.flush();
      em.clear();

      // Act
      final Specification<Member> spec = MemberSpec.username("member1")
          .and(MemberSpec.teamName("teamA"));
      final List<Member> members = memberRepository.findAll(spec);

      // Assert
      assertThat(members.size()).isEqualTo(1);
  }
  ~~~
* `Specification`을 구현하면 명세들을 조립 가능
  * `where()`, `and()`, `or()`, `not()` 제공

##### 정리
* 명세를 정의하려면 `Specification` 인터페이스를 구현
* 명세를 정의할 때는 `toPredicate(...)` 메서드만 구현하면 됨
  * `JPA Criteria`의 `Root`, `CriteriaQuery`, `CriteriaBuilder` 클래스를 파라미터 제공
  * 예제에서는 편의상 람다를 사용
* 실무에서는 `JPA Criteria`가 아닌 `QueryDSL`을 사용하길 권장

#### Query By Example
* [문서 참조](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#query-by-example)
* `Probe`
  * 필드에 데이터가 있는 실제 도메인 객체
  * 엔티티 객체 자체가 검색 조건이 됨
* `ExampleMatcher`
  * 특정 필드를 일치시키는 상세한 정보 제공, 재사용 가능
* `Example`
  * `Probe`와 `ExampleMatcher`로 구성, 쿼리를 생성하는데 사용

##### 장점
* 동적 쿼리를 편리하게 처리
* 도메인 객체를 그대로 사용
* 데이터 저장소를 `RDB`에서 `NOSQL`로 변경해도 코드 변경이 없게 추상화 되어 있음
* `Spring Data JPA`의 `JpaRepository` 인터페이스에 이미 포함

##### 단점
* 조인은 가능하지만 내부 조인(`INNER JOIN`)만 가능, 외부 조인(`LEFT JOIN`) 안됨
* 다음과 같은 중첩 제약조건 안됨
  * `firstname = ?0 or (firstname = ?1 and lastname = ?2)`
* 매칭 조건이 매우 단순함
  * 문자는 `starts/contains/ends/regex`
  * 다른 속성은 정확한 매칭(`=`)만지원

##### 정리
* 실무에서 사용하기에는 매칭 조건이 너무 단순하고, `LEFT JOIN`이 안됨
  * 실무에서는 `QueryDSL` 사용하길 권장
