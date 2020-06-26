package ru.prisonlife.plphones.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.prisonlife.PrisonLife;
import ru.prisonlife.Prisoner;
import ru.prisonlife.item.PrisonItem;
import ru.prisonlife.plugin.PLPlugin;

import java.util.ArrayList;
import java.util.List;

import static ru.prisonlife.plphones.Main.colorize;

public class PayGUIClose implements Listener {

    private PLPlugin plugin;
    public PayGUIClose(PLPlugin main) {
        this.plugin = main;
    }

    @EventHandler
    public void onClosed(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Prisoner prisoner = PrisonLife.getPrisoner(player);

        if (event.getView().getTitle().equals(ChatColor.BOLD + "" + ChatColor.GRAY + "Обменник")) {

            Integer moneySum = PrisonLife.getCurrencyManager().countMoney(event.getInventory());

            int slotsCount = event.getInventory().getSize();

            List<String> money = new ArrayList<>();
            money.add(PrisonItem.DOLLAR_ONE.getNamespace());
            money.add(PrisonItem.DOLLAR_TWO.getNamespace());
            money.add(PrisonItem.DOLLAR_FIVE.getNamespace());
            money.add(PrisonItem.DOLLAR_TEN.getNamespace());
            money.add(PrisonItem.DOLLAR_TWENTY.getNamespace());
            money.add(PrisonItem.DOLLAR_FIFTY.getNamespace());
            money.add(PrisonItem.DOLLAR_HUNDRED.getNamespace());

            Inventory inventory = event.getInventory();

            for (int i = 0; i < slotsCount; i++) {

                ItemStack item = inventory.getItem(i);

                if (item != null) {
                    if (money.contains(item.getItemMeta().getLocalizedName())) {
                        continue;
                    } else {
                        player.getInventory().addItem(item);
                    }
                }

            }

            if (moneySum != 0) {
                prisoner.setPhoneMoney(prisoner.getPhoneMoney() + moneySum);
                player.sendMessage(colorize(plugin.getConfig().getString("messages.phoneMoneyAdd")));
            }
        }
    }
}
