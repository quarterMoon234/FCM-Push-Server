# FCM Push Server

Spring Boot 기반으로 만든 학습용 백엔드 프로젝트입니다. MySQL에 사용자 정보를 저장하고, 회원가입과 로그인 API를 제공하며, Firebase Cloud Messaging을 통해 특정 디바이스 토큰으로 푸시 알림을 전송할 수 있습니다.

프로젝트 이름은 `FCM-Push-Server`이지만, 실제 패키지 구조는 `user_management`이며 사용자 관리 API와 푸시 알림 API를 함께 다룹니다.

## 주요 기능

- 사용자 회원가입 API
- 사용자 로그인 API
- 이메일 기반 사용자 조회
- 비밀번호 BCrypt 해시 저장 및 검증
- Firebase Admin SDK 기반 FCM 푸시 전송
- MySQL + JPA 기반 사용자 데이터 관리
- 테스트용 API 엔드포인트 제공

## 기술 스택

- Java 17
- Spring Boot 3.3.5
- Spring Web
- Spring Data JPA
- MySQL
- Firebase Admin SDK
- BCrypt
- Gradle
- Lombok

## 프로젝트 구조

```text
FCM-Push-Server/
├── build.gradle
├── settings.gradle
├── gradlew
├── src/
│   ├── main/
│   │   ├── java/com/user_manager/user_management/
│   │   │   ├── UserManagementApplication.java
│   │   │   ├── FirebaseMessagingService.java
│   │   │   ├── NotificationController.java
│   │   │   ├── NotificationMessage.java
│   │   │   ├── models/
│   │   │   │   ├── Login.java
│   │   │   │   └── User.java
│   │   │   ├── repository/
│   │   │   │   └── UserRepository.java
│   │   │   ├── rest_controllers/
│   │   │   │   ├── LoginApiController.java
│   │   │   │   ├── RegisterApiController.java
│   │   │   │   └── UserApiController.java
│   │   │   └── services/
│   │   │       └── UserService.java
│   │   └── resources/
│   │       ├── application.yaml
│   │       ├── db_script/main.sql
│   │       └── firebase-service-account.json
│   └── test/
└── README.md
```

## API 구성

### 사용자 API

기본 prefix: `/api/v1`

- `POST /api/v1/user/register`
  - `first_name`, `last_name`, `email`, `password`, `token`을 받아 사용자 등록
- `POST /api/v1/user/login`
  - 이메일과 비밀번호로 로그인
  - 성공 시 사용자 정보를 반환
- `GET /api/v1/test`
  - 테스트용 엔드포인트

### 알림 API

- `POST /notification`
  - FCM 디바이스 토큰으로 푸시 알림 전송

## 요청 예시

### 회원가입

`POST /api/v1/user/register`

폼 파라미터:

- `first_name`
- `last_name`
- `email`
- `password`
- `token`

### 로그인

`POST /api/v1/user/login`

```json
{
  "email": "user@example.com",
  "password": "plain-password"
}
```

### 푸시 알림 전송

`POST /notification`

```json
{
  "recipientToken": "device-token",
  "title": "알림 제목",
  "body": "알림 내용",
  "image": "https://example.com/image.png",
  "data": {
    "screen": "home",
    "type": "notice"
  }
}
```

## 실행 방법

### 1. 사전 준비

- Java 17
- MySQL
- Firebase 서비스 계정 키 파일

### 2. 설정 파일 준비

현재 프로젝트는 `src/main/resources/application.yaml`에서 DB 연결 정보를 직접 읽습니다.

기본 설정 예시:

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/user_manager
    username: your_username
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQLDialect
```

또한 `src/main/resources/firebase-service-account.json` 파일이 필요합니다. 이 파일은 Firebase Admin SDK 초기화에 사용됩니다.

### 3. DB 스키마 준비

`src/main/resources/db_script/main.sql`을 참고해 데이터베이스와 테이블을 준비합니다.

### 4. 애플리케이션 실행

macOS/Linux:

```bash
cd /Users/quarterMoon/Desktop/project/FCM-Push-Server
./gradlew bootRun
```

또는:

```bash
./gradlew test
./gradlew build
java -jar build/libs/*.jar
```

기본 포트:

- App: `http://localhost:8080`

## 구현 포인트

- 회원가입 시 비밀번호를 `BCrypt.hashpw()`로 해시화해 저장합니다.
- 로그인 시 저장된 해시와 입력 비밀번호를 `BCrypt.checkpw()`로 비교합니다.
- Firebase Admin SDK를 `firebase-service-account.json`으로 초기화합니다.
- 알림 전송 시 notification payload와 data payload를 함께 보낼 수 있습니다.
- 저장소 계층에서는 native query로 사용자 이메일, 비밀번호, 상세 정보를 조회합니다.
- 사용자 등록 시 FCM token까지 함께 저장하도록 설계되어 있습니다.

## 한계 및 참고 사항

- `application.yaml`에 DB 접속 정보가 직접 들어 있어 환경 분리가 되어 있지 않습니다.
- `firebase-service-account.json` 파일이 없으면 애플리케이션이 실행되지 않습니다.
- 인증은 세션/JWT 방식이 아니라 단순 로그인 검증 수준입니다.
- 예외 처리와 응답 형식이 전체적으로 일관되게 정리된 구조는 아닙니다.
- `UserRepository`에는 사용되지 않거나 중복된 register 메서드 정의가 포함되어 있습니다.
- 보안상 민감한 설정은 추후 환경 변수 또는 외부 설정으로 분리하는 것이 좋습니다.

## 학습 포인트

이 프로젝트는 아래 내용을 연습하기 좋은 구조입니다.

- Spring Boot REST API 작성
- JPA + MySQL 연동
- BCrypt 비밀번호 처리
- Firebase Admin SDK 사용법
- FCM 푸시 알림 전송 흐름
- Controller / Service / Repository 계층 분리
