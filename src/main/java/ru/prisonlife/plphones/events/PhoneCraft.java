package ru.prisonlife.plphones.events;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.prisonlife.PrisonLife;
import ru.prisonlife.Prisoner;
import ru.prisonlife.entity.PhoneEntity;
import ru.prisonlife.item.PrisonItem;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import static ru.prisonlife.plphones.Main.colorize;

public class PhoneCraft implements Listener {

    private final Plugin plugin;

    public PhoneCraft(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        FileConfiguration config = plugin.getConfig();

        Player player = (Player) event.getWhoClicked();
        Prisoner prisoner = PrisonLife.getPrisoner(player);
        ItemStack item = event.getCurrentItem();

        if (item.getItemMeta().getLocalizedName().equals(PrisonItem.PHONE.getNamespace())) {
            if (prisoner.hasPhone()) {
                player.sendMessage(colorize(config.getString("messages.alreadyCraftedPhone")));
            } else {
                prisoner.setPhoneNumber(generatePhoneNumber());
                player.sendMessage(colorize(config.getString("messages.phoneCraft")));
            }
        }
    }

    private int generatePhoneNumber() {
        Random random = new Random();
        List<Integer> phoneNumbers = PrisonLife.getPhones().stream().map(PhoneEntity::getPhone).collect(Collectors.toList());
        int phoneNumber;
        int min = 100_000;
        int max = 999_999;
        int diff = max - min + 1;

        do {
            phoneNumber = random.nextInt(diff) + min;
        } while (phoneNumbers.contains(phoneNumber));

        return phoneNumber;
    }
}