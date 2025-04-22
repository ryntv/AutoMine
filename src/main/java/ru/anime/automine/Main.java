package ru.anime.automine;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import ru.anime.automine.automine.AutoMine;
import ru.anime.automine.automine.LoadAutoMine;
import ru.anime.automine.command.AutoMineCommand;
import ru.anime.automine.event.BreakBlock;
import ru.anime.automine.event.ClickBlock;
import ru.anime.automine.event.JoinServer;
import ru.anime.automine.util.AutoMinePlaceholder;
import ru.anime.automine.util.HtmlGet;
import ru.anime.automine.util.Metrics;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public final class Main extends JavaPlugin {
    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    private static File AutoMines;
    private static FileConfiguration cfg;
    public static Map<String, AutoMine> autoMines = new HashMap<>();
    private AutoMinePlaceholder placeholderExpansion;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        cfg = getConfig();
        instance = this;

        HtmlGet.getDataFromUrl();
        createAutoMinesFile(true);
        getLogger().info("AutoMine Start!");
        new Metrics(this, 21311);
        getCommand("automine").setExecutor(new AutoMineCommand());
        getServer().getPluginManager().registerEvents(new ClickBlock(), this);
        new JoinServer(this);
        getServer().getPluginManager().registerEvents(new BreakBlock(), this);
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            placeholderExpansion = new AutoMinePlaceholder(this);
            placeholderExpansion.register();
        } else {
            getLogger().warning("PlaceholderAPI не обнаружен! Некоторые функции могут быть недоступны.");
        }
        Bukkit.getScheduler().runTaskTimer(this, this::tick, 0, 20);


    }

    @Override
    public void onDisable() {
        getLogger().info("Stop!");
        if (placeholderExpansion != null) {
            placeholderExpansion.unregister();
        }
        autoMines.values().forEach(AutoMine::stop);
    }

    public void reload() {
        autoMines.values().forEach(AutoMine::stop);
        autoMines.clear();

        reloadConfig();
        cfg = getConfig();
        createAutoMinesFile(false);
    }


    private void createAutoMinesFile(Boolean firstStart) {
        try {
            File autoMinesFile = new File(getDataFolder(), "AutoMines.yml");
            AutoMines = autoMinesFile;

            if (!autoMinesFile.getParentFile().exists()) {
                autoMinesFile.getParentFile().mkdirs();
            }


            if (autoMinesFile.createNewFile()) {
                getLogger().info("Файл AutoMines.yml успешно создан");
                if (firstStart) {
                    getLogger().info("Шахта загрузится через 15 секунд!");
                    BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
                    scheduler.runTaskLater(this, () -> {
                        LoadAutoMine.loadAutoMines(AutoMines);
                    }, 300L);
                } else {
                    LoadAutoMine.loadAutoMines(AutoMines);
                }


            } else {
                if (firstStart) {
                    getLogger().info("Шахта загрузится через 15 секунд!");
                    BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
                    scheduler.runTaskLater(this, () -> {
                        LoadAutoMine.loadAutoMines(AutoMines);
                    }, 300L);
                } else {
                    LoadAutoMine.loadAutoMines(AutoMines);
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void tick() {
        autoMines.values().forEach(AutoMine::tick);
    }

    public static File getAutoMines() {
        return AutoMines;
    }

    public static FileConfiguration getCfg() {
        return cfg;
    }
}
