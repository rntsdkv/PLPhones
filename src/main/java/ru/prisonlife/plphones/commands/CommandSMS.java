package ru.prisonlife.plphones.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;
import ru.prisonlife.PositionManager;
import ru.prisonlife.PrisonLife;
import ru.prisonlife.Prisoner;
import ru.prisonlife.database.json.BoldPoint;
import ru.prisonlife.entity.PhoneEntity;
import ru.prisonlife.item.PrisonItemFactory;
import ru.prisonlife.plphones.Main;
import ru.prisonlife.plugin.PLPlugin;

import java.util.Map;
import java.util.Random;

import static ru.prisonlife.plphones.Main.*;

public class CommandSMS implements CommandExecutor {

    private final Plugin plugin;

    public CommandSMS(Plugin plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(plugin.getConfig().getString("messages.wrongSender"));
            return true;
        }

        Player player = (Player) commandSender;
        Prisoner prisoner = PrisonLife.getPrisoner(player);

        if (!prisoner.hasPhone()) {
            player.sendMessage(colorize(plugin.getConfig().getString("messages.notPhone")));
            return true;
        }

        if (strings.length < 2) {
            player.sendMessage(colorize(plugin.getConfig().getString("messages.notEnoughArguments")));
            return false;
        }

        Integer receiverPhoneNumber = Integer.parseInt(strings[0]);
        PhoneEntity phoneEntity = findPhone(receiverPhoneNumber);

        if (phoneEntity == null) {
            player.sendMessage(colorize(plugin.getConfig().getString("messages.notNumber")));
            return true;
        }

        Prisoner receiverPrisoner = PrisonLife.getPrisoner(phoneEntity.getAccountNumber());
        String message = getMessage(strings);

        if (!checkBalance(prisoner)) {
            player.sendMessage(colorize(plugin.getConfig().getString("messages.notEnoughMoney")));
            return true;
        }

        if (checkHindrance(player)) {
            player.sendMessage(colorize(plugin.getConfig().getString("messages.shouldGoFuck")));
            return true;
        }

        if (newHindrance(player)) {
            player.sendMessage(colorize(plugin.getConfig().getString("messages.shouldGoFuck")));
            return true;
        }
        prisoner.setPhoneMoney(prisoner.getPhoneMoney() - Integer.parseInt(plugin.getConfig().getString("settings.messagePrice")));

        player.sendMessage(colorize("&l&7" + "SMS | Вы: &r" + message + "&l&7 | Получатель: " + receiverPrisoner.getName() + receiverPhoneNumber.toString()));
        receiverPrisoner.getPlayer().sendMessage(colorize("&l&7" + "SMS | &r" + message + "&l&7 | Отправитель: " + player.getName() + prisoner.getPhoneNumber().toString()));


        return true;
    }

    private PhoneEntity findPhone(Integer phoneNumber) {
        for (PhoneEntity phoneEntity : PrisonLife.getPhones()) {
            if (phoneEntity.getPhone().equals(phoneNumber)) return phoneEntity;
        }

        return null;
    }

    private boolean checkBalance(Prisoner prisoner) {
        // сделать проверку баланса телефона(бд)
        Integer moneyOnBalance = prisoner.getPhoneMoney();
        Integer messagePrice = Integer.parseInt(plugin.getConfig().getString("settings.messagePrice"));
        if (moneyOnBalance >= messagePrice) return true;
        return false;
    }

    private String getMessage(String[] strings) {
        StringBuilder text = new StringBuilder();

        for (Integer i = 0; i < strings.length && i != 0; i++) {
            text.append(strings[i]);
        }

        return text.toString();
    }

    private boolean checkHindrance(Player player) {
        Boolean hindranceExists = false;

        for(Location key : hindrancesPlayers.keySet()) {
            if (hindrancesPlayers.get(key) != player) {
                continue;
            }
            if (PositionManager.instance().atSector(BoldPoint.fromLocation(key), hindrancesRadius.get(key), PrisonLife.getPrisoner(player).getPoint())) {
                hindranceExists = true;
                break;
            }
        }

        if (hindranceExists) return true;
        return false;
    }

    private boolean newHindrance(Player player) {
        Random random = new Random();
        int rand = random.nextInt(100);
        int radius = random.nextInt(9) + 2;

        if (rand <= Integer.parseInt(plugin.getConfig().getString("settings.hindranceRandom"))) return false;

        hindrancesPlayers.put(player.getLocation(), player);
        hindrancesRadius.put(player.getLocation(), radius);
        hindrancesSeconds.put(player.getLocation(), 0);

        taskManager();

        return true;
    }

    private void taskManager() {

        if (task == null) {
            task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                for (Location key : hindrancesPlayers.keySet()) {
                    hindrancesSeconds.replace(key, hindrancesSeconds.get(key) + 1);
                    if ((hindrancesSeconds.get(key)) > 60) {
                        hindrancesPlayers.remove(key);
                        hindrancesRadius.remove(key);
                        hindrancesSeconds.remove(key);
                    }
                }
            }, 20, 20);
        }

        if (!hindrancesPlayers.isEmpty()) {
            task.cancel();
            task = null;
        }

    }
}
