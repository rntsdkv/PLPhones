package ru.prisonlife.plphones.events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
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

public class GUIClickListener implements Listener {

    private PLPlugin plugin;

    public GUIClickListener(PLPlugin main) {
        this.plugin = main;
    }
    FileConfiguration config = plugin.getConfig();

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Prisoner prisoner = PrisonLife.getPrisoner(player);
        Inventory inventory = event.getInventory();

        if (getPhoneFromInventory(inventory) != null) {
            clearPhoneAtInventory(player);

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