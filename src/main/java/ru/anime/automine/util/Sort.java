package ru.anime.automine.util;

import ru.anime.automine.automine.TypeMine;

import java.util.Collections;
import java.util.List;

public class Sort {
    public static List<TypeMine> sort(List<TypeMine> typeMine){
        Collections.sort(typeMine, (type1, type2) -> Integer.compare(type1.getChance(), type2.getChance()));
        return typeMine;
    }
}
