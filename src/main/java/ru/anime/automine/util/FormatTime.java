package ru.anime.automine.util;

import ru.anime.automine.Main;

public class FormatTime {
    private static String timeFormat;

    public static String integerFormat(int Sec) {
        int hour = Sec / 3600; // Количество часов
        int min = (Sec % 3600) / 60; // Количество минут
        int sec = Sec % 60; // Количество секунд
        String sHour;
        String sMin;
        String sSec;
        if (hour < 10){
             sHour = "0"+hour;
        } else {
             sHour = String.valueOf(hour);
        }
        if (min < 10){
             sMin = "0"+min;
        } else {
             sMin = String.valueOf(min);
        }
        if (sec < 10){
             sSec = "0"+sec;
        } else {
             sSec = String.valueOf(sec);
        }
        // Получаем строку формата времени из конфигурации
        String str = Main.getCfg().getString("timeFormat");

        // Проверка значения str для отладки
        System.out.println("Полученный формат времени: " + str);

        // Если строка формата времени не задана, используем формат по умолчанию
        if (str == null) {
            return hour + ":" + min + ":" + sec;
        }

        // Замена символов 'h', 'm' и 's' на соответствующие значения

        str = str.replace("h", sHour);
        str = str.replace("m", sMin);
        str = str.replace("s", sSec);

        // Проверка результата после замены для отладки
        System.out.println("Формат времени после замены: " + str);

        return str;
    }
    public static String stringFormat(int Sec){
        int hour = Sec / 3600;//3600
        int min = Sec % 3600 / 60;
        int sec = Sec % 60;
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
        if (hour == 0 && min == 0 && sec == 0){
            formattedTime.append(formatUnit(sec, "секунд", "секунд", "секунд"));
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
