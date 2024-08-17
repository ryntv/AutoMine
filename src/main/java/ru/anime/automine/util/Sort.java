package ru.anime.automine.util;

import ru.anime.automine.automine.TypeMine;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import ru.anime.automine.util.Pair;

public class Sort {
    public static List<TypeMine> sort(List<TypeMine> typeMineList){
        Collections.sort(typeMineList, new Comparator<TypeMine>() {
            @Override
            public int compare(TypeMine type1, TypeMine type2) {
                return Integer.compare(type1.getChance(), type2.getChance());
            }
        });

        for (TypeMine typeMine : typeMineList) {
            Collections.sort(typeMine.getBlockList(), new Comparator<Pair<Float, String>>() {
                @Override
                public int compare(Pair<Float, String> pair1, Pair<Float, String> pair2) {
                    return Float.compare(pair1.getKey(), pair2.getKey());
                }
            });
        }


        return typeMineList;
    }
}
