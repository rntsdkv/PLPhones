package ru.prisonlife.plphones.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.prisonlife.item.PrisonItem;

public class PayGUIClick implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getView().getTitle().equals(ChatColor.BOLD + "" + ChatColor.GRAY + "Обменник")) {

            int slotsCount = event.getInventory().getSize();
            String money1 = PrisonItem.DOLLAR_ONE.getNamespace();
            String money2 = PrisonItem.DOLLAR_TWO.getNamespace();
            String money5 = PrisonItem.DOLLAR_FIVE.getNamespace();
            String money10 = PrisonItem.DOLLAR_TEN.getNamespace();
            String money20 = PrisonItem.DOLLAR_TWENTY.getNamespace();
            String money50 = PrisonItem.DOLLAR_FIFTY.getNamespace();
            String money100 = PrisonItem.DOLLAR_HUNDRED.getNamespace();

            for (int i = 0; i < slotsCount; i++) {

                ItemStack item = event.getView().getItem(i);
                String itemName = event.getView().getItem(i).getItemMeta().getLocalizedName();

                if (!itemName.equals(money1) && !itemName.equals(money2) && !itemName.equals(money5) && !itemName.equals(money10) && !itemName.equals(money20) && !itemName.equals(money50) && !itemName.equals(money100)) {

                    event.getView().setItem(i, null);
                    player.getInventory().addItem(new ItemStack(item.getType(), item.getAmount()));
                }

            }

        }
    }
}
