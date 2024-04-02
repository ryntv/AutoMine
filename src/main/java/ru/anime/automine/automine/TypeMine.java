package ru.anime.automine.automine;

import java.util.List;
import java.util.Map;

public class TypeMine {
    private final String id;
    private final String name;
    private final Integer chance;
    private final Map<Float, String> blockList;
    private final List<String> update_message;

    public TypeMine(String id, String name, Integer chance, Map<Float, String> blockList, List<String> updateMessage) {
        this.id = id;
        this.name = name;
        this.chance = chance;
        this.blockList = blockList;
        this.update_message = updateMessage;
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

    public Map<Float, String> getBlockList() {
        return blockList;
    }

    public List<String> getUpdate_message() {
        return update_message;
    }
}
