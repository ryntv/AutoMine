package ru.anime.automine.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import ru.anime.automine.Main;
import ru.anime.automine.util.Hex;
import ru.anime.automine.util.HtmlGet;

import java.util.Objects;

import static ru.anime.automine.util.Hex.color;

public class JoinServer implements Listener {
    private String actualVersion;

    public JoinServer(Plugin plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                actualVersion = HtmlGet.getDataFromUrl();
                if (actualVersion == null) {
                    return;
                }
                if (!actualVersion.equals(plugin.getDescription().getVersion())) {
                    plugin.getServer().getScheduler().runTask(Main.getInstance(), () -> {
                        plugin.getServer().getPluginManager().registerEvents(JoinServer.this, plugin);
                    });
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }


    @EventHandler
    public void joinServer(PlayerJoinEvent event) {
        if (!Main.getCfg().getBoolean("checkUpdate")) {
            return;
        }
        if (event.getPlayer().hasPermission("automine.admin")) {

            event.getPlayer().sendMessage(Hex.color("&a&l " + "AutoMine: Вышла новая версия: " + actualVersion + "\n Скачать можно тут -> Discord: https://discord.gg/XCWzCRtxgq"));
        }

    }
}
