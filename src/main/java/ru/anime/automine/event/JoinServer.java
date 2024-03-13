package ru.anime.automine.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.anime.automine.util.HtmlGet;

import static ru.anime.automine.util.Hex.color;

public class JoinServer implements Listener {
    @EventHandler
    public void joinServer(PlayerJoinEvent event){
        if(!event.getPlayer().hasPermission("automine.admin")) {
            return;
        } else {
            String version = HtmlGet.getDataFromUrl();
            if (version != null){
                event.getPlayer().sendMessage(color("&a&l"+version));
            }
        }
    }
}
