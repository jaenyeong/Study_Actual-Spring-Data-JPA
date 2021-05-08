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
