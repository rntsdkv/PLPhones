package ru.prisonlife.plphones;

import net.minecraft.server.v1_15_R1.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import ru.prisonlife.plugin.PLPlugin;
import sun.jvm.hotspot.ui.ObjectHistogramPanel;

import static ru.prisonlife.plphones.Main.colorize;

public class PayGUI implements CommandExecutor, Listener {

    private PLPlugin plugin;
    private Inventory GUI;

    public PayGUI(PLPlugin main) {
        this.plugin = main;
        plugin.getCommand("phone pay").setExecutor(this);
        plugin.getServer().getPluginManager().registerEvents(this, (Plugin) this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(colorize(plugin.getConfig().getString("messages.wrongSender")));
        }

        Player senderPlayer = (Player) commandSender;

        GUI = Bukkit.createInventory(senderPlayer, 1, ChatColor.BOLD + "" + ChatColor.GRAY + "Обменник");

        senderPlayer.openInventory(GUI);
        return true;
    }

    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory().getName)
        if (inventoryEquals(event.getClickedInventory(), GUI)) {
            
        }
    }

    private boolean inventoryEquals(Inventory inventory1, Inventory inventory2) {
        if (InventoryUtils.equals(inventory1, inventory2)) return true;
        return false;
    }
}
