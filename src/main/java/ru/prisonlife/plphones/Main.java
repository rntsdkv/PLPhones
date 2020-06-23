package ru.prisonlife.plphones;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitTask;
import ru.prisonlife.item.PrisonItem;
import ru.prisonlife.item.PrisonItemBuilder;
import ru.prisonlife.plphones.commands.*;
import ru.prisonlife.plphones.events.PayGUIClick;
import ru.prisonlife.plphones.events.PayGUIClose;
import ru.prisonlife.plphones.events.PhoneCraft;
import ru.prisonlife.plugin.PLPlugin;
import ru.prisonlife.util.Pair;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends PLPlugin {

    public String getPluginName() {
        return "PLPhones";
    }

    public List<Pair<String, Object>> initPluginFiles() {
        return new ArrayList<>();
    }

    public static Map<Player, Player> SIMsellers = new HashMap<>();
    public static Map<Player, Integer> SIMprices = new HashMap<>();

    public static Map<Location, Player> hindrancesPlayers = new HashMap<>();
    public static Map<Location, Integer> hindrancesRadius = new HashMap<>();
    public static Map<Location, Integer> hindrancesSeconds = new HashMap<>();

    public static BukkitTask task;

    @Override
    public void onCreate() {
        copyConfigFile();
        ItemStack itemPhone = new PrisonItemBuilder().setPrisonItem(PrisonItem.PHONE).build();
        ShapedRecipe phoneRecipe = setCraft(itemPhone);
        Bukkit.addRecipe(phoneRecipe);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        registerCommands();
        registerListeners();
    }

    private void registerCommands() {
        new CommandSMS(this);
        new CommandAccept(this);
        new CommandPhonePay(this);
        new CommandSellSIM(this);
        new CommandSuperSIM(this);
        new CommandPhone(this);
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PhoneCraft(this), this);
        pluginManager.registerEvents(new PayGUIClick(), this);
        pluginManager.registerEvents(new PayGUIClose(), this);
    }

    private void copyConfigFile() {
        File config = new File(getDataFolder() + File.separator + "config.yml");
        if (!config.exists()) {
            getLogger().info("PLPhones | Default Config copying...");
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }
    }

    private ShapedRecipe setCraft(ItemStack item) {
        ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey(this, "plphones.phone"), item);
        shapedRecipe.shape(" A ", " BD", " C ");
        /**
         * На время тестов в качестве:
         *
         * антенны - глина,
         * корпуса - бедрок,
         * аккумулятора - наковальня,
         * симки - яйцо(не яйцо спавна, просто яичко)
         */
        // ItemStack itemAntenna = PrisonItemFactory.createItem(null, PrisonItem.PHONE_ANTENNA);
        // ItemStack itemBody = PrisonItemFactory.createItem(null, PrisonItem.PHONE_BODY);
        // ItemStack itemBattery = PrisonItemFactory.createItem(null, PrisonItem.PHONE_BATTERY);
        // ItemStack itemSIM = PrisonItemFactory.createItem(null, PrisonItem.PHONE_SIM_CARD);
        shapedRecipe.setIngredient('A', Material.CLAY_BALL);
        shapedRecipe.setIngredient('B', Material.BEDROCK);
        shapedRecipe.setIngredient('C', Material.ANVIL);
        shapedRecipe.setIngredient('D', Material.EGG);
        return shapedRecipe;
    }

    public static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

}
