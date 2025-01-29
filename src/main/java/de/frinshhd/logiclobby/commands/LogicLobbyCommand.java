package de.frinshhd.logiclobby.commands;

import de.frinshhd.logiclobby.Main;
import de.frinshhd.logiclobby.model.Config;
import de.frinshhd.logiclobby.utils.SpigotCommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LogicLobbyCommand extends SpigotCommandExecutor {

    public LogicLobbyCommand() {
        super("logiclobby");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 0) {
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "setspawn":
                if (!sender.hasPermission("logiclobby.admin.setspawn") || !(sender instanceof Player player)) {
                    return true;
                }

                Config config = Main.getManager().getConfig();
                config.getSpawn().setLocation(player.getLocation());
                Main.getManager().saveConfig(config);
                sender.sendMessage("§aSpawn successfully set.");
                return true;
            case "reload":
                if (!sender.hasPermission("logiclobby.admin.reload")) {
                    return true;
                }

                Main.reload();
                sender.sendMessage("§aLogicLobby successfully reloaded.");
                return true;
            case "version":
                if (!sender.hasPermission("logiclobby.admin.version")) {
                    return true;
                }

                sender.sendMessage("§aLogicLobby version: " + Main.getInstance().getDescription().getVersion());
                return true;
            default:
                return true;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length <= 1) {

            List<String> possibleArguments = new ArrayList<>(List.of(
                    "setSpawn",
                    "reload",
                    "version"
            ));

            possibleArguments.forEach(possibleArgument -> {
                if (possibleArgument.startsWith(args[0].toLowerCase())) {
                    completions.add(possibleArgument);
                }
            });
        }

        return completions;
    }

}
