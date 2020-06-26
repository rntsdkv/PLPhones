package ru.prisonlife.plphones.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.prisonlife.PrisonLife;
import ru.prisonlife.Prisoner;
import ru.prisonlife.plugin.PLPlugin;

import static ru.prisonlife.plphones.Main.*;

public class CommandSellSIM implements CommandExecutor {

    private PLPlugin plugin;

    public CommandSellSIM(PLPlugin main) {
        this.plugin = main;
        plugin.getCommand("sellsim").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        FileConfiguration config = plugin.getConfig();
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(config.getString("messages.wrongSender"));
            return true;
        }

        Player player = (Player) commandSender;
        Prisoner prisoner = PrisonLife.getPrisoner(player);

        if (strings.length != 2) {
            player.sendMessage(colorize(config.getString("messages.notEnoughArguments")));
            return false;
        }

        if (!prisoner.hasPhone()) {
            player.sendMessage(colorize(config.getString("messages.notPhone")));
            return true;
        }

        Player addressee = Bukkit.getPlayer(strings[0]);
        Prisoner addresseePrisoner = PrisonLife.getPrisoner(addressee);

        if (!addressee.isOnline()) {
            player.sendMessage(colorize(config.getString("messages.notPlayerOnline")));
            return true;
        }

        if (addressee == player) {
            player.sendMessage(colorize(config.getString("messages.canNotSellSimYourself")));
            return true;
        }

        if (!addresseePrisoner.hasPhone()) {
            player.sendMessage(colorize(config.getString("messages.playerHasNotGotPhone")));
            return true;
        }

        if (SIMsellers.containsKey(player)) {
            player.sendMessage(colorize(config.getString("messages.alreadySendAccept")));
            return true;
        }

        int price;
        try {
            price = Integer.parseInt(strings[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(colorize(config.getString("messages.notNumberAble")));
            return false;
        }

        if (price < 0) {
            player.sendMessage(colorize(config.getString("messages.notNumberAble")));
            return true;
        }

        sendAccept(player, addressee, price);

        return true;
    }

    private void sendAccept(Player seller, Player buyer, Integer price) {
        buyer.sendMessage(colorize("&l&bИгрок &1" + seller.getName() + "&b хочет поменяться с Вами сим-картой за &1" + price + "$"));
        TextComponent textComponent = new TextComponent(colorize("&l&2Принять"));
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(colorize("&l&2Принять предложение"))));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/phoneaccept " + seller.getName()));
        seller.spigot().sendMessage(textComponent);
        SIMsellers.put(seller, buyer);
        SIMprices.put(seller, price);
        setTask(seller);
    }

    private void setTask(Player seller) {
        new BukkitRunnable() {

            @Override
            public void run() {
                if (SIMsellers.containsKey(seller)) {
                    SIMsellers.remove(seller);
                    SIMprices.remove(seller);
                }
            }

        }.runTaskLater(this.plugin, Integer.parseInt(plugin.getConfig().getString("settings.sellTimeInSec")) * 20);
    }
}
