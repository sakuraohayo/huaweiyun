package javaweb.remember.utils;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DataBaseDateUtils {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public static Date String2Date(String str) throws ParseException {
        return new Date(formatter.parse(str).getTime());
    }

    public static String Date2String(Date date) {
        return formatter.format(date);
    }
}
