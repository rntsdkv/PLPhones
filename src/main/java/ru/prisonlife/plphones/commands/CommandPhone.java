package ru.prisonlife.plphones.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.prisonlife.PrisonLife;
import ru.prisonlife.Prisoner;
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
            return true;
        }

        Player player = (Player) commandSender;
        Prisoner prisoner = PrisonLife.getPrisoner(player);

        if (!prisoner.hasPhone()) {
            player.sendMessage(colorize(plugin.getConfig().getString("messages.notPhone")));
            return true;
        }

        String phoneNumber = prisoner.getPhoneNumber().toString();
        String moneyOnBalance = prisoner.getPhoneMoney().toString();

        player.sendMessage(colorize("&l&9Информация о телефоне:\n&3Номер: &b" + phoneNumber + "\n&3Баланс: &b" + moneyOnBalance));
        return true;
    }
}
