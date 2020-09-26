package ml.karmaconfigs.Supplies.Utils.Suministries;

import ml.karmaconfigs.Supplies.Utils.Animations.AnimationMaker;
import ml.karmaconfigs.Supplies.Utils.Files.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Private GSA code
 *
 * The use of this code
 * without GSA team authorization
 * will be a violation of
 * terms of use determined
 * in <a href="https://karmaconfigs.ml/license/"> here </a>
 */
public class SupplyLoader {

    private final Location location;

    private final FileManager manager;

    public SupplyLoader(Location location) {
        this.manager = new FileManager("data.yml", "supplyData");

        this.location = location;
    }

    public void removeSupply(Location location) {
        List<String> sections = new ArrayList<>();

        for (String str : manager.getFile().getConfigurationSection("Supply").getKeys(false)) {
            sections.add("Supply." + str);
        }

        for (String section : sections) {
            World w = Bukkit.getServer().getWorld(manager.getString(section + ".World"));
            double x = manager.getDouble(section + ".X");
            double y = manager.getDouble(section + ".Y");
            double z = manager.getDouble(section + ".Z");

            Location loc = new Location(w, x, y, z);

            if (loc.distance(location) < 2D) {
                manager.unset(section);
            }
        }
    }

    public void saveLoc(Suministry supply, Player player) {
        manager.set("Supply." + supply.getFileName(false) + "_" + player.getName() + ".World", location.getWorld().getName());
        manager.set("Supply." + supply.getFileName(false) + "_" + player.getName() + ".X", location.getX());
        manager.set("Supply." + supply.getFileName(false) + "_" + player.getName() + ".Y", location.getY());
        manager.set("Supply." + supply.getFileName(false) + "_" + player.getName() + ".Z", location.getZ());
    }

    public void saveLoc(Suministry supply, String player) {
        manager.set("Supply." + supply.getFileName(false) + "_" + player + ".World", location.getWorld().getName());
        manager.set("Supply." + supply.getFileName(false) + "_" + player + ".X", location.getX());
        manager.set("Supply." + supply.getFileName(false) + "_" + player + ".Y", location.getY());
        manager.set("Supply." + supply.getFileName(false) + "_" + player + ".Z", location.getZ());
    }

    public void restoreSupplies() {
        if (manager.isSet("Supply")) {
            for (String str : manager.getFile().getConfigurationSection("Supply").getKeys(false)) {
                Suministry supply = new Suministry(str.split("_")[0]);
                String caller = str.split("_")[1];

                World world = Bukkit.getServer().getWorld(manager.getString("Supply." + str + ".World"));
                double X = manager.getDouble("Supply." + str + ".X");
                double Y = manager.getDouble("Supply." + str + ".Y");
                double Z = manager.getDouble("Supply." + str + ".Z");

                Location location = new Location(world, X, Y, Z);

                location.getBlock().setType(Material.AIR);
                AnimationMaker maker = new AnimationMaker(null);
                maker.startAnimationFor(location, caller, supply.getFileName(false));
            }
        }
    }
}
