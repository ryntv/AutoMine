package ru.anime.automine.util;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.anime.automine.Main;
import ru.anime.automine.automine.AutoMine;

import java.util.Objects;

import static ru.anime.automine.util.FormatTime.integerFormat;
import static ru.anime.automine.util.FormatTime.stringFormat;

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

        return "am";
    }
    @Override
    public String onPlaceholderRequest(Player player, String identifier) {

        if (player == null) {
            return "";
        }

        if (identifier.startsWith("current_")) {
            String key = identifier.replace("current_", "");

            AutoMine autoMine = Main.autoMines.get(key);
            if (autoMine != null) {
                return autoMine.getPresently().getName();
            } else {
                return "Авто-шахта не найдена!!";
            }

        }
        if (identifier.startsWith("next_")) {
            String key = identifier.replace("next_", "");
            AutoMine autoMine = Main.autoMines.get(key);
            if (autoMine != null) {
                return autoMine.getNext().getName();
            } else {
                return "Авто-шахта не найдена!!";
            }

        }
        if (identifier.startsWith("time_")) {
            String key = identifier.replace("time_", "");
            AutoMine autoMine = Main.autoMines.get(key);
            if (autoMine != null) {
                if(Objects.equals(Main.getCfg().getString("TypeFormat"), "integer")){
                    return integerFormat(autoMine.getTime());
                }
                if(Objects.equals(Main.getCfg().getString("TypeFormat"), "string")){
                    return stringFormat(autoMine.getTime());
                }
            } else {
                return "Авто-шахта не найдена!!";
            }

        }
            return null;
        }
}
