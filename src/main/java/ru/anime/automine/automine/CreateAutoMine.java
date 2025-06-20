package ru.anime.automine.automine;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;
import ru.anime.automine.Main;
import ru.anime.automine.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreateAutoMine {

    public static void addAutoMine(String mineId, AutoMine mine, File configFile) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        // Создаем раздел для новой шахты
        String autoMineKey = "AutoMines." + mineId;
        if (config.isConfigurationSection(autoMineKey)) {
            Main.getInstance().getLogger().info("Такая шахта уже создана!");
            return;
        } else {

            config.createSection(autoMineKey);

            // Сохраняем данные о шахте в конфигурацию
            config.set(autoMineKey + ".firstPos", saveVector(mine.getFirstPos()));
            config.set(autoMineKey + ".secondPos", saveVector(mine.getSecondPos()));
            config.set(autoMineKey + ".world", mine.getWorld().getName());
            config.set(autoMineKey + ".spawnHologram", mine.getSpawnHologram());
            config.set(autoMineKey + ".hologramPos", saveLocation(mine.getHologramPos()));
            config.set(autoMineKey + ".lines", mine.getLines());
            config.set(autoMineKey + ".timeUpdate", mine.getTimeUpdate());

            // Создаем раздел для типов шахт
            String typeMineKey = autoMineKey + ".typeMine";
            config.createSection(typeMineKey);

            // Сохраняем данные о каждом типе шахты
            for (TypeMine typeMine : mine.getTypeMine()) {
                String typeId = typeMine.getId();
                String typeMineId = typeMineKey + "." + typeId;

                config.set(typeMineId + ".name", typeMine.getName());
                config.set(typeMineId + ".chance", typeMine.getChance());
                config.set(typeMineId + ".teleportPosition", typeMine.getTeleportPosition());
                config.set(typeMineId + ".minOnline", typeMine.getMinOnline());
                config.set(typeMineId + ".permission", typeMine.getPermission());
                config.set(typeMineId + ".noPermissionMessage", typeMine.getNoPermissionMessage());
                config.set(typeMineId + ".blockList", saveBlockList(typeMine.getBlockList()));
                config.set(typeMineId + ".update_message", new ArrayList<>(typeMine.getUpdate_message()));
                config.set(typeMineId + ".customDrops", saveCustomDrops(typeMine.getCustomDrop()));
            }

            // Сохраняем изменения в файл
            try {
                config.save(configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static ConfigurationSection saveVector(Vector vector) {
        ConfigurationSection section = new YamlConfiguration();
        section.set("x", vector.getX());
        section.set("y", vector.getY());
        section.set("z", vector.getZ());
        return section;
    }

    private static ConfigurationSection saveLocation(Location location) {
        ConfigurationSection section = new YamlConfiguration();
        if (location != null) {
            section.set("world", location.getWorld().getName());
            section.set("x", location.getX());
            section.set("y", location.getY());
            section.set("z", location.getZ());
        }
        return section;
    }

    private static List<String> saveBlockList(List<Pair<Float, String>> blockList) {
        List<String> list = new ArrayList<>();
        for (Pair<Float, String> entry : blockList) {
            list.add(entry.getKey() + " : " + entry.getValue());
        }
        return list;
    }
    private static List<String> saveCustomDrops(Map<Material, String> customDrops) {
        List<String> list = new ArrayList<>();
        for (Map.Entry<Material, String> entry : customDrops.entrySet()) {
            list.add(entry.getKey().name() + ":" + entry.getValue());
        }
        return list;
    }
}
