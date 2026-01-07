package com.ca.passwordmanager.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateUi {

    private static final SimpleDateFormat FMT =
            new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);

    private DateUi() {}

    public static String format(long utcMillis) {
        if (utcMillis <= 0) return "-";
        return FMT.format(new Date(utcMillis));
    }

    public static long addMonths(long utcMillis, int months) {
        if (utcMillis <= 0) return 0;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(utcMillis);
        c.add(Calendar.MONTH, months);
        return c.getTimeInMillis();
    }
}
