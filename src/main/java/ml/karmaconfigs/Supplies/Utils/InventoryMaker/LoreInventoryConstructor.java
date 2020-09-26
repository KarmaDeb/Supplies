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

public class LoreInventoryConstructor implements Listener, Suministry {

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
    public LoreInventoryConstructor(Player player) {
        this.player = player;
        this.Title = "&eLore help menu";
    }

    /**
     * Initialize the inventory constructor
     *
     * @param player the player
     * @param Title the inventory title
     */
    public LoreInventoryConstructor(Player player, String Title, String usedCMD) {
        this.Title = StringUtils.toColor(Title);
        this.player = player;

        createInventory(usedCMD);

        cmdData.put(player, usedCMD);
    }

    /**
     * Create an inventory for the player
     */
    private void createInventory(String cmd) {
        this.inventory = plugin.getServer().createInventory(null, 9, Title);

        inventory.setItem(0, getBackButton());
        inventory.setItem(3, getAdd(cmd));
        inventory.setItem(4, getSet(cmd));
        inventory.setItem(5, getGet(cmd));
        inventory.setItem(8, getBackButton());
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

    public ItemStack getBackButton() {
        String crossValue = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjQ3Y2YwZjNiOWVjOWRmMjQ4NWE5Y2Q0Nzk1YjYwYTM5MWM4ZTZlYmFjOTYzNTRkZTA2ZTMzNTdhOWE4ODYwNyJ9fX0=";
        String crossSignature = "D58uwdR/8HeszKRWeA8T8vVZs9TUi0Er7TZ9pKthRuxhRTgzWZ8pIdgY5dneawoqgdJlRtKhb8Qm6YLcUtJ9aPF49E5iiaj38FxPjOgGZzZ+X1NVqCSfglGg5UTJh7fS52enWKpj1yAzoObiMpmeP9Iwip40ednKcA0buRUAd59H61TpdFZrT3rkw1t4tLoMx74GUBqQsdzxVnlkCv2pwtWWX8JtDtOdKVh0Fiyy7R5q7c3DvOftiUOkRx6ORjroBjbY+95lSJQyfa6XJO2bBlV0cvjgxQlBcZse+7j0BgH66Yjgd72sZqnQ2zNgg7w4dkXm1EHCdaQtUmv2qqQlqt/+wfCJyjQIthvcMjGOCdC/Q7MsXDDjdXrLB6hI+MPtsWLwoYOfXiDKeseNNgeDGb5nfBrhtyLABHfNLFxAKDBJqywvArhh+LF3w5M9N6jdcwKrgClWLc7OL+nywWG6FRcchFfpvNtcHNKEBXedHBnEen6s1/mKZQWqFAPAZGlCkETDAhBtsJ2Oel+/fFilpe5POyoraSfR3Wfqoo6/4wHymIObf6t4kZ2XIOFbJbzb6VIplK0THCBB3tThlPcZyaz+X5k7u92awDC9o++d2UHwawSpYL3/QVoUlbhmZ95mQa5pZJYYaEhrLqoKdLwDhrbdLwFw9Sa/3GHBIZnbFJs=";
        ItemStack skull = createSkullTexture(crossValue, crossSignature);
        if (skull != null) {
            ItemMeta meta = skull.getItemMeta();

            meta.setDisplayName(StringUtils.toColor("&cBack"));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eGo back"));

            meta.setLore(lore);

            skull.setItemMeta(meta);

        } else {
            skull = new ItemStack(Material.LEGACY_WOOL, 1, (byte) 3);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();

            meta.setOwner("Chest");

            meta.setDisplayName(StringUtils.toColor("&cBack"));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eGo back"));

            meta.setLore(lore);

            skull.setItemMeta(meta);

        }
        return skull;
    }

    public ItemStack getAdd(String cmd) {
        ItemStack skull = createSkullTexture(InterrogateValue, InterrogateSignature);
        if (skull != null) {
            ItemMeta meta = skull.getItemMeta();

            meta.setDisplayName(StringUtils.toColor("&7/{cmd} lore add <name> <string>".replace("{cmd}", cmd)));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eAdds a line to the supply"));
            lore.add(StringUtils.toColor("&elore"));
            lore.add(" ");
            lore.add(StringUtils.toColor("&dPermission: &fsupply.lore.add"));

            meta.setLore(lore);

            skull.setItemMeta(meta);

        } else {
            skull = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (byte) 3);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();

            meta.setOwner("Cake");

            meta.setDisplayName(StringUtils.toColor("&7/{cmd} lore add <name> <string>".replace("{cmd}", cmd)));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eAdds a line to the supply"));
            lore.add(StringUtils.toColor("&elore"));
            lore.add(" ");
            lore.add(StringUtils.toColor("&dPermission: &fsupply.lore.add"));

            meta.setLore(lore);

            skull.setItemMeta(meta);

        }
        return skull;
    }

    public ItemStack getSet(String cmd) {
        ItemStack skull = createSkullTexture(InterrogateValue, InterrogateSignature);
        if (skull != null) {
            ItemMeta meta = skull.getItemMeta();

            meta.setDisplayName(StringUtils.toColor("&7/{cmd} lore set <name> <line> <string>".replace("{cmd}", cmd)));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eSet the supply lore line"));
            lore.add(StringUtils.toColor("&eto the defined line in <string>"));
            lore.add(" ");
            lore.add(StringUtils.toColor("&dPermission: &fsupply.lore.set"));

            meta.setLore(lore);

            skull.setItemMeta(meta);

        } else {
            skull = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (byte) 3);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();

            meta.setOwner("Cake");

            meta.setDisplayName(StringUtils.toColor("&7/{cmd} lore set <name> <line> <string>".replace("{cmd}", cmd)));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eSet the supply lore line"));
            lore.add(StringUtils.toColor("&eto the defined line in <string>"));
            lore.add(" ");
            lore.add(StringUtils.toColor("&dPermission: &fsupply.lore.set"));

            meta.setLore(lore);

            skull.setItemMeta(meta);

        }
        return skull;
    }

    public ItemStack getGet(String cmd) {
        ItemStack skull = createSkullTexture(InterrogateValue, InterrogateSignature);
        if (skull != null) {
            ItemMeta meta = skull.getItemMeta();

            meta.setDisplayName(StringUtils.toColor("&7/{cmd} lore get <name> <line>".replace("{cmd}", cmd)));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eGet the supply lore line"));
            lore.add(" ");
            lore.add(StringUtils.toColor("&dPermission: &fsupply.lore.get"));

            meta.setLore(lore);

            skull.setItemMeta(meta);

        } else {
            skull = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (byte) 3);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();

            meta.setOwner("Cake");

            meta.setDisplayName(StringUtils.toColor("&7/{cmd} lore get <name> <line>".replace("{cmd}", cmd)));

            List<String> lore = new ArrayList<>();

            lore.add(StringUtils.line(5, ChatColor.BLUE));
            lore.add(" ");
            lore.add(StringUtils.toColor("&eGet the supply lore line"));
            lore.add(" ");
            lore.add(StringUtils.toColor("&dPermission: &fsupply.lore.get"));

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
