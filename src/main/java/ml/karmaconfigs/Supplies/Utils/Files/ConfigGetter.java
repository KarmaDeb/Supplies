package ml.karmaconfigs.Supplies.Utils.Files;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import ml.karmaconfigs.Supplies.Suministry;
import ml.karmaconfigs.Supplies.Utils.Server.Console;
import ml.karmaconfigs.Supplies.Utils.Server.WarningLevel;
import ml.karmaconfigs.Supplies.Utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

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

public class ConfigGetter implements Suministry {

    private final FileManager manager;

    public ConfigGetter() {
        FileCreator creator = new FileCreator("config.yml", true);
        creator.createFile();
        creator.setDefaults();
        creator.saveFile();
        this.manager = new FileManager("config.yml");
    }

    public int updateInterval() {
        if (manager.getInt("UpdateInterval") >= 1 && manager.getInt("UpdateInterval") <= 1440) {
            return manager.getInt("UpdateInterval");
        } else {
            Console.out.send("The minimum update interval is 1 minute, and the max is 1440 (1 day), but you got " + manager.getInt("UpdateInterval"), WarningLevel.ERROR);
            manager.set("UpdateInterval", 5);
            return 5;
        }
    }

    public boolean playAnimation() {
        return manager.getBoolean("PlayAnimation");
    }

    public Material wandItem() {
        List<Material> materials = new ArrayList<>();

        for (Material material : Material.values()) {
            if (!materials.contains(material)) {
                materials.add(material);
            }
        }

        Material material = Material.valueOf(manager.getString("WandItem"));

        if (materials.contains(material)) {
            return material;
        } else {
            out.send("Your wand item material is not valid, by default the STICK will be used", WarningLevel.ERROR);
            return Material.STICK;
        }
    }

    public String wandItemName() {
        return manager.getString("WandItemName");
    }

    public boolean dropIfNoSize() {
        return manager.getBoolean("DropIfNoSize");
    }

    public boolean destroyUnderBlock() {
        return manager.getBoolean("DestroyUnderBlock");
    }

    public boolean enableShop() {
        return manager.getBoolean("Shop");
    }

    private List<String> blockedWorlds() {
        return manager.getList("DisabledWorlds");
    }

    public List<String> blockedRegions(World world) {
        List<String> regions = new ArrayList<>();
        List<String> blocked = manager.getList("DisabledRegions");
        for (String str : blocked) {
            if (str.split(";")[0] != null && !str.split(";")[0].isEmpty()) {
                if (str.split(";")[1] != null && !str.split(";")[1].isEmpty()) {
                    String name = str.split(";")[0];
                    if (world.getName().equals(name)) {
                        String region = str.replace(name + ";", "");
                        if (!regions.contains(region)) {
                            regions.add(region);
                        }
                    }
                }
            }
        }
        return regions;
    }

    public boolean isAllowed(World world) {
        String worldName = world.getName();
        if (manager.isSet("DisabledWorlds")) {
            return !manager.getList("DisabledWorlds").contains(worldName);
        } else {
            return true;
        }
    }

    public boolean isAllowed(World world, ProtectedRegion region) {
        if (manager.isSet("DisabledRegions")) {
            return !blockedRegions(world).contains(StringUtils.toColor(StringUtils.capitalize(region.getId())));
        } else {
            return true;
        }
    }

    public void blockWorld(World world) {
        List<String> disabledWorlds = blockedWorlds();
        if (!disabledWorlds.contains(world.getName())) {
            disabledWorlds.add(world.getName());
        }
        manager.set("DisabledWorlds", disabledWorlds);
    }

    public void blockRegion(World world, ProtectedRegion region) {
        List<String> blocked = manager.getList("DisabledRegions");
        if (!blocked.contains(world.getName() + ";" + StringUtils.capitalize(region.getId()))) {
            blocked.add(world.getName() + ";" + StringUtils.capitalize(region.getId()));
            manager.set("DisabledRegions", blocked);
        }
    }

    public void unblockWorld(World world) {
        List<String> disabledWorlds = blockedWorlds();
        if (!disabledWorlds.isEmpty()) {
            disabledWorlds.remove(world.getName());
            manager.set("DisabledWorlds", disabledWorlds);
        }
    }

    public void unblockRegion(World world, ProtectedRegion region) {
        List<String> blocked = manager.getList("DisabledRegions");
        if (blocked.contains(world.getName() + ";" + StringUtils.capitalize(region.getId()))) {
            blocked.remove(world.getName() + ";" + StringUtils.capitalize(region.getId()));
            manager.set("DisabledRegions", blocked);
        }
    }
}
