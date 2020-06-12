package ru.prisonlife.plphones;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static ru.prisonlife.plphones.Main.colorize;

public class PhoneCraft implements Listener {

    private Main plugin;

    public PhoneCraft(Main main) {
        this.plugin = main;
        plugin.getServer().getPluginManager().registerEvents(this);
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (checkForPhone(event.getInventory())) {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage(colorize(plugin.getConfig().getString("messages.craftPhone")));
            // ! отправить запрос в бд о создании телефона
            removePhone(player);
        }

    }

    private boolean checkForPhone(Inventory inventory) {
        // на время тестов телефон - это алмазный топор, после необходимо изменить на CustomModel
        if (inventory.contains(new ItemStack(Material.DIAMOND_AXE))) {
            return true;
        }
        return false;
    }

    private void removePhone(Player player) {
        // на время тестов телефон - это алмазный топор, после необходимо изменить на CustomModel
        player.getInventory().remove(new ItemStack(Material.DIAMOND_AXE));
    }

}
