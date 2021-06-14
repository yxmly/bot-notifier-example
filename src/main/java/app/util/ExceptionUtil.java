package app.util;

public class ExceptionUtil {
    public static void toRuntimeException(Exception e) {
        throw new RuntimeException(e);
    }
}
