package ru.prisonlife.plphones.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.prisonlife.PrisonLife;
import ru.prisonlife.Prisoner;
import ru.prisonlife.plugin.PLPlugin;

import static ru.prisonlife.plphones.Main.*;

public class CommandAccept implements CommandExecutor {

    private PLPlugin plugin;
    public CommandAccept(PLPlugin main) {
        this.plugin = main;
        plugin.getCommand("phoneaccept").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(plugin.getConfig().getString("messages.wrongSender"));
            return true;
        }

        Player buyer = (Player) commandSender;

        if (strings.length != 1) {
            buyer.sendMessage(colorize(plugin.getConfig().getString("messages.notEnoughArguments")));
            return false;
        }

        Player seller = Bukkit.getPlayer(strings[0]);

        if (seller == null) {
            buyer.sendMessage(colorize(plugin.getConfig().getString("messages.notPlayer")));
            return true;
        }

        if (!seller.isOnline()) {
            buyer.sendMessage(colorize(plugin.getConfig().getString("messages.notPlayerOnline")));
            return true;
        }

        if (!SIMsellers.containsKey(seller) || SIMsellers.get(seller) != buyer) {
            buyer.sendMessage(colorize(plugin.getConfig().getString("messages.playerNotSell")));
            return true;
        }

        if (SIMprices.get(seller) != 0) {
            if (PrisonLife.getCurrencyManager().countMoney(buyer.getInventory()) < SIMprices.get(seller)) {
                buyer.sendMessage(colorize(plugin.getConfig().getString("messages.notEnoughMoneyReal")));
                return true;
            }

            if (!PrisonLife.getCurrencyManager().canPuttedMoney(seller.getInventory(), SIMprices.get(seller))) {
                seller.sendMessage(colorize(plugin.getConfig().getString("messages.notEnoughSlots")));
                buyer.sendMessage(colorize(plugin.getConfig().getString("messages.sellWasCancelled")));
                return true;
            }

            PrisonLife.getCurrencyManager().reduceMoney(buyer.getInventory(), SIMprices.get(seller));
            for (ItemStack item : PrisonLife.getCurrencyManager().createMoney(SIMprices.get(seller))) {
                seller.getInventory().addItem(item);
            }
        }

        Prisoner sellerPrisoner = PrisonLife.getPrisoner(seller);
        Prisoner buyerPrisoner = PrisonLife.getPrisoner(seller);

        
        Integer sellerPhoneNumber = sellerPrisoner.getPhoneNumber();
        Integer buyerPhoneNumber = buyerPrisoner.getPhoneNumber();

        Integer sellerPhoneMoney = sellerPrisoner.getPhoneMoney();
        Integer buyerPhoneMoney = buyerPrisoner.getPhoneMoney();

        sellerPrisoner.setPhoneNumber(buyerPhoneNumber);
        buyerPrisoner.setPhoneNumber(sellerPhoneNumber);

        sellerPrisoner.setPhoneMoney(buyerPhoneMoney);
        buyerPrisoner.setPhoneMoney(sellerPhoneMoney);

        seller.sendMessage(colorize(plugin.getConfig().getString("messages.phoneSell").replace("%player%", buyer.getName()).replace("%phone%", sellerPhoneNumber.toString())));
        buyer.sendMessage(colorize(plugin.getConfig().getString("messages.phoneSell").replace("%player%", seller.getName()).replace("%phone%", buyerPhoneNumber.toString())));

        SIMsellers.remove(seller);
        SIMprices.remove(seller);

        return true;
    }
}
