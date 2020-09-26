package ml.karmaconfigs.Supplies.Utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import ml.karmaconfigs.Supplies.Suministry;
import ml.karmaconfigs.Supplies.Utils.Files.Config;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

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

public class User implements Suministry {

    private final Player player;

    private final static HashMap<Player, Chest> managingChest = new HashMap<>();

    /**
     * Initialize the user utils
     *
     * @param player the player
     */
    public User(Player player) {
        this.player = player;
    }

    /**
     * Send a message to the player
     *
     * @param message the message
     */
    public void send(String message) {
        player.sendMessage(StringUtils.toColor(message));
    }

    /**
     * Send a message to the player
     *
     * @param message the message
     */
    public void send(TextComponent message) {
        player.spigot().sendMessage(message);
    }

    /**
     * Give the player a
     * suministry grenade
     */
    public boolean giveGrenade(ml.karmaconfigs.Supplies.Utils.Suministries.Suministry supply, int amount) {
        ItemStack grenade = supply.getGrenade(1);

        List<ItemStack> grenades = new ArrayList<>();

        int size = 0;
        for (int i = 0; i < amount; i++) {
            if (i != amount - 1) {
                if (size == 64) {
                    grenade.setAmount(64);
                    grenades.add(grenade);
                    size = 0;
                }
                size++;
            } else {
                grenade.setAmount(size + 1);
                grenades.add(grenade);
            }
        }

        if (getFreeSlots() >= amount) {
            for (ItemStack stack : grenades) {
                player.getInventory().setItem(getFirstFreeSlot(), stack);
            }
            return true;
        } else {
            if (Config.dropIfNoSize) {
                for (ItemStack stack : grenades) {
                    player.getWorld().dropItem(player.getLocation().add(0D, 1D, 0D), stack);
                }
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Give the player a
     * suministry wand
     */
    public boolean giveWand() {
        ItemStack wand = new ItemStack(Config.wandItem, 1);
        ItemMeta wandMeta = wand.getItemMeta();

        wandMeta.setDisplayName(StringUtils.toColor(Config.wandItemName));

        wand.setItemMeta(wandMeta);

        if (getFreeSlots() >= 1) {
            player.getInventory().setItem(getFirstFreeSlot(), wand);
            return true;
        } else {
            if (Config.dropIfNoSize) {
                player.getLocation().getWorld().dropItemNaturally(player.getLocation().add(0D, 2D, 0D), wand);
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Set the managing chest of the
     * player
     *
     * @param chest the chest
     */
    public void setManagingChest(Chest chest) {
        managingChest.put(player, chest);
    }

    /**
     * Check if the player is managing any chest
     *
     * @return a boolean
     */
    public boolean isManagingChest() {
        return managingChest.get(player) != null;
    }

    /**
     * Check if the player is using
     * the wand item
     *
     * @return a boolean
     */
    public boolean hasWandItem() {
        ItemStack wand = new ItemStack(Config.wandItem, 1);
        ItemMeta wandMeta = wand.getItemMeta();

        wandMeta.setDisplayName(StringUtils.toColor(Config.wandItemName));

        wand.setItemMeta(wandMeta);

        if (player.getInventory().getItem(player.getInventory().getHeldItemSlot()) != null) {
            return player.getInventory().getItem(player.getInventory().getHeldItemSlot()).isSimilar(wand);
        } else {
            return false;
        }
    }

    /**
     * Check if the player is in a region
     *
     * @return a boolean
     */
    public boolean isInRegion() {
        try {
            RegionContainer container = WorldGuardPlugin.inst().getRegionContainer();
            RegionManager manager = container.get(player.getWorld());

            if (manager != null) {
                Collection<ProtectedRegion> regions = manager.getApplicableRegions(player.getLocation()).getRegions();

                return !regions.isEmpty();
            }
            return false;
        } catch (Error e) {
            BukkitPlayer bPlayer = BukkitAdapter.adapt(player);
            Location loc = bPlayer.getLocation();

            com.sk89q.worldguard.protection.regions.RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();
            ApplicableRegionSet set = query.getApplicableRegions(loc);
            Set<ProtectedRegion> regions = set.getRegions();

            return !regions.isEmpty();
        }
    }

    /**
     * Get the actual player region
     *
     * @return a ProtectedRegion
     */
    public ProtectedRegion getRegion() {
        try {
            RegionContainer container = WorldGuardPlugin.inst().getRegionContainer();
            RegionManager manager = container.get(player.getWorld());
            assert manager != null;

            Collection<ProtectedRegion> regions = manager.getApplicableRegions(player.getLocation()).getRegions();

            ProtectedRegion region = null;

            for (ProtectedRegion reg : regions) {
                if (reg != null) {
                    region = reg;
                    break;
                }
            }
            return region;
        } catch (Error e) {
            Location loc = BukkitAdapter.adapt(player.getLocation());

            com.sk89q.worldguard.protection.regions.RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();
            ApplicableRegionSet set = query.getApplicableRegions(loc);
            Set<ProtectedRegion> regions = set.getRegions();

            ProtectedRegion region = null;

            for (ProtectedRegion reg : regions) {
                if (reg != null) {
                    region = reg;
                    break;
                }
            }
            return region;
        }
    }

    /**
     * Get the player inventory free slots
     *
     * @return an integer
     */
    public int getFreeSlots() {
        try {
            int freeSlots = 0;
            for (ItemStack stack : player.getInventory().getContents()) {
                if (stack == null || stack.getType().equals(Material.AIR)) {
                    freeSlots++;
                }
            }
            return freeSlots * 64;
        } catch (NoSuchMethodError e) {
            int freeSlots = 0;
            for (ItemStack stack : player.getInventory().getContents()) {
                if (stack == null || stack.getType().equals(Material.AIR)) {
                    freeSlots++;
                }
            }
            return freeSlots * 64;
        }
    }

    /**
     * Get the player first free slot
     *
     * @return an integer
     */
    public int getFirstFreeSlot() {
        try {
            int freeSlot = 0;
            for (int i = 0; i < player.getInventory().getContents().length; i++) {
                ItemStack item = player.getInventory().getItem(i);
                if (item == null || item.getType().equals(Material.AIR)) {
                    freeSlot = i;
                    break;
                }
            }
            return freeSlot;
        } catch (NoSuchMethodError e) {
            int freeSlot = 0;
            for (int i = 0; i < player.getInventory().getContents().length; i++) {
                ItemStack item = player.getInventory().getItem(i);
                if (item == null || item.getType().equals(Material.AIR)) {
                    freeSlot = i;
                    break;
                }
            }
            return freeSlot;
        }
    }

    /**
     * Get the player's chest
     *
     * @return a Chest
     */
    public Chest getManagingChest() {
        return managingChest.get(player);
    }
}
