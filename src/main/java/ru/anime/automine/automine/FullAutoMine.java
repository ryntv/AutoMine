package ru.anime.automine.automine;

import ru.anime.automine.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import ru.anime.automine.util.Hex;

import java.util.List;
import java.util.Random;

import static ru.anime.automine.util.Hex.hex;

public class FullAutoMine {
    public static void fillBlocks(Vector min, Vector max, World world, TypeMine typeMine) {
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
        if (!typeMine.getUpdate_message().isEmpty()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                for (String element : typeMine.getUpdate_message()) {
                    String msg = Hex.setPlaceholders(null, element);
                    player.sendMessage(hex(msg));
                }
            }
        }
    }

    private static Material getRandomBlockType(Random random, List<Pair<Float, String>> blockList) {
        while (true) {
            float totalProbability = 0;
            for (Pair<Float, String> pair : blockList) {
                totalProbability += pair.getKey();
            }

            // Генерация случайного числа в диапазоне от 0 до общей суммы вероятностей
            float randomValue = random.nextFloat() * totalProbability;

            // Находим блок на основе случайного значения
            float cumulativeProbability = 0;
            for (Pair<Float, String> pair : blockList) {
                cumulativeProbability += pair.getKey();
                if (randomValue <= cumulativeProbability) {
                    // Возвращаем тип блока по значению в list
                    return Material.getMaterial(pair.getValue());
                }
            }
        }
    }
}
