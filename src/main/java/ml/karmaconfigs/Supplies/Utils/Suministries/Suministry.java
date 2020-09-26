package ml.karmaconfigs.Supplies.Utils.Suministries;

import me.zombie_striker.qg.api.QualityArmory;
import ml.karmaconfigs.Supplies.Utils.Server.WarningLevel;
import ml.karmaconfigs.Supplies.Utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Private GSA code
 *
 * The use of this code
 * without GSA team authorization
 * will be a violation of
 * terms of use determined
 * in <a href="https://karmaconfigs.ml/license/"> here </a>
 */
public class Suministry implements ml.karmaconfigs.Supplies.Suministry {

    private File file;
    private YamlConfiguration manager;

    public Suministry() {
        File folder = new File(plugin.getDataFolder() + "/chests");
        if (!folder.exists()) {
            if (folder.mkdir()) {
                out.send("&aSuministries folder &f/Suministry/chests &a have been created successfully");
            } else {
                out.send("An error occurred while saving suministries folder &f/Suministry/chests", WarningLevel.ERROR);
            }
        }
        Suministry suministry;
        for (Suministry ministry : getSuministries()) {
            suministry = ministry;
            this.file = new File(plugin.getDataFolder() + "/chests", suministry.getName() + ".yml");
            this.manager = YamlConfiguration.loadConfiguration(file);
            break;
        }
    }

    public Suministry(String name) {
        File folder = new File(plugin.getDataFolder() + "/chests");
        if (!folder.exists()) {
            if (folder.mkdir()) {
                out.send("&aSuministries folder &f/Suministry/chests &a have been created successfully");
            } else {
                out.send("An error occurred while saving suministries folder &f/Suministry/chests", WarningLevel.ERROR);
            }
        }
        this.file = new File(plugin.getDataFolder() + "/chests", /*ChatColor.stripColor(StringUtils.toColor(name))*/ name + ".yml");
        this.manager = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Get the suministry name
     */
    public String getName() {
        return manager.getString("Suministry.Name");
    }

    /**
     * Get the file name with the name extension
     * if specified
     *
     * @param includeExtension true/false
     *
     * @return a String
     */
    public String getFileName(boolean includeExtension) {
        if (includeExtension) {
            return file.getName();
        } else {
            return file.getName().replace(".yml", "");
        }
    }

    /**
     * Get the supply percentage, by default: 100%
     *
     * @return a double
     */
    public double getPercentage() {
        if (manager.isSet("Suministry.Percentage")) {
            return manager.getDouble("Suministry.Percentage");
        } else {
            return 100.0;
        }
    }

    /**
     * Get the supply price
     *
     * @return a double
     */
    public double getPrice() {
        if (manager.isSet("Suministry.Price")) {
            return manager.getDouble("Suministry.Price");
        } else {
            return 500.0;
        }
    }

    /**
     * Try to set the suministry name
     *
     * @param name the name
     * @return a boolean
     */
    public boolean setName(String name) {
        if (!manager.isSet("Suministry.NameHistory")) {
            List<String> history = new ArrayList<>();
            history.add(getName());
            history.add(name);
            manager.set("Suministry.NameHistory", history);
        }
        List<String> nameHistory = manager.getStringList("Suministry.NameHistory");
        if (!nameHistory.contains(getName())) {
            nameHistory.add(getName());
            manager.set("Suministry.NameHistory", nameHistory);
        }
        manager.set("Suministry.Name", name);
        try {
            manager.save(file);
            return true;
        } catch (Exception e) {
            out.send("An error occurred while saving suministry " + file.getName().replace(".yml", ""), WarningLevel.ERROR);
            out.send("&c" + e.fillInStackTrace());
            return false;
        }
    }

    /**
     * Set the suministry contents
     **/
    public boolean setContents(Inventory inventory) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeInt(inventory.getSize());

            ItemStack barrier = new ItemStack(Material.BARRIER, 1);
            ItemMeta barrierMeta = barrier.getItemMeta();

            barrierMeta.setDisplayName(StringUtils.toColor("&cREMOVE"));

            barrier.setItemMeta(barrierMeta);

            if (ml.karmaconfigs.Supplies.Suministry.hasQualityArmor()) {
                for (int i = 0; i < inventory.getSize(); i++) {
                    if (inventory.getItem(i) == null || inventory.getItem(i).getType().equals(Material.AIR))
                        inventory.setItem(i, barrier);
                    if (QualityArmory.isCustomItem(inventory.getItem(i))) {
                        dataOutput.writeObject(QualityArmory.getCustomItemAsItemStack(QualityArmory.getCustomItem(inventory.getItem(i))));
                    } else {
                        dataOutput.writeObject(inventory.getItem(i));
                    }
                }
            } else {
                for (int i = 0; i < inventory.getSize(); i++) {
                    if (inventory.getItem(i) == null || inventory.getItem(i).getType().equals(Material.AIR))
                        inventory.setItem(i, barrier);
                    dataOutput.writeObject(inventory.getItem(i));
                }
            }

            dataOutput.close();
            String inv = Base64Coder.encodeLines(outputStream.toByteArray());

            manager.set("Suministry.Contents", inv);
            return true;
        } catch (Exception e) {
            out.send("An error occurred while saving suministry contents", WarningLevel.ERROR);
            out.send("&c" + e.fillInStackTrace());
            return false;
        }
    }

    /**
     * Check if the suministry exists
     *
     * @return a boolean
     */
    public boolean exists() {
        return file.exists();
    }

    /**
     * Check if the grenade is actually
     * a grenade
     *
     * @param grenade the ItemStack grenade
     * @return a boolean
     */
    public boolean isGrenade(ItemStack grenade) {
        boolean isGrenade = false;
        if (grenade.hasItemMeta()) {
            if (grenade.getItemMeta().hasDisplayName()) {
                if (!StringUtils.toColor(getName()).equals(StringUtils.toColor(grenade.getItemMeta().getDisplayName()))) {
                    for (String str : getNameHistory()) {
                        if (StringUtils.toColor(str).equals(StringUtils.toColor(grenade.getItemMeta().getDisplayName()))) {
                            isGrenade = true;
                            break;
                        }
                    }
                } else {
                    isGrenade = true;
                }
            }
        }
        return isGrenade;
    }

    /**
     * Check if the grenade is actually
     * a grenade
     *
     * @param grenade the ItemStack grenade
     * @return a boolean
     */
    public boolean isGrenade(String grenade) {
        boolean isGrenade = false;
        if (!StringUtils.toColor(getName()).equals(grenade)) {
            for (String str : getNameHistory()) {
                if (StringUtils.toColor(str).equals(StringUtils.toColor(grenade))) {
                    isGrenade = true;
                    break;
                }
            }
        } else {
            isGrenade = true;
        }
        return isGrenade;
    }

    /**
     * Get the suministry contents
     *
     * @return a char of ItemStacks
     */
    public ItemStack[] getContents() {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(manager.getString("Suministry.Contents")));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());

            if (ml.karmaconfigs.Supplies.Suministry.hasQualityArmor()) {
                for (int i = 0; i < inventory.getSize(); i++) {
                    ItemStack item = (ItemStack) dataInput.readObject();
                    if (item != null) {
                        if (!QualityArmory.isCustomItem(item)) {
                            inventory.setItem(i, item);
                        } else {
                            inventory.setItem(i, QualityArmory.getCustomItemAsItemStack(QualityArmory.getCustomItem(item)));
                        }
                    }
                }
            } else {
                for (int i = 0; i < inventory.getSize(); i++) {
                    inventory.setItem(i, (ItemStack) dataInput.readObject());
                }
            }
            dataInput.close();

            ItemStack[] contents = new ItemStack[inventory.getSize() + 1];
            for (int i = 0 ; i < inventory.getSize(); i++) {
                contents[i] = inventory.getItem(i);
            }
            return contents;
        } catch (Exception e) {
            out.send("An error occurred while getting suministry contents", WarningLevel.ERROR);
            out.send("&c" + e.fillInStackTrace());
            return null;
        }
    }

    /**
     * Get all the suministries available
     *
     * @return a Suministry
     */
    public List<Suministry> getSuministries() {
        List<Suministry> suministries = new ArrayList<>();
        File file = new File(plugin.getDataFolder() + "/chests");
        if (file.listFiles() != null) {
            File[] files = file.listFiles();
            assert files != null;
            for (File fl : files) {
                Suministry suministry = new Suministry(fl.getName().replace(".yml", ""));
                if (suministry.exists()) {
                    if (!suministries.contains(suministry)) {
                        suministries.add(suministry);
                    }
                }
            }
        }
        return suministries;
    }

    /**
     * Get the history name of the
     * suministry
     *
     * @return a list of strings
     */
    public List<String> getNameHistory() {
        if (!manager.isSet("Suministry.NameHistory")) {
            List<String> history = new ArrayList<>();
            history.add(getName());
            history.add(name);
            manager.set("Suministry.NameHistory", history);

            try {
                manager.save(file);
            } catch (Exception e) {
                out.send("An error occurred while saving suministry " + file.getName().replace(".yml", ""), WarningLevel.ERROR);
                out.send("&c" + e.fillInStackTrace());
            }
        }
        return manager.getStringList("Suministry.NameHistory");
    }

    /**
     * Save the suministry
     */
    public boolean saveAs(String name) {
        if (!manager.isSet("Suministry.Name")) {
            manager.set("Suministry.Name", name);
        }
        if (!manager.isSet("Suministry.Percentage")) {
            manager.set("Suministry.Percentage", 100.0);
        }
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    out.send("&aSuministry file &f" + file.getName().replace(".yml", "") + "&a have been saved successfully");
                } else {
                    out.send("An error occurred while saving suministry " + file.getName().replace(".yml", ""), WarningLevel.ERROR);
                }
            } catch (Exception e) {
                out.send("An error occurred while saving suministry " + file.getName().replace(".yml", ""), WarningLevel.ERROR);
                out.send("&c" + e.fillInStackTrace());
                return false;
            }
        }
        try {
            manager.save(file);
            return true;
        } catch (Exception e) {
            out.send("An error occurred while saving suministry " + file.getName().replace(".yml", ""), WarningLevel.ERROR);
            out.send("&c" + e.fillInStackTrace());
            return false;
        }
    }

    /**
     * Remove the suministry
     */
    public boolean remove() {
        return file.delete();
    }

    /**
     * Get the supply lore manager
     *
     * @return a LoreManager
     */
    public LoreManager getLoreManager() {
        return new LoreManager(this);
    }

    /**
     * Get a supply grenade
     *
     * @param amount the grenade amounts
     * @return a ItemStack
     */
    public ItemStack getGrenade(int amount) {
        ItemStack grenade = new ItemStack(Material.LEGACY_REDSTONE_TORCH_ON, amount);
        ItemMeta grenadeMeta = grenade.getItemMeta();

        grenadeMeta.setDisplayName(StringUtils.toColor(getName()));

        if (getLoreManager().getLore() != null) {
            if (!getLoreManager().getLore().isEmpty()) {
                List<String> lre = new ArrayList<>();

                for (String str : getLoreManager().getLore()) {
                    if (!lre.contains(str)) {
                        lre.add(StringUtils.toColor(str));
                    }
                }

                grenadeMeta.setLore(lre);
            }
        }

        grenade.setItemMeta(grenadeMeta);

        return grenade;
    }

    /**
     * Set the supply percentage
     *
     * @param per the percentage
     */
    public void setPercentage(double per) {
        manager.set("Suministry.Percentage", per);
        try {
            manager.save(file);
        } catch (Exception e) {
            out.send("An error occurred while saving suministry " + file.getName().replace(".yml", ""), WarningLevel.ERROR);
            out.send("&c" + e.fillInStackTrace());
        }
    }

    /**
     * Set the supply price
     *
     * @param price the price
     */
    public void setPrice(double price) {
        manager.set("Suministry.Price", price);
        try {
            manager.save(file);
        } catch (Exception e) {
            out.send("An error occurred while saving suministry " + file.getName().replace(".yml", ""), WarningLevel.ERROR);
            out.send("&c" + e.fillInStackTrace());
        }
    }
}
