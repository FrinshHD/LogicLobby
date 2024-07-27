package de.frinshhd.logiclobby.commands;

import de.frinshhd.logiclobby.Main;
import de.frinshhd.logiclobby.menusystem.LobbySwitcherMenu;
import de.frinshhd.logiclobby.utils.ChatManager;
import de.frinshhd.logiclobby.utils.SpigotCommandExecutor;
import de.frinshhd.logiclobby.utils.SpigotTranslator;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LobbySwitcherCommand extends SpigotCommandExecutor {
    public LobbySwitcherCommand() {
        super("spawn");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        //check if sender is a player
        if (!(sender instanceof Player player)) {
            return true;
        }

        if (player.hasPermission("logiclobby.command.lobbyswitcher")) {
            new LobbySwitcherMenu(player).open();
        } else {
            ChatManager.sendMessage(sender, SpigotTranslator.build("noPermission"));
            return true;
        }

        return true;
    }
}
