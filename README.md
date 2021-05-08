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
