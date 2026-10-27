package util;

import java.util.regex.Pattern;

public class AccountValidator {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern HIRAGANA_PATTERN =
            Pattern.compile("^[぀-ゟー]*$");

    private static final Pattern AGE_PATTERN =
            Pattern.compile("^[0-9]{1,3}$");

    // 英字1文字以上・数字1文字以上を必須にする（英字のみ／数字のみは不可）
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z0-9_-]{8,32}$");

    private AccountValidator() {
    }

    // 権限は「一般 / 管理者」の2択（ラジオボタン以外の値が来たらエラー）
    public static String validateRole(String role) {
        if (!"user".equals(role) && !"admin".equals(role)) {
            return "ユーザー種別の値が不正です。";
        }
        return null;
    }

    // ステータスは「有効 / アクセス禁止」の2択
    public static String validateStatus(String status) {
        if (!"active".equals(status) && !"banned".equals(status)) {
            return "ステータスの値が不正です。";
        }
        return null;
    }

    // ユーザー名：必須・255文字以内
    public static String validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return "ユーザー名を入力してください。";
        }
        if (username.length() > 255) {
            return "ユーザー名は255文字以内で入力してください。";
        }
        return null;
    }

    public static String validateName(String name) {
        // 空白だけの入力も「未入力」として扱う
        if (name == null || name.trim().isEmpty()) {
            return "名前を入力してください。";
        }
        if (name.length() > 255) {
            return "名前は255文字以内で入力してください。";
        }
        return null;
    }

    public static String validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            return "メールアドレスを入力してください。";
        }
        if (email.length() > 255) {
            return "メールアドレスは255文字以内で入力してください。";
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return "メールアドレスの形式が正しくありません。";
        }
        return null;
    }

    public static String validateFurigana(String furigana) {
        if (furigana == null || furigana.isEmpty()) {
            return null;
        }
        if (furigana.length() > 255) {
            return "ふりがなは255文字以内で入力してください。";
        }
        if (!HIRAGANA_PATTERN.matcher(furigana).matches()) {
            return "ふりがなはひらがなのみで入力してください。";
        }
        return null;
    }

    public static String validateGender(String gender) {
        if (gender == null || gender.isEmpty()) {
            return null;
        }
        if (!gender.equals("male") && !gender.equals("female") && !gender.equals("other")) {
            return "性別の値が不正です。";
        }
        return null;
    }

    public static String validateAge(String ageStr) {
        if (ageStr == null || ageStr.isEmpty()) {
            return null;
        }
        if (!AGE_PATTERN.matcher(ageStr).matches()) {
            return "年齢は3桁までの数字で入力してください。";
        }
        return null;
    }

    public static String validateBio(String bio) {
        if (bio == null) {
            return null;
        }
        if (bio.codePointCount(0, bio.length()) > 1500) {
            return "自己紹介は1500文字以内で入力してください。";
        }
        return null;
    }

    public static String validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            return null;
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            return "パスワードは8〜32文字の半角英数字（英字・数字を両方含む）と _ - のみ使用できます。";
        }
        return null;
    }

    public static String validateImageSize(long size) {
        if (size > 2 * 1024 * 1024) {
            return "プロフィール画像は2MB以下にしてください。";
        }
        return null;
    }

    // 画像の「中身」が、本当に JPEG / PNG / GIF かを、ファイルの先頭の数バイトで確認する。
    // （拡張子だけを変えた、画像でないファイルを、ブラウザ側のチェックに頼らず、Java側で防ぐ）
    public static String validateImageContent(byte[] head) {
        String message = "正しい画像ファイル（jpg / jpeg / png / gif）を選択してください。";
        if (head == null || head.length < 6) {
            return message;
        }
        boolean jpeg = (head[0] & 0xFF) == 0xFF && (head[1] & 0xFF) == 0xD8 && (head[2] & 0xFF) == 0xFF;
        boolean png = head.length >= 8
                && (head[0] & 0xFF) == 0x89 && head[1] == 'P' && head[2] == 'N' && head[3] == 'G'
                && head[4] == 0x0D && head[5] == 0x0A && head[6] == 0x1A && head[7] == 0x0A;
        boolean gif = head[0] == 'G' && head[1] == 'I' && head[2] == 'F' && head[3] == '8'
                && (head[4] == '7' || head[4] == '9') && head[5] == 'a';
        return (jpeg || png || gif) ? null : message;
    }

    public static String validateImageExtension(String filename) {
        if (filename == null) {
            return "正しい画像ファイル（jpg / jpeg / png / gif）を選択してください。";
        }
        String lower = filename.toLowerCase();
        if (!(lower.endsWith(".jpg") || lower.endsWith(".jpeg")
                || lower.endsWith(".png") || lower.endsWith(".gif"))) {
            return "正しい画像ファイル（jpg / jpeg / png / gif）を選択してください。";
        }
        return null;
    }
}
