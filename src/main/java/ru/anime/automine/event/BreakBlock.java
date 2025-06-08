package ru.anime.automine.event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

import static ru.anime.automine.util.Hex.hex;

public class BreakBlock implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Location blockLocation = event.getBlock().getLocation();
        Material brokenBlockType = event.getBlock().getType();

        for (var entry : Main.autoMines.entrySet()) {
            var mine = entry.getValue();

            if (isInsideRectangle(blockLocation,
                    new Location(mine.getWorld(), mine.getFirstPos().getX(), mine.getFirstPos().getY(), mine.getFirstPos().getZ()),
                    new Location(mine.getWorld(), mine.getSecondPos().getX(), mine.getSecondPos().getY(), mine.getSecondPos().getZ()))) {

                TypeMine currentlyActiveType = mine.getPresently();

                String permission = currentlyActiveType.getPermission(); // Например: "mine.use.coal"
                boolean hasPermission = "none".equalsIgnoreCase(permission) || player.hasPermission(permission);

                if (!hasPermission) {
                    event.setCancelled(true);
                    player.sendMessage(hex(currentlyActiveType.getNoPermissionMessage()));
                    return;
                }

                // Выполняем дроп, если есть права
                currentlyActiveType.getCustomDrop().forEach((materialKey, dropData) -> {
                    Material dropMaterial = Material.matchMaterial(String.valueOf(materialKey));
                    customDrop(dropMaterial, dropData, brokenBlockType, event, materialKey, player);
                });

                return; // Выходим после первой подходящей шахты
            }
        }
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
            } else if (firstValue != null && firstValue.equals("CONSOLE")) {
                if (brokenBlockType == key2){
                    String command = secondValue.replaceAll("%player%", event.getPlayer().getName()); // подменяем ник
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
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
