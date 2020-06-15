package ru.prisonlife.plphones.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import ru.prisonlife.PrisonLife;
import ru.prisonlife.Prisoner;
import ru.prisonlife.entity.PhoneEntity;
import ru.prisonlife.plugin.PLPlugin;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import static ru.prisonlife.plphones.Main.colorize;

public class CommandSuperSIM implements CommandExecutor {

    private PLPlugin plugin;

    public CommandSuperSIM(PLPlugin main) {
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
            commandSender.sendMessage("Player has not got a phone!");
            return true;
        }

        prisoner.setPhoneNumber(generatePhoneNumber());

        if (prisoner.isOnline()) {
            prisoner.getPlayer().sendMessage(colorize(plugin.getConfig().getString("messages.changePhoneNumber") + prisoner.getPhoneNumber().toString()));
        }

        commandSender.sendMessage("Player's phone number changed!");
        return true;
    }

    private int generatePhoneNumber() {
        Random random = new Random();
        List<Integer> phoneNumbers = PrisonLife.getPhones().stream().map(PhoneEntity::getPhone).collect(Collectors.toList());
        int phoneNumber;
        int min = 1000;
        int max = 9999;
        int diff = max - min + 1;

        do {
            phoneNumber = random.nextInt(diff) + min;
        } while (phoneNumbers.contains(phoneNumber));

        return phoneNumber;
    }
}
