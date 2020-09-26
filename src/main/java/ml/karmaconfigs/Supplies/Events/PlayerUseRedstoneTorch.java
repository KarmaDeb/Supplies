package ml.karmaconfigs.Supplies.Events;

import ml.karmaconfigs.Supplies.API.Events.PlayerSupplyRequest;
import ml.karmaconfigs.Supplies.Utils.Animations.AnimationMaker;
import ml.karmaconfigs.Supplies.Utils.Files.Config;
import ml.karmaconfigs.Supplies.Utils.Files.Message;
import ml.karmaconfigs.Supplies.Utils.Particles.ParticleFixer;
import ml.karmaconfigs.Supplies.Utils.Particles.ParticleType;
import ml.karmaconfigs.Supplies.Utils.SoundFixer;
import ml.karmaconfigs.Supplies.Utils.StringUtils;
import ml.karmaconfigs.Supplies.Utils.Suministries.Suministry;
import ml.karmaconfigs.Supplies.Utils.User;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

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

public class PlayerUseRedstoneTorch implements Listener, ml.karmaconfigs.Supplies.Suministry {

    enum denyType {
        WORLD,WORLDGUARD
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerThrowSuministry(PlayerInteractEvent e) {
        if (e.getAction().toString().contains("AIR") || e.getAction().toString().contains("BLOCK")) {
            Player player = e.getPlayer();
            User user = new User(player);

            if (player.getInventory().getItem(player.getInventory().getHeldItemSlot()) != null) {
                if (!player.getInventory().getItem(player.getInventory().getHeldItemSlot()).getType().equals(Material.AIR)) {
                    if (player.getInventory().getItem(player.getInventory().getHeldItemSlot()).getType().equals(Material.LEGACY_REDSTONE_TORCH_ON)) {
                        denyType denyType = null;
                        boolean mustRun = true;
                        if (Config.isAllowed(player.getWorld())) {
                            if (ml.karmaconfigs.Supplies.Suministry.hasWorldGuard()) {
                                if (user.isInRegion()) {
                                    if (user.getRegion() != null) {
                                        if (!Config.isAllowed(player.getWorld(), user.getRegion())) {
                                            mustRun = false;
                                            denyType = PlayerUseRedstoneTorch.denyType.WORLDGUARD;
                                        }
                                    }
                                }
                            }
                        } else {
                            mustRun = false;
                            denyType = PlayerUseRedstoneTorch.denyType.WORLD;
                        }

                        ItemStack torch = player.getInventory().getItem(player.getInventory().getHeldItemSlot());
                        ItemMeta meta = torch.getItemMeta();

                        if (meta.hasDisplayName()) {
                            for (Suministry suministry : new Suministry().getSuministries()) {
                                if (suministry.isGrenade(torch)) {
                                    e.setCancelled(true);
                                    if (e.getAction().toString().contains("AIR")) {

                                        if (mustRun) {

                                            PlayerSupplyRequest event = new PlayerSupplyRequest(player, player.getLocation(), suministry.getName(), suministry);

                                            plugin.getServer().getPluginManager().callEvent(event);

                                            if (!event.isCancelled()) {

                                                ItemStack dropTorch = new ItemStack(Material.LEGACY_REDSTONE_TORCH_ON, 1);

                                                dropTorch.setItemMeta(torch.getItemMeta());

                                                for (Player online : plugin.getServer().getOnlinePlayers()) {
                                                    Message.invoked(online, player, StringUtils.toColor(suministry.getName()), player.getLocation());
                                                }

                                                Item dropped = player.getWorld().dropItemNaturally(player.getLocation(), dropTorch);
                                                dropped.setVelocity(player.getLocation().getDirection());
                                                dropped.setPickupDelay(20 * 100);

                                                if (torch.getAmount() - 1 != 0) {
                                                    torch.setAmount(torch.getAmount() - 1);
                                                    player.getInventory().setItem(player.getInventory().getHeldItemSlot(), torch);
                                                } else {
                                                    player.getInventory().setItem(player.getInventory().getHeldItemSlot(), null);
                                                }

                                                new BukkitRunnable() {
                                                    int back = 100;
                                                    @Override
                                                    public void run() {
                                                        if (back != 0) {
                                                            for (Entity item : dropped.getWorld().getEntities()) {
                                                                if (item.getEntityId() == dropped.getEntityId()) {
                                                                    if (item.getWorld().getHighestBlockYAt(item.getLocation()) < item.getLocation().add(0D, 1D, 0D).getY()) {
                                                                        for (int i = 0; i < 3; i++) {
                                                                            ParticleFixer smoke = new ParticleFixer(ParticleType.SMOKE);
                                                                            smoke.sendParticle(dropped.getLocation());
                                                                        }
                                                                        if (back == 80 || back == 60 || back == 40 || back == 20 || back == 10 || back == 5) {
                                                                            item.getWorld().strikeLightningEffect(item.getLocation());
                                                                        }
                                                                    } else {
                                                                        if (Config.destroyUnderBlock) {
                                                                            cancel();
                                                                            Location iLoc = item.getLocation();
                                                                            item.remove();
                                                                            dropped.remove();
                                                                            user.send(Message.prefix + Message.unableToLaunch);
                                                                            plugin.getServer().getScheduler().runTaskLater(plugin, () -> iLoc.getWorld().createExplosion(iLoc.getX(), iLoc.getY(), iLoc.getZ(), 4F, false, false), 20 * 4);
                                                                        } else {
                                                                            cancel();
                                                                            if (user.giveGrenade(suministry, 1)) {
                                                                                item.remove();
                                                                                dropped.remove();
                                                                            } else {
                                                                                dropped.setPickupDelay(0);
                                                                            }
                                                                            user.send(Message.prefix + Message.unableToLaunch);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            new AnimationMaker(dropped).startAnimationFor(dropTorch.getItemMeta().getDisplayName(), player);
                                                            for (int i = 0; i < 3; i++) {
                                                                SoundFixer.ENTITY_WITHER_SHOOT.playSound(dropped.getLocation());
                                                            }
                                                            dropped.getWorld().strikeLightningEffect(dropped.getLocation());
                                                            dropped.remove();
                                                            cancel();
                                                        }
                                                        back--;
                                                    }
                                                }.runTaskTimer(plugin, 0, 1);
                                                break;
                                            } else {
                                                player.updateInventory();
                                            }
                                        } else {
                                            switch (denyType) {
                                                case WORLD:
                                                    new User(player).send(Message.prefix + Message.notAllowedWorld);
                                                    break;
                                                case WORLDGUARD:
                                                    new User(player).send(Message.prefix + Message.notAllowedRegion);
                                                    break;
                                            }
                                        }
                                    } else {
                                        user.send(Message.prefix + Message.notInGround);
                                        player.updateInventory();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
