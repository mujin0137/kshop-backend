# ⚠️ 환경 변수 체크리스트

Railway Variables 탭에서 다음 5개가 **모두** 있어야 합니다:

## 필수 환경 변수

### 1. DATABASE_URL
```
jdbc:postgresql://호스트:포트/데이터베이스명
```
- ✅ `jdbc:postgresql://`로 시작해야 함
- ❌ `postgres://`로 시작하면 안 됨

### 2. DB_USERNAME
```
postgres
```

### 3. DB_PASSWORD
```
Railway PostgreSQL이 생성한 비밀번호
```

### 4. JWT_SECRET
```
32자 이상의 랜덤 문자열
```
생성 방법:
```bash
openssl rand -base64 32
```
또는 온라인에서: https://generate-secret.now.sh/32

### 5. CORS_ORIGINS
```
https://your-frontend-url.com
```
또는 여러 개:
```
https://app1.com,https://app2.com,http://localhost:5173
```

---

## 확인 방법

1. Railway 대시보드 상단의 **Variables** 탭 클릭
2. 위 5개 변수가 모두 있는지 확인
3. DATABASE_URL이 `jdbc:postgresql://`로 시작하는지 확인
4. JWT_SECRET이 32자 이상인지 확인

## 하나라도 없으면?

→ **"+ New Variable"** 버튼을 클릭해서 추가!

---

## Railway PostgreSQL 연결하는 법

### PostgreSQL 플러그인이 없다면:
1. Dashboard → "New" → "Database" → "PostgreSQL"
2. 자동으로 환경 변수 생성됨

### PostgreSQL 플러그인이 있다면:
1. PostgreSQL 서비스 클릭
2. "Connect" 탭에서 `DATABASE_URL` 복사
3. `postgres://`를 `jdbc:postgresql://`로 변경
4. kshop-backend 서비스의 Variables에 추가

---

## 다음 단계

환경 변수 설정 후:
1. Railway가 자동으로 재배포
2. **Deploy Logs** 탭에서 로그 확인
3. "Started KShopBackendApplication" 메시지 확인

