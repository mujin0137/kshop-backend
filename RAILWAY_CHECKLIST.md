# ✅ Railway 배포 체크리스트

## 1단계: Railway 설정

- [ ] Railway 계정 생성 (railway.app)
- [ ] 새 프로젝트 생성
- [ ] PostgreSQL 데이터베이스 추가
- [ ] GitHub 저장소 연결

## 2단계: 환경 변수 설정 (중요!)

Railway Variables 탭에서 다음을 설정:

- [ ] `DATABASE_URL` = `jdbc:postgresql://호스트:5432/railway`

  - Railway PostgreSQL 플러그인이 제공하는 URL을 jdbc 형식으로 변경
  - ❌ `postgres://...` → ✅ `jdbc:postgresql://...`

- [ ] `DB_USERNAME` = `postgres`

- [ ] `DB_PASSWORD` = Railway가 생성한 비밀번호

- [ ] `JWT_SECRET` = 랜덤 문자열 (최소 32자)

  ```bash
  # 터미널에서 생성:
  openssl rand -base64 32
  ```

- [ ] `CORS_ORIGINS` = 프론트엔드 URL
  - 예: `https://your-frontend.vercel.app`
  - 여러 개: `https://app1.com,https://app2.com`

## 3단계: 코드 푸시

- [ ] 변경사항 커밋
  ```bash
  git add .
  git commit -m "Configure for Railway deployment"
  git push
  ```

## 4단계: 배포 확인

- [ ] Railway 대시보드에서 빌드 로그 확인
- [ ] "Active" 상태 확인
- [ ] Health Check 테스트:

  ```
  https://your-app.railway.app/actuator/health
  ```

  예상 응답: `{"status":"UP"}`

- [ ] API 테스트:
  ```
  https://your-app.railway.app/api/products
  ```

## 5단계: 크래시 발생 시

- [ ] Railway Logs 확인
- [ ] 모든 환경 변수가 설정되었는지 확인
- [ ] DATABASE_URL 형식 확인 (`jdbc:postgresql://`로 시작)
- [ ] JWT_SECRET이 32자 이상인지 확인
- [ ] [TROUBLESHOOTING.md](./TROUBLESHOOTING.md) 참조

## 자주 하는 실수

❌ **DATABASE_URL이 `postgres://`로 시작**
→ `jdbc:postgresql://`로 변경 필요

❌ **JWT_SECRET이 너무 짧음**
→ 최소 32자 필요

❌ **환경 변수 누락**
→ 5개 모두 설정 필수

❌ **CORS_ORIGINS 미설정**
→ 프론트엔드에서 API 호출 실패

## 빠른 명령어

```bash
# Railway CLI 설치
npm i -g @railway/cli

# 로그인
railway login

# 로그 확인
railway logs

# 환경 변수 설정
railway variables set JWT_SECRET="your-secret"
railway variables set CORS_ORIGINS="https://your-app.com"

# 재배포
git push
```

## 도움이 필요하신가요?

1. [TROUBLESHOOTING.md](./TROUBLESHOOTING.md) - 자세한 문제 해결 가이드
2. [RAILWAY_DEPLOY.md](./RAILWAY_DEPLOY.md) - 배포 가이드
3. Railway Discord: https://discord.gg/railway
4. Railway Docs: https://docs.railway.app
