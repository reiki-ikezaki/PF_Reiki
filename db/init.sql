-- PF_Reiki 初期スキーマ + 初期データ
-- docker-compose の MYSQL_DATABASE で myapp_db は既に作成されているので USE のみ行う。
-- （このファイルは MySQL コンテナの初回起動時のみ自動実行されます）

USE myapp_db;
SET NAMES utf8mb4;

-- ─────────────────────────────────────
-- users … アカウント（一般ユーザー／管理者）
-- ─────────────────────────────────────
CREATE TABLE IF NOT EXISTS users (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    username       VARCHAR(255) NOT NULL UNIQUE,
    email          VARCHAR(255) NOT NULL,
    password       VARCHAR(255) NOT NULL,           -- PasswordUtil でハッシュ化した文字列を保存
    name           VARCHAR(255) NOT NULL,
    role           VARCHAR(20)  NOT NULL DEFAULT 'user',   -- 'admin' | 'user'
    status         VARCHAR(20)  NOT NULL DEFAULT 'active',  -- 'active' | 'banned' | 'deleted'
    profile_image  LONGBLOB     NULL,
    bio            TEXT         NULL,
    age            INT          NOT NULL DEFAULT 0,
    gender         VARCHAR(10)  NULL,                -- 'male' | 'female' | 'other'
    furigana       VARCHAR(255) NULL,
    created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at     TIMESTAMP    NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─────────────────────────────────────
-- categories … お問い合わせのカテゴリ
-- ─────────────────────────────────────
CREATE TABLE IF NOT EXISTS categories (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(255) NOT NULL,
    created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at     TIMESTAMP    NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─────────────────────────────────────
-- inquiries … お問い合わせ本体
-- ─────────────────────────────────────
CREATE TABLE IF NOT EXISTS inquiries (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    category_id    INT          NOT NULL,
    content        TEXT         NOT NULL,
    email          VARCHAR(255) NULL,
    status         VARCHAR(20)  NOT NULL DEFAULT '未対応',  -- '未対応' | '対応中' | '対応済み'
    created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at     TIMESTAMP    NULL DEFAULT NULL,
    KEY idx_inquiries_category (category_id)
    -- 外部キー制約は付けない：カテゴリ削除時に問い合わせ側のクリーンアップをアプリ側で行っていないため
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─────────────────────────────────────
-- likes … いいね（誰が・誰に）
-- ─────────────────────────────────────
CREATE TABLE IF NOT EXISTS likes (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    user_id        INT       NOT NULL DEFAULT 0,   -- 未ログインの場合は 0 が入る（LikeServlet参照）
    target_user_id INT       NOT NULL,
    created_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_likes_target (target_user_id)
    -- 外部キー制約は付けない：user_id=0（未ログイン分）を許容するため、
    -- また対象ユーザーの完全削除（DeleteUserPermanentServlet）を妨げないため
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─────────────────────────────────────
-- 初期データ
-- ─────────────────────────────────────

-- お問い合わせカテゴリ（contact.jsp から選べるもの）
INSERT INTO categories (name) VALUES ('意見'), ('質問'), ('その他');

-- 管理者アカウント（ユーザー名: admin / パスワード: admin1234）
-- パスワードは平文で入れているが、UserDao#findByLogin が初回ログイン時に
-- 自動でPBKDF2ハッシュへ変換してDBを更新する仕組みになっているのでこのままでOK。
INSERT INTO users (username, email, password, name, role, status, age)
VALUES ('admin', 'admin@example.com', 'admin1234', '管理者', 'admin', 'active', 0);
