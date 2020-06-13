package ru.prisonlife.plphones.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.prisonlife.plphones.Main;

import static ru.prisonlife.plphones.Main.colorize;

public class CommandPhone implements CommandExecutor {

    private Main plugin;

    public CommandPhone(Main main) {
        this.plugin = main;
        plugin.getCommand("phone").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(colorize(plugin.getConfig().getString("messages.wrongSender")));
        }

        Player player = (Player) commandSender;


        return true;
    }
}
