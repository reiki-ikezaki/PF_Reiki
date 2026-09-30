# ─────────────────────────────────────────────────────────
# PF_Reiki を Tomcat 上で動かすための Docker イメージ
#
#   build 段階 … Javaソースをコンパイルして .class を作る（javacが必要なのでJDKイメージ）
#   final 段階 … できあがった webapp を Tomcat に置くだけ（JREだけで良いので軽量）
#
# ビルド: docker build -t pf_reiki .
# 実行  : docker run -p 8080:8080 pf_reiki
#   ※ 単体で動かす場合は環境変数 DB_HOST 等でDB接続先を指定してください。
#      通常は docker-compose.yml から使うことを想定しています。
# ─────────────────────────────────────────────────────────

FROM tomcat:11.0-jdk21-temurin AS build

WORKDIR /workspace

# webapp一式（JSP・WEB-INF/lib・web.xml など）をコピー
COPY PF_Reiki/src/main/webapp ./webapp
# Javaソース（サーブレット・DAO・model・util）をコピー
COPY PF_Reiki/src/main/java ./javasrc

# Tomcat自身のlib（jakarta.servlet 等のAPI）＋ WEB-INF/lib の依存jar（mysqlドライバ等）を
# クラスパスにしてコンパイルし、Tomcatが読み込む WEB-INF/classes に出力する
RUN mkdir -p ./webapp/WEB-INF/classes && \
    javac -encoding UTF-8 \
          -d ./webapp/WEB-INF/classes \
          -cp "/usr/local/tomcat/lib/*:./webapp/WEB-INF/lib/*" \
          $(find ./javasrc -name '*.java')


FROM tomcat:11.0-jre21-temurin AS final

# Tomcat付属のサンプルアプリを削除
RUN rm -rf /usr/local/tomcat/webapps/*

# コンテキストパスは "/PF_Reiki" 固定にする。
# login.jsp などが "/PF_Reiki/xxx" を絶対パスで参照しているため、
# 配置先フォルダ名（＝コンテキストパス）をこれに合わせる必要がある。
COPY --from=build /workspace/webapp /usr/local/tomcat/webapps/PF_Reiki

EXPOSE 8080
