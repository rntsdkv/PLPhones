package ru.prisonlife.plphones;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import ru.prisonlife.PrisonLife;
import ru.prisonlife.Prisoner;
import ru.prisonlife.item.PrisonItem;
import ru.prisonlife.item.PrisonItemFactory;
import ru.prisonlife.plphones.commands.*;
import ru.prisonlife.plugin.PLPlugin;
import ru.prisonlife.util.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main extends PLPlugin {

    public String getPluginName() {
        return null;
    }

    public List<Pair<String, Object>> initPluginFiles() {
        return null;
    }

    public void onCreate() {
        copyConfigFile();
        ItemStack itemPhone = PrisonItemFactory.createItem(null, PrisonItem.PHONE);
        ItemStack itemAntenne = PrisonItemFactory.createItem(null, PrisonItem.);
        ShapedRecipe phoneRecipe = setCraft(itemPhone);
        Bukkit.addRecipe(phoneRecipe);
        new PhoneCraft(this);
        new CommandSMS(this);
        new CommandPhone(this);
        new CommandSuperSIM(this);
        new CommandSellSIM(this);
        new CommandPhonePay(this);
    }

    private void copyConfigFile() {
        File config = new File(getDataFolder() + File.separator + "config.yml");
        if (!config.exists()) {
            getLogger().info("PLPhones | Default Config copying...");
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }
    }

    private ShapedRecipe setCraft(ItemStack item, ItemStack antenne, ItemStack korpus, ItemStack battaree, ItemStack SIM) {
        NamespacedKey key = new NamespacedKey(plugin, "sword_of_zeus");
        ShapedRecipe shapedRecipe = new ShapedRecipe(key, item);
        shapedRecipe.shape(new String[] {"ABC", " D "});
        /**
         * На время тестов в качестве:
         *
         * антенны - глина,
         * корпуса - бедрок,
         * аккумулятора - наковальня,
         * симки - яйцо(не яйцо спавна, просто яичко)
         */
        shapedRecipe.setIngredient('A', Material.CLAY);
        shapedRecipe.setIngredient('B', Material.BEDROCK);
        shapedRecipe.setIngredient('C', Material.ANVIL);
        shapedRecipe.setIngredient('D', Material.EGG);
        return shapedRecipe;
    }

    public static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

}
