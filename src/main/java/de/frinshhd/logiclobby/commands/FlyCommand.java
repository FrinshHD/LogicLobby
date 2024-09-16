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

public class FlyCommand extends SpigotCommandExecutor {
    public FlyCommand() {
        super("fly");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        //check if sender is a player
        if (!(sender instanceof Player player)) {
            return true;
        }

        if (args.length == 0) {
            if (player.hasPermission("logiclobby.command.fly")) {
                //toggle player's flight mode
                if (!player.getAllowFlight()) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 15, 0, false, false));
                    new BukkitRunnable() {

                        @Override
                        public void run() {

                            player.setAllowFlight(true);
                            player.setFlying(true);
                            cancel();

                        }
                    }.runTaskLater(Main.getInstance(), 15);
                    player.setAllowFlight(true);
                    player.setFlying(true);

                    ChatManager.sendMessage(sender, SpigotTranslator.build("command.fly.enable"));
                    return true;
                } else if (player.getAllowFlight()) {
                    player.setAllowFlight(false);
                    player.setFlying(false);

                    ChatManager.sendMessage(sender, SpigotTranslator.build("command.fly.disable"));
                    return true;
                }
            } else {
                ChatManager.sendMessage(sender, SpigotTranslator.build("noPermission"));
                return true;
            }

            return true;
        }

        Player target = Bukkit.getServer().getPlayer(args[0]);

        if (target == null) {
            ChatManager.sendMessage(sender, SpigotTranslator.build("playerNotFound"));
            return true;
        }

        if (player.hasPermission("logiclobby.command.fly.others")) {
            if (!target.getAllowFlight()) {
                target.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 15, 0, false, false));
                new BukkitRunnable() {

                    @Override
                    public void run() {

                        target.setAllowFlight(true);
                        target.setFlying(true);
                        cancel();

                    }
                }.runTaskLater(Main.getInstance(), 15);
                target.setAllowFlight(true);
                target.setFlying(true);

                ChatManager.sendMessage(sender, SpigotTranslator.build("command.fly.enable.other", new TranslatorPlaceholder("player", target.getName())));
                return true;
            } else if (target.getAllowFlight()) {
                target.setAllowFlight(false);
                target.setFlying(false);

                ChatManager.sendMessage(sender, SpigotTranslator.build("command.fly.disable.other", new TranslatorPlaceholder("player", target.getName())));
                return true;
            }
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
