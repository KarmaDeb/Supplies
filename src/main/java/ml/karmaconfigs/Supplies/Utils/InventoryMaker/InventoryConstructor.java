package ml.karmaconfigs.Supplies.Utils.InventoryMaker;

import com.mojang.authlib.GameProfile;
import ml.karmaconfigs.Supplies.Suministry;
import ml.karmaconfigs.Supplies.Utils.InventoryMaker.Utils.ReflectionUtil;
import ml.karmaconfigs.Supplies.Utils.InventoryMaker.Utils.SkinManager;
import ml.karmaconfigs.Supplies.Utils.Server.WarningLevel;
import ml.karmaconfigs.Supplies.Utils.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/*
GNU LESSER GENERAL PUBLIC LICENSE
                       Version 2.1, February 1999
 Copyright (C) 1991, 1999 Free Software Foundation, Inc.
 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
[This is the first released version of the Lesser GPL.  It also counts
 as the successor of the GNU Library Public License, version 2, hence
 the version number 2.1.]
 */

public class InventoryConstructor implements Listener, Suministry {

    private final static HashMap<Player, String> cmdData = new HashMap<>();

    private Inventory inventory;
    private final String Title;
    private final Player player;

    private final String InterrogateValue = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2Q5MWY1MTI2NmVkZGM2MjA3ZjEyYWU4ZDdhNDljNWRiMDQxNWFkYTA0ZGFiOTJiYjc2ODZhZmRiMTdmNGQ0ZSJ9fX0=";
    private final String InterrogateSignature = "Njl3aVxpQImBT/U/GUBhVf4Uzq8ynzGZ5yq1+R++GZv8kcSrTbAC1QWn++NssCLa72JubJ/V444W6b4MKQqo65pn3nZuoKQ3nuZjvNWewc8jtKnCPtBTTgSPjquDEWkMElLqJ/PjqanFuwHkC9PnN9xBQAwZzOtRmnh69gd+4V3WY6JfMJqyuY8S5r1VUaIp3uf5jH2AzPv6zIgVmIAjOr6EWXL0NRKGaNxPxHSLddycEMt4HJ87pP0du90ZM4n3HGHPjVwK+vyEFJnaPwBg+T2pTfPL9/2DoUDtWf48eei+ILRZ+/cgPdTfxzuPciiRfjr/PXLm5u6sY7KlCd80xuLuj8TMJZpmCF6OE07pgg8nXpyirNostcs5qQK5QY/z4oNrIyFr1saQePg34gVtsHD7HulkzHiQ9zzNk7izVLwoijJtos5bfaD7wls42kQVwKUM6XJ58wr9/M2uMVTJa33sHPslYViAEDyRAl2/P7IIDmx72HfNf0tEQyO3ypb0uRC8+cy+LAgYz2uRYDABS6I6yJXA0bh78EXlbgb5vnfJWIPPCIP3H/gNbjdTvFRE3IBNI0n6m7Z50n/7zQyeq7Xsl/ekt5Oehy3GAwr/52308pwLFmLoEfIkhOpUTqV7GYTzkYzUI+K1SiLZIT8tZJFDWTalrvc3zIAvJyDkkxY=";

    /**
     * Initialize the inventory constructor
     * for some minor utils
     *
     * @param player the player
     */
    public InventoryConstructor(Player player) {
        this.player = player;
        this.Title = "&eHelp menu";
    }

    /**
     * Initialize the inventory constructor
     *
     * @param player the player
     * @param Title the inventory title
     */
    public InventoryConstructor(Player player, String Title, String usedCMD) {
        this.Title = StringUtils.toColor(Title);
        this.player = player;

        createInventory(usedCMD);

        cmdData.put(player, usedCMD);
    }

    /**
     * Create an inventory for the player
     */
    private void createInventory(String cmd) {
        this.inventory = plugin.getServer().createInventory(null, 45, Title);

        inventory.setItem(0, getCloseButton());
        inventory.setItem(8, getCloseButton());
        inventory.setItem(11, getHelp(cmd));
        inventory.setItem(13, getWand(cmd));
        inventory.setItem(15, getList(cmd));
        inventory.setItem(19, getSave(cmd));
        inventory.setItem(21, getRemove(cmd));
        inventory.setItem(23, getRename(cmd));
        inventory.setItem(25, getGive(cmd));
        inventory.setItem(29, getInfo(cmd));
        inventory.setItem(31, getBlock(cmd));
        inventory.setItem(33, getPercentage(cmd));
        inventory.setItem(37, getPrice(cmd));
        inventory.setItem(40, getLore(cmd));
        inventory.setItem(43, getShop(cmd));
    }

    /**
     * Show the player the inventory
     */
    public void show() {
        if (inventory != null) {
            player.openInventory(inventory);
        } else {
            InventoryConstructor constructor = new InventoryConstructor(player, "&eHelp menu", getUsedCmd());
            constructor.show();
        }
    }

    /**
     * Update the player inventory
     */
    @Deprecated
    public void update() {
        close();
        show();
    }

    /**
     * Close the player inventory if
     * he has it open
     */
    public void close() {
        player.closeInventory();
    }

    public ItemStack getCloseButton() {
        String crossValue = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjQ3Y2YwZjNiOWVjOWRmMjQ4NWE5Y2Q0Nzk1YjYwYTM5MWM4ZTZlYmFjOTYzNTRkZTA2ZTMzNTdhOWE4ODYwNyJ9fX0=";
        String crossSignature = "D58uwdR/8HeszKRWeA8T8vVZs9TUi0Er7TZ9pKthRuxhRTgzWZ8pIdgY5dneawoqgdJlRtKhb8Qm6YLcUtJ9aPF49E5iiaj38FxPjOgGZzZ+X1NVqCSfglGg5UTJh7fS52enWKpj1yAzoObiMpmeP9Iwip40ednKcA0buRUAd59H61TpdFZrT3rkw1t4tLoMx74GUBqQsdzxVnlkCv2pwtWWX8JtDtOdKVh0Fiyy7R5q7c3DvOftiUOkRx6ORjroBjbY+95lSJQyfa6XJO2bBlV0cvjgxQlBcZse+7j0BgH66Yjgd72sZqnQ2zNgg7w4dkXm1EHCdaQtUmv2qqQlqt/+wfCJyjQIthvcMjGOCdC/Q7MsXDDjdXrLB6hI+MPtsWLwoYOfXiDKeseNNgeDGb5nfBrhtyLABHfNLFxAKDBJqywvArhh+LF3w5M9N6jdcwKrgClWLc7OL+nywWG6FRcchFfpvNtcHNKEBXedHBnEen6s1/mKZQWqFAPAZGlCkETDAhBtsJ2Oel+/fFilpe5POyoraSfR3Wfqoo6/4wHymIObf6t4kZ2XIOFbJbzb6VIplK0THCBB3tThlPcZyaz+X5k7u92awDC9o++d2UHwawSpYL3/QVoUlbhmZ95mQa5pZJYYaEhrLqoKdLwDhrbdLwFw9Sa/3GHBIZnbFJs=";
        ItemStack skull = createSkullTexture(crossValue, crossSignature);
        if (skull != null) {
            ItemMeta meta = skull.getItemMeta();

            meta.setDisplayName(StringUtils.toColor("&cClose help menu"));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eCloses this help"));
            lore.add(StringUtils.toColor("&emenu"));

            meta.setLore(lore);

            skull.setItemMeta(meta);

        } else {
            skull = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (byte) 3);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();

            meta.setOwner("Chest");

            meta.setDisplayName(StringUtils.toColor("&cClose help menu"));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eCloses this help"));
            lore.add(StringUtils.toColor("&emenu"));

            meta.setLore(lore);

            skull.setItemMeta(meta);

        }
        return skull;
    }

    public ItemStack getHelp(String cmd) {
        ItemStack skull = createSkullTexture(InterrogateValue, InterrogateSignature);
        if (skull != null) {
            ItemMeta meta = skull.getItemMeta();

            meta.setDisplayName(StringUtils.toColor("&7/{cmd} help".replace("{cmd}", cmd)));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eOpens this help"));
            lore.add(StringUtils.toColor("&emenu"));
            lore.add(" ");
            lore.add(StringUtils.toColor("&dPermission: &fsupply.help"));

            meta.setLore(lore);

            skull.setItemMeta(meta);

        } else {
            skull = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (byte) 3);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();

            meta.setOwner("Cake");

            meta.setDisplayName(StringUtils.toColor("&7/{cmd} help".replace("{cmd}", cmd)));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eOpens this help"));
            lore.add(StringUtils.toColor("&emenu"));
            lore.add(" ");
            lore.add(StringUtils.toColor("&dPermission: &fsupply.help"));

            meta.setLore(lore);

            skull.setItemMeta(meta);

        }
        return skull;
    }

    public ItemStack getWand(String cmd) {
        ItemStack skull = createSkullTexture(InterrogateValue, InterrogateSignature);
        if (skull != null) {
            ItemMeta meta = skull.getItemMeta();

            meta.setDisplayName(StringUtils.toColor("&7/{cmd} wand".replace("{cmd}", cmd)));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eGives you the"));
            lore.add(StringUtils.toColor("&esuministry selector"));
            lore.add(StringUtils.toColor("&ewand"));
            lore.add(" ");
            lore.add(StringUtils.toColor("&dPermission: &fsupply.wand"));

            meta.setLore(lore);

            skull.setItemMeta(meta);

        } else {
            skull = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (byte) 3);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();

            meta.setOwner("Cake");

            meta.setDisplayName(StringUtils.toColor("&7/{cmd} wand".replace("{cmd}", cmd)));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eGives you the"));
            lore.add(StringUtils.toColor("&esuministry selector"));
            lore.add(StringUtils.toColor("&ewand"));
            lore.add(" ");
            lore.add(StringUtils.toColor("&dPermission: &fsupply.wand"));

            meta.setLore(lore);

            skull.setItemMeta(meta);

        }
        return skull;
    }

    public ItemStack getSave(String cmd) {
        ItemStack skull = createSkullTexture(InterrogateValue, InterrogateSignature);
        if (skull != null) {
            ItemMeta meta = skull.getItemMeta();

            meta.setDisplayName(StringUtils.toColor("&7/{cmd} save <name>".replace("{cmd}", cmd)));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eCreates a suministry"));
            lore.add(StringUtils.toColor("&ewith the last chest contents"));
            lore.add(StringUtils.toColor("&eyou selected"));
            lore.add(" ");
            lore.add(StringUtils.toColor("&dPermission: &fsupply.save"));

            meta.setLore(lore);

            skull.setItemMeta(meta);

        } else {
            skull = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (byte) 3);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();

            meta.setOwner("Cake");

            meta.setDisplayName(StringUtils.toColor("&7/{cmd} save <name>".replace("{cmd}", cmd)));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eCreates a suministry"));
            lore.add(StringUtils.toColor("&ewith the last chest contents"));
            lore.add(StringUtils.toColor("&eyou selected"));
            lore.add(" ");
            lore.add(StringUtils.toColor("&dPermission: &fsupply.save"));

            meta.setLore(lore);

            skull.setItemMeta(meta);

        }
        return skull;
    }

    public ItemStack getRemove(String cmd) {
        ItemStack skull = createSkullTexture(InterrogateValue, InterrogateSignature);
        if (skull != null) {
            ItemMeta meta = skull.getItemMeta();

            meta.setDisplayName(StringUtils.toColor("&7/{cmd} remove <name>".replace("{cmd}", cmd)));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eRemoves the specified"));
            lore.add(StringUtils.toColor("&esuministry"));
            lore.add(" ");
            lore.add(StringUtils.toColor("&dPermission: &fsupply.remove"));

            meta.setLore(lore);

            skull.setItemMeta(meta);

        } else {
            skull = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (byte) 3);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();

            meta.setOwner("Cake");

            meta.setDisplayName(StringUtils.toColor("&7/{cmd} remove <name>".replace("{cmd}", cmd)));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eRemoves the specified"));
            lore.add(StringUtils.toColor("&esuministry"));
            lore.add(" ");
            lore.add(StringUtils.toColor("&dPermission: &fsupply.remove"));

            meta.setLore(lore);

            skull.setItemMeta(meta);

        }
        return skull;
    }

    public ItemStack getGive(String cmd) {
        ItemStack skull = createSkullTexture(InterrogateValue, InterrogateSignature);
        if (skull != null) {
            ItemMeta meta = skull.getItemMeta();

            meta.setDisplayName(StringUtils.toColor("&7/{cmd} give [<name>|<random>] <amount>] [player]".replace("{cmd}", cmd)));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eGives you a suministry"));
            lore.add(StringUtils.toColor("&egrenade, or give the"));
            lore.add(StringUtils.toColor("&especified player a"));
            lore.add(StringUtils.toColor("&egrenade"));
            lore.add(" ");
            lore.add(StringUtils.toColor("&dPermission: &fsupply.give"));

            meta.setLore(lore);

            skull.setItemMeta(meta);

        } else {
            skull = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (byte) 3);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();

            meta.setOwner("Cake");

            meta.setDisplayName(StringUtils.toColor("&7/{cmd} give [<name>|<random>] <amount>] [player]".replace("{cmd}", cmd)));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eGives you a suministry"));
            lore.add(StringUtils.toColor("&egrenade, or give the"));
            lore.add(StringUtils.toColor("&especified player a"));
            lore.add(StringUtils.toColor("&egrenade"));
            lore.add(" ");
            lore.add(StringUtils.toColor("&dPermission: &fsupply.give"));

            meta.setLore(lore);

            skull.setItemMeta(meta);

        }
        return skull;
    }

    public ItemStack getPrice(String cmd) {
        ItemStack skull = createSkullTexture(InterrogateValue, InterrogateSignature);
        if (skull != null) {
            ItemMeta meta = skull.getItemMeta();

            meta.setDisplayName(StringUtils.toColor("&7/{cmd} price <name> <double>".replace("{cmd}", cmd)));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eSets the supply price"));
            lore.add(StringUtils.toColor("&ein shop"));
            lore.add(" ");
            lore.add(StringUtils.toColor("&dPermission: &fsupply.price"));

            meta.setLore(lore);

            skull.setItemMeta(meta);

        } else {
            skull = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (byte) 3);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();

            meta.setOwner("Cake");

            meta.setDisplayName(StringUtils.toColor("&7/{cmd} price <name> <double>".replace("{cmd}", cmd)));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eSets the supply price"));
            lore.add(StringUtils.toColor("&ein shop"));
            lore.add(" ");
            lore.add(StringUtils.toColor("&dPermission: &fsupply.price"));

            meta.setLore(lore);

            skull.setItemMeta(meta);

        }
        return skull;
    }

    public ItemStack getRename(String cmd) {
        ItemStack skull = createSkullTexture(InterrogateValue, InterrogateSignature);
        if (skull != null) {
            ItemMeta meta = skull.getItemMeta();

            meta.setDisplayName(StringUtils.toColor("&7/{cmd} rename <name> <new name>".replace("{cmd}", cmd)));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eRenames the suministry"));
            lore.add(StringUtils.toColor("&eto the new name"));
            lore.add(" ");
            lore.add(StringUtils.toColor("&dPermission: &fsupply.rename"));

            meta.setLore(lore);

            skull.setItemMeta(meta);

        } else {
            skull = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (byte) 3);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();

            meta.setOwner("Cake");

            meta.setDisplayName(StringUtils.toColor("&7/{cmd} rename <name> <new name>".replace("{cmd}", cmd)));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eRenames the suministry"));
            lore.add(StringUtils.toColor("&eto the new name"));
            lore.add(" ");
            lore.add(StringUtils.toColor("&dPermission: &fsupply.rename"));

            meta.setLore(lore);

            skull.setItemMeta(meta);

        }
        return skull;
    }

    public ItemStack getList(String cmd) {
        ItemStack skull = createSkullTexture(InterrogateValue, InterrogateSignature);
        if (skull != null) {
            ItemMeta meta = skull.getItemMeta();

            meta.setDisplayName(StringUtils.toColor("&7/{cmd} list".replace("{cmd}", cmd)));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eList the available"));
            lore.add(StringUtils.toColor("&esuministries"));
            lore.add(" ");
            lore.add(StringUtils.toColor("&dPermission: &fsupply.list"));

            meta.setLore(lore);

            skull.setItemMeta(meta);

        } else {
            skull = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (byte) 3);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();

            meta.setOwner("Cake");

            meta.setDisplayName(StringUtils.toColor("&7/{cmd} list".replace("{cmd}", cmd)));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eList the available"));
            lore.add(StringUtils.toColor("&esuministries"));
            lore.add(" ");
            lore.add(StringUtils.toColor("&dPermission: &fsupply.list"));

            meta.setLore(lore);

            skull.setItemMeta(meta);

        }
        return skull;
    }

    public ItemStack getInfo(String cmd) {
        ItemStack skull = createSkullTexture(InterrogateValue, InterrogateSignature);
        if (skull != null) {
            ItemMeta meta = skull.getItemMeta();

            meta.setDisplayName(StringUtils.toColor("&7/{cmd} info <name>".replace("{cmd}", cmd)));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eShows you the supply"));
            lore.add(StringUtils.toColor("&einformation"));
            lore.add(" ");
            lore.add(StringUtils.toColor("&dPermission: &fsupply.info"));

            meta.setLore(lore);

            skull.setItemMeta(meta);

        } else {
            skull = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (byte) 3);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();

            meta.setOwner("Cake");

            meta.setDisplayName(StringUtils.toColor("&7/{cmd} info <name>".replace("{cmd}", cmd)));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eShows you the supply"));
            lore.add(StringUtils.toColor("&einformation"));
            lore.add(" ");
            lore.add(StringUtils.toColor("&dPermission: &fsupply.info"));

            meta.setLore(lore);

            skull.setItemMeta(meta);

        }
        return skull;
    }

    public ItemStack getLore(String cmd) {
        ItemStack stack = new ItemStack(Material.CHEST, 1);

        ItemMeta meta = stack.getItemMeta();

        meta.setDisplayName(StringUtils.toColor("&7/{cmd} lore [<add>|<set>|<get>] <name> [<string>|<line>]".replace("{cmd}", cmd)));

        List<String> lore = new ArrayList<>();

        lore.add(StringUtils.line(5, ChatColor.BLUE));
        lore.add(" ");
        lore.add(StringUtils.toColor("&eManage the supply lore"));
        lore.add(StringUtils.toColor("&c&lCLICK FOR MORE INFO"));
        lore.add(" ");
        lore.add(StringUtils.toColor("&dPermission: &fsupply.lore"));

        meta.setLore(lore);

        stack.setItemMeta(meta);

        return stack;
    }

    public ItemStack getPercentage(String cmd) {
        ItemStack skull = createSkullTexture(InterrogateValue, InterrogateSignature);
        if (skull != null) {
            ItemMeta meta = skull.getItemMeta();

            meta.setDisplayName(StringUtils.toColor("&7/{cmd} %|per|percentage <name> <percentage[1.0-100.0]>".replace("{cmd}", cmd)));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eSet the supply items"));
            lore.add(StringUtils.toColor("&epercentage amount"));
            lore.add(" ");
            lore.add(StringUtils.toColor("&dPermission: &fsupply.percentage"));

            meta.setLore(lore);

            skull.setItemMeta(meta);

        } else {
            skull = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (byte) 3);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();

            meta.setOwner("Cake");

            meta.setDisplayName(StringUtils.toColor("&7/{cmd} %|per|percentage <name> <percentage[1.0-100.0]>".replace("{cmd}", cmd)));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eSet the supply items"));
            lore.add(StringUtils.toColor("&epercentage amount"));
            lore.add(" ");
            lore.add(StringUtils.toColor("&dPermission: &fsupply.percentage"));

            meta.setLore(lore);

            skull.setItemMeta(meta);

        }
        return skull;
    }

    public ItemStack getShop(String cmd) {
        ItemStack skull = createSkullTexture(InterrogateValue, InterrogateSignature);
        if (skull != null) {
            ItemMeta meta = skull.getItemMeta();

            meta.setDisplayName(StringUtils.toColor("&7/{cmd} shop".replace("{cmd}", cmd)));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eOpens the supply shop,"));
            lore.add(StringUtils.toColor("&ebut needs Vault to work."));
            lore.add(StringUtils.toColor("&ePermission is given to user"));
            lore.add(StringUtils.toColor("&eby default."));
            if (Suministry.hasVault()) {
                lore.add(" ");
                lore.add(StringUtils.toColor("&aClick to open the shop!"));
            }
            lore.add(" ");
            lore.add(StringUtils.toColor("&dPermission: &fsupply.shop"));

            meta.setLore(lore);

            skull.setItemMeta(meta);

        } else {
            skull = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (byte) 3);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();

            meta.setOwner("Cake");

            meta.setDisplayName(StringUtils.toColor("&7/{cmd} shop".replace("{cmd}", cmd)));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eOpens the supply shop,"));
            lore.add(StringUtils.toColor("&ebut needs Vault to work."));
            lore.add(StringUtils.toColor("&ePermission is given to user"));
            lore.add(StringUtils.toColor("&eby default."));
            if (Suministry.hasVault()) {
                lore.add(" ");
                lore.add(StringUtils.toColor("&aClick to open the shop!"));
            }
            lore.add(" ");
            lore.add(StringUtils.toColor("&dPermission: &fsupply.shop"));

            meta.setLore(lore);

            skull.setItemMeta(meta);

        }
        return skull;
    }

    public ItemStack getBlock(String cmd) {
        ItemStack skull = createSkullTexture(InterrogateValue, InterrogateSignature);
        if (skull != null) {
            ItemMeta meta = skull.getItemMeta();

            meta.setDisplayName(StringUtils.toColor("&7/{cmd} block <world> (Optional) <region>".replace("{cmd}", cmd)));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eBlocks the world or"));
            lore.add(StringUtils.toColor("&ethe region from supplies"));
            lore.add(" ");
            lore.add(StringUtils.toColor("&dPermission: &fsupply.block"));

            meta.setLore(lore);

            skull.setItemMeta(meta);

        } else {
            skull = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (byte) 3);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();

            meta.setOwner("Cake");

            meta.setDisplayName(StringUtils.toColor("&7/{cmd} block <world> (Optional) <region>".replace("{cmd}", cmd)));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eBlocks the world or"));
            lore.add(StringUtils.toColor("&ethe region from supplies"));
            lore.add(" ");
            lore.add(StringUtils.toColor("&dPermission: &fsupply.block"));

            meta.setLore(lore);

            skull.setItemMeta(meta);

        }
        return skull;
    }

    /**
     * Get the used cmd of the player
     *
     * @return a String
     */
    public String getUsedCmd() {
        return cmdData.getOrDefault(player, "sm");
    }

    private ItemStack createSkullTexture(String value, String signature) {
        Object properties = new SkinManager().createProperty("textures", value, signature);
        try {
            ItemStack head = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short)3);

            SkullMeta headMeta = (SkullMeta) head.getItemMeta();
            GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            Object propmap = ReflectionUtil.invokeMethod(profile.getClass(), profile, "getProperties");
            ReflectionUtil.invokeMethod(propmap, "clear");
            ReflectionUtil.invokeMethod(propmap.getClass(), propmap, "put", new Class[]{Object.class, Object.class}, "textures", properties);

            Field profileField;
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
            head.setItemMeta(headMeta);
            return head;
        } catch (Exception e) {
            out.send("An error occurred while trying to set inventory skull", WarningLevel.ERROR);
            out.send("&c" + e.fillInStackTrace());
        }
        return null;
    }
}
