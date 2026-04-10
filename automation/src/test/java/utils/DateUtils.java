package utils;

import java.time.LocalDate;

public class DateUtils {

    private DateUtils() {
    }

    public static LocalDate daysFromNow(int days) {
        return LocalDate.now().plusDays(days);
    }
}