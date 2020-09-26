package ml.karmaconfigs.Supplies.API.Events;

import ml.karmaconfigs.Supplies.API.MenuType;
import org.bukkit.entity.Player;
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

public class MenuOpenEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final boolean isSilent;
    private final MenuType type;

    /**
     * Initialize the player close help menu event
     *
     * @param player the player
     * @param isSilent if the event is silent or not
     */
    public MenuOpenEvent(Player player, MenuType type, boolean isSilent) {
        this.player = player;
        this.isSilent = isSilent;
        this.type = type;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isSilent() {
        return isSilent;
    }

    public MenuType getType() {
        return type;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
