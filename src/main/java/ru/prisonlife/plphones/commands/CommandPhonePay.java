package ru.prisonlife.plphones.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import ru.prisonlife.PrisonLife;
import ru.prisonlife.Prisoner;
import ru.prisonlife.plugin.PLPlugin;

import static ru.prisonlife.plphones.Main.colorize;

public class CommandPhonePay implements CommandExecutor {

    private PLPlugin plugin;

    public CommandPhonePay(PLPlugin main) {
        this.plugin = main;
        plugin.getCommand("phonepay").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        FileConfiguration config = plugin.getConfig();

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(colorize(config.getString("messages.wrongSender")));
            return true;
        }

        Player player = (Player) commandSender;
        Prisoner prisoner = PrisonLife.getPrisoner(player);

        if (!prisoner.hasPhone()) {
            player.sendMessage(colorize(config.getString("messages.notPhone")));
            return true;
        }

        Inventory GUI = Bukkit.createInventory(player, 9, ChatColor.BOLD + "" + ChatColor.GRAY + "Обменник");

        player.openInventory(GUI);
        return true;
    }
}
