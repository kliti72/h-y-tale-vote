package com.hytaletop.hytaletopvote.utils;

public class TimeUtils {
    
public static String formatTime(int minutes) {
        if (minutes <= 0) return "poco";
        int h = minutes / 60;
        int m = minutes % 60;
        if (h > 0 && m > 0) return "%dh %dmin".formatted(h, m);
        if (h > 0)           return "%dh".formatted(h);
        return "%dmin".formatted(m);
    }
}
