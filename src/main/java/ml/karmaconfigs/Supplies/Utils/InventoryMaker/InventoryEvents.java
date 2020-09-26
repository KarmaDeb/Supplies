package ml.karmaconfigs.Supplies.Utils.InventoryMaker;

import ml.karmaconfigs.Supplies.API.Events.MenuCloseEvent;
import ml.karmaconfigs.Supplies.API.Events.MenuOpenEvent;
import ml.karmaconfigs.Supplies.API.MenuType;
import ml.karmaconfigs.Supplies.Suministry;
import ml.karmaconfigs.Supplies.Utils.InventoryMaker.Shop.InfiniteInventoryPage;
import ml.karmaconfigs.Supplies.Utils.SoundFixer;
import ml.karmaconfigs.Supplies.Utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

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

public class InventoryEvents implements Listener, Suministry {

    private boolean isSimilar(ItemStack mainItem, List<ItemStack> items) {
        boolean isSimilar = false;
        for (ItemStack item : items) {
            if (item.hasItemMeta()) {
                if (item.getItemMeta().hasDisplayName()) {
                    if (mainItem.hasItemMeta()) {
                        if (mainItem.getItemMeta().hasDisplayName()) {
                            if (StringUtils.toColor(item.getItemMeta().getDisplayName()).equals(StringUtils.toColor(mainItem.getItemMeta().getDisplayName()))) {
                                isSimilar = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return isSimilar;
    }

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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMenuClose(MenuCloseEvent e) {
        if (!e.isSilent()) {
            Player player = e.getPlayer();

            if (e.getType().equals(MenuType.HELP)) {
                SoundFixer.ENTITY_GHAST_DEATH.playSound(player, 0.5F, 1.5F);
            } else {
                if (e.getType().equals(MenuType.LORE)) {
                    SoundFixer.BLOCK_CHEST_CLOSE.playSound(player, 0.5F, 0.5F);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMenuOpen(MenuOpenEvent e) {
        if (!e.isSilent()) {
            Player player = e.getPlayer();

            if (e.getType().equals(MenuType.HELP)) {
                SoundFixer.ENTITY_VILLAGER_YES.playSound(player, 0.5F, 1.0F);
            } else {
                if (e.getType().equals(MenuType.LORE)) {
                    SoundFixer.BLOCK_CHEST_OPEN.playSound(player, 0.5F, 0.5F);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent e) {
        InventoryView view = e.getView();
        if (view != null) {
            if (view.getTitle() != null) {
                if (!view.getTitle().isEmpty()) {
                    if (StringUtils.toColor(view.getTitle()).equals(StringUtils.toColor("&eHelp menu"))) {
                        Player player = (Player) e.getWhoClicked();

                        InventoryConstructor constructor = new InventoryConstructor(player);

                        if (e.getCurrentItem() != null) {
                            if (e.getCurrentItem().getType() != null) {
                                if (!e.getCurrentItem().getType().equals(Material.AIR)) {
                                    ItemStack close = constructor.getCloseButton();
                                    ItemStack help = constructor.getHelp(constructor.getUsedCmd());
                                    ItemStack wand = constructor.getWand(constructor.getUsedCmd());
                                    ItemStack list = constructor.getList(constructor.getUsedCmd());
                                    ItemStack save = constructor.getSave(constructor.getUsedCmd());
                                    ItemStack remove = constructor.getRemove(constructor.getUsedCmd());
                                    ItemStack rename = constructor.getRename(constructor.getUsedCmd());
                                    ItemStack give = constructor.getGive(constructor.getUsedCmd());
                                    ItemStack price = constructor.getPrice(constructor.getUsedCmd());
                                    ItemStack info = constructor.getInfo(constructor.getUsedCmd());
                                    ItemStack lore = constructor.getLore(constructor.getUsedCmd());
                                    ItemStack percentage = constructor.getPercentage(constructor.getUsedCmd());
                                    ItemStack shop = constructor.getShop(constructor.getUsedCmd());
                                    ItemStack block = constructor.getBlock(constructor.getUsedCmd());

                                    List<ItemStack> items = new ArrayList<>();
                                    items.add(help);
                                    items.add(wand);
                                    items.add(list);
                                    items.add(save);
                                    items.add(remove);
                                    items.add(rename);
                                    items.add(give);
                                    items.add(price);
                                    items.add(info);
                                    items.add(percentage);
                                    items.add(block);

                                    ItemStack current = e.getCurrentItem();

                                    if (isSimilar(current, close)) {
                                        constructor.close();
                                        plugin.getServer().getPluginManager().callEvent(new MenuCloseEvent(player, MenuType.HELP, false));
                                    } else {
                                        if (isSimilar(current, items)) {
                                            SoundFixer.ENTITY_VILLAGER_NO.playSound(player, 0.5F, 2.0F);
                                        } else {
                                            if (isSimilar(current, lore)) {
                                                LoreInventoryConstructor loreConstructor = new LoreInventoryConstructor(player, "&eLore help menu", constructor.getUsedCmd());

                                                plugin.getServer().getPluginManager().callEvent(new MenuCloseEvent(player, MenuType.HELP, true));
                                                plugin.getServer().getPluginManager().callEvent(new MenuOpenEvent(player, MenuType.LORE, false));

                                                constructor.close();

                                                loreConstructor.show();
                                            } else {
                                                if (isSimilar(current, shop)) {
                                                    plugin.getServer().getPluginManager().callEvent(new MenuCloseEvent(player, MenuType.HELP, false));

                                                    constructor.close();

                                                    List<ml.karmaconfigs.Supplies.Utils.Suministries.Suministry> supplies = new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry().getSuministries();

                                                    ArrayList<ItemStack> grenades = new ArrayList<>();
                                                    for (ml.karmaconfigs.Supplies.Utils.Suministries.Suministry supply : supplies) {
                                                        List<String> supplyLore = new ArrayList<>(supply.getLoreManager().getLore());

                                                        supplyLore.add(" ");
                                                        supplyLore.add(StringUtils.toColor("&ePrice: &b" + supply.getPrice()));

                                                        ItemStack grenade = supply.getGrenade(1);
                                                        ItemMeta meta = grenade.getItemMeta();
                                                        meta.setLore(supplyLore);

                                                        grenade.setItemMeta(meta);

                                                        grenades.add(grenade);
                                                    }

                                                    InfiniteInventoryPage page = new InfiniteInventoryPage(player, grenades);
                                                    page.openPage(0);
                                                } else {
                                                    SoundFixer.ENTITY_VILLAGER_NO.playSound(player, 0.5F, 0.1F);
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    SoundFixer.ENTITY_VILLAGER_NO.playSound(player, 0.5F, 0.1F);
                                }
                            } else {
                                SoundFixer.ENTITY_VILLAGER_NO.playSound(player, 0.5F, 0.1F);
                            }
                        } else {
                            SoundFixer.ENTITY_VILLAGER_NO.playSound(player, 0.5F, 0.1F);
                        }
                        e.setCancelled(true);
                    } else {
                        if (StringUtils.toColor(view.getTitle()).equals(StringUtils.toColor("&eLore help menu"))) {
                            Player player = (Player) e.getWhoClicked();

                            LoreInventoryConstructor loreConstructor = new LoreInventoryConstructor(player);

                            if (e.getCurrentItem() != null) {
                                if (e.getCurrentItem().getType() != null) {
                                    if (!e.getCurrentItem().getType().equals(Material.AIR)) {
                                        ItemStack back = loreConstructor.getBackButton();
                                        ItemStack add = loreConstructor.getAdd(loreConstructor.getUsedCmd());
                                        ItemStack set = loreConstructor.getSet(loreConstructor.getUsedCmd());
                                        ItemStack get = loreConstructor.getGet(loreConstructor.getUsedCmd());

                                        List<ItemStack> items = new ArrayList<>();
                                        items.add(add);
                                        items.add(set);
                                        items.add(get);

                                        ItemStack current = e.getCurrentItem();

                                        if (isSimilar(current, back)) {
                                            InventoryConstructor constructor = new InventoryConstructor(player, "&eHelp menu", loreConstructor.getUsedCmd());

                                            plugin.getServer().getPluginManager().callEvent(new MenuCloseEvent(player, MenuType.LORE, false));
                                            plugin.getServer().getPluginManager().callEvent(new MenuOpenEvent(player, MenuType.HELP, true));

                                            loreConstructor.close();

                                            constructor.show();
                                        } else {
                                            if (isSimilar(current, items)) {
                                                SoundFixer.ENTITY_VILLAGER_NO.playSound(player, 0.5F, 2.0F);
                                            } else {
                                                SoundFixer.ENTITY_VILLAGER_NO.playSound(player, 0.5F, 0.1F);
                                            }
                                        }
                                    } else {
                                        SoundFixer.ENTITY_VILLAGER_NO.playSound(player, 0.5F, 0.1F);
                                    }
                                } else {
                                    SoundFixer.ENTITY_VILLAGER_NO.playSound(player, 0.5F, 0.1F);
                                }
                            } else {
                                SoundFixer.ENTITY_VILLAGER_NO.playSound(player, 0.5F, 0.1F);
                            }
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
