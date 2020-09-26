package ml.karmaconfigs.Supplies;

import ml.karmaconfigs.Supplies.Utils.Server.Console;
import ml.karmaconfigs.Supplies.Utils.StringUtils;
import org.bukkit.plugin.java.JavaPlugin;

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

public interface Suministry {

    Main plugin = (Main) JavaPlugin.getProvidingPlugin(Main.class);
    String name = StringUtils.toColor("&7&l[ &cGSA &7&l] &6Supplies");
    String version = StringUtils.toColor("&c" + plugin.getDescription().getVersion());
    Console out = new Console();

    static boolean hasWorldGuard() {
        return plugin.getServer().getPluginManager().isPluginEnabled("WorldGuard");
    }

    static boolean hasHolographic() {
        return plugin.getServer().getPluginManager().isPluginEnabled("HolographicDisplays");
    }

    static boolean hasQualityArmor() {
        return plugin.getServer().getPluginManager().isPluginEnabled("QualityArmory");
    }

    static boolean hasVault() {
        return plugin.getServer().getPluginManager().isPluginEnabled("Vault");
    }


}
