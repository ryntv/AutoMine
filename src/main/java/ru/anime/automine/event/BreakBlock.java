package ru.anime.automine.event;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import ru.anime.automine.Main;
import ru.anime.automine.automine.TypeMine;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BreakBlock implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) { // Данная часть кода отвечает за отслеживания где сломался блок
        Player player = event.getPlayer();
        Location blockLocation = event.getBlock().getLocation(); // Получаем местоположение сломанного блока
        Material brokenBlockType = event.getBlock().getType();

        Main.autoMines.forEach((value, key) -> {
            // Проверяем, находится ли сломанный блок внутри прямоугольника
            if (isInsideRectangle(blockLocation, new Location(key.getWorld(), key.getFirstPos().getX(), key.getFirstPos().getY(), key.getFirstPos().getZ()),
                    new Location(key.getWorld(), key.getSecondPos().getX(), key.getSecondPos().getY(), key.getSecondPos().getZ()))) {
                List<TypeMine> typeMine = key.getTypeMine();
                for (TypeMine number : typeMine) {
                    number.getCustomDrop().forEach((key2, value2) -> {
                        if (key.getPresently() == number){
                            customDrop(Material.matchMaterial(String.valueOf(key2)), value2, brokenBlockType, event, key2, player);
                        }
                        });
                }
            }
        });
    }
    private void customDrop(Material material, String string, Material brokenBlockType, BlockBreakEvent event, Material key2, Player player) {
        if (material != null) {
            Pattern pattern = Pattern.compile("<(.*?)>");
            Matcher matcher = pattern.matcher(string);

            String firstValue = null;
            String secondValue = null;

            if (matcher.find()) {
                firstValue = matcher.group(1); // Содержимое первых скобок
            }
            if (matcher.find()) {
                secondValue = matcher.group(1); // Содержимое вторых скобок
            }

            if (firstValue != null && firstValue.equals("DROP")) {
                if (brokenBlockType == key2) {
                    event.setDropItems(false); // Отмена стандартного дропа
                    event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.valueOf(secondValue)));
                }
            }
            else if (firstValue != null && firstValue.equals("GIVE")) {
                if (brokenBlockType == key2) {
                    event.setDropItems(false); // Отмена стандартного дропа
                    ItemStack itemStack = new ItemStack(Material.valueOf(secondValue));
                    // Попробовать добавить предмет в инвентарь
                    if (!player.getInventory().addItem(itemStack).isEmpty()) {
                        // Если инвентарь полон, дропнуть предмет под ноги
                        Location location = player.getLocation();
                        location.getWorld().dropItemNaturally(location, itemStack);
                    }
                }
            }
        }
    }

    private boolean isInsideRectangle(Location blockLocation, Location corner1, Location corner2) {
        double xMin = Math.min(corner1.getX(), corner2.getX());
        double xMax = Math.max(corner1.getX(), corner2.getX());
        double yMin = Math.min(corner1.getY(), corner2.getY());
        double yMax = Math.max(corner1.getY(), corner2.getY());
        double zMin = Math.min(corner1.getZ(), corner2.getZ());
        double zMax = Math.max(corner1.getZ(), corner2.getZ());

        double blockX = blockLocation.getX();
        double blockY = blockLocation.getY();
        double blockZ = blockLocation.getZ();

        return (blockX >= xMin && blockX <= xMax) &&
                (blockY >= yMin && blockY <= yMax) &&
                (blockZ >= zMin && blockZ <= zMax);
    }
}
