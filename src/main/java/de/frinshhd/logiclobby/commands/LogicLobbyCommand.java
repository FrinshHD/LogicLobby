package de.frinshhd.logiclobby.commands;

import de.frinshhd.logiclobby.Main;
import de.frinshhd.logiclobby.utils.SpigotCommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
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

        switch (args[0]) {
            case "reload":
                if (!sender.hasPermission("logiclobby.reload")) {
                    return false;
                }

                Main.reload();
                sender.sendMessage("§aLogicLobby successfully reloaded.");
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
                    "reload"
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
