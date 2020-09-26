package ml.karmaconfigs.Supplies.Events;

import ml.karmaconfigs.Supplies.Suministry;
import ml.karmaconfigs.Supplies.Utils.Animations.AnimationMaker;
import ml.karmaconfigs.Supplies.Utils.BeaconChest;
import ml.karmaconfigs.Supplies.Utils.Files.Config;
import ml.karmaconfigs.Supplies.Utils.Files.Message;
import ml.karmaconfigs.Supplies.Utils.Holograms;
import ml.karmaconfigs.Supplies.Utils.SoundFixer;
import ml.karmaconfigs.Supplies.Utils.Suministries.SupplyLoader;
import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

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

public class ChestOpenEvent implements Listener, Suministry {

    private final static HashMap<Beacon, Boolean> isRunning = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChestOpen(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (Config.isAllowed(player.getWorld())) {
            if (e.hasBlock()) {
                if (e.getClickedBlock().getType().equals(Material.BEACON)) {
                    Beacon beacon = (Beacon) e.getClickedBlock().getState();

                    AnimationMaker maker = new AnimationMaker(null);

                    if (maker.getChests().contains(beacon)) {
                        SupplyLoader loader = new SupplyLoader(null);

                        loader.removeSupply(beacon.getLocation());

                        e.setCancelled(true);
                        if (isRunning.getOrDefault(beacon, false).equals(false)) {
                            isRunning.put(beacon, true);

                            Inventory inventory = BeaconChest.getInventory(beacon);

                            SoundFixer.ENTITY_HORSE_ARMOR.playSound(player, 0.5F, 0.1F);

                            ItemStack[] stacks = inventory.getContents();

                            List<ItemStack> items = new ArrayList<>();

                            for (ItemStack stack : stacks) {
                                if (stack != null) {
                                    if (!stack.getType().equals(Material.AIR)) {
                                        items.add(stack);
                                    } else {
                                        items.add(new ItemStack(Material.AIR));
                                    }
                                } else {
                                    items.add(new ItemStack(Material.AIR));
                                }
                            }

                            List<ItemStack> countItems = new ArrayList<>();

                            for (ItemStack stack : items) {
                                if (stack != null) {
                                    if (!stack.getType().equals(Material.AIR)) {
                                        countItems.add(stack);
                                    }
                                }
                            }

                            if (Suministry.hasHolographic()) {
                                Holograms holograms = new Holograms(beacon, Message.openingHolo(player, countItems.size()));
                                holograms.updateText();
                            }

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    new BukkitRunnable() {
                                        int i = items.size();
                                        int size = countItems.size();
                                        @Override
                                        public void run() {
                                            int rand = new Random().nextInt(100);
                                            if (i != 0) {
                                                if (items.get(i - 1) != null) {
                                                    if (!items.get(i - 1).getType().equals(Material.AIR)) {
                                                        Item dropped = player.getWorld().dropItemNaturally(e.getClickedBlock().getLocation().add(0D, 1D, 0D), items.get(i - 1));
                                                        dropped.setVelocity(dropped.getVelocity().multiply(3));
                                                        SoundFixer.ENTITY_ITEM_PICKUP.playSound(beacon.getLocation(), 0.5F, 0.2F);
                                                        if (rand >= 85) {
                                                            SoundFixer.ENTITY_HORSE_ARMOR.playSound(beacon.getLocation(), 0.5F, 0.1F);
                                                        }

                                                        size--;

                                                        if (Suministry.hasHolographic()) {
                                                            Holograms holograms = new Holograms(beacon, Message.openingHolo(player, size));
                                                            holograms.updateText();
                                                        }
                                                    } else {
                                                        SoundFixer.UI_BUTTON_CLICK.playSound(beacon.getLocation(), 0.5F, 2.0F);
                                                    }
                                                }
                                            } else {
                                                if (Suministry.hasHolographic()) {
                                                    Holograms.getHologram(beacon).delete();
                                                }

                                                BeaconChest.getInventory(beacon).clear();
                                                beacon.getBlock().breakNaturally(null);
                                                new BukkitRunnable() {
                                                    @Override
                                                    public void run() {
                                                        for (Entity entity : beacon.getLocation().getWorld().getNearbyEntities(beacon.getLocation(), 5, 5, 5)) {
                                                            if (entity instanceof Item) {
                                                                Item item = (Item) entity;
                                                                if (item.getItemStack().getType().equals(Material.BEACON)) {
                                                                    maker.getChests().remove(beacon);
                                                                    item.remove();
                                                                    isRunning.remove(beacon);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }.runTaskLater(plugin, 5);
                                                cancel();
                                            }
                                            i--;
                                        }
                                    }.runTaskTimer(plugin, 0, 10);
                                }
                            }.runTaskLater(plugin, 20);
                        }
                    }
                }
            }
        }
    }
}
