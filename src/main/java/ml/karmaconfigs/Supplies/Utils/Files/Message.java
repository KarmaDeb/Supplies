package ml.karmaconfigs.Supplies.Utils.Files;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import ml.karmaconfigs.Supplies.Utils.Suministries.Suministry;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

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

public interface Message {

    String prefix = new MessagesGetter().getPrefix();

    static String permission(Permission permission) {
        return new MessagesGetter().permission(permission);
    }

    static String giveUsage(String arg) {
        return new MessagesGetter().giveUsage(arg);
    }

    static String wandUsage(String arg) {
        return new MessagesGetter().wandUsage(arg);
    }

    static String receivedWand(String arg) {
        return new MessagesGetter().receivedWand(arg);
    }

    static String saveUsage(String arg) {
        return new MessagesGetter().saveUsage(arg);
    }

    static String removeUsage(String arg) {
        return new MessagesGetter().removeUsage(arg);
    }

    static String renameUsage(String arg) {
        return new MessagesGetter().renameUsage(arg);
    }

    static String percentageUsage(String arg, String subarg) {
        return new MessagesGetter().percentageUsage(arg, subarg);
    }

    static String loreUsage(String arg) {
        return new MessagesGetter().loreUsage(arg);
    }

    static String loreSetUsage(String arg) {
        return new MessagesGetter().loreSetUsage(arg);
    }

    static String loreGetUsage(String arg) {
        return new MessagesGetter().loreGetUsage(arg);
    }

    static String loreAddUsage(String arg) {
        return new MessagesGetter().loreAddUsage(arg);
    }

    static String shopUsage(String arg) {
        return new MessagesGetter().shopUsage(arg);
    }

    static String priceUsage(String arg) {
        return new MessagesGetter().priceUsage(arg);
    }

    static String infoUsage(String arg) {
        return new MessagesGetter().infoUsage(arg);
    }

    static String blockUsage(String arg) { return new MessagesGetter().blockUsage(arg); }

    static String notExists(String name) {
        return new MessagesGetter().notExists(name);
    }

    String noAvailable = new MessagesGetter().noAvailable();

    String noSelection = new MessagesGetter().noSelection();

    static String saved(String name) {
        return new MessagesGetter().saved(name);
    }

    String errorSaving = new MessagesGetter().errorSaving();

    static String removed(String name) {
        return new MessagesGetter().removed(name);
    }

    String errorRemoving = new MessagesGetter().errorRemoving();

    static String selected(Location location) {
        return new MessagesGetter().selected(location);
    }

    static String received(String name, int amount) {
        return new MessagesGetter().received(name, amount);
    }

    static String gave(Player target, String name, int amount) {
        return new MessagesGetter().gave(target, name, amount);
    }

    static String gaveRandom(int amount, Player target) {
        return new MessagesGetter().gaveRandom(amount, target);
    }

    static String random(int amount) {
        return new MessagesGetter().random(amount);
    }

    String maxPer = new MessagesGetter().maxPer();

    String minPer = new MessagesGetter().minPer();

    static String percentageSet(String name, double amount) {
        return new MessagesGetter().percentageSet(name, amount);
    }

    static String incorrectLoreLine(String name, int amount) {
        return new MessagesGetter().incorrectLoreLine(name, amount);
    }

    static String noLore(String name) {
        return new MessagesGetter().noLore(name);
    }

    static String getLine(String name, int line, String lore) {
        return new MessagesGetter().getLine(name, line, lore);
    }

    static String loreSet(String name, int line, String lore) {
        return new MessagesGetter().loreSet(name, line, lore);
    }

    static String loreAdd(String name, String lore) {
        return new MessagesGetter().loreAdd(name, lore);
    }

    static String loreRemove(String name, int line) {
        return new MessagesGetter().loreRemove(name, line);
    }

    static String priceSet(String name, double price) {
        return new MessagesGetter().priceSet(name, price);
    }

    static String hologram(Player player, String name) {
        return new MessagesGetter().hologram(player, name);
    }

    static String hologram(String player, String name) {
        return new MessagesGetter().hologram(player, name);
    }

    String noSize = new MessagesGetter().noSizeInInventory();

    static String noSizeOther(Player player) {
        return new MessagesGetter().noSizeOther(player);
    }

    String noVault = new MessagesGetter().noVault();

    static String notEnoughMoney(double amount, String name) {
        return new MessagesGetter().notEnoughMoney(amount, name);
    }

    static String bought(double price, String name) {
        return new MessagesGetter().bought(price, name);
    }

    static String blockedWorld(World world) {
        return new MessagesGetter().blockedWorld(world);
    }

    static String blockedRegion(World world, ProtectedRegion region) {
        return new MessagesGetter().blockedRegion(world, region);
    }

    static String unblocked(World world) {
        return new MessagesGetter().unblocked(world);
    }

    static String unblockedRegion(World world, ProtectedRegion region) {
        return new MessagesGetter().unblockedRegion(world, region);
    }

    String notExists = new MessagesGetter().notExists();

    static List<String> openingHolo(Player player, int itemsLeft) {
        return new MessagesGetter().openingHolo(player, itemsLeft);
    }

    static void invoked(Player who, Player player, String name, Location location) {
        new MessagesGetter().invokedMessage(who, player, name, location);
    }

    static String info(Suministry supply) {
        return new MessagesGetter().info(supply);
    }

    String notInGround = new MessagesGetter().notInGround();

    static String renamed(String oldName, String newName) {
        return new MessagesGetter().renamed(oldName, newName);
    }

    static String renameError(String name) {
        return new MessagesGetter().renameError(name);
    }

    String unableToLaunch = new MessagesGetter().unableToLaunch();

    String notAllowedWorld = new MessagesGetter().notAllowedWorld();

    String notAllowedRegion = new MessagesGetter().notAllowedRegion();

    static String offline(String target) {
        return new MessagesGetter().offline(target);
    }

    String shopDisabled = new MessagesGetter().shopDisabled();

    static String shopTitle(int page) {
        return new MessagesGetter().shopTitle(page);
    }

    String nextName = new MessagesGetter().shopNext();

    List<String> nextLore = new MessagesGetter().shopNextLore();

    String backName = new MessagesGetter().shopBack();

    List<String> backLore = new MessagesGetter().shopBackLore();
}
