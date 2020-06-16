package ru.prisonlife.plphones.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.prisonlife.PrisonLife;
import ru.prisonlife.Prisoner;
import ru.prisonlife.item.PrisonItem;
import ru.prisonlife.item.PrisonItemFactory;
import ru.prisonlife.plugin.PLPlugin;
import static ru.prisonlife.plphones.Main.colorize;

public class PayGUIClose implements Listener {

    private PLPlugin plugin;

    public PayGUIClose(PLPlugin main) {
        this.plugin = main;
        plugin.getServer().getPluginManager().registerEvents(this, (Plugin) this);
    }

    @EventHandler
    public void onClosed(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Prisoner prisoner = PrisonLife.getPrisoner(player);
        int prisonerMoney = prisoner.getPhoneMoney();

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

                if (item.equals(money1)) {
                    prisoner.setPhoneMoney(prisoner.getPhoneMoney() + item.getAmount());
                } else if (item.equals(money2)) {
                    prisoner.setPhoneMoney(prisoner.getPhoneMoney() + item.getAmount() * 5);
                } else if (item.equals(money5)) {
                    prisoner.setPhoneMoney(prisoner.getPhoneMoney() + item.getAmount() * 10);
                } else if (item.equals(money10)) {
                    prisoner.setPhoneMoney(prisoner.getPhoneMoney() + item.getAmount() * 10);
                } else if (item.equals(money20)) {
                    prisoner.setPhoneMoney(prisoner.getPhoneMoney() + item.getAmount() * 20);
                }else if (item.equals(money50)) {
                    prisoner.setPhoneMoney(prisoner.getPhoneMoney() + item.getAmount() * 50);
                } else if (item.equals(money100)) {
                    prisoner.setPhoneMoney(prisoner.getPhoneMoney() + item.getAmount() * 100);
                }

            }

            int prisonerMoneyDiff = prisoner.getPhoneMoney() - prisonerMoney;

            if (prisonerMoneyDiff != 0) {
                player.sendMessage(colorize("&l&6Вы пополнили баланс на " + prisonerMoneyDiff + "$\nВаш баланс: " + prisoner.getPhoneMoney().toString()));
            }
        }
    }
}
