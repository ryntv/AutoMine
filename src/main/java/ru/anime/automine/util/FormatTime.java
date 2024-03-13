package ru.anime.automine.util;

public class FormatTime {
    public static String integerFormat(int Sec){
        int hour = Sec / 3600;//3600
        int min = Sec % 3600 / 60;
        int sec = Sec % 60;
        return String.format("%02d:%02d:%02d", hour, min, sec);
    }
    public static String stringFormat(int hour, int min, int sec){

        StringBuilder formattedTime = new StringBuilder();

        if (hour > 0) {
            formattedTime.append(formatUnit(hour, "час", "часа", "часов")).append(" ");
        }

        if (min > 0) {
            formattedTime.append(formatUnit(min, "минута", "минуты", "минут")).append(" ");
        }

        if (hour == 0 && min == 0 && sec > 0) {
            formattedTime.append(formatUnit(sec, "секунда", "секунды", "секунд"));
        }

        return formattedTime.toString().trim();
    }

    private static String formatUnit(int value, String form1, String form2, String form5) {
        value = Math.abs(value);
        int remainder10 = value % 10;
        int remainder100 = value % 100;

        if (remainder10 == 1 && remainder100 != 11) {
            return String.format("%d %s", value, form1);
        } else if (remainder10 >= 2 && remainder10 <= 4 && (remainder100 < 10 || remainder100 >= 20)) {
            return String.format("%d %s", value, form2);
        } else {
            return String.format("%d %s", value, form5);
        }
    }
}
