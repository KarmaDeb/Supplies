package ml.karmaconfigs.Supplies.Events;

import ml.karmaconfigs.Supplies.Utils.Files.Config;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

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

public class ArmorStandDamage implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onArmorStandDamaged(EntityDamageEvent e) {
        if (e.getEntity() instanceof ArmorStand) {
            if (e.getEntity().hasMetadata("Suministry")) {
                if (Config.isAllowed(e.getEntity().getWorld())) {
                    if (e.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) ||
                            e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION))
                        e.setCancelled(true);
                }
            }
        }
    }
}
