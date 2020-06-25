package ru.prisonlife.plphones.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import ru.prisonlife.item.PrisonItem;

import java.util.Optional;

public class GUIClick implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        boolean shift = event.getClick().isShiftClick();

        if (shift) {
            if (getPhoneFromInventory(player.getInventory()) != null) {
                clearPhoneAtInventory(player);
            }
        }

    }

    private void clearPhoneAtInventory(Player player) {
        PlayerInventory inventory = player.getInventory();
        Integer phoneSlot = getPhoneFromInventory(inventory);
        if (Optional.ofNullable(phoneSlot).isPresent()) inventory.setItem(phoneSlot, null);
    }

    private Integer getPhoneFromInventory(Inventory inventory) {
        int size = inventory.getSize();

        for (int i = 0; i < size; i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null) continue;
            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.getLocalizedName().equals(PrisonItem.PHONE.getNamespace())) return i;
        }

        return null;
    }
}
