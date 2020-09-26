package ml.karmaconfigs.Supplies.Utils.Suministries;

import ml.karmaconfigs.Supplies.Utils.Server.WarningLevel;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
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
public class LoreManager implements ml.karmaconfigs.Supplies.Suministry {

    private final File file;
    private final YamlConfiguration manager;

    /**
     * Initialize the lore manager
     * for that supply
     *
     * @param supply the supply
     */
    public LoreManager(Suministry supply) {
        this.file = new File(plugin.getDataFolder() + "/chests", supply.getFileName(true));
        this.manager = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Add a lore line to the
     * supply torch
     *
     * @param str the line
     */
    public void addLore(String str) {
        List<String> lore = new ArrayList<>();
        if (manager.isSet("Suministry.Lore")) {
            lore.addAll(manager.getStringList("Suministry.Lore"));
        }
        lore.add(str);

        manager.set("Suministry.Lore", lore);

        try {
            manager.save(file);
        } catch (Exception e) {
            out.send("An error occurred while saving suministry " + file.getName().replace(".yml", ""), WarningLevel.ERROR);
            out.send("&c" + e.fillInStackTrace());
        }
    }

    /**
     * Set the lore line
     *
     * @param line the line
     * @param str the text
     * @return a boolean
     */
    public boolean setLine(int line, String str) {
        try {
            List<String> lore = getLore();
            if (lore.get(line) != null) {
                if (!lore.get(line).isEmpty()) {
                    if (!str.equals("null")) {
                        lore.set(line, str);
                    } else {
                        lore.remove(line);
                    }

                    manager.set("Suministry.Lore", lore);

                    try {
                        manager.save(file);
                    } catch (Exception e) {
                        out.send("An error occurred while saving suministry " + file.getName().replace(".yml", ""), WarningLevel.ERROR);
                        out.send("&c" + e.fillInStackTrace());
                    }
                    return true;
                }
            }
            return false;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Check if the lore has the
     * specified line
     *
     * @param line the line
     * @return a boolean
     */
    public boolean hasLine(int line) {
        try {
            if (!getLore().isEmpty()) {
                if (getLore().size() > line) {
                    if (getLore().get(line) != null) {
                        return !getLore().get(line).isEmpty();
                    }
                }
            }
            return false;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Get the lore line
     *
     * @param line the line
     * @return a boolean/string
     */
    public String getLine(int line) {
        if (!getLore().isEmpty()) {
            if (getLore().get(line) != null) {
                if (!getLore().get(line).isEmpty()) {
                    return getLore().get(line);
                }
            }
        }
        return "{empty}";
    }

    /**
     * Get the supply torch lore
     *
     * @return a list of Strings
     */
    public List<String> getLore() {
        if (manager.isSet("Suministry.Lore")) {
            List<String> replaced = new ArrayList<>();
            for (String str : manager.getStringList("Suministry.Lore")) {
                if (!replaced.contains(str)) {
                    if (!str.equals(" ")) {
                        replaced.add(str.replace("[", "{open}")
                                .replace("]", "{close}")
                                .replace(",", "{comma}"));
                    } else {
                        replaced.add("{space}");
                    }
                }
            }
            return replaced;
        } else {
            return Collections.emptyList();
        }
    }
}
