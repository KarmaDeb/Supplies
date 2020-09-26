package ml.karmaconfigs.Supplies.Utils.InventoryMaker.Shop;

import ml.karmaconfigs.Supplies.Utils.Files.Message;
import ml.karmaconfigs.Supplies.Utils.SoundFixer;
import ml.karmaconfigs.Supplies.Utils.StringUtils;
import ml.karmaconfigs.Supplies.Utils.Suministries.Suministry;
import ml.karmaconfigs.Supplies.Utils.User;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

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

public class ShopInventoryEvent implements Listener {

    /**
     * Check if the player GUI is a
     * song selector GUI
     *
     * @return a boolean
     */
    private boolean isShopGUI(String title, Player player) {
        InfiniteInventoryPage inv = InfiniteInventoryPage.getPlayerInventory(player);

        String check;

        if (InfiniteInventoryPage.getPlayerInventory(player) != null) {
            check = Message.shopTitle(inv.getPlayerPage());
        } else {
            check = Message.shopTitle(0);
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < title.length(); i++) {
            String letter = String.valueOf(title.charAt(i));
            if (letter.matches(".*[aA-zZ].*") || letter.matches(".*[0-9].*") || letter.matches(" ")) {
                builder.append(letter);
            }
        }

        title = builder.toString();

        builder = new StringBuilder();
        for (int i = 0; i < check.length(); i++) {
            String letter = String.valueOf(check.charAt(i));
            if (letter.matches(".*[aA-zZ].*") || letter.matches(".*[0-9].*") || letter.matches(" ") && !letter.matches("&" + ".*[aA-zZ]") && !letter.matches("&" + ".*[0-9].*")) {
                builder.append(letter);
            }
        }
        check = builder.toString();

        return title.contains(check);
    }

    /**
     * Check if the item is similar
     * to the other item
     *
     * @param mainItem the item to check for
     * @param targetItem the item to check
     * @return a boolean
     */
    private boolean isSimilar(ItemStack mainItem, ItemStack targetItem) {
        boolean isSimilar = false;
        if (targetItem.hasItemMeta()) {
            if (targetItem.getItemMeta().hasDisplayName()) {
                if (mainItem.hasItemMeta()) {
                    if (mainItem.getItemMeta().hasDisplayName()) {
                        if (StringUtils.toColor(targetItem.getItemMeta().getDisplayName()).equals(StringUtils.toColor(mainItem.getItemMeta().getDisplayName()))) {
                            isSimilar = true;
                        }
                    }
                }
            }
        }
        return isSimilar;
    }

    /**
     * Get the supply from an ItemStack
     * <code>Should be a grenade!</code>
     *
     * @param stack the ItemStack
     * @return a Suministry
     */
    private Suministry getSupply(ItemStack stack) {
        Suministry suministry = null;
        for (Suministry supply : new Suministry().getSuministries()) {
            if (supply.isGrenade(stack)) {
                suministry = supply;
                break;
            }
        }
        return suministry;
    }

    @EventHandler(ignoreCancelled = true)
    public void onClick(InventoryClickEvent e){
        if(e.getWhoClicked() instanceof Player) {
            Player player = (Player) e.getWhoClicked();
            User user = new User(player);

            InventoryView view = e.getView();

            InfiniteInventoryPage inv = InfiniteInventoryPage.getPlayerInventory(player);
            if (inv != null) {

                if (view != null) {
                    if (view.getTitle() != null && !view.getTitle().isEmpty()) {
                        if (isShopGUI(view.getTitle(), player)) {

                            if (e.getCurrentItem() == null) return;
                            if (e.getCurrentItem().getItemMeta() == null) return;
                            if (e.getCurrentItem().getItemMeta().getDisplayName() == null) return;

                            if (isSimilar(e.getCurrentItem(), inv.nextButton())) {
                                if (inv.getPlayerPage() + 1 != inv.getPages()) {
                                    inv.openPage(inv.getPlayerPage() + 1);
                                    SoundFixer.UI_BUTTON_CLICK.playSound(player);
                                }
                            } else {
                                if (isSimilar(e.getCurrentItem(), inv.backButton())) {
                                    if (inv.getPlayerPage() > 0) {
                                        inv.openPage(inv.getPlayerPage() - 1);
                                        SoundFixer.UI_BUTTON_CLICK.playSound(player);
                                    }
                                } else {
                                    if (getSupply(e.getCurrentItem()) != null) {
                                        Suministry supply = getSupply(e.getCurrentItem());

                                        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
                                        Economy eco = rsp.getProvider();

                                        if (eco.has(player, supply.getPrice())) {
                                            if (user.giveGrenade(supply, 1)) {
                                                eco.withdrawPlayer(player, supply.getPrice());
                                                player.closeInventory();
                                                SoundFixer.ENTITY_EXPERIENCE_ORB_PICKUP.playSound(player);
                                                user.send(Message.prefix + Message.bought(supply.getPrice(), supply.getName()));
                                            } else {
                                                user.send(Message.prefix + Message.noSize);
                                                SoundFixer.BLOCK_ANVIL_BREAK.playSound(player);
                                            }
                                        } else {
                                            SoundFixer.ENTITY_VILLAGER_NO.playSound(player);
                                            user.send(Message.prefix + Message.notEnoughMoney(supply.getPrice() - eco.getBalance(player), supply.getName()));
                                        }
                                    }
                                }
                            }
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
