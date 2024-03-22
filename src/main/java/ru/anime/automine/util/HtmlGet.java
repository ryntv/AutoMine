package ru.anime.automine.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HtmlGet {
    public static String getDataFromUrl() {
        try {
            // Создание URL для запроса
            URL url = new URL("http://ryntv55s.beget.tech/AutoMine.php");

            // Открытие соединения
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Установка метода запроса (GET)
            connection.setRequestMethod("GET");

            // Получение ответа
            int responseCode = connection.getResponseCode();

            // Чтение ответа
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Вывод ответа
            if (String.valueOf(response).equals("0.1.3")){
                connection.disconnect();
            } else {
                return "AutoMine: Вышла новая версия: " + response;
            }

            // Закрытие соединения
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
