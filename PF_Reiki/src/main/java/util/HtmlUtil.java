package util;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class HtmlUtil {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

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

    public static String statusLabel(String status) {
        if (status == null) {
            return "";
        }
        switch (status) {
            case "active":  return "アクセス許可";
            case "banned":  return "アクセス禁止";
            case "deleted": return "削除済み";
            default:        return status;
        }
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

    public static String formatDateTime(Timestamp timestamp) {
        if (timestamp == null) {
            return "-";
        }
        return DATE_FORMAT.format(timestamp.toLocalDateTime());
    }
}
