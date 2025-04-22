package ru.anime.automine.automine;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import ru.anime.automine.util.UtilHologram;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class AutoMine {
    private final String id;
    private final Vector firstPos;
    private final Vector secondPos;
    private final String spawnHologram;
    private final Location hologramPos;
    private final List<String> lines;
    private final World world;
    private final List<TypeMine> typeMine;
    private final int  timeUpdate;
    private int time;
    private TypeMine presently;
    private TypeMine next;

    public AutoMine(String id, Vector firstPos, Vector secondPos, String spawnHologram, Location hologramPos, List<String> lines,
                    World world, List<TypeMine> typeMine, Integer timeUpdate) {
        this.id = id;
        this.firstPos = firstPos;
        this.secondPos = secondPos;
        this.spawnHologram = spawnHologram;
        this.hologramPos = hologramPos;
        this.lines = lines;
        this.world = world;
        this.typeMine = typeMine;
        this.timeUpdate = timeUpdate;
    }

    public void tick() {
        if (time == 0) {
            presently = null;
            update();
        }
        if (spawnHologram.equals("true")) {
            UtilHologram.createOrUpdateHologram(
                    lines, hologramPos, id
            );
        }
        time--;
    }
    public void update(){
        time = timeUpdate;
        if (presently == null && next == null){
            presently = genRandomType(typeMine);
        }
        if(next == null){
           next = genRandomType(typeMine);
            FullAutoMine.fillBlocks(firstPos, secondPos, world, presently);
        } else {
            presently = next;
            FullAutoMine.fillBlocks(firstPos, secondPos, world, presently);
            next = genRandomType(typeMine);
        }
    }
    public void stop(){
        UtilHologram.remove(id);
    }
    private TypeMine genRandomType(List<TypeMine> mine) {
        Random random = new Random();
        while (true) {
            int randomChance = random.nextInt(100);
            for (TypeMine type : mine) {

                if (type.getChance() >= randomChance) {
                    return type;
                }
            }
        }
    }
    public static boolean isBetweenLocations(Location point, Location loc1, Location loc2) {
        // Получаем координаты каждой точки
        double minX = Math.min(loc1.getX(), loc2.getX());
        double minY = Math.min(loc1.getY(), loc2.getY());
        double minZ = Math.min(loc1.getZ(), loc2.getZ());
        double maxX = Math.max(loc1.getX(), loc2.getX());
        double maxY = Math.max(loc1.getY(), loc2.getY());
        double maxZ = Math.max(loc1.getZ(), loc2.getZ());

        // Получаем координаты текущего игрока
        double x = point.getX();
        double y = point.getY();
        double z = point.getZ();

        // Проверяем, находится ли игрок внутри прямоугольника, образованного двумя точками
        return x >= minX && x <= maxX &&
                y >= minY && y <= maxY &&
                z >= minZ && z <= maxZ;
    }
    public static void teleportPlayer(Vector firstPos, Vector secondPos, World world, String teleportPosition) {
        double maxY;
        if (secondPos.getY() > firstPos.getY()){
            maxY = secondPos.getY();
        } else {
            maxY = firstPos.getY();
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            Location playerLocation = player.getLocation();
            Location firs = new Location(world, firstPos.getX(), firstPos.getY(), firstPos.getZ());
            Location second = new Location(world, secondPos.getX(), secondPos.getY(), secondPos.getZ());
            // Проверяем, находится ли местоположение игрока между заданными местоположениями
            if (isBetweenLocations(playerLocation, firs, second)) {
                Vector headDirection = player.getEyeLocation().getDirection();
                if (teleportPosition.equals("null")){
                    player.teleport(new Location(world, playerLocation.getX(), maxY + 1, playerLocation.getZ()).setDirection(headDirection));
                } else {
                    String[] numbers = null;
                    numbers = teleportPosition.split(",");

                    float x = Float.parseFloat(numbers[0].trim());
                    float y = Float.parseFloat(numbers[1].trim());
                    float z = Float.parseFloat(numbers[2].trim());

                    player.teleport(new Location(world, x, y,z));
                }

            }
        }
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setPresently(TypeMine presently) {
        this.presently = presently;
    }

    public void setNext(TypeMine next) {
        this.next = next;
    }

    public String getId() {
        return id;
    }

    public Vector getFirstPos() {
        return firstPos;
    }

    public Vector getSecondPos() {
        return secondPos;
    }

    public String getSpawnHologram() {
        return spawnHologram;
    }
    public Location getHologramPos() {
        return hologramPos;
    }

    public List<String> getLines() {
        return lines;
    }

    public World getWorld() {
        return world;
    }

    public List<TypeMine> getTypeMine() {
        return typeMine;
    }

    public int getTimeUpdate() {
        return timeUpdate;
    }

    public int getTime() {
        return time;
    }

    public TypeMine getPresently() {
        return presently;
    }

    public TypeMine getNext() {
        return next;
    }
}
