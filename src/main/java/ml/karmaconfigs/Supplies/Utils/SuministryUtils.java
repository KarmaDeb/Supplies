package ml.karmaconfigs.Supplies.Utils;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import ml.karmaconfigs.Supplies.Events.ArmorStandDamage;
import ml.karmaconfigs.Supplies.Events.ChestOpenEvent;
import ml.karmaconfigs.Supplies.Events.ChestSelectEvent;
import ml.karmaconfigs.Supplies.Events.PlayerUseRedstoneTorch;
import ml.karmaconfigs.Supplies.Metrics;
import ml.karmaconfigs.Supplies.Suministry;
import ml.karmaconfigs.Supplies.SuministryCommands;
import ml.karmaconfigs.Supplies.Utils.Files.FileCreator;
import ml.karmaconfigs.Supplies.Utils.InventoryMaker.InventoryEvents;
import ml.karmaconfigs.Supplies.Utils.InventoryMaker.Shop.ShopInventoryEvent;
import ml.karmaconfigs.Supplies.Utils.Suministries.SupplyLoader;
import ml.karmaconfigs.Supplies.Version.StarCheck;
import org.bukkit.ChatColor;

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

public class SuministryUtils implements Suministry {

    /**
     * What the plugin will do
     * on enable
     */
    public void start() {
        out.send(StringUtils.line(15, ChatColor.GOLD));
        out.send(" ");
        out.send("&aEnabling {0} &aversion {1}");
        out.send("&aSetting up files...");
        setupFiles();
        out.send("&aRegistering events...");
        registerEvents();
        out.send("&aRegistering commands...");
        registerCommands();
        out.send(" ");
        if (Suministry.hasHolographic()) {
            out.send("&aHolographicDisplays found, hooking it!");
        } else {
            out.send("&cHolographicDisplays not found, holograms won't be displayed" +
                    " &cover supplies");
        }
        if (Suministry.hasQualityArmor()) {
            out.send("&aQualityArmory found, hooking it!");
        }
        out.send(StringUtils.line(15, ChatColor.GOLD));
        new StarCheck();
        registerMetrics();

        SupplyLoader loader = new SupplyLoader(null);
        loader.restoreSupplies();
    }

    /**
     * What the plugin will do
     * on disable
     */
    public void stop() {
        out.send(StringUtils.line(15, ChatColor.GOLD));
        out.send(" ");
        out.send("&eDisabling {0} &eversion {1}");
        out.send(" ");
        out.send(StringUtils.line(15, ChatColor.GOLD));

        for (Hologram holo : Holograms.getHolograms()) {
            if (!holo.isDeleted()) {
                holo.delete();
            }
        }
    }

    /**
     * Create and generate the files
     */
    private void setupFiles() {
        FileCreator config = new FileCreator("config.yml", true);
        config.createFile();
        config.setDefaults();
        config.saveFile();

        FileCreator messages = new FileCreator("messages.yml", true);
        messages.createFile();
        messages.setDefaults();
        messages.saveFile();
    }

    /**
     * Register the plugin events
     */
    private void registerEvents() {
        plugin.getServer().getPluginManager().registerEvents(new ArmorStandDamage(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerUseRedstoneTorch(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new ChestSelectEvent(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new InventoryEvents(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new ChestOpenEvent(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new ShopInventoryEvent(), plugin);
    }

    /**
     * Register the plugin commands
     */
    private void registerCommands() {
        plugin.getCommand("supply").setExecutor(new SuministryCommands());
    }

    /**
     * Register plugin metrics
     */
    private void registerMetrics() {
        int pluginId = 7456;
        Metrics metrics = new Metrics(plugin, pluginId);
    }
}
