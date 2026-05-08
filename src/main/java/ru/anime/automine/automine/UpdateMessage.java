package ru.anime.automine.automine;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.anime.automine.util.Hex;

import static ru.anime.automine.util.Hex.hex;

public class UpdateMessage {
    public static void updateMessage(TypeMine typeMine){
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (String element : typeMine.getUpdate_message()) {
                String msg = Hex.setPlaceholders(null, element);
                player.sendMessage(hex(msg));
            }
        }
    }
}
