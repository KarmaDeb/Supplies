package ml.karmaconfigs.Supplies.Utils.Files;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import ml.karmaconfigs.Supplies.Suministry;
import ml.karmaconfigs.Supplies.Utils.StringUtils;
import ml.karmaconfigs.Supplies.Utils.User;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

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

public class MessagesGetter implements Suministry {

    private final FileManager manager;

    public MessagesGetter() {
        FileCreator creator = new FileCreator("messages.yml", true);
        creator.createFile();
        creator.setDefaults();
        creator.saveFile();
        this.manager = new FileManager("messages.yml");
    }

    public String getPrefix() {
        return manager.getString("Prefix");
    }

    public String permission(Permission permission) {
        return manager.getString("Permission").replace("{permission}", permission.getName());
    }

    public String giveUsage(String arg) {
        return manager.getString("GiveUsage").replace("{arg}", arg);
    }

    public String wandUsage(String arg) {
        return manager.getString("WandUsage").replace("{arg}", arg);
    }

    public String receivedWand(String arg) {
        return manager.getString("WandReceived").replace("{arg}", arg);
    }

    public String saveUsage(String arg) {
        return manager.getString("SaveUsage").replace("{arg}", arg);
    }

    public String removeUsage(String arg) {
        return manager.getString("RemoveUsage").replace("{arg}", arg);
    }

    public String renameUsage(String arg) {
        return manager.getString("RenameUsage").replace("{arg}", arg);
    }

    public String percentageUsage(String arg, String subarg) {
        return manager.getString("PercentageUsage").replace("{arg}", arg).replace("{subarg}", subarg);
    }

    public String loreUsage(String arg) {
        return manager.getString("LoreUsage").replace("{arg}", arg);
    }

    public String loreSetUsage(String arg) {
        return manager.getString("LoreSetUsage").replace("{arg}", arg);
    }

    public String loreGetUsage(String arg) {
        return manager.getString("LoreGetUsage").replace("{arg}", arg);
    }

    public String loreAddUsage(String arg) {
        return manager.getString("LoreAddUsage").replace("{arg}", arg);
    }

    public String blockUsage(String arg) {
        return manager.getString("BlockUsage").replace("{arg}", arg);
    }

    public String shopUsage(String arg) {
        return manager.getString("ShopUsage").replace("{arg}", arg);
    }

    public String priceUsage(String arg) {
        return manager.getString("PriceUsage").replace("{arg}", arg);
    }

    public String infoUsage(String arg) {
        return manager.getString("InfoUsage").replace("{arg}", arg);
    }

    public String notExists(String name) {
        return manager.getString("NotExists").replace("{name}", name);
    }

    public String noAvailable() {
        return manager.getString("NoAvailable");
    }

    public String noSelection() {
        return manager.getString("NoSelection");
    }

    public String saved(String name) {
        return manager.getString("Saved").replace("{name}", name);
    }
    
    public String errorSaving() {
        return manager.getString("ErrorSaving");
    }

    public String removed(String name) {
        return manager.getString("Removed").replace("{name}", name);
    }

    public String errorRemoving() {
        return manager.getString("ErrorRemoving");
    }

    public String selected(Location location) {
        return manager.getString("Selected")
                .replace("{location}",
                        StringUtils.toColor("&eWorld&7: &f" + location.getWorld().getName() +
                                "&d, &eX&7: " + location.getX() +
                                "&d, &eY&7: " + location.getY() +
                                "&d, &eZ&7: " + location.getZ()))
                .replace("{location_extract_world}", StringUtils.toColor("&eX&7: " + location.getX() +
                                "&d, &eY&7: " + location.getY() +
                                "&d, &eZ&7: " + location.getZ()))
                .replace("{round_location_extract_world}",
                        StringUtils.toColor("&eX&7: " + Math.round(location.getX()) +
                                "&d, &eY&7: " + Math.round(location.getY()) +
                                "&d, &eZ&7: " + Math.round(location.getZ())))
                .replace("{round_location}",
                        StringUtils.toColor("&eWorld&7: " + location.getWorld().getName() +
                                "&d, &eX&7: " + Math.round(location.getX()) +
                                "&d, &eY&7: " + Math.round(location.getY()) +
                                "&d, &eZ&7: " + Math.round(location.getZ())));
    }

    public String received(String name, int amount) {
        return manager.getString("Received").replace("{amount}", String.valueOf(amount)).replace("{name}", StringUtils.toColor(name));
    }

    public String gave(Player target, String name, int amount) {
        return manager.getString("Gave").replace("{amount}", String.valueOf(amount)).replace("{name}", StringUtils.toColor(name)).replace("{player}",
                target.getName());
    }

    public String gaveRandom(int amount, Player target) {
        return manager.getString("GaveRandom").replace("{player}", target.getName()).replace("{amount}", String.valueOf(amount));
    }

    private List<String> invoked(Player player, String name, Location location) {
        List<String> replace = new ArrayList<>();
        for (String str : manager.getList("Invoked")) {
            replace.add(str.replace("{player}", player.getName())
                    .replace("{name}", StringUtils.toColor(name))
                    .replace("{location}", StringUtils.toColor("&eX&7: " + Math.round(location.getX()) +
                            "&d, &eY&7: " + Math.round(location.getY()) +
                            "&d, &eZ&7: " + Math.round(location.getZ()))));
        }

        return replace;
    }

    public String random(int amount) {
        return manager.getString("Random").replace("{amount}", String.valueOf(amount));
    }

    public String maxPer() {
        return manager.getString("MaxPer");
    }

    public String minPer() {
        return manager.getString("MinPer");
    }

    public String percentageSet(String name, double amount) {
        return manager.getString("PercentageSet").replace("{name}", name).replace("{percentage}", String.valueOf(amount))
                .replace("{percentage_no_decimal}", String.valueOf(amount).split("\\.")[0]);
    }

    public String incorrectLoreLine(String name, int amount) {
        return manager.getString("IncorrectLoreLine").replace("{name}", name).replace("{lines}", String.valueOf(amount));
    }

    public String noLore(String name) {
        return manager.getString("NoLore").replace("{name}", name);
    }

    public String getLine(String name, int line, String lore) {
        return manager.getString("GetLine").replace("{name}", name).replace("{line}", String.valueOf(line)).replace("{lore}", lore);
    }

    public String loreSet(String name, int line, String text) {
        return manager.getString("LoreSet").replace("{name}", name).replace("{line}", String.valueOf(line)).replace("{lore}", text);
    }

    public String loreAdd(String name, String text) {
        return manager.getString("LoreAdd").replace("{lore}", text).replace("{name}", name);
    }

    public String loreRemove(String name, int line) {
        return manager.getString("LoreRemove").replace("{name}", name).replace("{line}", String.valueOf(line));
    }

    public String priceSet(String name, double price) {
        return manager.getString("PriceSet").replace("{name}", name).replace("{price}", String.valueOf(price));
    }

    public String hologram(Player player, String name) {
        return manager.getString("Hologram").replace("{player}", player.getName()).replace("{name}", name);
    }

    public String hologram(String player, String name) {
        return manager.getString("Hologram").replace("{player}", player).replace("{name}", name);
    }

    public String noSizeInInventory() {
        return manager.getString("NoSizeInInventory");
    }

    public String noSizeOther(Player player) {
        return manager.getString("NoSizeOther").replace("{player}", player.getName());
    }

    public String noVault() {
        return manager.getString("NoVault");
    }

    public String notEnoughMoney(double amount, String name) {
        return manager.getString("NotEnoughMoney").replace("{money}", String.valueOf(amount)).replace("{name}", name);
    }

    public String bought(double price, String name) {
        return manager.getString("Bought").replace("{money}", String.valueOf(price)).replace("{name}", name);
    }

    public String blockedWorld(World world) {
        return manager.getString("BlockedWorld").replace("{world}", world.getName());
    }

    public String blockedRegion(World world, ProtectedRegion region) {
        return manager.getString("BlockedRegion").replace("{region}", StringUtils.capitalize(region.getId())).replace("{world}", world.getName());
    }

    public String unblocked(World world) {
        return manager.getString("Unblocked").replace("{world}", world.getName());
    }

    public String unblockedRegion(World world, ProtectedRegion region) {
        return manager.getString("UnblockedRegion").replace("{region}", StringUtils.capitalize(region.getId())).replace("{world}", world.getName());
    }

    public String notExists() {
        return manager.getString("NotExist");
    }

    public List<String> openingHolo(Player player, int itemsLeft) {
        List<String> replaced = new ArrayList<>();

        for (String str : manager.getList("OpeningHolo")) {
            if (!replaced.contains(str)) {
                replaced.add(StringUtils.toColor(str
                        .replace("{player}", player.getName())
                        .replace("{left}", String.valueOf(itemsLeft)) + "&r"));
            }
        }

        return replaced;
    }

    public void invokedMessage(Player who, Player player, String name, Location location) {
        final Permission teleportPermission = new Permission("supply.invoked.teleport", PermissionDefault.OP);
        for (String str : invoked(player, name, location)) {
            if (!str.contains("[CLICK_TO_SEE_LOCATION]")) {
                new User(who).send(str);
            } else {
                TextComponent preComponent = new TextComponent(ChatColor.translateAlternateColorCodes('&', str.replace("[CLICK_TO_SEE_LOCATION]", "")));
                TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', clickToSeeLocation()));

                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', "&eX&7: " + Math.round(location.getX()) +
                        "&d, &eY&7: " + Math.round(location.getY()) +
                        "&d, &eZ&7: " + Math.round(location.getZ())))));
                if (who.hasPermission(teleportPermission)) {
                    component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp " + location.getX() + " " + location.getY() + " " + location.getZ()));
                }

                preComponent.addExtra(component);

                new User(who).send(preComponent);
            }
        }
    }

    @SuppressWarnings("all")
    public String info(ml.karmaconfigs.Supplies.Utils.Suministries.Suministry supply) {
        List<String> replaced = new ArrayList<>();

        ItemStack[] items = supply.getContents();

        List<ItemStack> stacks = new ArrayList<>();

        ItemStack barrier = new ItemStack(Material.BARRIER, 1);
        ItemMeta barrierMeta = barrier.getItemMeta();

        barrierMeta.setDisplayName(StringUtils.toColor("&cREMOVE"));

        barrier.setItemMeta(barrierMeta);


        for (ItemStack stack : items) {
            if (stack != null) {
                if (!stack.getType().equals(Material.AIR)) {
                    if (!stacks.contains(stack)) {
                        if (!stack.isSimilar(barrier)) {
                            stacks.add(stack);
                        }
                    }
                }
            }
        }

        for (String str : manager.getList("Info")) {
            if (!replaced.contains(str)) {
                if (!supply.getLoreManager().getLore().isEmpty()) {
                    replaced.add(StringUtils.toColor(str
                            .replace("[", "{open}")
                            .replace("]", "{close}")
                            .replace(",", "{comma}")
                            .replace("{supply}", supply.getFileName(false))
                            .replace("{name}", "&f" + supply.getName())
                            .replace("{items}", String.valueOf(stacks.size()))
                            .replace("{lines}", String.valueOf(supply.getLoreManager().getLore().size()))
                            .replace("{lore}", "&7- " + supply.getLoreManager().getLore().toString()
                                    .replace("[", "&r").replace("]", "&r")
                                    .replace(",", StringUtils.toColor("&r\n &7-&r"))
                                    .replace("{space}", "\n"))) + "&r\n&r");
                } else {
                    replaced.add(StringUtils.toColor(str
                            .replace("[", "{open}")
                            .replace("]", "{close}")
                            .replace(",", "{comma}")
                            .replace("{supply}", supply.getFileName(false))
                            .replace("{name}", "&f" + supply.getName())
                            .replace("{items}", String.valueOf(stacks.size()))
                            .replace("{lines}", String.valueOf(supply.getLoreManager().getLore().size()))
                            .replace("{lore}", "&7- &cNo lore")) + "&r\n");
                }
            }
        }

        return replaced.toString()
                .replace("[", "")
                .replace("]", "")
                .replace(",", "")
                .replace("{open}", "[")
                .replace("{close}", "]")
                .replace("{comma}", ",");
    }

    private String clickToSeeLocation() {
        return manager.getString("ClickToSeeLocation");
    }

    public String notInGround() {
        return manager.getString("NotInGround");
    }

    public String renamed(String name, String newName) {
        return manager.getString("Renamed").replace("{supply}", name).replace("{name}", newName);
    }

    public String renameError(String name) {
        return manager.getString("RenameError").replace("{supply}", name);
    }

    public String unableToLaunch() {
        return manager.getString("UnableToLaunch");
    }

    public String notAllowedWorld() {
        return manager.getString("NotAllowedWorld");
    }

    public String notAllowedRegion() {
        return manager.getString("NotAllowedRegion");
    }


    public String offline(String name) {
        return manager.getString("Offline").replace("{player}", name);
    }

    public String shopDisabled() {
        return manager.getString("Shop.Disabled");
    }

    public String shopTitle(int page) {
        return manager.getString("Shop.Title").replace("{page}", String.valueOf(page));
    }

    public String shopNext() {
        return manager.getString("Shop.Next");
    }

    public List<String> shopNextLore() {
        return StringUtils.toColor(manager.getList("Shop.NextLore"));
    }

    public String shopBack() {
        return manager.getString("Shop.Back");
    }

    public List<String> shopBackLore() {
        return StringUtils.toColor(manager.getList("Shop.BackLore"));
    }
}
