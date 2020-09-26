package ml.karmaconfigs.Supplies.Utils.InventoryMaker.Shop;

import ml.karmaconfigs.Supplies.Suministry;
import ml.karmaconfigs.Supplies.Utils.Files.Message;
import ml.karmaconfigs.Supplies.Utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Private GSA code
 *
 * The use of this code
 * without GSA team authorization
 * will be a violation of
 * terms of use determined
 * in <a href="https://karmaconfigs.ml/license/"> here </a>
 */
public class InfiniteInventoryPage implements Suministry {

    private final ArrayList<Inventory> pages = new ArrayList<>();
    private static final HashMap<UUID, InfiniteInventoryPage> users = new HashMap<>();
    private static final HashMap<UUID, Integer> playerPage = new HashMap<>();

    private final Player player;

    /**
     * Initialize the infinite inventory page
     *
     * @param user the player
     * @param items the items to add
     */
    public InfiniteInventoryPage(Player user, ArrayList<ItemStack> items) {
        player = user;
        Inventory page = getBlankPage(0);
        for (ItemStack item : items) {
            if (page.firstEmpty() == 46) {
                pages.add(page);
                page = getBlankPage(getPages());
            }
            page.addItem(item);
        }
        pages.add(page);
        users.put(player.getUniqueId(), this);
        playerPage.put(player.getUniqueId(), 0);
    }

    /**
     * Open the player a shop in that
     * page
     *
     * @param page the page
     */
    public void openPage(int page) {
        player.openInventory(pages.get(page));
        playerPage.put(player.getUniqueId(), page);
        users.put(player.getUniqueId(), this);
    }

    /**
     * Get the player infinite inventory page
     *
     * @param player the player
     * @return a InfiniteInventoryPage
     */
    public static InfiniteInventoryPage getPlayerInventory(Player player) {
        return users.get(player.getUniqueId());
    }

    /**
     * Get the player inventory page
     *
     * @return a integer
     */
    public int getPlayerPage() {
        if (player != null) {
            if (playerPage.get(player.getUniqueId()) != null) {
                return playerPage.get(player.getUniqueId());
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    /**
     * Get all the inventory pages
     *
     * @return an integer
     */

    public int getPages() {
        return pages.size();
    }

    /**
     * Creates a new inventory page
     *
     * @return an Inventory
     */
    private Inventory getBlankPage(int page) {
        String title = StringUtils.toColor(Message.shopTitle(page));
        Inventory inv = plugin.getServer().createInventory(null, 54, title);

        inv.setItem(45, backButton());
        inv.setItem(53, nextButton());
        return inv;
    }

    public ItemStack nextButton() {
        ItemStack next = new ItemStack(Material.GREEN_WOOL, 1);
        ItemMeta meta = next.getItemMeta();

        meta.setDisplayName(StringUtils.toColor(Message.nextName));
        meta.setLore(Message.nextLore);

        next.setItemMeta(meta);

        return next;
    }

    public ItemStack backButton() {
        ItemStack back = new ItemStack(Material.RED_WOOL, 1);
        ItemMeta meta = back.getItemMeta();

        meta.setDisplayName(StringUtils.toColor(Message.backName));
        meta.setLore(Message.backLore);

        back.setItemMeta(meta);

        return back;
    }
}
