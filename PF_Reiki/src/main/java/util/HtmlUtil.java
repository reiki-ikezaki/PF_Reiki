package util;

public class HtmlUtil {

    private HtmlUtil() {
    }

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

    public static String escapeAndBr(String text) {
        return escape(text).replace("\r\n", "<br>").replace("\n", "<br>");
    }

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
