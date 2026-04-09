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
| Build | Gradle |
| Utility | Lombok, Spring Validation |

---

## 프로젝트 구조

```
book-management/
├── .vscode/
│   ├── launch.json          # VS Code 실행 구성
│   └── settings.json        # VS Code Java 설정
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

## Oracle XE 설치 및 계정 생성

### Windows

1. [Oracle XE 21c 다운로드](https://www.oracle.com/database/technologies/xe-downloads.html)
2. `OracleXE213_Win64.zip` 압축 해제 후 `setup.exe` 실행
3. 설치 중 SYS/SYSTEM 비밀번호 설정 (예: `oracle123`)
4. 설치 완료 후 서비스 자동 시작 확인

### Mac (Docker 사용 권장)

```bash
# Docker로 Oracle XE 21c 실행
docker run -d \
  --name oracle-xe \
  -p 1521:1521 \
  -e ORACLE_PASSWORD=oracle123 \
  gvenzl/oracle-xe:21-slim

# 컨테이너 시작 완료까지 약 1~2분 대기
docker logs -f oracle-xe
```

### bookadmin 계정 생성

SQL*Plus 또는 DBeaver로 SYSTEM 계정 접속 후 실행:

```sql
-- PDB(XEPDB1)에 접속
ALTER SESSION SET CONTAINER = XEPDB1;

-- 애플리케이션 전용 계정 생성
CREATE USER bookadmin IDENTIFIED BY bookadmin123;
GRANT CONNECT, RESOURCE TO bookadmin;
GRANT CREATE SESSION TO bookadmin;
GRANT UNLIMITED TABLESPACE TO bookadmin;

-- DBeaver 접속 정보
-- Host: localhost  Port: 1521  Service: XEPDB1
-- User: bookadmin  Password: bookadmin123
```

> **팁**: DBeaver Community Edition을 사용하면 GUI로 편하게 접속할 수 있습니다.

---

## schema.sql / data.sql 실행 방법

### DBeaver 사용 (권장)

1. `bookadmin` 계정으로 XEPDB1 연결
2. `src/main/resources/sql/schema.sql` 파일 열기
3. 전체 선택(Ctrl+A) → 실행(Ctrl+Enter)
4. `data.sql` 파일도 동일하게 실행

### SQL*Plus 사용

```bash
# XEPDB1에 bookadmin으로 접속
sqlplus bookadmin/bookadmin123@localhost:1521/XEPDB1

# schema.sql 실행
SQL> @C:\path\to\book-management\src\main\resources\sql\schema.sql

# data.sql 실행
SQL> @C:\path\to\book-management\src\main\resources\sql\data.sql
```

### 실행 결과 확인

```sql
SELECT COUNT(*) FROM category;   -- 5건 확인
SELECT COUNT(*) FROM book;       -- 20건 확인
```

---

## application.yml 설정

`src/main/resources/application.yml`에서 DB 접속 정보를 수정합니다:

```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@localhost:1521/XEPDB1  # Oracle 접속 URL
    username: bookadmin                            # ← 계정명 수정
    password: bookadmin123                         # ← 비밀번호 수정
    driver-class-name: oracle.jdbc.OracleDriver
```

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

> **보안 팁**: 운영 환경에서는 코드나 설정 파일에 비밀번호를 직접 적지 마세요.  
> AWS Secrets Manager, HashiCorp Vault, Kubernetes Secret 등의 시크릿 매니저를 통해 환경변수를 주입하는 방식이 권장됩니다.

---

## VS Code에서 실행하는 방법

### 필수 Extension 설치

VS Code에서 다음 Extension Pack을 설치합니다:

| Extension | 용도 |
|-----------|------|
| **Extension Pack for Java** (Microsoft) | Java 언어 지원, 디버거, Maven/Gradle |
| **Spring Boot Extension Pack** (VMware) | Spring Boot 대시보드, 실행/디버그 지원 |

### 실행 방법 (3가지 중 선택)

**방법 1: Spring Boot Dashboard (권장)**
1. 좌측 사이드바 → Spring Boot 아이콘 클릭
2. `BookManagementApplication` 옆 ▶ 버튼 클릭

**방법 2: Run and Debug (F5)**
1. `F5` 키 또는 실행 → 디버깅 시작
2. `.vscode/launch.json`의 `BookManagementApplication` 구성으로 실행

**방법 3: 터미널**
```bash
cd book-management
./gradlew bootRun
```

### 접속 URL

실행 후 브라우저에서 접속:

```
http://localhost:8080/books
```

REST API 테스트:
```
http://localhost:8080/api/books
http://localhost:8080/api/books/1
```

---

## 실행 순서 요약

```
① Oracle XE 21c 설치
        ↓
② bookadmin 계정 생성 (SYSTEM 계정으로 SQL 실행)
        ↓
③ schema.sql 실행 (테이블 + 시퀀스 생성)
        ↓
④ data.sql 실행 (카테고리 5건, 도서 20건 INSERT)
        ↓
⑤ application.yml DB 접속 정보 확인/수정
        ↓
⑥ VS Code에서 Extension Pack for Java + Spring Boot Extension Pack 설치
        ↓
⑦ 프로젝트 폴더 열기 → Gradle 자동 빌드 완료 대기
        ↓
⑧ Spring Boot Dashboard 또는 F5로 실행
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
      path: /h2-console   # H2 웹 콘솔 경로
  sql:
    init:
      mode: always
      schema-locations: classpath:sql/schema.sql
      data-locations: classpath:sql/data.sql

mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.example.bookmanagement.domain
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
| `http://localhost:8080/books` | 도서 관리 메인 화면 |
| `http://localhost:8080/h2-console` | H2 웹 콘솔 (DB 직접 조회) |

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

### Oracle 드라이버를 찾을 수 없음
```
Unable to load class oracle.jdbc.OracleDriver
```
→ `build.gradle`의 `ojdbc11` 버전 확인 후 `./gradlew --refresh-dependencies` 실행

### DB 연결 실패 (Connection refused)
```
ORA-12541: TNS:no listener
```
→ Oracle 서비스 실행 여부 확인:
- Windows: 서비스 관리자에서 `OracleServiceXE`, `OracleXETNSListener` 시작
- Docker: `docker start oracle-xe`

### XEPDB1 접속 오류
```
ORA-12514: TNS:listener does not currently know of service requested
```
→ URL을 `jdbc:oracle:thin:@localhost:1521:XE`로 변경 (SID 방식) 또는 리스너 재시작

### Gradle 빌드 시 JDK 버전 오류
```
error: release version 17 not supported
```
→ VS Code에서 Java 17 JDK 경로 설정:
1. `Ctrl+Shift+P` → `Java: Configure Java Runtime`
2. JDK 17 경로 지정

### Thymeleaf 템플릿 캐시로 변경 미반영
→ `application.yml`에서 `spring.thymeleaf.cache: false` 확인 (이미 설정됨)

### Lombok 어노테이션이 인식되지 않음
→ VS Code Extension `Lombok Annotations Support for VS Code` 설치

### 포트 8080이 이미 사용 중
```
Web server failed to start. Port 8080 was already in use.
```
다른 프로세스가 8080 포트를 점유하고 있습니다.

- **Windows**: `netstat -ano | findstr :8080` 으로 PID 확인 후 `taskkill /PID [PID] /F`
- **Mac/Linux**: `lsof -i :8080` 으로 PID 확인 후 `kill -9 [PID]`
- **또는** `application.yml`에서 포트 변경:
  ```yaml
  server:
    port: 8081
  ```

### Mac/Linux에서 gradlew 권한 오류
```
permission denied: ./gradlew
```
→ 실행 권한 부여 후 재시도:
```bash
chmod +x gradlew
./gradlew bootRun
```

### 첫 빌드가 너무 오래 걸림 (정상 동작)

첫 빌드 시 Gradle이 Spring Boot, MyBatis, Oracle 드라이버 등 수십 개의 의존성을 Maven Central에서 다운로드합니다.

- 첫 빌드는 네트워크 환경에 따라 **5~10분**이 걸릴 수 있음
- VS Code 우측 하단 상태바의 진행 표시 확인
- 멈춘 것처럼 보여도 강제 종료하지 말 것
- 두 번째 빌드부터는 로컬 캐시를 사용하므로 빠름 (수십 초 이내)

---

## 개발 환경

- JDK 17 (Eclipse Temurin 또는 Oracle JDK)
- Oracle XE 21c
- VS Code + Extension Pack for Java + Spring Boot Extension Pack
- DBeaver Community (DB 관리 도구, 선택 사항)
