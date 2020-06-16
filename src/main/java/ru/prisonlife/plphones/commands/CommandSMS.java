package ru.prisonlife.plphones.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;
import ru.prisonlife.PositionManager;
import ru.prisonlife.PrisonLife;
import ru.prisonlife.Prisoner;
import ru.prisonlife.database.json.BoldPoint;
import ru.prisonlife.plphones.Main;
import ru.prisonlife.plugin.PLPlugin;

import java.util.Map;
import java.util.Random;

import static ru.prisonlife.plphones.Main.*;

public class CommandSMS implements CommandExecutor {

    final Random random = new Random();
    private PLPlugin plugin;

    public CommandSMS(PLPlugin main) {
        this.plugin = main;
        plugin.getCommand("sms").setExecutor(this);
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(plugin.getConfig().getString("messages.wrongSender"));
            return true;
        }

        Prisoner senderPrisoner = (Prisoner) commandSender;
        Player senderPlayer = (Player) commandSender;

        Integer playerPhoneNumber = senderPrisoner.getPhoneNumber();

        if (!senderPrisoner.hasPhone()) {
            senderPlayer.sendMessage(colorize(plugin.getConfig().getString("messages.notPhone")));
            return true;
        }

        if (strings.length < 2) {
            senderPlayer.sendMessage(colorize(plugin.getConfig().getString("messages.notEnoughArguments")));
            return false;
        }

        Integer addresseePhoneNumber = Integer.parseInt(strings[0]);

        Prisoner addresseePrisoner = (Prisoner) PrisonLife.getPrisoner(addresseePhoneNumber);
        Player addresseePlayer = (Player) Bukkit.getPlayer(addresseePrisoner.getName());

        if (addresseePlayer == null) {
            senderPlayer.sendMessage(colorize(plugin.getConfig().getString("messages.notNumber")));
            return true;
        }

        String message = getMessage(strings);

        if (!checkBalance(senderPrisoner)) {
            senderPlayer.sendMessage(colorize(plugin.getConfig().getString("messages.notEnoughMoney")));
            return true;
        }


        if (checkHindrance(senderPrisoner.getPoint())) {
            senderPlayer.sendMessage(colorize(plugin.getConfig().getString("messages.shouldGoFuck")));
            return true;
        }

        if (newHindrance(senderPlayer.getLocation())) {
            senderPlayer.sendMessage(colorize(plugin.getConfig().getString("messages.shouldGoFuck")));
            return true;
        }
        senderPrisoner.setPhoneMoney(senderPrisoner.getPhoneMoney() - Integer.parseInt(plugin.getConfig().getString("settings.messagePrice")));

        senderPlayer.sendMessage(colorize("&l&7" + "SMS | Вы: &r" + message + "&l&7 | Получатель: " + addresseePlayer.getName() + addresseePhoneNumber.toString()));
        addresseePlayer.sendMessage(colorize("&l&7" + "SMS | &r" + message + "&l&7 | Отправитель: " + senderPlayer.getName() + playerPhoneNumber.toString()));


        return true;
    }

    private boolean checkBalance(Prisoner prisoner) {
        // сделать проверку баланса телефона(бд)
        Integer moneyOnBalance = prisoner.getPhoneMoney();
        Integer messagePrice = Integer.parseInt(plugin.getConfig().getString("settings.messagePrice"));
        if (moneyOnBalance >= messagePrice) return true;
        return false;
    }

    private void moneyIsRunningOut(Prisoner prisoner) {
        Integer moneyOnBalance = prisoner.getPhoneMoney();
        Integer messagePrice = Integer.parseInt(plugin.getConfig().getString("settings.messagePrice"));
        if (moneyOnBalance < messagePrice) {
            Player player = (Player) Bukkit.getPlayer(prisoner.getName());
            player.sendMessage(colorize(plugin.getConfig().getString("messages.moneyIsRunningOut")));
            player.sendMessage(colorize("&l&6Остаток на балансе: " + moneyOnBalance.toString() + "$"));
        }
    }

    private String getMessage(String[] strings) {
        StringBuilder text = new StringBuilder();

        for (Integer i = 0; i < strings.length && i != 0; i++) {
            text.append(strings[i]);
        }

        return text.toString();
    }

    private boolean checkHindrance(BoldPoint playerPoint) {
        Boolean hindranceExists = false;

        Map<Location, Integer> hindrances = Main.hindrances;
        for(Location key : hindrances.keySet()) {
            if (PositionManager.instance().atSector(BoldPoint.fromLocation(key), hindrances.get(key), playerPoint)) {
                hindranceExists = true;
                break;
            }
        }

        if (hindranceExists) return true;
        return false;
    }

    private boolean newHindrance(Location location) {
        int rand = random.nextInt(100);
        int radius = random.nextInt(9) + 2;

        if (rand <= Integer.parseInt(plugin.getConfig().getString("settings.hindranceRandom"))) return false;

        hindrances.put(location, radius);
        hindrancesLocations.add(location);

        taskManager();

        return true;
    }

    private void taskManager() {

        if (task == null) {
            task = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    hindrances.remove(hindrancesLocations.get(0));
                    hindrancesLocations.remove(0);
                }
            }, 1200, 1200);
        }

        if (!hindrances.isEmpty()) {
            task.cancel();
            task = null;
        }

    }
}
