package ru.prisonlife.plphones.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.prisonlife.plphones.Main;
import ru.prisonlife.plugin.PLPlugin;

import static ru.prisonlife.plphones.Main.SIMsellers;
import static ru.prisonlife.plphones.Main.colorize;

public class CommandAccept implements CommandExecutor {

    private PLPlugin plugin;
    public CommandAccept(PLPlugin main) {
        this.plugin = main;
        plugin.getCommand("phone accept").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(plugin.getConfig().getString("messages.wrongSender"));
            return true;
        }

        Player buyer = (Player) commandSender;

        if (strings.length == 0) {
            buyer.sendMessage(colorize(plugin.getConfig().getString("messages.notEnoughArguments")));
            return false;
        }

        Player seller = Bukkit.getPlayer(strings[0]);

        if (!SIMsellers.containsKey(seller) || SIMsellers.get(seller) != buyer) {
            buyer.sendMessage(colorize(plugin.getConfig().getString("messages.playerNotSell")));
            return true;
        }

        if (seller == null) {
            buyer.sendMessage(colorize(plugin.getConfig().getString("messages.notPlayer")));
            return true;
        }

        if (!seller.isOnline()) {
            buyer.sendMessage(colorize(plugin.getConfig().getString("messages.notPlayerOnline")));
            return true;
        }



        return true;
    }
}
