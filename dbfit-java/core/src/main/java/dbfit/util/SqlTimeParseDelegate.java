package dbfit.util;
import java.lang.RuntimeException;
import java.lang.System;
import java.sql.Time;
import dbfit.util.Log;

public class SqlTimeParseDelegate {
    public static Object parse(String s) {

        // the format of time should be hh.mm.ss or time
        if (s.contains(".")) {
            String[] parts = s.split("\\.");
            Log.log("part: " + parts+", length = " + parts.length);

            if (parts.length == 1)
            {
                return new Time(Integer.parseInt(parts[0]), 0, 0);
            }
            else if (parts.length == 2)
            {
                return new Time(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), 0);
            }
            else if (parts.length == 3)
            {
                return new Time(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
            }
            else
            {
                throw new RuntimeException("Time should be in format hh.mm.ss or nnn where nnn is time in long");
            }
        } else {
            return new Time(Long.parseLong(s));
        }
    };
}
