package de.frinshhd.logiclobby.commands;

import de.frinshhd.logiclobby.Main;
import de.frinshhd.logiclobby.utils.ChatManager;
import de.frinshhd.logiclobby.utils.SpigotCommandExecutor;
import de.frinshhd.logiclobby.utils.SpigotTranslator;
import de.frinshhd.logiclobby.utils.TranslatorPlaceholder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SpawnCommand extends SpigotCommandExecutor {
    public SpawnCommand() {
        super("spawn");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        //check if sender is a player
        if (!(sender instanceof Player player)) {
            return true;
        }

        if (player.hasPermission("logiclobby.command.spawn")) {
            ChatManager.sendMessage(sender, SpigotTranslator.build("command.spawn"));
            player.teleport(Main.getManager().getConfig().getSpawn().getLocation());
        } else {
            ChatManager.sendMessage(sender, SpigotTranslator.build("noPermission"));
            return true;
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length <= 1) {
            List<String> possibleArguments = new ArrayList<>();

            if (sender.hasPermission("logiclobby.command.fly.others")) {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    if (player.equals(sender)) {
                        return;
                    }

                    possibleArguments.add(player.getName());
                });


            }

            possibleArguments.forEach(possibleArgument -> {
                if (possibleArgument.startsWith(args[0].toLowerCase())) {
                    completions.add(possibleArgument);
                }
            });
        }

        return completions;
    }
}
