# PF_Reiki

## Dockerでの起動

前提: Docker Desktop がインストール・起動していること（[インストール手順](https://docs.docker.com/get-started/get-docker/)）。

```bash
docker compose up --build
```

- アプリ: http://localhost:8080/PF_Reiki/login
- 初期管理者アカウント: ユーザー名 `admin` / パスワード `admin1234`（[db/init.sql](db/init.sql) で作成）
- DB（MySQL）は `db` サービス、アプリ（Tomcat）は `app` サービスとして起動します
- お問い合わせの通知メールを使う場合は `PF_Reiki/src/main/webapp/WEB-INF/mail.properties` を作成し、`docker-compose.yml` の `app.volumes` のコメントを外してください

停止:
```bash
docker compose down        # コンテナを止める（DBデータは volume に残る）
docker compose down -v     # DBデータも含めて完全に削除する
```
