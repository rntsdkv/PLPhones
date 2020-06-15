package ru.prisonlife.plphones.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.prisonlife.PrisonLife;
import ru.prisonlife.Prisoner;
import ru.prisonlife.plphones.Main;
import ru.prisonlife.plugin.PLPlugin;

import static ru.prisonlife.plphones.Main.colorize;

public class CommandSellSIM implements CommandExecutor {

    private PLPlugin plugin;

    public CommandSellSIM(PLPlugin main) {
        this.plugin = main;
        plugin.getCommand("sellsim").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(plugin.getConfig().getString("messages.wrongSender"));
            return true;
        }

        Player player = (Player) commandSender;
        // Prisoner prisoner = (Prisoner) PrisonLife.getPrisoner(player);

        if (strings.length < 1) {
            player.sendMessage(colorize(plugin.getConfig().getString("messages.notEnoughArguments")));
            return false;
        }

        Player addressee = Bukkit.getPlayer(strings[0]);

        if (addressee == null) {
            player.sendMessage(colorize(plugin.getConfig().getString("messages.notPlayer")));
            return true;
        }

        int price;
        try {
            price = Integer.parseInt(strings[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(colorize(plugin.getConfig().getString("messages.notNumberAble")));
            return false;
        }

        addressee.sendMessage(colorize("&l&1Игрок &b" + player.getName() + "&1 хочет поменяться с Вами сим-картой за &b" + Integer.toString(price)));
        // TODO сделать дальше.....................
        return true;
    }
}
