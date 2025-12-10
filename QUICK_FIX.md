# 🚨 Health Check 실패 해결 방법

## 현재 문제
빌드는 성공했지만 애플리케이션이 응답하지 않습니다.

## 가장 가능성 높은 원인

### 1. 환경 변수가 설정되지 않음 ⚠️⚠️⚠️

Railway 대시보드에서 **반드시** 확인:

```
✓ DATABASE_URL  (jdbc:postgresql://로 시작)
✓ DB_USERNAME
✓ DB_PASSWORD
✓ JWT_SECRET (32자 이상)
✓ CORS_ORIGINS
```

**하나라도 없으면 앱이 시작되지 않습니다!**

### 2. DATABASE_URL 형식 오류

❌ 잘못된 형식:
```
postgres://railway.internal:5432/railway
```

✅ 올바른 형식:
```
jdbc:postgresql://railway.internal:5432/railway
```

**Railway PostgreSQL 플러그인이 제공하는 URL을 변경해야 합니다!**

## 즉시 확인할 사항

### Railway 대시보드에서:

1. **Variables 탭 열기**
   - 5개 환경 변수가 모두 있는지 확인
   - DATABASE_URL이 `jdbc:postgresql://`로 시작하는지 확인

2. **Deployments → View Logs**
   ```
   찾아야 할 에러 메시지:
   
   - "Could not resolve placeholder 'DATABASE_URL'"
     → 환경 변수 누락
   
   - "Could not create connection to database"
     → DATABASE_URL 형식 또는 연결 오류
   
   - "JWT secret key is too short"
     → JWT_SECRET이 32자 미만
   
   - "Port 8080 is already in use"
     → 포트 설정 오류 (이제 수정됨)
   ```

3. **PostgreSQL 플러그인 확인**
   - PostgreSQL이 같은 프로젝트에 있는지 확인
   - "Connected" 상태인지 확인

## 해결 단계

### Step 1: 환경 변수 설정 (Railway Dashboard)

```bash
# DATABASE_URL 올바른 형식으로 설정
DATABASE_URL=jdbc:postgresql://containers-us-west-xxx.railway.app:5432/railway

# 나머지 변수들
DB_USERNAME=postgres
DB_PASSWORD=Railway가_생성한_비밀번호
JWT_SECRET=생성된_32자_이상의_랜덤_문자열
CORS_ORIGINS=https://your-frontend-url.com
```

### Step 2: 코드 재배포

```bash
git add .
git commit -m "Fix port configuration"
git push
```

### Step 3: 로그 확인

Railway 대시보드에서:
- Deployments → 최신 배포 클릭 → View Logs

정상 시작 로그:
```
Starting KShopBackendApplication
Tomcat started on port(s): XXXX (http)
Started KShopBackendApplication in X seconds
데이터 초기화 완료!
```

## Railway PostgreSQL 연결 방법

Railway에서 PostgreSQL의 DATABASE_URL을 복사한 후:

**Before (Railway 제공):**
```
postgres://postgres:password@containers-us-west.railway.app:5432/railway
```

**After (Java JDBC 형식):**
```
jdbc:postgresql://containers-us-west.railway.app:5432/railway
```

단순히 앞에 `jdbc:`를 붙이고 `postgres`를 `postgresql`로 변경!

## 여전히 안 되면

### Railway CLI로 로그 실시간 확인:

```bash
# Railway CLI 설치
npm i -g @railway/cli

# 로그인
railway login

# 로그 보기
railway logs
```

### 확인해야 할 것:

1. **메모리 부족 (OOM)?**
   ```
   java.lang.OutOfMemoryError
   Process killed
   ```
   → Pro 플랜 필요 또는 메모리 줄이기

2. **데이터베이스 연결 실패?**
   ```
   HikariPool-1 - Exception during pool initialization
   ```
   → DATABASE_URL 다시 확인

3. **JWT 오류?**
   ```
   JWT secret key is too short
   ```
   → JWT_SECRET 32자 이상으로 변경

## 테스트 명령어

로컬에서 환경 변수로 테스트:

```bash
export DATABASE_URL="jdbc:postgresql://localhost:5432/mydb"
export DB_USERNAME="postgres"
export DB_PASSWORD="password"
export JWT_SECRET="test-secret-key-at-least-32-characters-long"
export CORS_ORIGINS="http://localhost:5173"
export PORT="8080"

./mvnw spring-boot:run
```

앱이 시작되면:
```bash
curl http://localhost:8080/actuator/health
# 응답: {"status":"UP"}
```

## 긴급 연락처

- Railway Discord: https://discord.gg/railway
- Railway Status: https://status.railway.app
- Railway Docs: https://docs.railway.app/deploy/deployments

