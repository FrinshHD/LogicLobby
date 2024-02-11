package de.frinshhd.logiclobby.commands;

import de.frinshhd.logiclobby.menusystem.LobbySwitcherMenu;
import de.frinshhd.logiclobby.menusystem.TeleporterMenu;
import de.frinshhd.logiclobby.utils.SpigotCommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DevCommand extends SpigotCommandExecutor {
    public DevCommand() {
        super("dev");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        if (args[0].equals("lobby")) {
            new LobbySwitcherMenu(player).open();
        }

        if (args[0].equals("teleporter")) {
            new TeleporterMenu(player).open();
        }


        return false;
    }
}
