package javaweb.remember.utils;

public class DataBaseArrayUtils {
    public static final String SEPARATOR = "#";

    public static String ArrayToString(String[] strs) {
        if (strs == null || strs.length == 0) {
            return "";
        }
        StringBuilder str = new StringBuilder();
        for (String s : strs) {
            str.append(s);
            str.append(SEPARATOR);
        }
        return str.toString();
    }

    public static String[] StringToArray(String str) {
        if(str == null || "".equals(str)){
            return null;
        }
        if(!str.contains("#")){
            return new String[]{str};
        }
        return str.split("#");
    }
}
