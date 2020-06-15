package ru.prisonlife.plphones.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.prisonlife.Prisoner;
import ru.prisonlife.plphones.Main;
import ru.prisonlife.plugin.PLPlugin;

import static ru.prisonlife.plphones.Main.colorize;

public class CommandPhone implements CommandExecutor {

    private PLPlugin plugin;

    public CommandPhone(PLPlugin main) {
        this.plugin = main;
        plugin.getCommand("phone").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(colorize(plugin.getConfig().getString("messages.wrongSender")));
        }

        Player senderPlayer = (Player) commandSender;
        Prisoner senderPrisoner = (Prisoner) commandSender;

        if (!senderPrisoner.hasPhone()) {
            senderPlayer.sendMessage(colorize(plugin.getConfig().getString("messages.notPhone")));
            return true;
        }

        String phoneNumber = senderPrisoner.getPhoneNumber().toString();
        String moneyOnBalance = senderPrisoner.getPhoneMoney().toString();

        senderPlayer.sendMessage(colorize("&l&9Информация о телефоне:\n&3Номер: &b" + phoneNumber + "\n&3Остаток на балансе: &b" + moneyOnBalance));
        return true;
    }
}
