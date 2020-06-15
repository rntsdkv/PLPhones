package ru.prisonlife.plphones.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import ru.prisonlife.plphones.Main;
import ru.prisonlife.plugin.PLPlugin;

import static ru.prisonlife.plphones.Main.colorize;

public class CommandPhonePay implements CommandExecutor {

    private PLPlugin plugin;

    public CommandPhonePay(PLPlugin main) {
        this.plugin = main;
        plugin.getCommand("phone pay").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(colorize(plugin.getConfig().getString("messages.wrongSender")));
        }

        Player senderPlayer = (Player) commandSender;

        Inventory GUI = Bukkit.createInventory(senderPlayer, 1, ChatColor.BOLD + "" + ChatColor.GRAY + "Обменник");

        senderPlayer.openInventory(GUI);
        return true;
    }
}
