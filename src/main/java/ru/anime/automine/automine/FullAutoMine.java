package ru.anime.automine.automine;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import ru.anime.automine.Main;

import java.util.Map;
import java.util.Random;

import static ru.anime.automine.util.Hex.hex;

public class FullAutoMine {
    public static void fillBlocks(Vector min, Vector max, World world, TypeMine typeMine){
            int x1 = Math.min(min.getBlockX(), max.getBlockX());
            int y1 = Math.min(min.getBlockY(), max.getBlockY());
            int z1 = Math.min(min.getBlockZ(), max.getBlockZ());

            int x2 = Math.max(min.getBlockX(), max.getBlockX());
            int y2 = Math.max(min.getBlockY(), max.getBlockY());
            int z2 = Math.max(min.getBlockZ(), max.getBlockZ());

            Random random = new Random();
            AutoMine.teleportPlayer(min, max, world);
            for (int x = x1; x <= x2; x++) {
                for (int y = y1; y <= y2; y++) {
                    for (int z = z1; z <= z2; z++) {
                        Location currentLocation = new Location(world, x, y, z);
                        currentLocation.getBlock().setType(getRandomBlockType(random, typeMine.getBlockList()));
                    }
                }
            }
            if (typeMine.getUpdate_message().isEmpty()){

            } else {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    for (String element : typeMine.getUpdate_message()) {
                        player.sendMessage(hex(element));
                    }
                }
            }
        }

        private static Material getRandomBlockType(Random random, Map<Integer, String> blockList) {
            int totalWeight = blockList.keySet().stream().mapToInt(Integer::intValue).sum();
            int randomWeight = random.nextInt(totalWeight) + 1;
            while (true) {
                for (Map.Entry<Integer, String> entry : blockList.entrySet()) {
                    randomWeight -= entry.getKey();
                    if (randomWeight <= 0) {
                        // Возвращаем тип блока по значению в map
                        return Material.getMaterial(entry.getValue());
                    }
                }
            }
        }
    }

