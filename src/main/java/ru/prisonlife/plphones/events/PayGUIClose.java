package ru.prisonlife.plphones.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import ru.prisonlife.PrisonLife;
import ru.prisonlife.Prisoner;
import ru.prisonlife.item.PrisonItem;
import java.util.ArrayList;
import java.util.List;

import static ru.prisonlife.plphones.Main.colorize;

public class PayGUIClose implements Listener {

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

            for (int i = 0; i < slotsCount; i++) {

                ItemStack item = event.getView().getItem(i);

                if (!money.contains(item.getItemMeta().getDisplayName())) {
                    player.getInventory().addItem(item);
                }

            }

            prisoner.setPhoneMoney(prisoner.getPhoneMoney() + moneySum);

            if (moneySum != 0) {
                player.sendMessage(colorize("&l&6Вы пополнили баланс на " + moneySum + "$\nБаланс: " + prisoner.getPhoneMoney().toString()));
            }
        }
    }
}
