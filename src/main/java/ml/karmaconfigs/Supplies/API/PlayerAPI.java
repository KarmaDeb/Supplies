package ml.karmaconfigs.Supplies.API;

import ml.karmaconfigs.Supplies.Suministry;
import ml.karmaconfigs.Supplies.Utils.User;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

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

public class PlayerAPI implements Suministry {

    private final Player player;

    /**
     * Initialize the player API
     *
     * @param player the player
     */
    public PlayerAPI(Player player) {
        this.player = player;
    }

    /**
     * Try to give the player a suministry
     *
     * @param name the suministry name
     * @return a boolean
     */
    public boolean giveSuministry(String name, int amount) {
        ml.karmaconfigs.Supplies.Utils.Suministries.Suministry suministry = new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry(name);

        if (suministry.exists()) {
            User user = new User(player);

            return user.giveGrenade(suministry, amount);
        } else {
            return false;
        }
    }

    /**
     * Check if the player is using the selection wand
     *
     * @return a boolean
     */
    public boolean hasSelectionWand() {
        User user = new User(player);

        return user.hasWandItem();
    }

    /**
     * Check if the player is managing a chest
     *
     * @return a boolean
     */
    public boolean isManagingChest() {
        User user = new User(player);

        return user.isManagingChest();
    }

    /**
     * Get the player's managing chest
     *
     * @return a Chest
     */
    public Chest getManagingChest() {
        User user = new User(player);

        return user.getManagingChest();
    }

    /**
     * Get the player user
     *
     * @return an User
     */
    public User getUser() {
        return new User(player);
    }

    /**
     * Give the player the selection wand
     */
    public boolean giveSelectionWand() {
        User user = new User(player);

        return user.giveWand();
    }

    /**
     * Set the player's chest
     *
     * @param chest the chest
     */
    public void setManagingChest(Chest chest) {
        User user = new User(player);

        user.setManagingChest(chest);
    }
}
