package ru.prisonlife.plphones.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.prisonlife.plphones.Main;

import static ru.prisonlife.plphones.Main.colorize;

public class PhoneSMS implements CommandExecutor {

    private Main plugin;

    public PhoneSMS(Main main) {
        this.plugin = main;
        plugin.getCommand("sms").setExecutor(this);
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(plugin.getConfig().getString("messages.wrongSender"));
            return true;
        }

        Player player = (Player) commandSender;

        Integer playerPhoneNumber = checkPhoneByPlayer(player);

        if (playerPhoneNumber == null) {
            player.sendMessage(colorize(plugin.getConfig().getString("messages.noPhone")));
            return true;
        }

        if (strings.length < 1) {
            player.sendMessage(colorize(plugin.getConfig().getString("messages.notEnoughArguments")));
            return false;
        }

        Integer phoneNumber = Integer.parseInt(strings[0]);

        Player addressee = checkPhoneByNumber(phoneNumber);

        if (addressee == null) {
            player.sendMessage(colorize(plugin.getConfig().getString("messages.noNumber")));
            return true;
        }

        String message = getMessage(strings);

        if (!checkBalance(player)) {
            player.sendMessage(colorize(plugin.getConfig().getString("messages.notEnoughMoney")));
            return true;
        }

        player.sendMessage(colorize("&l&7" + "SMS | Вы: &r" + message + "&l&7 | Получатель: " + addressee.getName() + phoneNumber.toString()));
        addressee.sendMessage(colorize("&l&7" + "SMS | &r" + message + "&l&7 | Отправитель: " + player.getName() + playerPhoneNumber.toString()));

        return true;
    }

    private Integer checkPhoneByPlayer(Player player) {
        // сделать проверку наличия телефона(бд)
        if (true) {
            return phoneNumber;
        }
        return null;
    }

    private Player checkPhoneByNumber(Integer phoneNumber) {
        // сделать проверку наличия телефона(бд)
        if (true) {
            return PlayerName;
        }
        return null;
    }

    private boolean checkBalance(Player player) {
        // сделать проверку баланса телефона(бд)
        if (true) {
            return true;
        }
        return false;
    }

    private String getMessage(String[] strings) {
        StringBuilder text = new StringBuilder();

        for (Integer i = 0; i < strings.length && i != 0; i++) {
            text.append(strings[i]);
        }

        return text.toString();
    }
}
