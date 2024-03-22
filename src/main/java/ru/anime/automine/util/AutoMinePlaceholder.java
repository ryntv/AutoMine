package ru.anime.automine.util;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.anime.automine.Main;
import ru.anime.automine.automine.AutoMine;

import static ru.anime.automine.util.FormatTime.integerFormat;

public class AutoMinePlaceholder extends PlaceholderExpansion {

    private final Main plugin;

    public AutoMinePlaceholder(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getAuthor() {
        return null;
    }
    @Override
    public boolean persist() {
        return true;
    }
    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }
    @Override
    public String getIdentifier() {
        // Возвращает идентификатор плейсхолдера
        return "am";
    }
    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        // Обработка запроса плейсхолдера
        if (player == null) {
            return "";
        }

        // Пример обработки плейсхолдера
        if (identifier.startsWith("current_")) {
            // Извлекаем значение из идентификатора
            String key = identifier.replace("current_", "");

            AutoMine autoMine = Main.autoMines.get(key);
            if (autoMine != null) {
                return autoMine.getPresently().getName();
            } else {
                return "Авто-шахта не найдена!!";
            }

        }
        if (identifier.startsWith("next_")) {
            // Извлекаем значение из идентификатора
            String key = identifier.replace("next_", "");

            AutoMine autoMine = Main.autoMines.get(key);
            if (autoMine != null) {
                return autoMine.getNext().getName();
            } else {
                return "Авто-шахта не найдена!!";
            }

        }
        if (identifier.startsWith("time_")) {
            // Извлекаем значение из идентификатора
            String key = identifier.replace("time_", "");

            AutoMine autoMine = Main.autoMines.get(key);
            if (autoMine != null) {
                return integerFormat(autoMine.getTime());
            } else {
                return "Авто-шахта не найдена!!";
            }

        }
            return null;
        }
}
