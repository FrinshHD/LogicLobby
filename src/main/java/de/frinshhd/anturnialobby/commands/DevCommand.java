package de.frinshhd.anturnialobby.commands;

import de.frinshhd.anturnialobby.menusystem.TeleporterMenu;
import de.frinshhd.anturnialobby.model.Teleporter;
import de.frinshhd.anturnialobby.utils.SpigotCommandExecutor;
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

        new TeleporterMenu(player).open(true);

        return false;
    }
}
