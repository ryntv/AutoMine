package ru.anime.automine.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HtmlGet {
    public static String getDataFromUrl() {
        HttpURLConnection connection = null;
        try {
            URL url = new URL("https://raw.githubusercontent.com/ryntv/AutoMine/refs/heads/master/version");
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(15000);
            connection.setRequestMethod("GET");

            int code = connection.getResponseCode();

            if (code == 200) {
                try (InputStream inputStream = connection.getInputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    return String.join("\n", reader.lines().toList());
                }
            }
        } catch (IOException ignore) {
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }
}
