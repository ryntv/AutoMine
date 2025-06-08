package ru.anime.automine.automine;

import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import ru.anime.automine.util.Pair;

public class TypeMine {
    private final String id;
    private final String name;
    private final Integer chance;
    private final String teleportPosition;
    private final Integer minOnline;
    private final String permission;
    private final String noPermissionMessage;
    private List<Pair<Float, String>> blockList;
    private final List<String> update_message;
    private final Map<Material, String> customDrop;

    public TypeMine(String id, String name, Integer chance, String teleportPosition, Integer minOnline, String permission, String noPermissionMessage, List<Pair<Float, String>> blockList, List<String> updateMessage, Map<Material, String> customDrop) {
        this.id = id;
        this.name = name;
        this.chance = chance;
        this.teleportPosition = teleportPosition;
        this.minOnline = minOnline;
        this.permission = permission;
        this.noPermissionMessage = noPermissionMessage;
        this.blockList = blockList;
        this.update_message = updateMessage;
        this.customDrop = customDrop;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getChance() {
        return chance;
    }
    public Map<Material, String> getCustomDrop() {
        return customDrop;
    }

    public List<Pair<Float, String>> getBlockList() {
        return blockList;
    }

    public List<String> getUpdate_message() {
        return update_message;
    }

    public String getTeleportPosition() {
        return teleportPosition;
    }
    public Integer getMinOnline() {
        return minOnline;
    }

    public String getPermission() {
        return permission;
    }

    public String getNoPermissionMessage() {
        return noPermissionMessage;
    }
}

