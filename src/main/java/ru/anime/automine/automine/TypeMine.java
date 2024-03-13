package ru.anime.automine.automine;

import java.util.Map;

public class TypeMine {
    private final String id;
    private final String name;
    private final Integer chance;
    private final Map<Integer, String> blockList;

    public TypeMine(String id, String name, Integer chance, Map<Integer, String> blockList) {
        this.id = id;
        this.name = name;
        this.chance = chance;
        this.blockList = blockList;
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

    public Map<Integer, String> getBlockList() {
        return blockList;
    }
}
