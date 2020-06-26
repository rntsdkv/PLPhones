package ru.prisonlife.plphones.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import ru.prisonlife.item.PrisonItem;
import ru.prisonlife.plugin.PLPlugin;

public class GUIClickListener implements Listener {

    private PLPlugin plugin;

    public GUIClickListener(PLPlugin main) {
        this.plugin = main;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        PlayerInventory inventory = event.getWhoClicked().getInventory();

        findPhone(inventory);
    }

    private void findPhone(Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);

            if (item != null) {
                if (item.getItemMeta().getLocalizedName().equals(PrisonItem.PHONE.getNamespace())) {
                    inventory.setItem(i, null);
                }
            }
        }
    }
}
