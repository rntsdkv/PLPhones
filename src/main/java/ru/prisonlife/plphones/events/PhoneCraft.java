package ru.prisonlife.plphones.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import ru.prisonlife.PrisonLife;
import ru.prisonlife.Prisoner;
import ru.prisonlife.entity.PhoneEntity;
import ru.prisonlife.item.PrisonItem;
import java.util.List;
import java.util.Optional;
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

        if (event.getCursor().getItemMeta().getDisplayName().equals(PrisonItem.PHONE.getNamespace())) {

            event.getCursor().setAmount(0);
            Player player = (Player) event.getWhoClicked();
            Prisoner prisoner = PrisonLife.getPrisoner(player);

            if (prisoner.hasPhone()) {
                player.sendMessage(colorize(plugin.getConfig().getString("messages.alreadyCraftedPhone")));
            } else {
                prisoner.setPhoneNumber(generatePhoneNumber());
                player.sendMessage(colorize(plugin.getConfig().getString("messages.phoneCraft")));
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