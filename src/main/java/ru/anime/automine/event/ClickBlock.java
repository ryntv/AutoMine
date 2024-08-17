package ru.anime.automine.event;

import org.bukkit.Material;
import ru.anime.automine.util.Pair;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import ru.anime.automine.Main;
import ru.anime.automine.automine.AutoMine;
import ru.anime.automine.automine.CreateAutoMine;
import ru.anime.automine.automine.TypeMine;

import java.util.*;

import static ru.anime.automine.Main.autoMines;
import static ru.anime.automine.command.AutoMineCommand.nameAutoMine;
import static ru.anime.automine.util.Hex.color;


public class ClickBlock implements Listener {

    private final HashMap<UUID, Long> antiDoubleClick = new HashMap<>();
    public static Map<UUID, Map<String, Location>> playerCreate = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        if (!playerCreate.containsKey(playerId)) {
            return;
        }

        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Location firstLocation = event.getClickedBlock().getLocation();
            playerCreate.get(playerId).put("firstLocation", firstLocation);

            event.getPlayer().sendMessage(color("&aПервая точка: " + firstLocation.getBlockX() + " " + firstLocation.getBlockY() + " " + firstLocation.getBlockZ()));
            createAutoMine(playerCreate.get(playerId), event);
            event.setCancelled(true);
        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (antiDoubleClick.getOrDefault(event.getPlayer().getUniqueId(), 0L) > System.currentTimeMillis()) {
                return;
            } else {
                antiDoubleClick.put(event.getPlayer().getUniqueId(), System.currentTimeMillis() + 20L);
                Location secondLocation = event.getClickedBlock().getLocation();
                playerCreate.get(playerId).put("secondLocation", secondLocation);

                event.getPlayer().sendMessage(color("&aВторая точка: " + secondLocation.getBlockX() + " " + secondLocation.getBlockY() + " " + secondLocation.getBlockZ()));
                createAutoMine(playerCreate.get(playerId), event);
                event.setCancelled(true);
            }
        }
    }

    public void createAutoMine(Map<String, Location> locations, PlayerInteractEvent event) {
        Location firstLocation = locations.get("firstLocation");
        Location secondLocation = locations.get("secondLocation");
        if (firstLocation != null && secondLocation != null) {

            String key = nameAutoMine.get(event.getPlayer().getUniqueId());
            List<String> lines = List.of("Текущая шахта: " + "%am_current_" + key + "%",
                    "Следующая шахта: " + "%am_next_" + key + "%",
                    "До обновления: " + "%am_time_" + key + "%");

            Map<Float, String> blockListDefault = new TreeMap<>();
            blockListDefault.put(50F, "IRON_ORE");
            blockListDefault.put(100F, "STONE");
            Map<Float, String> blockListSuper = new TreeMap<>();
            blockListSuper.put(50F, "GOLD_ORE");
            blockListSuper.put(100F, "STONE");
            List<String> messageDefault = new ArrayList<>();
            messageDefault.add(" ");
            messageDefault.add("Шахта обновилась");
            messageDefault.add(" ");
            List<String> messageSuper = new ArrayList<>();
            Map<Material, String> custom = new HashMap<>();
            custom.put(Material.DIRT, "<GIVE><DIAMOND>");

            TypeMine typeMine = new TypeMine("default", "Обычная", 100, mapToList(blockListDefault), messageDefault, custom);
            TypeMine typeMine2 = new TypeMine("super", "Редкая", 50, mapToList(blockListSuper), messageSuper, custom);


            List<TypeMine> typeList = List.of(typeMine, typeMine2);
            AutoMine autoMine = new AutoMine(nameAutoMine.get(event.getPlayer().getUniqueId()), firstLocation.toVector(), secondLocation.toVector(),"true" ,
                    average(firstLocation, secondLocation, event.getPlayer().getWorld()), lines, event.getPlayer().getWorld(), typeList, 300);
            if (autoMines.containsKey(nameAutoMine.get(event.getPlayer().getUniqueId()))){
                event.getPlayer().sendMessage(color("&aТакая шахта уже существует!"));
                event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_WANDERING_TRADER_NO, 1.0f, 1.0f);
            } else {
                autoMines.put(nameAutoMine.get(event.getPlayer().getUniqueId()), autoMine);
                event.getPlayer().sendMessage(color("&aАвто-шахта успешно создана!"));
                event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2.0f, 1.0f);
                CreateAutoMine.addAutoMine(nameAutoMine.get(event.getPlayer().getUniqueId()), autoMine, Main.getAutoMines());
            }
            playerCreate.remove(event.getPlayer().getUniqueId());
            nameAutoMine.remove(event.getPlayer().getUniqueId());

        }
    }

    public Location average(Location v1, Location v2, World world) {
        double centerY = Math.max(v1.getY(), v2.getY()) + 3;

        return new Location(world, v1.getX(), centerY, v1.getZ());
    }

    private static List<Pair<Float, String>> mapToList(Map<Float, String> map) {
        List<Pair<Float, String>> list = new ArrayList<>();
        for (Map.Entry<Float, String> entry : map.entrySet()) {
            Float key = entry.getKey();
            String value = entry.getValue();
            list.add(Pair.of(key, value));
        }
        return list;
    }

}
//

//