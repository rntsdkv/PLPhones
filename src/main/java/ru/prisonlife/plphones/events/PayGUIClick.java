package ru.prisonlife.plphones.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.prisonlife.item.PrisonItem;
import ru.prisonlife.item.PrisonItemFactory;
import ru.prisonlife.plugin.PLPlugin;

public class PayGUIClick implements Listener {

    private PLPlugin plugin;
    public PayGUIClick(PLPlugin main) {
        this.plugin = main;
        plugin.getServer().getPluginManager().registerEvents(this, (Plugin) this);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getView().getTitle().equals(ChatColor.BOLD + "" + ChatColor.GRAY + "Обменник")) {

            int slotsCount = event.getInventory().getSize();
            ItemStack money1 = PrisonItemFactory.createItem(null, PrisonItem.PRISON_MONEY);
            ItemStack money10 = PrisonItemFactory.createItem(null, PrisonItem.PRISON_MONEY_TEN);
            ItemStack money50 = PrisonItemFactory.createItem(null, PrisonItem.PRISON_MONEY_FIFTY);
            ItemStack money100 = PrisonItemFactory.createItem(null, PrisonItem.CIGARETTE_HUNDRED);

            for (int i = 0; i < slotsCount; i++) {

                ItemStack item = event.getView().getItem(i);

                if (!item.equals(money1) && !item.equals(money10) && !item.equals(money50) && !item.equals(money100)) {

                    event.getView().setItem(i, null);
                    player.getInventory().addItem(new ItemStack(item.getType(), item.getAmount()));
                }

            }

        }
    }
}
