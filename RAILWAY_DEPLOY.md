# Railway 배포 가이드

## 크래시 문제 해결 방법

### 1. 환경 변수 설정

Railway 대시보드에서 다음 환경 변수들을 **반드시** 설정해야 합니다:

```
DATABASE_URL=jdbc:postgresql://호스트:5432/데이터베이스명
DB_USERNAME=사용자명
DB_PASSWORD=비밀번호
JWT_SECRET=당신의_JWT_시크릿_키_최소_32자
CORS_ORIGINS=https://your-frontend-url.com
PORT=8080
```

### 2. 메모리 설정

Railway의 기본 메모리가 부족할 수 있습니다. 다음을 확인하세요:

- **Free Plan**: 최대 512MB RAM
- **Pro Plan**: 더 많은 RAM 사용 가능

현재 설정:

- Xmx512m (최대 힙 메모리)
- Xms256m (초기 힙 메모리)

### 3. 데이터베이스 연결

**중요**: Railway PostgreSQL 플러그인을 사용하는 경우:

1. PostgreSQL 플러그인 추가
2. 환경 변수가 자동으로 설정되는지 확인
3. DATABASE_URL이 올바른 형식인지 확인

### 4. 빌드 확인

Railway에서 다음과 같이 빌드됩니다:

```bash
./mvnw clean package -DskipTests
```

### 5. Health Check

애플리케이션이 정상적으로 실행되면:

- Health Check URL: `/actuator/health`
- 응답: `{"status":"UP"}`

### 6. 로그 확인

Railway 대시보드에서 로그를 확인하여 크래시 원인 파악:

```bash
railway logs
```

일반적인 크래시 원인:

- ❌ DATABASE_URL이 설정되지 않음
- ❌ JWT_SECRET이 너무 짧음 (최소 32자)
- ❌ 메모리 부족 (OOM)
- ❌ 포트 바인딩 실패
- ❌ 데이터베이스 연결 실패

### 7. 재배포

변경사항을 커밋하고 푸시하면 자동 배포:

```bash
git add .
git commit -m "Fix Railway deployment"
git push
```

## 트러블슈팅

### 크래시가 계속되는 경우:

1. Railway 로그에서 에러 메시지 확인
2. 환경 변수가 모두 설정되었는지 확인
3. 데이터베이스가 실행 중인지 확인
4. 메모리 사용량 확인

### 데이터베이스 연결 오류:

```
Could not create connection to database server
```

→ DATABASE_URL, DB_USERNAME, DB_PASSWORD 확인

### JWT 오류:

```
JWT secret key is too short
```

→ JWT_SECRET을 최소 32자 이상으로 설정

### 메모리 부족:

```
java.lang.OutOfMemoryError: Java heap space
```

→ Railway Pro 플랜으로 업그레이드 또는 메모리 최적화
