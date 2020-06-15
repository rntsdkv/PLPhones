package ru.prisonlife.plphones.commands;

import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import ru.prisonlife.PrisonLife;
import ru.prisonlife.Prisoner;
import ru.prisonlife.plphones.Main;
import ru.prisonlife.plugin.PLPlugin;

import java.util.ArrayList;
import java.util.List;

import static ru.prisonlife.plphones.Main.*;

public class CommandSellSIM implements CommandExecutor {

    private PLPlugin plugin;

    public CommandSellSIM(PLPlugin main) {
        this.plugin = main;
        plugin.getCommand("sellsim").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(plugin.getConfig().getString("messages.wrongSender"));
            return true;
        }

        Player player = (Player) commandSender;
        Prisoner prisoner = (Prisoner) PrisonLife.getPrisoner(player);

        if (strings.length < 2) {
            player.sendMessage(colorize(plugin.getConfig().getString("messages.notEnoughArguments")));
            return false;
        }

        if (!prisoner.hasPhone()) {
            player.sendMessage(colorize(plugin.getConfig().getString("messages.notPhone")));
            return true;
        }

        Player addressee = Bukkit.getPlayer(strings[0]);
        Prisoner addresseePrisoner = (Prisoner) addressee;

        if (addressee == null) {
            player.sendMessage(colorize(plugin.getConfig().getString("messages.notPlayer")));
            return true;
        }

        if (!addresseePrisoner.hasPhone()) {
            player.sendMessage(colorize(plugin.getConfig().getString("messages.playerHasNotGotPhone")));
            return true;
        }

        if (SIMsellers.containsKey(player)) {
            player.sendMessage(colorize(plugin.getConfig().getString("messages.alreadySendAccept")));
            return true;
        }

        int price;
        try {
            price = Integer.parseInt(strings[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(colorize(plugin.getConfig().getString("messages.notNumberAble")));
            return false;
        }

        if (price < 0 || price > 64) {
            player.sendMessage(colorize(plugin.getConfig().getString("messages.priceLimit")));
            return true;
        }

        sendAccept(player, addressee, price);

        return true;
    }

    private void sendAccept(Player seller, Player buyer, Integer price) {
        buyer.sendMessage(colorize("&l&1Игрок &b" + buyer.getName() + "&1 хочет поменяться с Вами сим-картой за &b" + Integer.toString(price)));
        IChatBaseComponent component = IChatBaseComponent.ChatSerializer
                .a("{\"text\":\"&l&bВам предложили сделку: \", \"extra\":[{\"text\":\"&l&2ПРИНЯТЬ\", \"clickEvent\":{\"action\":\"run_command\", \"value\":\"/phone accept " + seller.getName() + "\"}}]}");
        PacketPlayOutChat packet = new PacketPlayOutChat(component);
        ((CraftPlayer) buyer).getHandle().playerConnection.sendPacket(packet);
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

        }.runTaskLater(this.plugin, 1200);
    }
}
