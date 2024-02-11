package de.frinshhd.logiclobby.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SpigotCommandExecutor implements CommandExecutor {
    private String commandName = null;

    public SpigotCommandExecutor(String commandName) {
        setCommandName(commandName);
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        return false;
    }
}
