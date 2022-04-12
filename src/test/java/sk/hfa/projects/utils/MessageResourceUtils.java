package sk.hfa.projects.utils;

public class MessageResourceUtils {

    public static String getErrorAsStringWithoutTimestamp(String title, String message, int statusCode) {
        // We must omit the timestamp property, since this number is generated each time the build method is called.
        // If we were to include the timestamp property, all such tests would fail due to non-equal timestamp value.
        return "\"status\":" + statusCode + ",\"title\":\"" + title + "\",\"message\":\"" + message;
    }

}
