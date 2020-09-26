package ml.karmaconfigs.Supplies.Events;

import ml.karmaconfigs.Supplies.API.Events.PlayerSelectSupplyEvent;
import ml.karmaconfigs.Supplies.Suministry;
import ml.karmaconfigs.Supplies.Utils.Files.Message;
import ml.karmaconfigs.Supplies.Utils.SoundFixer;
import ml.karmaconfigs.Supplies.Utils.User;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

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

public class ChestSelectEvent implements Listener, Suministry {

    Permission permission = new Permission("supply.wand", PermissionDefault.OP);

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getBlock() != null) {
            if (e.getBlock().getType().equals(Material.CHEST)) {
                Player player = e.getPlayer();
                if (player.hasPermission(permission)) {
                    User user = new User(player);
                    if (user.hasWandItem()) {
                        Chest chest = (Chest) e.getBlock().getState();

                        PlayerSelectSupplyEvent event = new PlayerSelectSupplyEvent(player, e.getBlock(), chest);

                        plugin.getServer().getPluginManager().callEvent(event);

                        if (!event.isCancelled()) {
                            e.setCancelled(true);
                            SoundFixer.ENTITY_EXPERIENCE_ORB_PICKUP.playSound(player, 0.5F, 0.1F);
                            user.setManagingChest(chest);
                            user.send(Message.prefix + Message.selected(chest.getLocation()));
                        }
                    }
                }
            }
        }
    }
}
