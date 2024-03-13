package ru.anime.automine.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import ru.anime.automine.Main;

import java.util.*;
import java.util.regex.Pattern;

import static ru.anime.automine.event.ClickBlock.playerCreate;
import static ru.anime.automine.util.Hex.color;

public class AutoMineCommand implements CommandExecutor, TabCompleter {
    public static Map<UUID, String> nameAutoMine = new HashMap<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("automine.admin")) {

            return true;
        }
        if (args.length == 0){
            sender.sendMessage("Укажите аргумент");
            return true;
        }
        if (args[0].equals("reload")){
            Main.getInstance().reload();
            sender.sendMessage(color("&aPlugin Reload!"));
        }
        if (args[0].equals("create")){
            if (args.length < 2){
                sender.sendMessage(color("&cВы не указали название для авто-шахты!"));
                return true;
            }
            
            if (checkEnglish(args[1])){
                Player player = (Player) sender;
                nameAutoMine.put(player.getUniqueId(), args[1]);
                Map<String, Location> locationList = new HashMap<>();
                playerCreate.put(player.getUniqueId(), locationList);
                sender.sendMessage(color("&aЛКМ - первая точка"));
                sender.sendMessage(color("&aПКМ - вторая точка"));
            } else {
                sender.sendMessage(color("&cНазвание должно состоять из латинских букв"));
            }
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission("automine.admin")) {
            return null;
        }
        List<String> completions = new ArrayList<>();
        if (args.length == 1){
                completions.add("create");
                completions.add("reload");
        }
        // Фильтруем результаты по введенному тексту
        String input = args[args.length - 1].toLowerCase();
        completions.removeIf(option -> !option.toLowerCase().startsWith(input));
        return completions;
    }
    private static boolean checkEnglish(String string){
        String regex = "^[a-zA-Z0-9]*";
        return Pattern.matches(regex, string);
    }

}
