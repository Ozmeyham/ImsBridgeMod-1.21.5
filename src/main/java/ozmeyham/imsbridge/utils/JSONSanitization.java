package ozmeyham.imsbridge.utils;

public class JSONSanitization {
    public static String sanitizeMessage (String msg) {
        return msg.replace("\"","''").replace("\\","\\\\");
    }
}
// that was surprisingly simple to implement lmao