# 도서 관리 시스템 (Book Management System)

공공 SI 면접 준비용 Java 풀스택 토이 프로젝트.  
Spring Boot + MyBatis + Oracle + Thymeleaf를 활용한 CRUD 웹 애플리케이션입니다.

---

## 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Java 17 |
| Framework | Spring Boot 3.2.5 |
| ORM | MyBatis 3.0.3 (XML Mapper) |
| Database | Oracle XE 21c (ojdbc11) |
| View | Thymeleaf |
| CSS | Bootstrap 5.3 (CDN) |
| JS | jQuery 3.7 (CDN) |
| Build | Gradle 8.5 (Wrapper) |
| Utility | Lombok, Spring Validation |

---

## 프로젝트 구조

```
book-management/
├── .vscode/
│   ├── launch.json          # VS Code 실행 구성
│   └── settings.json        # VS Code Java 설정
├── gradle/wrapper/          # Gradle Wrapper (생성 필요)
├── gradlew                  # Gradle Wrapper 실행 스크립트 (Mac/Linux)
├── gradlew.bat              # Gradle Wrapper 실행 스크립트 (Windows)
├── build.gradle             # Gradle 의존성 및 빌드 설정
├── settings.gradle
├── src/main/java/com/example/bookmanagement/
│   ├── BookManagementApplication.java   # 진입점
│   ├── controller/
│   │   ├── BookController.java          # Thymeleaf View 컨트롤러
│   │   └── BookApiController.java       # REST JSON API 컨트롤러
│   ├── service/
│   │   ├── BookService.java             # 서비스 인터페이스
│   │   └── BookServiceImpl.java         # 서비스 구현체
│   ├── mapper/
│   │   ├── BookMapper.java              # MyBatis Mapper 인터페이스
│   │   └── CategoryMapper.java
│   ├── domain/
│   │   ├── Book.java                    # 도서 도메인 클래스
│   │   └── Category.java               # 카테고리 도메인 클래스
│   ├── dto/
│   │   ├── BookSearchDto.java           # 검색 조건 DTO
│   │   └── PageDto.java                 # 페이지네이션 결과 DTO
│   ├── config/
│   │   └── MyBatisConfig.java           # MyBatis @MapperScan 설정
│   └── exception/
│       └── GlobalExceptionHandler.java  # 전역 예외 처리
├── src/main/resources/
│   ├── application.yml                  # DB 접속 정보, MyBatis 설정
│   ├── mapper/
│   │   ├── BookMapper.xml               # 도서 SQL (동적 쿼리 포함)
│   │   └── CategoryMapper.xml
│   ├── templates/
│   │   ├── layout/default.html          # 공통 레이아웃 프래그먼트
│   │   ├── books/list.html              # 도서 목록 (검색 + 페이지네이션)
│   │   ├── books/detail.html            # 도서 상세
│   │   ├── books/form.html              # 등록/수정 공용 폼
│   │   └── error/404.html, 500.html     # 에러 페이지
│   ├── static/
│   │   ├── css/style.css
│   │   └── js/book.js                   # jQuery Ajax 예시 포함
│   └── sql/
│       ├── schema.sql                   # DDL (테이블 + 시퀀스 생성)
│       └── data.sql                     # 샘플 데이터 (카테고리 5건, 도서 20건)
└── src/test/java/.../service/
    └── BookServiceTest.java             # 단위 테스트 (Mockito)
```

---

## 사전 준비물

다음이 PC에 설치되어 있어야 합니다.

| 항목 | 다운로드 | 비고 |
|------|---------|------|
| **JDK 17** | https://adoptium.net/temurin/releases/?version=17 | Eclipse Temurin 권장. 설치 시 "Set JAVA_HOME" 체크 |
| **Oracle XE 21c** | https://www.oracle.com/database/technologies/xe-downloads.html | Windows x64 |
| **VS Code** | https://code.visualstudio.com/ | |
| **Gradle 8.5** | https://gradle.org/releases/ | binary-only zip (Wrapper 생성용) |
| **DBeaver Community** | https://dbeaver.io/download/ | DB 관리 도구 (강력 권장) |

VS Code 익스텐션:
- **Extension Pack for Java** (Microsoft)
- **Spring Boot Extension Pack** (VMware)

---

## 1단계: JDK 환경변수 확인

cmd 새로 열고:

```bash
java -version
```

→ `openjdk version "17.0.x"` 출력 확인

```bash
echo %JAVA_HOME%
```

→ JDK 설치 경로 출력 확인 (예: `D:\program_files\Java\jdk-17`)

> **JAVA_HOME이 비어 있거나 17이 아니면**: `Win + R` → `sysdm.cpl` → 고급 → 환경 변수에서 시스템 변수에 `JAVA_HOME`을 JDK 설치 경로로 설정하고, `Path`에 `%JAVA_HOME%\bin`을 추가하세요.

---

## 2단계: Oracle XE 21c 설치

### Windows

1. [Oracle XE 21c 다운로드](https://www.oracle.com/database/technologies/xe-downloads.html)
2. `OracleXE213_Win64.zip` 압축 해제 후 `setup.exe` **우클릭 → 관리자 권한으로 실행**
3. 설치 마법사 진행
4. **데이터베이스 비밀번호 입력** (SYS/SYSTEM/PDBADMIN 공통)

> **비밀번호 주의사항**: `@`, `$`, `#` 같은 특수문자는 cmd 파싱 문제를 일으킵니다. **영문+숫자 조합 사용 권장** (예: `oracle1234`)

5. 설치 완료 (10~20분 소요)

### Mac (Docker 사용 권장)

```bash
docker run -d \
  --name oracle-xe \
  -p 1521:1521 \
  -e ORACLE_PASSWORD=oracle1234 \
  gvenzl/oracle-xe:21-slim

docker logs -f oracle-xe
```

### 서비스 실행 확인

`Win + R` → `services.msc` → 다음 두 개가 "실행 중"인지 확인:
- `OracleServiceXE`
- `OracleOraDB21Home1TNSListener`

---

## 3단계: bookadmin 계정 생성

cmd에서 SYSTEM 계정으로 접속:

```bash
sqlplus system/oracle1234@localhost:1521/XE
```

> 본인이 설치 시 입력한 비밀번호 사용. 비밀번호 입력에 문제가 있다면 [트러블슈팅의 "Oracle 비밀번호 인증 실패"](#oracle-비밀번호-인증-실패) 참고.

`SQL>` 프롬프트에서 한 줄씩 실행:

```sql
ALTER SESSION SET CONTAINER = XEPDB1;
CREATE USER bookadmin IDENTIFIED BY bookadmin123;
GRANT CONNECT, RESOURCE TO bookadmin;
GRANT CREATE SESSION TO bookadmin;
GRANT UNLIMITED TABLESPACE TO bookadmin;
exit
```

> **중요**: SYSTEM 계정은 컨테이너 DB(`XE`)에 접속하지만, bookadmin 같은 일반 사용자는 PDB(`XEPDB1`)에 접속합니다. URL의 서비스명이 다른 점에 주의하세요.

---

## 4단계: schema.sql / data.sql 실행

**DBeaver 사용을 강력히 권장합니다.** sqlplus는 한글 인코딩 문제로 데이터가 깨질 수 있습니다.

### 방법 A: DBeaver 사용 (권장)

1. [DBeaver Community Edition](https://dbeaver.io/download/) 설치
2. 새 연결 → **Oracle** 선택
3. 접속 정보 입력:
   - Host: `localhost`
   - Port: `1521`
   - Database: `XEPDB1` (Service Name 선택)
   - Username: `bookadmin`
   - Password: `bookadmin123`
4. **Test Connection** → 성공 시 **Finish**
5. SQL Editor 열기 (Ctrl+])
6. `book-management/src/main/resources/sql/schema.sql` 내용을 복사해서 붙여넣기 → `Alt+X`로 전체 실행
7. `data.sql`도 동일하게 실행

### 방법 B: sqlplus 사용 (한글 깨짐 주의)

cmd에서 **인코딩 설정 후** 진행:

```bash
chcp 65001
set NLS_LANG=.AL32UTF8
sqlplus bookadmin/bookadmin123@localhost:1521/XEPDB1
```

```sql
@D:\path\to\book-management\src\main\resources\sql\schema.sql
@D:\path\to\book-management\src\main\resources\sql\data.sql
```

### 실행 결과 확인

```sql
SELECT COUNT(*) FROM category;   -- 5건
SELECT COUNT(*) FROM book;       -- 20건
SELECT title, author FROM book WHERE ROWNUM <= 3;  -- 한글이 정상 출력되어야 함
```

---

## 5단계: Gradle Wrapper 생성

**이 단계를 건너뛰면 Gradle 버전 충돌로 빌드가 실패합니다.**

프로젝트에 `gradlew`/`gradlew.bat`이 없거나, PC에 너무 최신 버전의 Gradle이 설치되어 있으면 `NoSuchMethodError: LenientConfiguration.getArtifacts` 같은 에러가 발생합니다. Gradle 8.5로 Wrapper를 만들어 버전을 고정하세요.

### 5-1. Gradle 8.5 다운로드

1. https://gradle.org/releases/ 에서 **Gradle 8.5** binary-only zip 다운로드
2. 압축 해제 → 원하는 위치에 배치 (예: `D:\gradle-8.5\`)
3. 폴더 구조 확인: `D:\gradle-8.5\bin\gradle.bat` 경로가 정확한지 확인

### 5-2. 프로젝트에 Wrapper 생성

cmd에서 프로젝트 폴더로 이동:

```bash
cd D:\path\to\book-management
D:\gradle-8.5\bin\gradle.bat wrapper --gradle-version 8.5
```

성공 시 다음 출력:
```
BUILD SUCCESSFUL in 2s
```

프로젝트에 `gradlew`, `gradlew.bat`, `gradle/wrapper/` 폴더가 생성됩니다.

---

## 6단계: application.yml 확인

**MyBatis가 DTO 클래스를 못 찾는 문제를 방지하기 위해 `type-aliases-package`에 두 패키지가 모두 들어있어야 합니다.**

`book-management/src/main/resources/application.yml` 내용 확인:

```yaml
spring:
  datasource:
    url: ${DB_URL:jdbc:oracle:thin:@localhost:1521/XEPDB1}
    username: ${DB_USERNAME:bookadmin}
    password: ${DB_PASSWORD:bookadmin123}
    driver-class-name: oracle.jdbc.OracleDriver
  thymeleaf:
    cache: false

mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.example.bookmanagement.domain, com.example.bookmanagement.dto
  configuration:
    map-underscore-to-camel-case: true

logging:
  level:
    com.example.bookmanagement.mapper: DEBUG

server:
  port: 8080
```

> 만약 `type-aliases-package`에 `dto`가 누락되어 있으면, 실행 시 `Could not resolve type alias 'BookSearchDto'` 에러가 발생합니다.

### DB 접속 정보 변경이 필요하면

DB 사용자명/비밀번호가 다르면 `application.yml`을 직접 수정하거나, 환경변수로 덮어쓸 수 있습니다 (다음 섹션 참고).

---

## 환경변수 사용 (선택)

`application.yml`의 DB 접속 정보는 환경변수로 덮어쓸 수 있습니다.  
환경변수를 설정하지 않으면 `application.yml`의 기본값(`bookadmin` / `bookadmin123`)이 그대로 사용됩니다.

### Windows (cmd)

```cmd
set DB_URL=jdbc:oracle:thin:@localhost:1521/XEPDB1
set DB_USERNAME=mybookadmin
set DB_PASSWORD=mypassword
.\gradlew bootRun
```

### Windows (PowerShell)

```powershell
$env:DB_URL="jdbc:oracle:thin:@localhost:1521/XEPDB1"
$env:DB_USERNAME="mybookadmin"
$env:DB_PASSWORD="mypassword"
.\gradlew bootRun
```

### Mac / Linux

```bash
export DB_URL=jdbc:oracle:thin:@localhost:1521/XEPDB1
export DB_USERNAME=mybookadmin
export DB_PASSWORD=mypassword
./gradlew bootRun
```

> **보안 팁**: 운영 환경에서는 코드나 설정 파일에 비밀번호를 직접 적지 마세요. AWS Secrets Manager, HashiCorp Vault, Kubernetes Secret 등의 시크릿 매니저를 통해 환경변수를 주입하는 방식이 권장됩니다.

---

## 7단계: VS Code에서 프로젝트 열기

### 필수 Extension 설치

VS Code에서 다음 Extension Pack을 설치합니다:

| Extension | 용도 |
|-----------|------|
| **Extension Pack for Java** (Microsoft) | Java 언어 지원, 디버거, Maven/Gradle |
| **Spring Boot Extension Pack** (VMware) | Spring Boot 대시보드, 실행/디버그 지원 |

### 프로젝트 열기

1. `File` → `Open Folder` → `book-management` 폴더 선택
2. "Do you trust the authors?" → **Yes**
3. **Gradle 자동 빌드 대기** (첫 빌드 5~10분 소요, 의존성 다운로드)
4. 좌측 하단이 **"Java: Ready"** 가 되면 준비 완료

> 빨간 `Java: Error`가 나오면, OUTPUT 패널에서 "Language Support for Java" 선택 후 메시지 확인. JDK 환경변수나 Gradle 버전 문제일 가능성이 높습니다.

---

## 8단계: 애플리케이션 실행

### 방법 1: 터미널 (가장 확실)

VS Code 터미널 열기 (`` Ctrl + ` ``):

```bash
.\gradlew bootRun
```

### 방법 2: Spring Boot Dashboard

1. 좌측 사이드바 → Spring Boot 아이콘(잎사귀) 클릭
2. `BookManagementApplication` 옆 ▶ 버튼 클릭

### 방법 3: F5 (디버그 모드)

`BookManagementApplication.java` 파일을 열고 `F5` 키.

### 성공 로그

```
Started BookManagementApplication in 5.234 seconds
Tomcat started on port 8080 (http) with context path ''
```

---

## 9단계: 브라우저 접속

| URL | 설명 |
|-----|------|
| http://localhost:8080/books | 도서 목록 (메인 화면) |
| http://localhost:8080/books/1 | 도서 상세 |
| http://localhost:8080/books/new | 도서 등록 폼 |
| http://localhost:8080/api/books | REST API (JSON 목록) |
| http://localhost:8080/api/books/1 | REST API (JSON 단건) |

도서 20권이 한글로 정상 표시되면 **완성!** 🎉

---

## 실행 순서 요약

```
① JDK 17 설치 + JAVA_HOME 환경변수 확인
        ↓
② Oracle XE 21c 설치 (특수문자 없는 비밀번호 권장)
        ↓
③ sqlplus로 bookadmin 계정 생성
        ↓
④ DBeaver로 schema.sql + data.sql 실행 (한글 깨짐 방지)
        ↓
⑤ Gradle 8.5 다운로드 → gradle wrapper 생성
        ↓
⑥ application.yml 확인 (type-aliases-package에 dto 포함)
        ↓
⑦ VS Code에서 폴더 열기 → Java: Ready 확인
        ↓
⑧ .\gradlew bootRun 으로 실행
        ↓
⑨ http://localhost:8080/books 접속
```

---

## 빠른 실행: H2 인메모리 DB 사용 (Oracle 없이 동작 확인)

Oracle 설치가 부담스럽거나 빠르게 화면만 먼저 확인하고 싶을 때 사용합니다.  
H2는 JVM 위에서 돌아가는 내장 DB로, 별도 설치가 전혀 필요 없습니다.

### 1단계: build.gradle에 H2 의존성 추가

```gradle
dependencies {
    // ... 기존 의존성 유지 ...
    runtimeOnly 'com.h2database:h2'   // 추가
}
```

### 2단계: application.yml을 H2용으로 교체

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:bookdb;MODE=Oracle;DB_CLOSE_DELAY=-1
    username: sa
    password:
    driver-class-name: org.h2.Driver
  h2:
    console:
      enabled: true
      path: /h2-console
  sql:
    init:
      mode: always
      schema-locations: classpath:sql/schema.sql
      data-locations: classpath:sql/data.sql

mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.example.bookmanagement.domain, com.example.bookmanagement.dto
  configuration:
    map-underscore-to-camel-case: true

logging:
  level:
    com.example.bookmanagement.mapper: DEBUG
```

`MODE=Oracle` 옵션 덕분에 Oracle 문법(`DUAL`, `SYSDATE`, `SEQUENCE` 등)을 H2에서도 대부분 사용할 수 있습니다.

### 3단계: 실행 후 접속

| URL | 설명 |
|-----|------|
| http://localhost:8080/books | 도서 관리 메인 화면 |
| http://localhost:8080/h2-console | H2 웹 콘솔 (DB 직접 조회) |

H2 콘솔 접속 정보:
- JDBC URL: `jdbc:h2:mem:bookdb`
- User Name: `sa`
- Password: (비워둠)

> **주의**: H2는 애플리케이션을 재시작하면 데이터가 초기화됩니다 (인메모리 특성).  
> 면접에서는 "Oracle을 사용했으며, H2는 로컬 환경 빠른 확인용으로 병행했습니다"라고 답하세요.

---

## 주요 기능

| URL | Method | 설명 |
|-----|--------|------|
| `/books` | GET | 도서 목록 (검색 + 페이지네이션) |
| `/books/{id}` | GET | 도서 상세 |
| `/books/new` | GET | 등록 폼 |
| `/books` | POST | 등록 처리 |
| `/books/{id}/edit` | GET | 수정 폼 |
| `/books/{id}/edit` | POST | 수정 처리 |
| `/books/{id}/delete` | POST | 삭제 처리 |
| `/api/books` | GET | REST API — JSON 목록 |
| `/api/books/{id}` | GET | REST API — JSON 단건 |

---

## 면접용 학습 포인트

### 1. Spring DI/IoC가 어디에 적용됐는지

> **DI(의존성 주입)**: `BookController`는 `BookService`를 직접 `new`로 생성하지 않습니다.  
> `@RequiredArgsConstructor`가 생성자를 만들고, Spring IoC 컨테이너가 `BookServiceImpl` 빈을 찾아서 주입합니다.

```java
// BookController — BookService를 직접 생성하지 않음
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService; // Spring이 주입
}
```

**설명 포인트**: "컨트롤러는 서비스의 구체 구현체(Impl)를 몰라도 됩니다. 인터페이스에만 의존하기 때문에 구현체를 교체해도 컨트롤러 코드를 수정할 필요가 없습니다 — 이것이 DIP(의존역전 원칙)입니다."

---

### 2. Controller → Service → Mapper 계층 분리 이유

| 계층 | 역할 | 이 프로젝트의 위치 |
|------|------|--------------------|
| Controller | HTTP 요청/응답 처리, 뷰 반환 | `BookController`, `BookApiController` |
| Service | 비즈니스 로직, 트랜잭션 경계 | `BookServiceImpl` |
| Mapper | SQL 실행, DB 접근 | `BookMapper` + `BookMapper.xml` |

**설명 포인트**: "계층을 분리하면 각 계층이 단일 책임을 갖습니다. DB를 Oracle에서 MySQL로 바꿔도 Mapper만 수정하면 되고, 비즈니스 규칙이 바뀌어도 Service만 수정합니다. 테스트도 계층별로 독립적으로 작성할 수 있습니다."

---

### 3. @Transactional 동작 방식

```java
@Transactional          // 쓰기 — 예외 발생 시 롤백
public void createBook(Book book) { ... }

@Transactional(readOnly = true)   // 읽기 전용 — DB 최적화 힌트
public Book getBookById(Long id) { ... }
```

**Spring의 @Transactional 처리 흐름**:
```
클라이언트 → [프록시 객체가 가로챔] → 트랜잭션 시작
          → 실제 Service 메서드 실행
          → 정상 완료: COMMIT
          → RuntimeException 발생: ROLLBACK
```

**설명 포인트**: "Spring은 `@Transactional`이 붙은 Bean에 대해 AOP 프록시를 생성합니다. 메서드 호출을 가로채서 트랜잭션을 시작하고, 메서드 완료 후 커밋 또는 롤백합니다. 개발자는 트랜잭션 코드를 직접 작성할 필요가 없습니다."

---

### 4. MyBatis XML 매퍼 동작 흐름

```
1. @Mapper 인터페이스 (BookMapper.java)
         ↕ namespace로 연결
2. XML 매퍼 (BookMapper.xml) — 실제 SQL 정의
         ↕
3. MyBatis가 런타임에 인터페이스 구현체 자동 생성
         ↕
4. Spring Bean으로 등록 → Service에 주입
```

**핵심 동적 SQL 태그**:
- `<if test="...">`: 조건부 SQL 생성
- `<where>`: 동적 WHERE 절 (불필요한 AND 자동 제거)
- `<include refid="...">`: SQL 조각 재사용
- `<selectKey>`: INSERT 전 Oracle 시퀀스 값 채번

**type-aliases-package 설정**: `application.yml`의 `type-aliases-package`에 명시된 패키지의 클래스는 XML에서 풀 경로 없이 클래스명만으로 사용 가능합니다. 이 프로젝트에서는 `domain`과 `dto` 두 패키지를 모두 등록했습니다.

---

### 5. RESTful API 컨트롤러 vs 일반 MVC 컨트롤러

| 구분 | BookController | BookApiController |
|------|----------------|-------------------|
| 어노테이션 | `@Controller` | `@RestController` |
| 반환값 | 뷰 이름 (String) | 객체 (JSON 직렬화) |
| 응답 형태 | HTML 페이지 | JSON |
| 클라이언트 | 브라우저 (Thymeleaf) | Ajax, 외부 시스템 |
| 주 용도 | 서버사이드 렌더링 | API 연동, SPA 프론트 |

**설명 포인트**: "`@RestController`는 `@Controller + @ResponseBody`의 합성입니다. 메서드 반환 객체를 Jackson이 JSON으로 자동 직렬화합니다. 이 프로젝트에서는 Thymeleaf MVC와 REST API를 동시에 제공하여 Ajax 연동도 가능하게 했습니다."

---

### 6. AOP (관점 지향 프로그래밍)

**AOP란?** 공통 관심사(로깅, 트랜잭션, 보안 등)를 비즈니스 로직과 분리하는 프로그래밍 패러다임입니다.  
비즈니스 코드에 공통 로직이 뒤섞이는 것을 방지합니다.

**이 프로젝트에서 AOP가 적용된 곳**:

| 적용 위치 | AOP 역할 |
|-----------|----------|
| `@Transactional` | 메서드 호출 전/후에 트랜잭션 시작·커밋·롤백 자동 처리 |
| `@Slf4j` (Lombok) | 로그 코드를 직접 작성하지 않고 공통 로깅 인프라 활용 |
| `@ControllerAdvice` | 모든 컨트롤러의 예외를 한 곳에서 가로채서 처리 |

**프록시 기반 동작 원리**:
```
원본 BookServiceImpl
        ↓ Spring이 감싸서 프록시 생성
[프록시] → 트랜잭션 시작 → [원본 메서드 실행] → 커밋/롤백
```

**설명 포인트**: "메서드마다 트랜잭션 시작·커밋·롤백 코드를 일일이 작성하지 않아도 `@Transactional` 하나로 처리되는 이유가 AOP 때문입니다. Spring은 빈을 생성할 때 `@Transactional`이 붙어 있으면 원본 클래스를 감싸는 프록시 객체를 만들어 메서드 호출을 가로챕니다."

---

### 7. DTO와 Domain 분리 이유

| 구분 | 클래스 | 역할 |
|------|--------|------|
| Domain | `Book`, `Category` | DB 테이블과 1:1 매핑되는 도메인 객체 |
| DTO | `BookSearchDto` | 검색 조건 전달 (DB에 없는 `page`, `size`, `offset` 포함) |
| DTO | `PageDto<T>` | 페이지네이션 결과 전달 (화면/API 전용 구조) |

**분리하는 이유**:
- DB 스키마가 바뀌어도 화면이나 API 응답 형태가 영향받지 않음
- 화면에서 필요한 `page`, `offset` 같은 계산 필드를 Domain에 넣지 않아도 됨
- Domain 객체가 DB 외 용도로 오염되지 않음 (단일 책임 원칙)

**설명 포인트**: "Domain은 DB의 모습, DTO는 화면·API의 모습입니다. 둘을 섞으면 한쪽 변경이 다른 쪽에 영향을 줘서 유지보수가 어려워집니다. 예를 들어 페이지 번호(`page`)는 DB에 없는 개념이므로 Domain이 아닌 `BookSearchDto`에 선언했습니다."

---

### 8. 글로벌 예외 처리 (@ControllerAdvice)

```java
@ControllerAdvice                          // 모든 컨트롤러에 적용
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)  // 특정 예외 타입 지정
    public String handleIllegalArgument(IllegalArgumentException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error/404";   // 에러 페이지 렌더링
    }
}
```

**이점**:
- 컨트롤러마다 `try-catch`를 반복 작성할 필요 없음
- 예외 응답 형식(에러 메시지, HTTP 상태 코드)을 한 곳에서 통일
- 새로운 예외 유형 추가 시 이 클래스에만 `@ExceptionHandler` 추가

**설명 포인트**: "`@ControllerAdvice`는 모든 컨트롤러에 걸쳐 동작하는 AOP와 같은 개념입니다. 예외 처리 로직을 한 곳에 모아 중복을 제거하고, 예외 유형별로 다른 에러 페이지나 JSON 응답을 반환할 수 있습니다."

---

### 9. Oracle 시퀀스 (MySQL AUTO_INCREMENT와의 차이)

**MySQL vs Oracle PK 채번 방식 비교**:

| 구분 | MySQL | Oracle |
|------|-------|--------|
| PK 자동 채번 | `AUTO_INCREMENT` 컬럼 속성 | `SEQUENCE` 별도 객체 |
| 다음 값 조회 | 직접 조회 불가 | `seq_book.NEXTVAL` |
| 현재 값 조회 | `LAST_INSERT_ID()` | `seq_book.CURRVAL` |
| 페이징 | `LIMIT offset, size` | `OFFSET n ROWS FETCH NEXT n ROWS ONLY` |
| NULL 안전 함수 | `IFNULL(a, b)` | `NVL(a, b)` |
| 더미 테이블 | 불필요 | `FROM DUAL` |

**이 프로젝트에서 시퀀스 사용 방식**:
```xml
<!-- MyBatis <selectKey>: INSERT 전에 시퀀스 값을 가져와 book.bookId에 주입 -->
<insert id="insert" parameterType="Book">
    <selectKey keyProperty="bookId" resultType="long" order="BEFORE">
        SELECT seq_book.NEXTVAL FROM DUAL
    </selectKey>
    INSERT INTO book (BOOK_ID, ...) VALUES (#{bookId}, ...)
</insert>
```

**설명 포인트**: "MySQL의 `AUTO_INCREMENT`는 INSERT 후 DB가 알아서 채번하지만, Oracle의 시퀀스는 개발자가 명시적으로 `NEXTVAL`을 호출해야 합니다. MyBatis의 `<selectKey>`로 INSERT 직전에 시퀀스 값을 조회하고, 그 값을 PK로 사용하는 패턴을 씁니다."

---

## 트러블슈팅

### Oracle 비밀번호 인증 실패

```
ORA-01017: invalid username/password
```

**원인 1: 비밀번호가 실제로 다름**

기억하는 비밀번호로 다시 시도. 비밀번호에 특수문자(`@`, `$`, `#` 등)가 있으면 cmd가 잘못 해석할 수 있음.

**원인 2: 비밀번호의 `@` 문자**

`@`는 sqlplus에서 호스트 구분자로 인식되어 파싱이 깨집니다. 비밀번호 부분만 따로 입력:

```bash
sqlplus system@localhost:1521/XE
Enter password: [여기에 입력, 화면에 안 보임]
```

**원인 3: 비밀번호를 모름 → 재설정**

cmd를 **관리자 권한**으로 실행 후:

```bash
sqlplus / as sysdba
```

```sql
ALTER USER system IDENTIFIED BY oracle1234;
ALTER USER system ACCOUNT UNLOCK;
exit
```

새 비밀번호로 재접속:
```bash
sqlplus system/oracle1234@localhost:1521/XE
```

---

### Gradle 빌드 실패: NoSuchMethodError

```
NoSuchMethodError: 'java.util.Set org.gradle.api.artifacts.LenientConfiguration.getArtifacts(...)'
```

**원인**: PC에 설치된 Gradle이 너무 최신 버전이거나, Gradle Wrapper가 없어서 시스템 Gradle이 사용됨.

**해결**: [5단계: Gradle Wrapper 생성](#5단계-gradle-wrapper-생성)을 진행해서 Gradle 8.5로 Wrapper 생성. 이후:

```bash
.\gradlew bootRun
```

`gradlew` 사용 시 시스템 Gradle 대신 8.5가 자동으로 사용됩니다.

---

### MyBatis: type alias를 찾을 수 없음

```
Could not resolve type alias 'BookSearchDto'.
Cause: java.lang.ClassNotFoundException: Cannot find class: BookSearchDto
```

**원인**: `application.yml`의 `type-aliases-package`에 `dto` 패키지가 누락됨.

**해결**: `application.yml` 수정:

```yaml
mybatis:
  type-aliases-package: com.example.bookmanagement.domain, com.example.bookmanagement.dto
```

콤마로 두 패키지를 모두 지정해야 합니다.

---

### sqlplus에서 한글 깨짐

```
ERROR: ORA-01756: 단일 인용부를 지정해 주십시오
ERROR: ORA-00917: 누락된 콤마
```

**원인**: sqlplus가 SQL 파일을 잘못된 인코딩(MS949/CP949)으로 읽음. 한글이 깨지면서 따옴표 짝까지 어그러짐.

**해결**: cmd 인코딩 설정 후 재실행:

```bash
chcp 65001
set NLS_LANG=.AL32UTF8
sqlplus bookadmin/bookadmin123@localhost:1521/XEPDB1
```

또는 **DBeaver를 사용하세요** (한글 인코딩 문제 없음).

이미 깨진 데이터가 들어갔다면 정리:
```sql
DELETE FROM book;
DELETE FROM category;
COMMIT;
DROP SEQUENCE seq_book;
DROP SEQUENCE seq_category;
CREATE SEQUENCE seq_book START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE seq_category START WITH 1 INCREMENT BY 1 NOCACHE;
```

---

### Oracle 드라이버를 찾을 수 없음
```
Unable to load class oracle.jdbc.OracleDriver
```
→ `build.gradle`의 `ojdbc11` 버전 확인 후 `.\gradlew --refresh-dependencies` 실행

---

### DB 연결 실패 (Connection refused)
```
ORA-12541: TNS:no listener
```
→ Oracle 서비스 실행 여부 확인:
- Windows: `services.msc`에서 `OracleServiceXE`, `OracleOraDB21Home1TNSListener` 시작
- Docker: `docker start oracle-xe`

---

### XEPDB1 접속 오류
```
ORA-12514: TNS:listener does not currently know of service requested
```
→ URL의 서비스명 확인:
- SYSTEM 계정 → `XE` 사용
- bookadmin 같은 일반 사용자 → `XEPDB1` 사용

---

### VS Code의 Java: Error

좌측 하단에 빨간 `Java: Error`가 표시됨.

**진단**: OUTPUT 패널 → 우측 드롭다운에서 "Language Support for Java" 선택 → 메시지 확인

**자주 있는 원인**:
1. `JAVA_HOME` 환경변수가 잘못된 경로를 가리킴 → 환경변수 수정 후 VS Code 재시작
2. PATH의 우선순위 충돌 (Oracle javapath 등) → `where java`로 확인
3. Gradle 버전 호환성 문제 → [Gradle Wrapper 생성](#5단계-gradle-wrapper-생성)

---

### 포트 8080이 이미 사용 중
```
Web server failed to start. Port 8080 was already in use.
```

- **Windows**: `netstat -ano | findstr :8080` 으로 PID 확인 후 `taskkill /PID [PID] /F`
- **Mac/Linux**: `lsof -i :8080` 으로 PID 확인 후 `kill -9 [PID]`
- **또는** `application.yml`에서 포트 변경:
  ```yaml
  server:
    port: 8081
  ```

---

### Mac/Linux에서 gradlew 권한 오류
```
permission denied: ./gradlew
```
→ 실행 권한 부여:
```bash
chmod +x gradlew
./gradlew bootRun
```

---

### Lombok 어노테이션이 인식되지 않음
→ VS Code Extension `Lombok Annotations Support for VS Code` 설치 후 재시작

---

### 첫 빌드가 너무 오래 걸림 (정상 동작)

첫 빌드 시 Gradle이 Spring Boot, MyBatis, Oracle 드라이버 등 수십 개의 의존성을 다운로드합니다.

- 첫 빌드는 네트워크 환경에 따라 **5~10분** 소요
- VS Code 우측 하단 상태바의 진행 표시 확인
- 멈춘 것처럼 보여도 강제 종료하지 말 것
- 두 번째 빌드부터는 로컬 캐시를 사용하므로 빠름 (수십 초 이내)

---

### Thymeleaf 템플릿 캐시로 변경 미반영
→ `application.yml`에서 `spring.thymeleaf.cache: false` 확인 (이미 설정됨)

---

## 개발 환경

- JDK 17 (Eclipse Temurin)
- Oracle XE 21c
- Gradle 8.5 (Wrapper)
- VS Code + Extension Pack for Java + Spring Boot Extension Pack
- DBeaver Community (DB 관리 도구, 권장)