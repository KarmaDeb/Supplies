package ml.karmaconfigs.Supplies.Utils;

import ml.karmaconfigs.Supplies.Suministry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Private GSA code
 *
 * The use of this code
 * without GSA team authorization
 * will be a violation of
 * terms of use determined
 * in <a href="https://karmaconfigs.ml/license/"> here </a>
 */
public class BeaconChest implements Suministry {

    private final Beacon beacon;
    private static final HashMap<Beacon, Inventory> beaconChest = new HashMap<>();

    public BeaconChest(Beacon beacon) {
        this.beacon = beacon;
        beaconChest.put(beacon, plugin.getServer().createInventory(null, 27));
    }

    public void setInventory(ItemStack[] items, double percentage) {
        ItemStack barrier = new ItemStack(Material.BARRIER, 1);
        ItemMeta barrierMeta = barrier.getItemMeta();

        barrierMeta.setDisplayName(StringUtils.toColor("&cREMOVE"));

        barrier.setItemMeta(barrierMeta);

        Inventory inventory = plugin.getServer().createInventory(null, 27);

        List<ItemStack> stacks = new ArrayList<>();

        for (ItemStack stack : items) {
            if (stack != null) {
                if (!stack.getType().equals(Material.AIR)) {
                    stacks.add(stack);
                } else {
                    stacks.add(new ItemStack(Material.AIR));
                }
            } else {
                stacks.add(new ItemStack(Material.AIR));
            }
        }

        for (ItemStack item : items) {
            if (item != null) {
                if (!item.isSimilar(barrier)) {
                    if (percentage == 100.0) {
                        inventory.addItem(item);
                    } else {
                        if (!isEmpty(inventory)) {
                            double random = new Random().nextInt(100);
                            if (random <= percentage) {
                                inventory.addItem(item);
                            }
                        } else {
                            int size = stacks.size();
                            int random = new Random().nextInt(size);
                            if (stacks.get(random) != null) {
                                if (!stacks.get(random).isSimilar(barrier)) {
                                    if (!stacks.get(random).getType().equals(Material.AIR)) {
                                        inventory.addItem(stacks.get(random));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        beaconChest.put(beacon, inventory);
    }

    public static Inventory getInventory(Beacon beacon) {
        return beaconChest.get(beacon);
    }

    public Location getLocation() {
        return beacon.getLocation();
    }

    public Beacon getChest() {
        return beacon;
    }

    private boolean isEmpty(Inventory inv) {
        boolean empty = true;
        for (ItemStack item : inv.getContents()) {
            if (item != null) {
                if (!item.getType().equals(Material.AIR)) {
                    empty = false;
                }
            }
        }
        return empty;
    }
}
