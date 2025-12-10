# Railway 배포 크래시 해결 가이드

## 🚨 크래시가 발생하는 주요 원인

### 1. 환경 변수 누락 ⚠️

가장 흔한 원인입니다. Railway 대시보드에서 다음을 확인하세요:

```bash
# 필수 환경 변수 체크리스트
✓ DATABASE_URL
✓ DB_USERNAME
✓ DB_PASSWORD
✓ JWT_SECRET (최소 32자)
✓ CORS_ORIGINS
```

**확인 방법:**

1. Railway 대시보드 열기
2. 프로젝트 선택
3. Variables 탭 클릭
4. 위 5개 환경 변수가 모두 있는지 확인

### 2. 데이터베이스 연결 실패 🔌

**증상:**

```
Could not create connection to database server
Connection refused
```

**해결 방법:**

1. Railway에서 PostgreSQL 플러그인 추가:
   - Dashboard → New → Database → PostgreSQL
2. 데이터베이스와 애플리케이션이 같은 프로젝트에 있는지 확인
3. DATABASE_URL 형식 확인:
   ```
   jdbc:postgresql://호스트:5432/데이터베이스명
   ```
   ❌ 잘못된 형식: `postgres://...`
   ✅ 올바른 형식: `jdbc:postgresql://...`

### 3. 메모리 부족 (OOM) 💾

**증상:**

```
java.lang.OutOfMemoryError: Java heap space
Process exited with code 137
```

**해결 방법:**

- Free Plan: 최대 512MB (현재 설정으로 충분함)
- 메모리 부족 시 Pro Plan으로 업그레이드

**현재 메모리 설정:**

- 최대 힙: 512MB
- 최소 힙: 256MB
- 커넥션 풀: 최대 5개

### 4. JWT Secret 오류 🔑

**증상:**

```
JWT secret key is too short
Illegal argument exception
```

**해결 방법:**

```bash
# JWT_SECRET은 최소 32자 이상이어야 합니다
# 예시: 랜덤 문자열 생성
openssl rand -base64 32
```

### 5. 포트 바인딩 실패 🔌

**증상:**

```
Port 8080 is already in use
Web process failed to bind to $PORT
```

**해결 방법:**

- PORT 환경 변수를 설정하지 마세요 (Railway가 자동 설정)
- 또는 `PORT=8080` 설정

## 📋 체크리스트

배포 전 확인사항:

- [ ] Railway PostgreSQL 플러그인 추가됨
- [ ] 모든 환경 변수 설정됨 (DATABASE_URL, DB_USERNAME, DB_PASSWORD, JWT_SECRET, CORS_ORIGINS)
- [ ] JWT_SECRET이 32자 이상
- [ ] DATABASE_URL이 `jdbc:postgresql://`로 시작
- [ ] CORS_ORIGINS에 프론트엔드 URL 입력
- [ ] 코드 변경사항 커밋 및 푸시됨

## 🔍 로그 확인 방법

Railway CLI 설치:

```bash
npm i -g @railway/cli
railway login
railway logs
```

또는 Railway 대시보드:

1. 프로젝트 선택
2. Deployments 탭
3. 최신 배포 클릭
4. View Logs

## 🎯 일반적인 로그 메시지

### 정상 실행:

```
Started KShopBackendApplication in X.XXX seconds
Tomcat started on port(s): 8080
데이터 초기화 완료!
```

### 데이터베이스 연결 실패:

```
HikariPool-1 - Exception during pool initialization
Could not create connection to database server
```

→ DATABASE_URL, DB_USERNAME, DB_PASSWORD 확인

### 환경 변수 누락:

```
Could not resolve placeholder 'DATABASE_URL'
```

→ Railway Variables 탭에서 환경 변수 추가

### JWT 오류:

```
JWT secret key is too short
```

→ JWT_SECRET을 32자 이상으로 변경

## 🚀 재배포 방법

코드 수정 후:

```bash
git add .
git commit -m "Fix deployment issues"
git push
```

Railway가 자동으로 재배포합니다.

## ✅ 배포 성공 확인

1. Railway 대시보드에서 "Active" 상태 확인
2. 브라우저에서 Health Check 접속:

   ```
   https://your-app.railway.app/actuator/health
   ```

   응답: `{"status":"UP"}`

3. API 테스트:
   ```
   https://your-app.railway.app/api/products
   ```

## 💡 추가 팁

1. **로컬에서 먼저 테스트:**

   ```bash
   ./mvnw clean package
   java -jar target/backend-0.0.1-SNAPSHOT.jar
   ```

2. **환경 변수 로컬 테스트:**

   ```bash
   export DATABASE_URL="jdbc:postgresql://localhost:5432/mydb"
   export DB_USERNAME="postgres"
   export DB_PASSWORD="password"
   export JWT_SECRET="your-secret-key-at-least-32-characters-long"
   export CORS_ORIGINS="http://localhost:5173"
   ./mvnw spring-boot:run
   ```

3. **Railway CLI로 환경 변수 설정:**
   ```bash
   railway variables set JWT_SECRET="your-secret-key"
   railway variables set CORS_ORIGINS="https://your-frontend.com"
   ```

## 🆘 그래도 안 되면

1. Railway 대시보드에서 전체 로그 다운로드
2. 로그에서 에러 메시지 찾기
3. 이 문서의 해당 섹션 참조
4. Railway Discord 또는 Support에 문의
