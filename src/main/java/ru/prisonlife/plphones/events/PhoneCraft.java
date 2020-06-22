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
import ru.prisonlife.plugin.PLPlugin;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import static ru.prisonlife.plphones.Main.colorize;

public class PhoneCraft implements Listener {

    private final PLPlugin plugin;

    public PhoneCraft(PLPlugin main) {
        this.plugin = main;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (checkForPhone(event.getInventory())) {
            Player player = (Player) event.getWhoClicked();
            Prisoner prisoner = (Prisoner) player;

            if (prisoner.hasPhone()) {
                player.sendMessage(colorize(plugin.getConfig().getString("messages.alreadyCraftedPhone")));
            }
            else {
                prisoner.setPhoneNumber(generatePhoneNumber());
                player.sendMessage(colorize(plugin.getConfig().getString("messages.craftPhone")));
            }

            clearPhoneAtInventory(player);
        }

    }

    private boolean checkForPhone(Inventory inventory) {
        return Optional.ofNullable(getPhoneFromInventory(inventory)).isPresent();
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

    private void clearPhoneAtInventory(Player player) {
        PlayerInventory inventory = player.getInventory();
        Integer phoneSlot = getPhoneFromInventory(inventory);
        if (Optional.ofNullable(phoneSlot).isPresent()) inventory.setItem(phoneSlot, null);
    }

    private Integer getPhoneFromInventory(Inventory inventory) {
        int size = inventory.getSize();

        for (int i = 0; i < size; i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null) continue;
            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.getLocalizedName().equals(PrisonItem.PHONE.getNamespace())) return i;
        }

        return null;
    }
}