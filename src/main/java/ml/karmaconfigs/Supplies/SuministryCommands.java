package ml.karmaconfigs.Supplies;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import ml.karmaconfigs.Supplies.API.Events.MenuOpenEvent;
import ml.karmaconfigs.Supplies.API.MenuType;
import ml.karmaconfigs.Supplies.Utils.Files.Config;
import ml.karmaconfigs.Supplies.Utils.Files.Message;
import ml.karmaconfigs.Supplies.Utils.InventoryMaker.InventoryConstructor;
import ml.karmaconfigs.Supplies.Utils.InventoryMaker.Shop.InfiniteInventoryPage;
import ml.karmaconfigs.Supplies.Utils.Server.WarningLevel;
import ml.karmaconfigs.Supplies.Utils.SoundFixer;
import ml.karmaconfigs.Supplies.Utils.StringUtils;
import ml.karmaconfigs.Supplies.Utils.User;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.*;

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

public class SuministryCommands implements CommandExecutor, Suministry {

    private final Permission blockPermission = new Permission("supply.block", PermissionDefault.OP);
    private final Permission shopPermission = new Permission("supply.shop", PermissionDefault.TRUE);
    private final Permission pricePermission = new Permission("supply.price", PermissionDefault.OP);
    private final Permission infoPermission = new Permission("supply.info", PermissionDefault.OP);
    private final Permission lorePermission = new Permission("supply.lore", PermissionDefault.OP);
    private final Permission loreAddPermission = new Permission("supply.lore.add", PermissionDefault.OP);
    private final Permission loreSetPermission = new Permission("supply.lore.set", PermissionDefault.OP);
    private final Permission loreGetPermission = new Permission("supply.lore.get", PermissionDefault.OP);
    private final Permission percentagePermission = new Permission("supply.percentage", PermissionDefault.OP);
    private final Permission listPermission = new Permission("supply.list", PermissionDefault.OP);
    private final Permission renamePermission = new Permission("supply.rename", PermissionDefault.OP);
    private final Permission helpPermission = new Permission("supply.help", PermissionDefault.OP);
    private final Permission wandPermission = new Permission("supply.wand", PermissionDefault.OP);
    private final Permission removePermission = new Permission("supply.remove", PermissionDefault.OP);
    private final Permission savePermission = new Permission("supply.save", PermissionDefault.OP);
    private final Permission givePermission = new Permission("supply.give", PermissionDefault.OP);

    public SuministryCommands() {
        loreAddPermission.addParent(lorePermission, true);
        loreSetPermission.addParent(lorePermission, true);
        loreGetPermission.addParent(lorePermission, true);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            User user = new User(player);

            if (args.length == 0) {
                user.send(StringUtils.line(10, ChatColor.BLACK));
                user.send(" ");
                user.send(name + " &f&l&m=&r " + version);
                user.send("&f/{0} &7help".replace("{0}", arg));
                user.send(" ");
                user.send(StringUtils.line(10, ChatColor.BLACK));
            } else {
                if (args[0] != null) {
                    if (args[0].equals("help")) {
                        if (player.hasPermission(helpPermission)) {
                            InventoryConstructor constructor = new InventoryConstructor(player, "&eHelp menu", arg);
                            constructor.show();
                            plugin.getServer().getPluginManager().callEvent(new MenuOpenEvent(player, MenuType.HELP, false));
                            SoundFixer.BLOCK_NOTE_BLOCK_PLING.playSound(player);
                        } else {
                            user.send(Message.prefix + Message.permission(helpPermission));
                        }
                    } else {
                        if (args[0].equals("wand")) {
                            if (player.hasPermission(wandPermission)) {
                                if (args.length == 1) {
                                    if (user.giveWand()) {
                                        user.send(Message.prefix + Message.receivedWand(arg));
                                    } else {
                                        user.send(Message.prefix + Message.noSize);
                                    }
                                } else {
                                    user.send(Message.prefix + Message.wandUsage(arg));
                                }
                            } else {
                                user.send(Message.prefix + Message.permission(wandPermission));
                            }
                        } else {
                            if (args[0].equals("remove")) {
                                if (player.hasPermission(removePermission)) {
                                    if (args.length == 2) {
                                        String name = args[1];

                                        ml.karmaconfigs.Supplies.Utils.Suministries.Suministry suministry = new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry(name);

                                        if (suministry.exists()) {
                                            if (suministry.remove()) {
                                                user.send(Message.prefix + Message.removed(name));
                                            } else {
                                                user.send(Message.prefix + Message.errorRemoving);
                                            }
                                        } else {
                                            user.send(Message.prefix + Message.notExists(name));
                                        }
                                    } else {
                                        user.send(Message.prefix + Message.removeUsage(arg));
                                    }
                                } else {
                                    user.send(Message.prefix + Message.permission(removePermission));
                                }
                            } else {
                                if (args[0].equals("save")) {
                                    if (player.hasPermission(savePermission)) {
                                        if (args.length == 2) {
                                            String name = args[1];
                                            if (user.isManagingChest()) {
                                                ml.karmaconfigs.Supplies.Utils.Suministries.Suministry suministry = new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry(name);

                                                if (suministry.setContents(user.getManagingChest().getInventory()) && suministry.saveAs(name)) {
                                                    user.send(Message.prefix + Message.saved(name));
                                                } else {
                                                    user.send(Message.prefix + Message.errorSaving);
                                                }
                                            } else {
                                                user.send(Message.prefix + Message.noSelection);
                                            }
                                        } else {
                                            user.send(Message.prefix + Message.saveUsage(arg));
                                        }
                                    } else {
                                        user.send(Message.prefix + Message.permission(savePermission));
                                    }
                                } else {
                                    if (args[0].equals("rename")) {
                                        if (player.hasPermission(renamePermission)) {
                                            if (args.length >= 3) {
                                                String name = args[1];
                                                StringBuilder builder = new StringBuilder();
                                                for (int i = 2; i < args.length; i++) {
                                                    if (i != args.length - 1) {
                                                        builder.append(args[i]).append(" ");
                                                    } else {
                                                        builder.append(args[i]);
                                                    }
                                                }
                                                String newName = builder.toString();

                                                ml.karmaconfigs.Supplies.Utils.Suministries.Suministry suministry = new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry(name);

                                                if (suministry.exists()) {
                                                    if (suministry.setName(newName)) {
                                                        user.send(Message.prefix + Message.renamed(name, newName));
                                                    } else {
                                                        user.send(Message.prefix + Message.renameError(name));
                                                    }
                                                } else {
                                                    user.send(Message.prefix + Message.notExists(name));
                                                }
                                            } else {
                                                user.send(Message.prefix + Message.renameUsage(arg));
                                            }
                                        } else {
                                            user.send(Message.prefix + Message.permission(renamePermission));
                                        }
                                    } else {
                                        if (args[0].equals("list")) {
                                            if (player.hasPermission(listPermission)) {
                                                TextComponent list = new TextComponent(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', Message.prefix + "&fAvailable supplies&7: "));
                                                for (int i = 0; i < new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry().getSuministries().size(); i++) {
                                                    TextComponent component;
                                                    ml.karmaconfigs.Supplies.Utils.Suministries.Suministry suministry = new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry().getSuministries().get(i);
                                                    if (i != new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry().getSuministries().size() - 1) {
                                                        component = new TextComponent(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&e" + suministry.getFileName(false) + "&7, "));
                                                    } else {
                                                        component = new TextComponent(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&e" + suministry.getSuministries().get(i).getFileName(false)));
                                                    }
                                                    if (player.hasPermission(givePermission)) {
                                                        if (Config.isAllowed(player.getWorld())) {
                                                            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&aClick to give 1 " + suministry.getName()))));
                                                            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/{arg} give ".replace("{arg}", arg) + suministry.getFileName(false) + " 1"));
                                                        }
                                                    }
                                                    list.addExtra(component);
                                                }

                                                user.send(list);
                                            } else {
                                                user.send(Message.prefix + Message.permission(listPermission));
                                            }
                                        } else {
                                            if (args[0].equals("give")) {
                                                if (player.hasPermission(givePermission)) {
                                                    if (args.length == 3) {
                                                        String name = args[1];

                                                        try {
                                                            int amount = Integer.parseInt(args[2]);

                                                            if (!name.equals("random")) {
                                                                ml.karmaconfigs.Supplies.Utils.Suministries.Suministry suministry = new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry(name);

                                                                if (suministry.exists()) {
                                                                    if (user.giveGrenade(suministry, amount)) {
                                                                        user.send(Message.prefix + Message.received(suministry.getName(), amount));
                                                                    } else {
                                                                        user.send(Message.prefix + Message.noSize);
                                                                    }
                                                                } else {
                                                                    user.send(Message.prefix + Message.notExists(name));
                                                                }
                                                            } else {
                                                                if (!new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry().getSuministries().isEmpty()) {
                                                                    HashMap<ml.karmaconfigs.Supplies.Utils.Suministries.Suministry, Integer> grenades = new HashMap<>();
                                                                    boolean isGiven = false;
                                                                    for (int i = 0; i < amount; i++) {
                                                                        int random = new Random().nextInt(new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry().getSuministries().size());

                                                                        ml.karmaconfigs.Supplies.Utils.Suministries.Suministry supply = new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry().getSuministries().get(random);

                                                                        grenades.put(supply, grenades.getOrDefault(supply, 0) + 1);
                                                                    }
                                                                    for (ml.karmaconfigs.Supplies.Utils.Suministries.Suministry supply : grenades.keySet()) {
                                                                        isGiven = user.giveGrenade(supply, grenades.get(supply));
                                                                    }
                                                                    if (isGiven) {
                                                                        user.send(Message.prefix + Message.random(amount));
                                                                    } else {
                                                                        user.send(Message.prefix + Message.noSize);
                                                                    }
                                                                } else {
                                                                    user.send(Message.prefix + Message.noAvailable);
                                                                }
                                                            }
                                                        } catch (NumberFormatException e) {
                                                            user.send(Message.prefix + Message.giveUsage(arg));
                                                        }
                                                    } else {
                                                        if (args.length == 4) {
                                                            String tar = args[3];
                                                            if (plugin.getServer().getPlayer(tar) != null) {
                                                                Player targ = plugin.getServer().getPlayer(tar);
                                                                if (!targ.equals(player)) {
                                                                    User target = new User(targ);

                                                                    String name = args[1];

                                                                    try {
                                                                        int amount = Integer.parseInt(args[2]);

                                                                        if (!name.equals("random")) {
                                                                            ml.karmaconfigs.Supplies.Utils.Suministries.Suministry suministry = new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry(name);

                                                                            if (suministry.exists()) {
                                                                                target.giveGrenade(suministry, amount);
                                                                                target.send(Message.prefix + Message.received(suministry.getName(), amount));
                                                                                user.send(Message.prefix + Message.gave(targ, suministry.getName(), amount));
                                                                            } else {
                                                                                user.send(Message.prefix + Message.notExists(name));
                                                                            }
                                                                        } else {
                                                                            if (!new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry().getSuministries().isEmpty()) {
                                                                                HashMap<ml.karmaconfigs.Supplies.Utils.Suministries.Suministry, Integer> grenades = new HashMap<>();
                                                                                boolean isGiven = false;
                                                                                for (int i = 0; i < amount; i++) {
                                                                                    int random = new Random().nextInt(new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry().getSuministries().size());

                                                                                    ml.karmaconfigs.Supplies.Utils.Suministries.Suministry supply = new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry().getSuministries().get(random);

                                                                                    grenades.put(supply, grenades.getOrDefault(supply, 0) + 1);
                                                                                }
                                                                                for (ml.karmaconfigs.Supplies.Utils.Suministries.Suministry supply : grenades.keySet()) {
                                                                                    isGiven = target.giveGrenade(supply, grenades.get(supply));
                                                                                }
                                                                                if (isGiven) {
                                                                                    target.send(Message.prefix + Message.random(amount));
                                                                                    user.send(Message.prefix + Message.gaveRandom(amount, targ));
                                                                                } else {
                                                                                    user.send(Message.prefix + Message.noSizeOther(targ));
                                                                                }
                                                                            } else {
                                                                                user.send(Message.prefix + Message.noAvailable);
                                                                            }
                                                                        }
                                                                    } catch (NumberFormatException e) {
                                                                        user.send(Message.prefix + Message.giveUsage(arg));
                                                                    }
                                                                } else {
                                                                    player.performCommand(arg + " " + args[0] + " " + args[1] + " " + args[2]);
                                                                    return true;
                                                                }
                                                            } else {
                                                                user.send(Message.prefix + Message.offline(tar));
                                                            }
                                                        } else {
                                                            user.send(Message.prefix + Message.giveUsage(arg));
                                                        }
                                                    }
                                                } else {
                                                    user.send(Message.prefix + Message.permission(givePermission));
                                                }
                                            } else {
                                                if (args[0].equals("percentage") || args[0].equals("per") || args[0].equals("%")) {
                                                    if (player.hasPermission(percentagePermission)) {
                                                        if (args.length == 3) {
                                                            String name = args[1];

                                                            try {
                                                                double per;
                                                                String val;
                                                                if (!args[2].contains(".")) {
                                                                    val = args[2] + ".0";
                                                                } else {
                                                                    val = args[2].split("\\.")[0] + "." + args[2].split("\\.")[1].substring(0, 1);
                                                                }

                                                                per = Double.parseDouble(val);

                                                                ml.karmaconfigs.Supplies.Utils.Suministries.Suministry supply = new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry(name);

                                                                if (supply.exists()) {
                                                                    if (per <= 100.0 && per >= 1.0) {
                                                                        supply.setPercentage(per);
                                                                        user.send(Message.prefix + Message.percentageSet(supply.getName(), per));
                                                                    } else {
                                                                        if (per >= 100.0) {
                                                                            user.send(Message.prefix + Message.maxPer);
                                                                        } else {
                                                                            if (per <= 1.0) {
                                                                                user.send(Message.prefix + Message.minPer);
                                                                            }
                                                                        }
                                                                    }
                                                                } else {
                                                                    user.send(Message.prefix + Message.notExists(name));
                                                                }
                                                            } catch (NumberFormatException e) {
                                                                user.send(Message.prefix + Message.percentageUsage(arg, args[0]));
                                                            }
                                                        } else {
                                                            user.send(Message.prefix + Message.percentageUsage(arg, args[0]));
                                                        }
                                                    } else {
                                                        user.send(Message.prefix + Message.permission(percentagePermission));
                                                    }
                                                } else {
                                                    if (args[0].equals("lore")) {
                                                        if (player.hasPermission(lorePermission)) {
                                                            if (args.length >= 3) {
                                                                String name = args[1];

                                                                ml.karmaconfigs.Supplies.Utils.Suministries.Suministry supply = new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry(name);

                                                                if (supply.exists()) {
                                                                    if (args[2].equals("set")) {
                                                                        if (player.hasPermission(loreSetPermission)) {
                                                                            if (args.length == 5) {
                                                                                if (!supply.getLoreManager().getLore().isEmpty()) {
                                                                                    try {
                                                                                        int line = Integer.parseInt(args[3]);

                                                                                        StringBuilder builder = new StringBuilder();

                                                                                        for (int i = 4; i < args.length; i++) {
                                                                                            builder.append(StringUtils.toColor(args[i].replace("''", " ")));
                                                                                        }

                                                                                        String text = StringUtils.toColor(builder.toString());

                                                                                        if (supply.getLoreManager().setLine(line - 1, text)) {
                                                                                            if (!StringUtils.noColor(text).equals("null")) {
                                                                                                user.send(Message.prefix + Message.loreSet(supply.getName(), line, text));
                                                                                            } else {
                                                                                                user.send(Message.prefix + Message.loreRemove(supply.getName(), line));
                                                                                            }
                                                                                        } else {
                                                                                            user.send(Message.prefix + Message.incorrectLoreLine(supply.getName(), supply.getLoreManager().getLore().size()));
                                                                                        }

                                                                                    } catch (NumberFormatException e) {
                                                                                        user.send(Message.prefix + Message.loreSetUsage(arg));
                                                                                    }
                                                                                } else {
                                                                                    user.send(Message.prefix + Message.noLore(supply.getName()));
                                                                                }
                                                                            } else {
                                                                                user.send(Message.prefix + Message.loreSetUsage(arg));
                                                                            }
                                                                        } else {
                                                                            user.send(Message.prefix + Message.permission(loreSetPermission));
                                                                        }
                                                                    } else {
                                                                        if (args[2].equals("add")) {
                                                                            if (player.hasPermission(loreAddPermission)) {
                                                                                if (args.length >= 4) {
                                                                                    StringBuilder builder = new StringBuilder();

                                                                                    for (int i = 3; i < args.length; i++) {
                                                                                        if (i != args.length - 1) {
                                                                                            builder.append(StringUtils.toColor(args[i].replace("''", " "))).append(" ");
                                                                                        } else {
                                                                                            builder.append(StringUtils.toColor(args[i].replace("''", " ")));
                                                                                        }
                                                                                    }

                                                                                    String text = builder.toString();

                                                                                    supply.getLoreManager().addLore(text);
                                                                                    user.send(Message.prefix + Message.loreAdd(supply.getName(), text));
                                                                                } else {
                                                                                    user.send(Message.prefix + Message.loreAddUsage(arg));
                                                                                }
                                                                            } else {
                                                                                user.send(Message.prefix + Message.permission(loreAddPermission));
                                                                            }
                                                                        } else {
                                                                            if (args[2].equals("get")) {
                                                                                if (player.hasPermission(loreGetPermission)) {
                                                                                    if (args.length == 4) {
                                                                                        if (!supply.getLoreManager().getLore().isEmpty()) {
                                                                                            try {
                                                                                                int line = Integer.parseInt(args[3]);

                                                                                                if (supply.getLoreManager().hasLine(line - 1)) {
                                                                                                    user.send(Message.prefix + Message.getLine(supply.getName(), line, supply.getLoreManager().getLine(line - 1)));
                                                                                                } else {
                                                                                                    user.send(Message.prefix + Message.incorrectLoreLine(supply.getName(), supply.getLoreManager().getLore().size()));
                                                                                                }

                                                                                            } catch (NumberFormatException e) {
                                                                                                user.send(Message.prefix + Message.loreGetUsage(arg));
                                                                                            }
                                                                                        } else {
                                                                                            user.send(Message.prefix + Message.noLore(supply.getName()));
                                                                                        }
                                                                                    } else {
                                                                                        user.send(Message.prefix + Message.loreGetUsage(arg));
                                                                                    }
                                                                                } else {
                                                                                    user.send(Message.prefix + Message.permission(loreGetPermission));
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                } else {
                                                                    user.send(Message.prefix + Message.notExists(name));
                                                                }
                                                            } else {
                                                                user.send(Message.prefix + Message.loreUsage(arg));
                                                            }
                                                        } else {
                                                            user.send(Message.prefix + Message.permission(lorePermission));
                                                        }
                                                    } else {
                                                        if (args[0].equals("info")) {
                                                            if (player.hasPermission(infoPermission)) {
                                                                if (args.length == 2) {
                                                                    String name = args[1];

                                                                    ml.karmaconfigs.Supplies.Utils.Suministries.Suministry supply = new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry(name);

                                                                    if (supply.exists()) {
                                                                        user.send(Message.info(supply));
                                                                    } else {
                                                                        user.send(Message.prefix + Message.notExists(name));
                                                                    }
                                                                } else {
                                                                    user.send(Message.prefix + Message.infoUsage(arg));
                                                                }
                                                            } else {
                                                                user.send(Message.prefix + Message.permission(infoPermission));
                                                            }
                                                        } else {
                                                            if (args[0].equals("shop")) {
                                                                if (Config.shopEnabled) {
                                                                    if (Suministry.hasVault()) {
                                                                        if (player.hasPermission(shopPermission)) {
                                                                            if (args.length == 1) {
                                                                                List<ml.karmaconfigs.Supplies.Utils.Suministries.Suministry> supplies = new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry().getSuministries();

                                                                                ArrayList<ItemStack> grenades = new ArrayList<>();
                                                                                for (ml.karmaconfigs.Supplies.Utils.Suministries.Suministry supply : supplies) {
                                                                                    List<String> lore = new ArrayList<>(supply.getLoreManager().getLore());

                                                                                    lore.add(" ");
                                                                                    lore.add(StringUtils.toColor("&ePrice: &b" + supply.getPrice()));

                                                                                    ItemStack grenade = supply.getGrenade(1);
                                                                                    ItemMeta meta = grenade.getItemMeta();
                                                                                    meta.setLore(lore);

                                                                                    grenade.setItemMeta(meta);

                                                                                    grenades.add(grenade);
                                                                                }

                                                                                InfiniteInventoryPage page = new InfiniteInventoryPage(player, grenades);
                                                                                page.openPage(0);
                                                                            } else {
                                                                                user.send(Message.prefix + Message.shopUsage(arg));
                                                                            }
                                                                        } else {
                                                                            user.send(Message.prefix + Message.permission(shopPermission));
                                                                        }
                                                                    } else {
                                                                        user.send(Message.prefix + Message.noVault);
                                                                    }
                                                                } else {
                                                                    user.send(Message.prefix + Message.shopDisabled);
                                                                }
                                                            } else {
                                                                if (args[0].equals("price")) {
                                                                    if (player.hasPermission(pricePermission)) {
                                                                        if (args.length == 3) {
                                                                            String name = args[1];

                                                                            ml.karmaconfigs.Supplies.Utils.Suministries.Suministry supply = new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry(name);

                                                                            if (supply.exists()) {
                                                                                try {
                                                                                    double price;
                                                                                    String val;
                                                                                    if (!args[2].contains(".")) {
                                                                                        val = args[2] + ".0";
                                                                                    } else {
                                                                                        val = args[2].split("\\.")[0] + "." + args[2].split("\\.")[1].substring(0, 1);
                                                                                    }
                                                                                    price = Double.parseDouble(val);

                                                                                    supply.setPrice(price);
                                                                                    user.send(Message.prefix + Message.priceSet(supply.getName(), price));
                                                                                } catch (NumberFormatException e) {
                                                                                    user.send(Message.prefix + Message.priceUsage(arg));
                                                                                }
                                                                            } else {
                                                                                user.send(Message.prefix + Message.notExists(name));
                                                                            }
                                                                        } else {
                                                                            user.send(Message.prefix + Message.priceUsage(arg));
                                                                        }
                                                                    } else {
                                                                        user.send(Message.prefix + Message.permission(pricePermission));
                                                                    }
                                                                } else {
                                                                    if (args[0].equals("block")) {
                                                                        if (player.hasPermission(blockPermission)) {
                                                                            if (args.length == 2) {
                                                                                String world = args[1];

                                                                                if (plugin.getServer().getWorld(world) != null) {
                                                                                    World w = plugin.getServer().getWorld(world);
                                                                                    if (Config.isAllowed(w)) {
                                                                                        Config.blockWorld(w);
                                                                                        user.send(Message.prefix + Message.blockedWorld(w));
                                                                                    } else {
                                                                                        Config.unblockWorld(w);
                                                                                        user.send(Message.prefix + Message.unblocked(w));
                                                                                    }
                                                                                } else {
                                                                                    user.send(Message.prefix + Message.notExists);
                                                                                }
                                                                            } else {
                                                                                if (args.length == 3) {
                                                                                    String world = args[1];
                                                                                    String reg = args[2];

                                                                                    if (plugin.getServer().getWorld(world) != null) {
                                                                                        World w = plugin.getServer().getWorld(world);
                                                                                        ProtectedRegion region = getRegion(reg);
                                                                                        if (region != null) {
                                                                                            if (Config.isAllowed(w, region)) {
                                                                                                Config.blockRegion(w, region);
                                                                                                user.send(Message.prefix + Message.blockedRegion(w, region));
                                                                                            } else {
                                                                                                Config.unblockRegion(w, region);
                                                                                                user.send(Message.prefix + Message.unblockedRegion(w, region));
                                                                                            }
                                                                                        } else {
                                                                                            user.send(Message.prefix + Message.notExists);
                                                                                        }
                                                                                    } else {
                                                                                        user.send(Message.prefix + Message.notExists);
                                                                                    }
                                                                                } else {
                                                                                    user.send(Message.prefix + Message.blockUsage(arg));
                                                                                }
                                                                            }
                                                                        } else {
                                                                            user.send(Message.prefix + Message.permission(blockPermission));
                                                                        }
                                                                    } else {
                                                                        user.send(StringUtils.line(10, ChatColor.BLACK));
                                                                        user.send(" ");
                                                                        user.send(name + " &f&l&m=&r " + version);
                                                                        user.send("&f/{0} &7help".replace("{0}", arg));
                                                                        user.send(" ");
                                                                        user.send(StringUtils.line(10, ChatColor.BLACK));
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
                            }
                        }
                    }
                } else {
                    user.send(StringUtils.line(10, ChatColor.BLACK));
                    user.send(" ");
                    user.send(name + " &f&l&m=&r " + version);
                    user.send("&f/{0} &7help".replace("{0}", arg));
                    user.send(" ");
                    user.send(StringUtils.line(10, ChatColor.BLACK));
                }
            }
        } else {
            if (args.length == 0) {
                out.send(StringUtils.line(10, ChatColor.WHITE));
                out.send(" ");
                out.send(name + " &f&l&m=&r " + version);
                out.send("&f/{0} &7help".replace("{0}", arg));
                out.send(" ");
                out.send(StringUtils.line(10, ChatColor.WHITE));
            } else {
                if (args[0] != null) {
                    if (args[0].equals("help")) {
                        out.send("&f----------- {0} &f-----------".replace("{0}", "&bSupplies help"));
                        out.send(" ");
                        out.send("&f/{cmd} help &d- &3Shows this".replace("{cmd}", arg));
                        out.send("&f/{cmd} remove &d- &3Remove a supply".replace("{cmd}", arg));
                        out.send("&f/{cmd} give <name|random> <amount> <player> &d- &3Give the player a supply grenade".replace("{cmd}", arg));
                        out.send("&f/{cmd} price <name> <price> &d- &3Set a supply price".replace("{cmd}", arg));
                        out.send("&f/{cmd} rename &d- &3Renames a supply".replace("{cmd}", arg));
                        out.send("&f/{cmd} list &d- &3List of available supplies".replace("{cmd}", arg));
                        out.send("&f/{cmd} info <name> &d- &3Display a supply info".replace("{cmd}", arg));
                        out.send("&f/{cmd} lore add <name> <string> &d- &3Add a lore line".replace("{cmd}", arg));
                        out.send("&f/{cmd} lore set <name> <line> <string>  &d- &3Set the lore line".replace("{cmd}", arg));
                        out.send("&f/{cmd} lore get <name> <line> &d- &3Get the lore line".replace("{cmd}", arg));
                        out.send("&f/{cmd} %|per|percentage <name> <percentage[1.0-100.0]> &d- &3Manage the drop percentage of items".replace("{cmd}", arg));
                        out.send("&f/{cmd} block <world> (Optional) <region> &d- &3Blocks the region/world".replace("{cmd}", arg));
                    } else {
                        if (args[0].equals("wand")) {
                            out.send("This command is for players only!", WarningLevel.ERROR);
                        } else {
                            if (args[0].equals("remove")) {
                                if (args.length == 2) {
                                    String name = args[1];

                                    ml.karmaconfigs.Supplies.Utils.Suministries.Suministry suministry = new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry(name);

                                    if (suministry.exists()) {
                                        if (suministry.remove()) {
                                            out.send(Message.prefix + Message.removed(name));
                                        } else {
                                            out.send(Message.prefix + Message.errorRemoving);
                                        }
                                    } else {
                                        out.send(Message.prefix + Message.notExists(name));
                                    }
                                } else {
                                    out.send(Message.prefix + Message.removeUsage(arg));
                                }
                            } else {
                                if (args[0].equals("save")) {
                                    out.send("This command is for players only!", WarningLevel.ERROR);
                                } else {
                                    if (args[0].equals("rename")) {
                                        if (args.length >= 3) {
                                            String name = args[1];
                                            StringBuilder builder = new StringBuilder();
                                            for (int i = 2; i < args.length; i++) {
                                                if (i != args.length - 1) {
                                                    builder.append(args[i]).append(" ");
                                                } else {
                                                    builder.append(args[i]);
                                                }
                                            }
                                            String newName = builder.toString();

                                            ml.karmaconfigs.Supplies.Utils.Suministries.Suministry suministry = new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry(name);

                                            if (suministry.exists()) {
                                                if (suministry.setName(newName)) {
                                                    out.send(Message.prefix + Message.renamed(name, newName));
                                                } else {
                                                    out.send(Message.prefix + Message.renameError(name));
                                                }
                                            } else {
                                                out.send(Message.prefix + Message.notExists(name));
                                            }
                                        } else {
                                            out.send(Message.prefix + Message.renameUsage(arg));
                                        }
                                    } else {
                                        if (args[0].equals("list")) {
                                            List<String> supplies = new ArrayList<>();
                                            for (int i = 0; i < new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry().getSuministries().size(); i++) {
                                                supplies.add(new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry().getSuministries().get(i).getFileName(false));
                                            }

                                            out.send("&fAvailable supplies&7: " + supplies.toString()
                                                    .replace("[", "")
                                                    .replace("]", "")
                                                    .replace(",", "&e, &7")
                                                    .replace("{replace_open}", "[")
                                                    .replace("{replace_close}", "]")
                                                    .replace("{replace_comma}", ","));
                                        } else {
                                            if (args[0].equals("give")) {
                                                if (args.length == 3) {
                                                    out.send("You can only give yourself a supply grenade when you are a player!", WarningLevel.ERROR);
                                                } else {
                                                    if (args.length == 4) {
                                                        String tar = args[3];
                                                        if (plugin.getServer().getPlayer(tar) != null) {
                                                            Player targ = plugin.getServer().getPlayer(tar);
                                                            User target = new User(targ);

                                                            String name = args[1];

                                                            try {
                                                                int amount = Integer.parseInt(args[2]);

                                                                if (!name.equals("random")) {
                                                                    ml.karmaconfigs.Supplies.Utils.Suministries.Suministry suministry = new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry(name);

                                                                    if (suministry.exists()) {
                                                                        target.giveGrenade(suministry, amount);
                                                                        target.send(Message.prefix + Message.received(suministry.getName(), amount));
                                                                        out.send(Message.prefix + Message.gave(targ, suministry.getName(), amount));
                                                                    } else {
                                                                        out.send(Message.prefix + Message.notExists(name));
                                                                    }
                                                                } else {
                                                                    if (!new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry().getSuministries().isEmpty()) {
                                                                        HashMap<ml.karmaconfigs.Supplies.Utils.Suministries.Suministry, Integer> grenades = new HashMap<>();
                                                                        boolean isGiven = false;
                                                                        for (int i = 0; i < amount; i++) {
                                                                            int random = new Random().nextInt(new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry().getSuministries().size());

                                                                            ml.karmaconfigs.Supplies.Utils.Suministries.Suministry supply = new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry().getSuministries().get(random);

                                                                            grenades.put(supply, grenades.getOrDefault(supply, 0) + 1);
                                                                        }
                                                                        for (ml.karmaconfigs.Supplies.Utils.Suministries.Suministry supply : grenades.keySet()) {
                                                                            isGiven = target.giveGrenade(supply, grenades.get(supply));
                                                                        }
                                                                        if (isGiven) {
                                                                            target.send(Message.prefix + Message.random(amount));
                                                                            out.send(Message.prefix + Message.gaveRandom(amount, targ));
                                                                        } else {
                                                                            out.send(Message.prefix + Message.noSizeOther(targ));
                                                                        }
                                                                    } else {
                                                                        out.send(Message.prefix + Message.noAvailable);
                                                                    }
                                                                }
                                                            } catch (NumberFormatException e) {
                                                                out.send(Message.prefix + Message.giveUsage(arg));
                                                            }
                                                        } else {
                                                            out.send(Message.prefix + Message.offline(tar));
                                                        }
                                                    } else {
                                                        out.send(Message.prefix + Message.giveUsage(arg));
                                                    }
                                                }
                                            } else {
                                                if (args[0].equals("percentage") || args[0].equals("per") || args[0].equals("%")) {
                                                    if (args.length == 3) {
                                                        String name = args[1];

                                                        try {
                                                            double per;
                                                            String val;
                                                            if (!args[2].contains(".")) {
                                                                val = args[2] + ".0";
                                                            } else {
                                                                val = args[2].split("\\.")[0] + "." + args[2].split("\\.")[1].substring(0, 1);
                                                            }

                                                            per = Double.parseDouble(val);

                                                            ml.karmaconfigs.Supplies.Utils.Suministries.Suministry supply = new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry(name);

                                                            if (supply.exists()) {
                                                                if (per <= 100.0 && per >= 1.0) {
                                                                    supply.setPercentage(per);
                                                                    out.send(Message.prefix + Message.percentageSet(supply.getName(), per));
                                                                } else {
                                                                    if (per >= 100.0) {
                                                                        out.send(Message.prefix + Message.maxPer);
                                                                    } else {
                                                                        if (per <= 1.0) {
                                                                            out.send(Message.prefix + Message.minPer);
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                out.send(Message.prefix + Message.notExists(name));
                                                            }
                                                        } catch (NumberFormatException e) {
                                                            out.send(Message.prefix + Message.percentageUsage(arg, args[0]));
                                                        }
                                                    } else {
                                                        out.send(Message.prefix + Message.percentageUsage(arg, args[0]));
                                                    }
                                                } else {
                                                    if (args[0].equals("lore")) {
                                                        if (args.length >= 3) {
                                                            String name = args[1];

                                                            ml.karmaconfigs.Supplies.Utils.Suministries.Suministry supply = new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry(name);

                                                            if (supply.exists()) {
                                                                if (args[2].equals("set")) {
                                                                    if (args.length == 5) {
                                                                        if (!supply.getLoreManager().getLore().isEmpty()) {
                                                                            try {
                                                                                int line = Integer.parseInt(args[3]);

                                                                                StringBuilder builder = new StringBuilder();

                                                                                for (int i = 4; i < args.length; i++) {
                                                                                    builder.append(StringUtils.toColor(args[i].replace("''", " ")));
                                                                                }

                                                                                String text = StringUtils.toColor(builder.toString());

                                                                                if (supply.getLoreManager().setLine(line - 1, text)) {
                                                                                    if (!StringUtils.noColor(text).equals("null")) {
                                                                                        out.send(Message.prefix + Message.loreSet(supply.getName(), line, text));
                                                                                    } else {
                                                                                        out.send(Message.prefix + Message.loreRemove(supply.getName(), line));
                                                                                    }
                                                                                } else {
                                                                                    out.send(Message.prefix + Message.incorrectLoreLine(supply.getName(), supply.getLoreManager().getLore().size()));
                                                                                }

                                                                            } catch (NumberFormatException e) {
                                                                                out.send(Message.prefix + Message.loreSetUsage(arg));
                                                                            }
                                                                        } else {
                                                                            out.send(Message.prefix + Message.noLore(supply.getName()));
                                                                        }
                                                                    } else {
                                                                        out.send(Message.prefix + Message.loreSetUsage(arg));
                                                                    }
                                                                } else {
                                                                    if (args[2].equals("add")) {
                                                                        if (args.length >= 4) {
                                                                            StringBuilder builder = new StringBuilder();

                                                                            for (int i = 3; i < args.length; i++) {
                                                                                if (i != args.length - 1) {
                                                                                    builder.append(StringUtils.toColor(args[i].replace("''", " "))).append(" ");
                                                                                } else {
                                                                                    builder.append(StringUtils.toColor(args[i].replace("''", " ")));
                                                                                }
                                                                            }

                                                                            String text = builder.toString();

                                                                            supply.getLoreManager().addLore(text);
                                                                            out.send(Message.prefix + Message.loreAdd(supply.getName(), text));
                                                                        } else {
                                                                            out.send(Message.prefix + Message.loreAddUsage(arg));
                                                                        }
                                                                    } else {
                                                                        if (args[2].equals("get")) {
                                                                            if (args.length == 4) {
                                                                                if (!supply.getLoreManager().getLore().isEmpty()) {
                                                                                    try {
                                                                                        int line = Integer.parseInt(args[3]);

                                                                                        if (supply.getLoreManager().hasLine(line - 1)) {
                                                                                            out.send(Message.prefix + Message.getLine(supply.getName(), line, supply.getLoreManager().getLine(line - 1)));
                                                                                        } else {
                                                                                            out.send(Message.prefix + Message.incorrectLoreLine(supply.getName(), supply.getLoreManager().getLore().size()));
                                                                                        }

                                                                                    } catch (NumberFormatException e) {
                                                                                        out.send(Message.prefix + Message.loreGetUsage(arg));
                                                                                    }
                                                                                } else {
                                                                                    out.send(Message.prefix + Message.noLore(supply.getName()));
                                                                                }
                                                                            } else {
                                                                                out.send(Message.prefix + Message.loreGetUsage(arg));
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                out.send(Message.prefix + Message.notExists(name));
                                                            }
                                                        } else {
                                                            out.send(Message.prefix + Message.loreUsage(arg));
                                                        }
                                                    } else {
                                                        if (args[0].equals("info")) {
                                                            if (args.length == 2) {
                                                                String name = args[1];

                                                                ml.karmaconfigs.Supplies.Utils.Suministries.Suministry supply = new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry(name);

                                                                if (supply.exists()) {
                                                                    out.send(Message.info(supply));
                                                                } else {
                                                                    out.send(Message.prefix + Message.notExists(name));
                                                                }
                                                            } else {
                                                                out.send(Message.prefix + Message.infoUsage(arg));
                                                            }
                                                        } else {
                                                            if (args[0].equals("shop")) {
                                                                out.send("This command is for players only!", WarningLevel.ERROR);
                                                            } else {
                                                                if (args[0].equals("price")) {
                                                                    if (args.length == 3) {
                                                                        String name = args[1];

                                                                        ml.karmaconfigs.Supplies.Utils.Suministries.Suministry supply = new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry(name);

                                                                        if (supply.exists()) {
                                                                            try {
                                                                                double price;
                                                                                String val;
                                                                                if (!args[2].contains(".")) {
                                                                                    val = args[2] + ".0";
                                                                                } else {
                                                                                    val = args[2].split("\\.")[0] + "." + args[2].split("\\.")[1].substring(0, 1);
                                                                                }
                                                                                price = Double.parseDouble(val);

                                                                                supply.setPrice(price);
                                                                                out.send(Message.prefix + Message.priceSet(supply.getName(), price));
                                                                            } catch (NumberFormatException e) {
                                                                                out.send(Message.prefix + Message.priceUsage(arg));
                                                                            }
                                                                        } else {
                                                                            out.send(Message.prefix + Message.notExists(name));
                                                                        }
                                                                    } else {
                                                                        out.send(Message.prefix + Message.priceUsage(arg));
                                                                    }
                                                                } else {
                                                                    if (args[0].equals("block")) {
                                                                        if (args.length == 2) {
                                                                            String world = args[1];

                                                                            if (plugin.getServer().getWorld(world) != null) {
                                                                                World w = plugin.getServer().getWorld(world);
                                                                                if (Config.isAllowed(w)) {
                                                                                    Config.blockWorld(w);
                                                                                    out.send(Message.prefix + Message.blockedWorld(w));
                                                                                } else {
                                                                                    Config.unblockWorld(w);
                                                                                    out.send(Message.prefix + Message.unblocked(w));
                                                                                }
                                                                            } else {
                                                                                out.send(Message.prefix + Message.notExists);
                                                                            }
                                                                        } else {
                                                                            if (args.length == 3) {
                                                                                String world = args[1];
                                                                                String reg = args[2];

                                                                                if (plugin.getServer().getWorld(world) != null) {
                                                                                    World w = plugin.getServer().getWorld(world);
                                                                                    ProtectedRegion region = getRegion(reg);
                                                                                    if (region != null) {
                                                                                        System.out.println("Region not null");
                                                                                        if (Config.isAllowed(w, region)) {
                                                                                            Config.blockRegion(w, region);
                                                                                            out.send(Message.prefix + Message.blockedRegion(w, region));
                                                                                        } else {
                                                                                            Config.unblockRegion(w, region);
                                                                                            out.send(Message.prefix + Message.unblockedRegion(w, region));
                                                                                        }
                                                                                    } else {
                                                                                        System.out.println("Region is null");
                                                                                        out.send(Message.prefix + Message.notExists);
                                                                                    }
                                                                                } else {
                                                                                    out.send(Message.prefix + Message.notExists);
                                                                                }
                                                                            } else {
                                                                                out.send(Message.prefix + Message.blockUsage(arg));
                                                                            }
                                                                        }
                                                                    } else {
                                                                        out.send(StringUtils.line(10, ChatColor.WHITE));
                                                                        out.send(" ");
                                                                        out.send(name + " &f&l&m=&r " + version);
                                                                        out.send("&f/{0} &7help".replace("{0}", arg));
                                                                        out.send(" ");
                                                                        out.send(StringUtils.line(10, ChatColor.WHITE));
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
                            }
                        }
                    }
                } else {
                    out.send(StringUtils.line(10, ChatColor.WHITE));
                    out.send(" ");
                    out.send(name + " &f&l&m=&r " + version);
                    out.send("&f/{0} &7help".replace("{0}", arg));
                    out.send(" ");
                    out.send(StringUtils.line(10, ChatColor.WHITE));
                }
            }
        }
        return false;
    }

    /**
     * Get the actual player region
     *
     * @return a ProtectedRegion
     */
    public ProtectedRegion getRegion(String id) {
        ProtectedRegion region = null;
        try {
            RegionContainer container = WorldGuardPlugin.inst().getRegionContainer();
            for (World world : plugin.getServer().getWorlds()) {
                RegionManager manager = container.get(world);
                assert manager != null;

                for (ProtectedRegion reg : manager.getRegions().values()) {
                    if (reg != null) {
                        if (StringUtils.noColor(StringUtils.capitalize(reg.getId())).equals(StringUtils.noColor(StringUtils.capitalize(id)))) {
                            region = reg;
                            break;
                        }
                    }
                }
                if (region != null) {
                    break;
                }
            }
        } catch (Error e) {
            com.sk89q.worldguard.protection.regions.RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

            for (World world : plugin.getServer().getWorlds()) {
                for (ProtectedRegion reg : Objects.requireNonNull(container.get(BukkitAdapter.adapt(world))).getRegions().values()) {
                    if (reg != null) {
                        if (StringUtils.noColor(StringUtils.capitalize(reg.getId())).equals(StringUtils.noColor(StringUtils.capitalize(id)))) {
                            region = reg;
                            break;
                        }
                    }
                }
                if (region != null) {
                    break;
                }
            }
        }

        return region;
    }
}
