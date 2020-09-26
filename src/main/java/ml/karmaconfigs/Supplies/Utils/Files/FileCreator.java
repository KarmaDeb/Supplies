package ml.karmaconfigs.Supplies.Utils.Files;

import ml.karmaconfigs.Supplies.Suministry;
import ml.karmaconfigs.Supplies.Utils.Server.WarningLevel;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
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
public class FileCreator implements Suministry {

    private final File folder;
    private final File file;
    private YamlConfiguration config;
    private YamlConfiguration cfg;

    private final boolean isResource;

    private boolean isInt(Object path) {
        try {
            Integer.parseInt(String.valueOf(path));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean isBoolean(Object path) {
        return String.valueOf(path).equalsIgnoreCase("true") ||
                String.valueOf(path).equalsIgnoreCase("false");
    }

    /**
     * Starts the file creator
     *
     * @param fileName the file name
     * @param fileDir the file dir
     * @param isResource if the file is inside the plugin itself
     */
    public FileCreator(String fileName, String fileDir, boolean isResource) {
        this.isResource = isResource;
        if (isResource) {
            InputStream theFile = (plugin).getClass().getResourceAsStream("/" + fileName);
            InputStreamReader DF = new InputStreamReader(theFile);
            this.cfg = YamlConfiguration.loadConfiguration(DF);
        }

        this.file = new File( plugin.getDataFolder() + File.separator + fileDir, fileName);
        this.folder = new File(plugin.getDataFolder() + File.separator + fileDir);
    }

    /**
     * Starts the file creator
     *
     * @param fileName the file name
     * @param isResource if the file is inside the plugin itself
     */
    public FileCreator(String fileName, boolean isResource) {
        this.isResource = isResource;
        if (isResource) {
            InputStream theFile = (plugin).getClass().getResourceAsStream("/" + fileName);
            InputStreamReader DF = new InputStreamReader(theFile);
            this.cfg = YamlConfiguration.loadConfiguration(DF);
        }

        this.file = new File( plugin.getDataFolder(), fileName);
        this.folder = plugin.getDataFolder();
    }

    /**
     * Starts the file creator
     *
     * @param fileName the file name
     * @param resourceFile if the resource file is custom
     */
    public FileCreator(String fileName, String resourceFile) {
        InputStream theFile = (plugin).getClass().getResourceAsStream("/" + resourceFile);
        InputStreamReader DF = new InputStreamReader(theFile);
        this.cfg = YamlConfiguration.loadConfiguration(DF);
        this.isResource = true;
        this.file = new File( plugin.getDataFolder(), fileName);
        this.folder = plugin.getDataFolder();
    }

    /**
     * Starts the file creator
     *
     * @param fileName the file name
     * @param fileDir the file dir
     * @param resourceFile if the resource file is custom
     */
    public FileCreator(String fileName, String fileDir, String resourceFile) {
        InputStream theFile = (plugin).getClass().getResourceAsStream("/" + resourceFile);
        InputStreamReader DF = new InputStreamReader(theFile);
        this.cfg = YamlConfiguration.loadConfiguration(DF);
        this.isResource = true;

        this.file = new File( plugin.getDataFolder() + "/" + fileDir, fileName);
        this.folder = new File(plugin.getDataFolder() + "/" + fileDir);
    }

    /**
     * Create the file and the folder
     * if not exists
     */
    public void createFile() {
        if (!folder.exists()) {
            if (folder.mkdir()) {
                out.send("The folder " + folder.getName() + " didn't exist and one have been created", WarningLevel.WARNING);
            } else {
                out.send("The plugin tried to create the folder " + folder.getName() + " but an error occurred", WarningLevel.ERROR);
            }
        }
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    out.send("The plugin tried to create the folder " + file.getName() + " but an error occurred", WarningLevel.ERROR);
                } else {
                    out.send("The file " + folder.getName() + " didn't exist and have been created", WarningLevel.WARNING);
                }
            } catch (Exception e) {
                out.send("An error occurred while trying to create file " + file.getName(), WarningLevel.ERROR);
                out.send("&c" + e.fillInStackTrace());
            }
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Set the defaults for the file
     * reading the internal file
     */
    public void setDefaults() {
        if (isResource) {
            List<String> sections = new ArrayList<>();

            for (String path : config.getKeys(false)) {
                if (cfg.get(path) == null) {
                    config.set(path, null);
                }
            }

            for (String path : cfg.getKeys(false)) {
                if (config.get(path) == null) {
                    config.set(path, cfg.get(path));
                } else {
                    if (isBoolean(cfg.get(path))) {
                        if (!isBoolean(config.get(path))) {
                            config.set(path, cfg.get(path));
                        }
                    }
                    if (isInt(cfg.get(path))) {
                        if (!isInt(config.get(path))) {
                            config.set(path, cfg.get(path));
                        }
                    }
                    if (!isBoolean(cfg.get(path)) && !isInt(cfg.get(path))) {
                        if (isInt(config.get(path)) || isBoolean(config.get(path))) {
                            config.set(path, cfg.get(path));
                        }
                    }
                }
                if (config.getConfigurationSection(path) != null) {
                    sections.add(path);
                }
            }
            if (!sections.isEmpty()) {
                for (String section : sections) {
                    for (String str : cfg.getConfigurationSection(section).getKeys(false)) {
                        String path = section + "." + str;
                        if (config.get(path) == null) {
                            config.set(path, cfg.get(path));
                        } else {
                            if (isBoolean(cfg.get(path))) {
                                if (!isBoolean(config.get(path))) {
                                    config.set(path, cfg.get(path));
                                }
                            }
                            if (isInt(cfg.get(path))) {
                                if (!isInt(config.get(path))) {
                                    config.set(path, cfg.get(path));
                                }
                            }
                            if (!isBoolean(cfg.get(path)) && !isInt(cfg.get(path))) {
                                if (isInt(config.get(path)) || isBoolean(config.get(path))) {
                                    config.set(path, cfg.get(path));
                                }
                            }
                        }
                    }
                }
                for (String section : sections) {
                    for (String str : config.getConfigurationSection(section).getKeys(false)) {
                        String path = section + "." + str;
                        if (cfg.get(path) == null) {
                            config.set(path, null);
                        }
                    }
                }
            }
        }
    }

    /**
     * Save the file
     */
    public void saveFile() {
        try {
            config.save(file);
        } catch (Exception e) {
            out.send("An error occurred while trying to save the file " + file.getName(), WarningLevel.ERROR);
            out.send("&c" + e.fillInStackTrace());
        }
    }

    /**
     * Check if the file exists
     *
     * @return a boolean
     */
    public boolean exists() {
        return file.exists();
    }

    /**
     * Reloads the file
     * <code>No longer used</code>
     */
    @Deprecated
    public void reloadFile() {
        out.send("reloadFile() method is deprecated, because its not longer used", WarningLevel.WARNING);
        this.config = YamlConfiguration.loadConfiguration(file);
    }
}
