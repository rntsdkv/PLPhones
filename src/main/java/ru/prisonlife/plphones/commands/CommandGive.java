package ru.prisonlife.plphones.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.prisonlife.PrisonLife;
import ru.prisonlife.plugin.PLPlugin;

public class CommandGive implements CommandExecutor {

    private PLPlugin plugin;

    public CommandGive(PLPlugin main) {
        this.plugin = main;
        plugin.getCommand("give").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;

        for (ItemStack item : PrisonLife.getCurrencyManager().createMoney(Integer.parseInt(strings[0]))) {
            player.getInventory().addItem(item);
        }
        return true;
    }
}
