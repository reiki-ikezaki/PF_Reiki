package util;

// ▼ ユーザー入力をHTML表示する際のエスケープ・整形ユーティリティ
public class HtmlUtil {

    private HtmlUtil() {
    }

    // ▼ HTML特殊文字をエスケープする
    public static String escape(String text) {
        if (text == null) {
            return "";
        }
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    // ▼ エスケープした上で改行を <br> に変換する（本文表示用）
    public static String escapeAndBr(String text) {
        return escape(text).replace("\r\n", "<br>").replace("\n", "<br>");
    }

    // ▼ 性別コード（male/female/other）を日本語表記に変換する
    public static String genderLabel(String gender) {
        if (gender == null) {
            return "";
        }
        switch (gender) {
            case "male":   return "男性";
            case "female": return "女性";
            case "other":  return "その他";
            default:       return gender;
        }
    }
}
