package ru.anime.automine.automine;

import org.bukkit.Material;
import ru.anime.automine.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;
import ru.anime.automine.Main;
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
                    String spawnHologram = config.getString(autoMineKey + ".spawnHologram");
                    List<String> lines = config.getStringList(autoMineKey + ".lines");
                    int timeUpdate = config.getInt(autoMineKey + ".timeUpdate");

                    List<TypeMine> typeMineList = new ArrayList<>();
                    if (config.contains(autoMineKey + ".typeMine")) {
                        ConfigurationSection typeMineSection = config.getConfigurationSection(autoMineKey + ".typeMine");

                        for (String typeMineId : typeMineSection.getKeys(false)) {
                            String typeMineKey = autoMineKey + ".typeMine." + typeMineId;
                            String name = config.getString(typeMineKey + ".name");
                            int chance = config.getInt(typeMineKey + ".chance");
                            String teleportPosition = config.getString(typeMineKey + ".teleportPosition");
                            List<Pair<Float, String>> blockList = loadBlockList(config, typeMineKey + ".blockList");
                            List<String> updateMessage = config.getStringList(typeMineKey + ".update_message");
                            Map<Material, String> customDrops = loadCustomDrops(config, typeMineKey + ".customDrops");
                            TypeMine typeMine = new TypeMine(typeMineId, name, chance, teleportPosition , blockList, updateMessage, customDrops);
                            typeMineList.add(typeMine);
                        }
                    }
                    String key = autoMineId.replaceFirst("AutoMines\\.", "");
                    AutoMine autoMine = new AutoMine(key, firstPos, secondPos, spawnHologram, hologramPos, lines, world, Sort.sort(typeMineList), timeUpdate);
                    autoMines.put(key, autoMine);
                    Main.getInstance().getLogger().info("Шахта: " + key + " загрузилась!");
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

    private static List<Pair<Float, String>> loadBlockList(YamlConfiguration config, String path) {
        List<Pair<Float, String>> blockList = new ArrayList<>();

        if (config.isList(path)) {
            List<String> list = config.getStringList(path);
            for (String entry : list) {
                String[] parts = entry.split(" : ");
                if (parts.length == 2) {
                    try {
                        float probability = Float.parseFloat(parts[0]);
                        String block = parts[1];
                        blockList.add(Pair.of(probability, block));
                    } catch (NumberFormatException e) {
                        // Обработка ошибки преобразования строки в число
                        e.printStackTrace();
                    }
                } else {
                    // Неверный формат строки
                    System.err.println("Invalid format for blockList entry: " + entry);
                }
            }
        }

        return blockList;
    }
    private static Map<Material, String> loadCustomDrops(YamlConfiguration config, String path) {
        Map<Material, String> customDrops = new HashMap<>();
        for (String entry : config.getStringList(path)) {
            String[] parts = entry.split(":");
            Material material = Material.valueOf(parts[0]);
            String drop = parts[1];
            customDrops.put(material, drop);
        }
        return customDrops;
    }
}