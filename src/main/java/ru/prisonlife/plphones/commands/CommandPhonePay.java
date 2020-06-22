package ru.prisonlife.plphones.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import static ru.prisonlife.plphones.Main.colorize;

public class CommandPhonePay implements CommandExecutor {

    private final Plugin plugin;

    public CommandPhonePay(Plugin plugin) {
        this.plugin = plugin;
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
