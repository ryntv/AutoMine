package ru.anime.automine.automine;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;
import ru.anime.automine.util.Sort;

import java.io.File;
import java.util.*;

import static ru.anime.automine.Main.autoMines;

public class LoadAutoMine {
    public static Map<String, AutoMine> loadAutoMines(File configFile) {

        try {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
            if (!config.contains("AutoMines")) {
                config.createSection("AutoMines");
                config.save(configFile);
            }

            if (config.contains("AutoMines")) {
                ConfigurationSection autoMinesSection = config.getConfigurationSection("AutoMines");

                for (String autoMineId : autoMinesSection.getKeys(false)) {
                    String autoMineKey = "AutoMines." + autoMineId;

                    Vector firstPos = loadVector(config, autoMineKey + ".firstPos");
                    Vector secondPos = loadVector(config, autoMineKey + ".secondPos");
                    World world = Bukkit.getWorld(config.getString(autoMineKey + ".world"));
                    Location hologramPos = loadLocation(config, autoMineKey + ".hologramPos", world);
                    List<String> lines = config.getStringList(autoMineKey + ".lines");
                    int timeUpdate = config.getInt(autoMineKey + ".timeUpdate");

                    List<TypeMine> typeMineList = new ArrayList<>();
                    if (config.contains(autoMineKey + ".typeMine")) {
                        ConfigurationSection typeMineSection = config.getConfigurationSection(autoMineKey + ".typeMine");

                        for (String typeMineId : typeMineSection.getKeys(false)) {
                            String typeMineKey = autoMineKey + ".typeMine." + typeMineId;
                            String name = config.getString(typeMineKey + ".name");
                            int chance = config.getInt(typeMineKey + ".chance");
                            Map<Integer, String> blockList = loadBlockList(config, typeMineKey + ".blockList");
                            Map<Integer, String> treeMap = new TreeMap<>(blockList);
                            TypeMine typeMine = new TypeMine(typeMineId, name, chance, treeMap);
                            typeMineList.add(typeMine);
                        }
                    }
                    String key = autoMineId.replaceFirst("AutoMines\\.", "");
                    AutoMine autoMine = new AutoMine(key, firstPos, secondPos, hologramPos, lines, world, Sort.sort(typeMineList), timeUpdate);
                    System.out.println("Авто-шахта: " + key + " загружена!");
                    autoMines.put(key, autoMine);
                    List<TypeMine> type = autoMine.getTypeMine();
                    for (TypeMine mine : type) {
                        System.out.println(mine.getChance());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return autoMines;
    }

    private static Vector loadVector(YamlConfiguration config, String path) {
        double x = config.getDouble(path + ".x");
        double y = config.getDouble(path + ".y");
        double z = config.getDouble(path + ".z");
        return new Vector(x, y, z);
    }

    private static Location loadLocation(YamlConfiguration config, String path, World world) {
        if (config.contains(path + ".world")) {
            if (world != null) {
                double x = config.getDouble(path + ".x");
                double y = config.getDouble(path + ".y");
                double z = config.getDouble(path + ".z");

                return new Location(world, x, y, z);
            } else {
                // Мир не найден
                return null;
            }
        } else {
            // Ключ "world" отсутствует в конфигурации
            return null;
        }
    }
    private static Map<Integer, String> loadBlockList(YamlConfiguration config, String path) {
        Map<Integer, String> blockList = new HashMap<>();

        if (config.isConfigurationSection(path)) {
            ConfigurationSection section = config.getConfigurationSection(path);

            for (String key : section.getKeys(false)) {
                int blockKey = Integer.parseInt(key);
                String blockValue = section.getString(key);
                blockList.put(blockKey, blockValue);
            }
        }

        return blockList;
    }
}
