package ml.karmaconfigs.Supplies.Utils.Files;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

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

public interface Config {

    int updateInterval = new ConfigGetter().updateInterval();

    boolean playAnimation = new ConfigGetter().playAnimation();

    Material wandItem = new ConfigGetter().wandItem();

    String wandItemName = new ConfigGetter().wandItemName();

    boolean dropIfNoSize = new ConfigGetter().dropIfNoSize();

    boolean destroyUnderBlock = new ConfigGetter().destroyUnderBlock();

    boolean shopEnabled = new ConfigGetter().enableShop();

    static boolean isAllowed(World world) {
        return new ConfigGetter().isAllowed(world);
    }

    static boolean isAllowed(World world, ProtectedRegion region) {
        return new ConfigGetter().isAllowed(world, region);
    }

    static void blockWorld(World world) {
        new ConfigGetter().blockWorld(world);
    }

    static void blockRegion(World world, ProtectedRegion region) {
        new ConfigGetter().blockRegion(world, region);
    }

    static void unblockWorld(World world) {
        new ConfigGetter().unblockWorld(world);
    }

    static void unblockRegion(World world, ProtectedRegion region) {
        new ConfigGetter().unblockRegion(world, region);
    }
}
