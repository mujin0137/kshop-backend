# K-SHOP Backend API

Spring Boot 기반의 전자상거래 백엔드 API

## 🚀 Railway 배포

### 빠른 배포 가이드

1. **Railway에 프로젝트 생성**

   ```bash
   railway login
   railway init
   ```

2. **PostgreSQL 데이터베이스 추가**

   - Railway 대시보드에서 "New" → "Database" → "PostgreSQL" 선택

3. **환경 변수 설정** (Railway Variables 탭)

   ```
   DATABASE_URL=jdbc:postgresql://호스트:5432/railway
   DB_USERNAME=postgres
   DB_PASSWORD=생성된_비밀번호
   JWT_SECRET=최소_32자_이상의_랜덤_문자열
   CORS_ORIGINS=https://your-frontend-url.com
   ```

4. **배포**
   ```bash
   git add .
   git commit -m "Initial deployment"
   git push
   ```

### 크래시 문제 해결

배포 후 크래시가 발생하나요? 다음 문서를 참고하세요:

- [RAILWAY_DEPLOY.md](./RAILWAY_DEPLOY.md) - 배포 가이드
- [TROUBLESHOOTING.md](./TROUBLESHOOTING.md) - 문제 해결 가이드

## 🛠️ 기술 스택

- **Java 17**
- **Spring Boot 3.2.1**
- **Spring Security + JWT**
- **Spring Data JPA**
- **PostgreSQL**
- **Lombok**

## 📋 주요 기능

- ✅ 사용자 인증 (JWT)
- ✅ 상품 관리
- ✅ 장바구니
- ✅ 관리자 기능
- ✅ CORS 설정

## 🏃 로컬 실행

### 요구사항

- Java 17
- PostgreSQL
- Maven

### 실행 방법

1. **환경 변수 설정**

   ```bash
   export DATABASE_URL="jdbc:postgresql://localhost:5432/kshop"
   export DB_USERNAME="postgres"
   export DB_PASSWORD="your_password"
   export JWT_SECRET="your-secret-key-at-least-32-characters-long"
   export CORS_ORIGINS="http://localhost:5173"
   ```

2. **빌드 및 실행**

   ```bash
   ./mvnw clean package
   ./mvnw spring-boot:run
   ```

3. **접속**
   - API: http://localhost:8080
   - Health Check: http://localhost:8080/actuator/health

## 📡 API 엔드포인트

### 인증

- `POST /api/auth/signup` - 회원가입
- `POST /api/auth/login` - 로그인

### 상품

- `GET /api/products` - 상품 목록 조회
- `GET /api/products/{id}` - 상품 상세 조회

### 장바구니

- `GET /api/cart` - 장바구니 조회
- `POST /api/cart` - 장바구니 추가
- `PUT /api/cart/{id}` - 장바구니 수량 변경
- `DELETE /api/cart/{id}` - 장바구니 삭제

### 관리자 (ADMIN 권한 필요)

- `POST /api/admin/products` - 상품 등록
- `PUT /api/admin/products/{id}` - 상품 수정
- `DELETE /api/admin/products/{id}` - 상품 삭제

## 🔒 보안

- JWT 토큰 기반 인증
- BCrypt 비밀번호 암호화
- CORS 설정
- Role 기반 권한 관리 (USER, ADMIN)

## 📦 빌드

```bash
./mvnw clean package
```

빌드된 파일: `target/backend-0.0.1-SNAPSHOT.jar`

## 🐳 Docker (향후 지원 예정)

```bash
docker build -t kshop-backend .
docker run -p 8080:8080 kshop-backend
```

## 🤝 기여

이슈와 풀 리퀘스트를 환영합니다!

## 📄 라이센스

MIT License
