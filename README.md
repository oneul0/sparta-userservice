# 사용자 서비스 웹 애플리케이션

사용자 회원가입, 로그인, 관리자 역할 관리 API를 제공합니다. 데이터는 **인메모리 데이터 구조**를 사용하여 저장됩니다. 따라서 데이터는 애플리케이션 실행 중에만 유지되며 종료 시 초기화됩니다. 빌드 도구로 **Gradle**을 사용합니다. 아래는 애플리케이션 사용 방법에 대한 안내입니다.

## 목차
- [사전 요구 사항](#사전-요구-사항)
- [설치](#설치)
- [API 엔드포인트](#api-엔드포인트)
  - [사용자 회원가입](#사용자-회원가입)
  - [사용자 로그인](#사용자-로그인)
  - [관리자 회원가입](#관리자-회원가입)
  - [관리자 로그인](#관리자-로그인)
  - [관리자 권한 부여](#관리자-권한-부여)
- [요청 및 응답 형식](#요청-및-응답-형식)
- [사용 예시](#사용-예시)
- [참고](#참고)

## 사전 요구 사항
- **Java**: JDK 17 이상
- **Gradle**: 버전 7.0 이상 (프로젝트에 포함된 Gradle Wrapper 사용 권장)
- **Swagger**: 애플리케이션 실행 후 `/swagger-ui/index.html`에서 API 문서 확인 가능

## 설치
1. **저장소 복제**:
   ```bash
   git clone <저장소-URL>
   cd user-service
   ```

2. **Gradle 빌드 및 실행**:
   Gradle Wrapper를 사용하여 프로젝트를 빌드하고 실행합니다:
   ```bash
   ./gradlew clean build
   ./gradlew bootRun
   ```

3. **Swagger UI 접속**:
   애플리케이션 실행 후 브라우저에서 `http://localhost:8080/swagger-ui/index.html`에 접속하여 API 문서를 확인하세요.

## API 엔드포인트

### 사용자 회원가입
- **URL**: `/signup`
- **메서드**: `POST`
- **설명**: 일반 사용자를 인메모리 자료구조에 등록합니다.
- **요청 본문**:
  ```json
  {
    "username": "string",
    "password": "string",
    "email": "string"
  }
  ```
- **응답**:
  - **200 OK**: 회원가입 성공, 사용자 정보 반환
  ```json
  {
    "userId": "long",
    "username": "string",
    "email": "string"
  }
  ```

### 사용자 로그인
- **URL**: `/login`
- **메서드**: `POST`
- **설명**: 일반 사용자가 로그인하여 JWT 토큰을 받습니다.
- **요청 본문**:
  ```json
  {
    "username": "string",
    "password": "string"
  }
  ```
- **응답**:
  - **200 OK**: 로그인 성공, JWT 토큰 반환
  ```json
  {
    "token": "string"
  }
  ```

### 관리자 회원가입
- **URL**: `/admin/signup`
- **메서드**: `POST`
- **설명**: 관리자 계정을 인메모리 자료구조에 등록합니다.
- **요청 본문**:
  ```json
  {
    "username": "string",
    "password": "string",
    "email": "string"
  }
  ```
- **응답**:
  - **200 OK**: 관리자 회원가입 성공, 사용자 정보 반환
  ```json
  {
    "userId": "long",
    "username": "string",
    "email": "string"
  }
  ```

### 관리자 로그인
- **URL**: `/admin/login`
- **메서드**: `POST`
- **설명**: 관리자가 로그인하여 JWT 토큰을 받습니다.
- **요청 본문**:
  ```json
  {
    "username": "string",
    "password": "string"
  }
  ```
- **응답**:
  - **200 OK**: 로그인 성공, JWT 토큰 반환
  ```json
  {
    "token": "string"
  }
  ```

### 관리자 권한 부여
- **URL**: `/admin/users/{userId}/roles`
- **메서드**: `PATCH`
- **설명**: 특정 사용자에게 관리자 권한을 부여합니다.
- **경로 변수**:
  - `userId`: 권한을 부여할 사용자 ID (Long)
- **응답**:
  - **200 OK**: 관리자 권한 부여 성공, 사용자 정보 반환
  ```json
  {
    "userId": "long",
    "username": "string",
    "email": "string"
  }
  ```

## 요청 및 응답 형식
- 모든 요청은 JSON 형식의 본문을 사용합니다.
- 성공 응답은 HTTP 상태 코드 `200 OK`와 함께 JSON 형식으로 반환됩니다.
- 오류 응답은 HTTP 상태 코드와 함께 오류 메시지를 포함합니다.

## 사용 예시
아래는 `curl` 명령어를 사용한 API 호출 예시입니다.

1. **사용자 회원가입**:
   ```bash
   curl -X POST http://localhost:8080/signup \
   -H "Content-Type: application/json" \
   -d '{"username":"user1","password":"password123","email":"user1@example.com"}'
   ```

2. **사용자 로그인**:
   ```bash
   curl -X POST http://localhost:8080/login \
   -H "Content-Type: application/json" \
   -d '{"username":"user1","password":"password123"}'
   ```

3. **관리자 회원가입**:
   ```bash
   curl -X POST http://localhost:8080/admin/signup \
   -H "Content-Type: application/json" \
   -d '{"username":"admin1","password":"admin123","email":"admin1@example.com"}'
   ```

4. **관리자 로그인**:
   ```bash
   curl -X POST http://localhost:8080/admin/login \
   -H "Content-Type: application/json" \
   -d '{"username":"admin1","password":"admin123"}'
   ```

5. **관리자 권한 부여**:
   ```bash
   curl -X PATCH http://localhost:8080/admin/users/1/roles \
   -H "Content-Type: application/json"
   ```
