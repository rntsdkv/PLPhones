package ru.prisonlife.plphones;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import ru.prisonlife.plugin.PLPlugin;
import ru.prisonlife.util.Pair;

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
        String itemName = ChatColor.GRAY + "Телефон";
        List<String> itemLore = new ArrayList<String>();
        itemLore.add(ChatColor.AQUA + "" + ChatColor.BOLD + "Новый телефончик, хоть и собранный из ошмётков, сделает тебя авторитетом среди заключенных!");
        ItemStack itemPhone = newCraft(Material.DIAMOND_AXE, itemName, itemLore);
        ShapedRecipe phoneRecipe = setCraft(itemPhone);
        Bukkit.addRecipe(phoneRecipe);
    }

    private ItemStack newCraft(Material id, String name, List<String> lore) {
        ItemStack itemPhone = new ItemStack(id);
        ItemMeta metaPhone = itemPhone.getItemMeta();
        metaPhone.setDisplayName(name);
        metaPhone.setLore(lore);
        itemPhone.setItemMeta(metaPhone);
        return itemPhone;
    }

    private ShapedRecipe setCraft(ItemStack item) {
        ShapedRecipe shapedRecipe = new ShapedRecipe(item);
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


}
