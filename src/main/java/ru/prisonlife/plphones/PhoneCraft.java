package ru.prisonlife.plphones;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.prisonlife.PrisonLife;
import ru.prisonlife.Prisoner;
import ru.prisonlife.item.PrisonItem;
import ru.prisonlife.plugin.PLPlugin;

import java.util.Random;

import static ru.prisonlife.plphones.Main.colorize;

public class PhoneCraft implements Listener {

    private PLPlugin plugin;

    public PhoneCraft(Main main) {
        this.plugin = main;
        plugin.getServer().getPluginManager().registerEvents(this, (Plugin) this);
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (checkForPhone(event.getInventory())) {

            Player player = (Player) event.getWhoClicked();
            Prisoner prisoner = (Prisoner) player;

            if (prisoner.hasPhone()) {

                player.sendMessage(colorize(plugin.getConfig().getString("messages.alreadyCraftedPhone")));

            } else {

                Integer prisonerPhoneNumber = 0;
                // TODO донат-возможность: 4значный номер телефона, маршалл
                Integer min = 100_000;
                Integer max = 999_999;
                Integer diff = max - min;
                Random random = new Random();
                while (prisonerPhoneNumber == 0) {
                    prisonerPhoneNumber = random.nextInt(diff + 1) + min;
                    Player prisonerCheck = (Player) Bukkit.getPlayer(PrisonLife.getPrisoner(prisonerPhoneNumber).getName());
                    if (prisonerCheck != null) {
                        prisonerPhoneNumber = 0;
                    }
                }
                prisoner.setPhoneNumber(prisonerPhoneNumber);
                player.sendMessage(colorize(plugin.getConfig().getString("messages.craftPhone")));

            }

            removePhone(player);
        }

    }

    private boolean checkForPhone(Inventory inventory) {
        if (inventory.contains(new ItemStack(PrisonItem.PHONE)) {
            return true;
        }
        return false;
    }

    private void removePhone(Player player) {
        player.getInventory().remove(new ItemStack(PrisonItem.PHONE));
    }

}
