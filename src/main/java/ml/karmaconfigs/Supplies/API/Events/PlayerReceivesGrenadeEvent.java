package ml.karmaconfigs.Supplies.API.Events;

import ml.karmaconfigs.Supplies.Utils.StringUtils;
import ml.karmaconfigs.Supplies.Utils.Suministries.Suministry;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

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

public class PlayerReceivesGrenadeEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private Suministry suministry;
    private final String name;

    private boolean isCancelled;

    /**
     * Initialize the player receive grenade event
     *
     * @param player the player
     * @param name the player name
     */
    public PlayerReceivesGrenadeEvent(Player player, String name) {
        this.isCancelled = false;
        this.player = player;
        this.name = name;
        for (Suministry supply : new Suministry().getSuministries()) {
            if (StringUtils.toColor(supply.getName()).equals(StringUtils.toColor(name))) {
                this.suministry = supply;
                break;
            }
        }
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    public Player getPlayer() {
        return player;
    }

    public String getName() {
        return name;
    }

    public Suministry getSuministry() {
        return suministry;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
