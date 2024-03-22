package ru.anime.automine.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import ru.anime.automine.Main;
import ru.anime.automine.automine.AutoMine;
import ru.anime.automine.automine.FullAutoMine;
import ru.anime.automine.automine.TypeMine;

import java.util.*;
import java.util.regex.Pattern;

import static ru.anime.automine.event.ClickBlock.playerCreate;
import static ru.anime.automine.util.Hex.color;

public class AutoMineCommand implements CommandExecutor, TabCompleter {
    public static Map<UUID, String> nameAutoMine = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("automine.admin") && !sender.hasPermission("automine.update") && !sender.hasPermission("automine.set")
                && !sender.hasPermission("automine.create")) {
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("Укажите аргумент");
            return true;
        }
        if (args[0].equals("reload")) {
            Main.getInstance().reload();
            sender.sendMessage(color("&aPlugin Reload!"));
        }
        if (args[0].equals("set")) {
            if (!sender.hasPermission("automine.admin") && !sender.hasPermission("automine.set")) {
                return true;
            }
            if (args.length < 3) {
                sender.sendMessage("Вы должны указать название шахты!");
                return true;
            }
            String mineName = args[1];

            for (Map.Entry<String, AutoMine> entry : Main.autoMines.entrySet()) {
                if (mineName.equals(entry.getKey())) {
                    AutoMine autoMine = entry.getValue();
                    String permission = "automine.set." + entry.getKey();
                    if (!sender.hasPermission(permission)) { // Проверяем разрешение
                        sender.sendMessage("У вас нет разрешения на обновление этой шахты.");
                        return true;
                    }
                    List<TypeMine> typeMines = autoMine.getTypeMine();
                    for (TypeMine typeMine : typeMines) {
                        if (typeMine.getId().equalsIgnoreCase(args[2])) {
                            autoMine.setPresently(typeMine);
                            autoMine.setTime(autoMine.getTimeUpdate());
                            Main.autoMines.put(entry.getKey(), autoMine);
                            FullAutoMine.fillBlocks(autoMine.getFirstPos(), autoMine.getSecondPos(), autoMine.getWorld(), typeMine);
                            return true;
                        }
                    }
                    return true;
                }
            }

            sender.sendMessage(color("&cШахта с указанным названием не найдена."));
        }
        if (args[0].equals("update")) {
            if (!sender.hasPermission("automine.admin") && !sender.hasPermission("automine.update")) {
                return true;
            }
            if (args.length < 3) {
                sender.sendMessage(color("&cВы должны указать название шахты!"));
                return true;
            }
            String mineName = args[1];

            for (Map.Entry<String, AutoMine> entry : Main.autoMines.entrySet()) {
                if (mineName.equals(entry.getKey())) {
                    AutoMine autoMine = entry.getValue();
                    String permission = "automine.update." + entry.getKey();
                    if (!sender.hasPermission(permission)) { // Проверяем разрешение
                        sender.sendMessage(color("&cУ вас нет разрешения на обновление этой шахты."));
                        return true;
                    }

                    if (args[2].equals("next")) {
                        autoMine.setTime(0);
                        Main.autoMines.put(entry.getKey(), autoMine);
                        //  sender.sendMessage("Время шахты " + mineName + " обновлено.");
                        return true;
                    } else {
                        List<TypeMine> typeMines = autoMine.getTypeMine();

                        for (TypeMine typeMine : typeMines) {
                            if (typeMine.getId().equalsIgnoreCase(args[2])) {
                                autoMine.setNext(typeMine);
                                Main.autoMines.put(entry.getKey(), autoMine);
                                //  sender.sendMessage("Следующая шахта для " + mineName + " установлена как " + args[2]);
                                return true;
                            }
                        }

                        //  sender.sendMessage("Указанный тип шахты для " + mineName + " не найден.");
                        return true;
                    }
                }
            }

            sender.sendMessage("Шахта с указанным названием не найдена.");
        }
        if (args[0].equals("create")) {
            if (!sender.hasPermission("automine.admin") && !sender.hasPermission("automine.create")) {
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage(color("&cВы не указали название для авто-шахты!"));
                return true;
            }

            if (checkEnglish(args[1])) {
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

        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            if (sender.hasPermission("automine.admin") || sender.hasPermission("automine.create")) {
                completions.add("create");
                completions.add("reload");
            }
            if (sender.hasPermission("automine.admin") || sender.hasPermission("automine.update")) {
                completions.add("update");
            }
            if (sender.hasPermission("automine.admin") || sender.hasPermission("automine.set")) {
                completions.add("set");
            }
        return completions;
        }
        if (args[0].equalsIgnoreCase("update") && sender.hasPermission("automine.update") || args[0].equalsIgnoreCase("set") && sender.hasPermission("automine.set")) {
            if (args.length == 2) {
                completions.addAll(Main.autoMines.keySet());
            }
            if (args.length < 2) {
                return null;
            }
            if (args[1] != null) {
                List<String> typeMine = new ArrayList<>();

                for (Map.Entry<String, AutoMine> entry : Main.autoMines.entrySet()) {
                    if (entry.getKey().equals(args[1])) {

                        List<TypeMine> auto = entry.getValue().getTypeMine();
                        for (TypeMine item : auto) {
                            typeMine.add(item.getId());
                        }
                    }
                    if (args.length == 3 && args[0].equalsIgnoreCase("update")) {
                        completions.add("next");
                    }
                    completions.addAll(typeMine);

                }

            }
        }
        // Фильтруем результаты по введенному тексту
        String input = args[args.length - 1].toLowerCase();
        completions.removeIf(option -> !option.toLowerCase().startsWith(input));
        return completions;
    }

    private static boolean checkEnglish(String string) {
        String regex = "^[a-zA-Z0-9]*";
        return Pattern.matches(regex, string);
    }

}
