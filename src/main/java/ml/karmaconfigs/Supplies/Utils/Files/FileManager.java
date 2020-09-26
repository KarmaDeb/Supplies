package ml.karmaconfigs.Supplies.Utils.Files;

import ml.karmaconfigs.Supplies.Suministry;
import ml.karmaconfigs.Supplies.Utils.Server.WarningLevel;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
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
@SuppressWarnings("unused")
public class FileManager implements Suministry {

    private final File managed;
    private final YamlConfiguration file;

    /**
     * Starts the file manager
     *
     * @param fileName the file name
     */
    public FileManager(String fileName) {
        this.managed = new File(plugin.getDataFolder(), fileName);
        if (!managed.exists()) {
            FileCreator creator = new FileCreator(fileName, false);
            out.send("The file " + fileName + " not exists, one have been created", WarningLevel.WARNING);
            creator.createFile();
            creator.setDefaults();
            creator.saveFile();
        }
        this.file = YamlConfiguration.loadConfiguration(managed);
    }

    /**
     * Starts the file manager
     *
     * @param fileName the file name
     * @param fileDir the file directory
     */
    public FileManager(String fileName, String fileDir) {
        this.managed = new File(plugin.getDataFolder() + "/" + fileDir, fileName);
        if (!managed.exists()) {
            FileCreator creator = new FileCreator(fileName, fileDir, false);
            out.send("The file " + fileDir + "/" + fileName + " not exists, one have been created", WarningLevel.WARNING);
            creator.createFile();
            creator.setDefaults();
            creator.saveFile();
        }
        this.file = YamlConfiguration.loadConfiguration(managed);
    }

    /**
     * Gets the managed file
     *
     * @return file
     */
    public File getManaged() {
        return managed;
    }

    /**
     * Gets the managed file configuration
     *
     * @return YamlConfiguration format file configuration
     */
    public YamlConfiguration getFile() {
        return file;
    }

    /**
     * Set a path with no info
     *
     * @param path the path
     */
    public void set(String path) {
        file.set(path, "");
        save();
    }

    /**
     * Set a path value as object
     *
     * @param path the path
     * @param value the value
     */
    public void set(String path, Object value) {
        file.set(path, value);
        save();
    }

    /**
     * Set a path value as object
     *
     * @param path the path
     * @param value the value
     */
    public void set(String path, String value) {
        file.set(path, value);
        save();
    }

    /**
     * Set a path value as a string list
     *
     * @param path the path
     * @param value the value
     */
    public void set(String path, List<String> value) {
        file.set(path, value);
        save();
    }

    /**
     * Set a path value as boolean
     *
     * @param path the path
     * @param value the value
     */
    public void set(String path, Boolean value) {
        file.set(path, value);
        save();
    }

    /**
     * Set a path value as integer
     *
     * @param path the path
     * @param value the value
     */
    public void set(String path, Integer value) {
        file.set(path, value);
        save();
    }

    /**
     * Set a path value as double
     *
     * @param path the path
     * @param value the value
     */
    public void set(String path, Double value) {
        file.set(path, value);
        save();
    }

    /**
     * Set a path value as float
     *
     * @param path the path
     * @param value the value
     */
    public void set(String path, Float value) {
        file.set(path, value);
        save();
    }

    /**
     * Removes a path
     *
     * @param path the path
     */
    public void unset(String path) {
        file.set(path, null);
        save();
    }

    /**
     * Check if the path is
     * empty
     *
     * @param path the path
     * @return a boolean
     */
    public boolean isEmpty(String path) {
        if (isSet(path)) {
            return get(path).toString().isEmpty();
        } else {
            return true;
        }
    }

    /**
     * Check if the path is
     * set
     *
     * @param path the path
     * @return a boolean
     */
    public boolean isSet(String path) {
        return get(path) != null;
    }

    /**
     * Gets the value of a path
     *
     * @param path the path
     * @return object
     */
    public Object get(String path) {
        return file.get(path);
    }

    /**
     * Gets the value of a path
     *
     * @param path the path
     * @return string
     */
    public String getString(String path) {
        return file.getString(path);
    }

    /**
     * Gets the value of a path
     *
     * @param path the path
     * @return list of strings
     */
    public List<String> getList(String path) {
        return file.getStringList(path);
    }

    /**
     * Gets the value of a path
     *
     * @param path the path
     * @return boolean
     */
    public Boolean getBoolean(String path) {
        return file.getBoolean(path);
    }

    /**
     * Gets the value of a path
     *
     * @param path the path
     * @return integer
     */
    public Integer getInt(String path) {
        return file.getInt(path);
    }

    /**
     * Gets the value of a path
     *
     * @param path the path
     * @return double
     */
    public Double getDouble(String path) {
        return file.getDouble(path);
    }

    /**
     * Gets the value of a path
     *
     * @param path the path
     * @return float
     */
    public Float getFloat(String path) {
        return (float)file.getDouble(path);
    }

    public void delete() {
        if (managed.delete()) {
            out.send("The file " + managed.getName() + " have been removed", WarningLevel.WARNING);
        } else {
            out.send("The file " + managed.getName() + " couldn't be removed", WarningLevel.ERROR);
        }
    }

    public void save() {
        try {
            file.save(managed);
        } catch (Exception e) {
            out.send("An error occurred while trying to save file " + managed.getName(), WarningLevel.ERROR);
            out.send("&c" + e.fillInStackTrace());
        }
    }
}
