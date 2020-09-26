package ml.karmaconfigs.Supplies.API.Events;

import ml.karmaconfigs.Supplies.Utils.Suministries.Suministry;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
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

public class PlayerSupplyRequest extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private Location location;
    private String name;
    private Suministry suministry;

    private boolean isCancelled;

    /**
     * Initialize the supply call event
     *
     * @param player the player
     * @param location the location
     * @param name the suministry name
     * @param suministry the suministry
     */
    public PlayerSupplyRequest(Player player, Location location, String name, Suministry suministry) {
        this.player = player;
        this.location = location;
        this.name = name;
        this.suministry = suministry;

        this.isCancelled = false;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setLocation(Player target) {
        this.location = target.getLocation();
    }

    public void setLocation(Block block) {
        this.location = block.getLocation();
    }

    public void setLocation(Entity entity) {
        this.location = entity.getLocation();
    }

    public void setLocation(Item item) {
        this.location = item.getLocation();
    }

    public void setSuministry(Suministry suministry) {
        this.suministry = suministry;
        this.name = suministry.getName();
    }

    public Player getPlayer() {
        return player;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public Suministry getSuministry() {
        return suministry;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
