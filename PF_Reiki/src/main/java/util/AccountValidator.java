package util;

import java.util.regex.Pattern;

// ▼ アカウント追加・編集で共通して使うバリデーション
public class AccountValidator {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // ▼ ひらがな（ー含む）のみ許可
    private static final Pattern HIRAGANA_PATTERN =
            Pattern.compile("^[぀-ゟー]*$");

    private static final Pattern AGE_PATTERN =
            Pattern.compile("^[0-9]{1,3}$");

    // ▼ 半角英数字と _ - のみ、8〜32文字
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^[A-Za-z0-9_-]{8,32}$");

    private AccountValidator() {
    }

    // ▼ 名前（必須・255文字以内）
    public static String validateName(String name) {
        if (name == null || name.isEmpty()) {
            return "名前を入力してください。";
        }
        if (name.length() > 255) {
            return "名前は255文字以内で入力してください。";
        }
        return null;
    }

    // ▼ メールアドレス（必須・形式チェック・255文字以内）
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

    // ▼ ふりがな（任意・255文字以内・ひらがなのみ）
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

    // ▼ 性別（任意・選択肢以外はエラー）
    public static String validateGender(String gender) {
        if (gender == null || gender.isEmpty()) {
            return null;
        }
        if (!gender.equals("male") && !gender.equals("female") && !gender.equals("other")) {
            return "性別の値が不正です。";
        }
        return null;
    }

    // ▼ 年齢（任意・数字のみ・3桁まで）
    public static String validateAge(String ageStr) {
        if (ageStr == null || ageStr.isEmpty()) {
            return null;
        }
        if (!AGE_PATTERN.matcher(ageStr).matches()) {
            return "年齢は3桁までの数字で入力してください。";
        }
        return null;
    }

    // ▼ 自己紹介（任意・1500文字以内、絵文字も可）
    public static String validateBio(String bio) {
        if (bio == null) {
            return null;
        }
        if (bio.codePointCount(0, bio.length()) > 1500) {
            return "自己紹介は1500文字以内で入力してください。";
        }
        return null;
    }

    // ▼ パスワード（任意・入力時のみ8〜32文字の半角英数字と _ - を要求）
    //    空欄の場合は「変更しない」として扱う
    public static String validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            return null;
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            return "パスワードは8〜32文字の半角英数字と _ - のみ使用できます。";
        }
        return null;
    }

    // ▼ プロフィール画像（2MB以内）
    public static String validateImageSize(long size) {
        if (size > 2 * 1024 * 1024) {
            return "プロフィール画像は2MB以下にしてください。";
        }
        return null;
    }
}
