package ru.prisonlife.plphones.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import ru.prisonlife.PrisonLife;
import ru.prisonlife.Prisoner;
import ru.prisonlife.plphones.Main;
import ru.prisonlife.plugin.PLPlugin;

import java.util.Random;

import static ru.prisonlife.plphones.Main.colorize;

public class CommandSuperSIM implements CommandExecutor {

    private PLPlugin plugin;

    public CommandSuperSIM(Main main) {
        this.plugin = main;
        plugin.getCommand("supersim").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof ConsoleCommandSender)) {
            commandSender.sendMessage(colorize("&l&6Эту команду можно использовать только в консоли!"));
            return true;
        }

        if (strings.length == 0) {
            commandSender.sendMessage(colorize(plugin.getConfig().getString("messages.notEnoughArguments")));
            return false;
        }

        Prisoner prisoner = PrisonLife.getPrisoner(strings[0]);

        if (prisoner.getPlayer() == null) {
            commandSender.sendMessage("Player is not found!");
            return true;
        }

        if (!prisoner.hasPhone()) {
            commandSender.sendMessage("Player have not got a phone!");
            return true;
        }

        Integer prisonerPhoneNumber = 0;
        Integer min = 1_000;
        Integer max = 9_999;
        Integer diff = max - min;
        Random random = new Random();
        while (prisonerPhoneNumber == 0) {
            prisonerPhoneNumber = random.nextInt(diff + 1) + min;
            Prisoner prisonerCheck = PrisonLife.getPrisoner(prisonerPhoneNumber);
            if (prisonerCheck.getPlayer() != null) {
                prisonerPhoneNumber = 0;
            }
        }

        prisoner.setPhoneNumber(prisonerPhoneNumber);
        if (prisoner.isOnline()) {
            prisoner.getPlayer().sendMessage(colorize(plugin.getConfig().getString("messages.changePhoneNumber") + prisonerPhoneNumber.toString()));
        }

        commandSender.sendMessage("Player's phone number changed!");
        return true;
    }
}
