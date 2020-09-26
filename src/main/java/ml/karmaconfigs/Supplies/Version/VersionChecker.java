package ml.karmaconfigs.Supplies.Version;

import ml.karmaconfigs.Supplies.Suministry;
import ml.karmaconfigs.Supplies.Utils.Server.WarningLevel;

/**
 * Private GSA code
 *
 * The use of this code
 * without GSA team authorization
 * will be a violation of
 * terms of use determined
 * in <a href="https://karmaconfigs.ml/license/"> here </a>
 */
public class VersionChecker implements Suministry {

    private final int actual;
    private final int latest;

    /**
     * Starts the version checker
     */
    public VersionChecker() {
        String version = plugin.getDescription().getVersion().replaceAll("[aA-zZ]", "").replace(".", "");
        this.actual = Integer.parseInt(version.replace(" ", ""));
        this.latest = new GetLatestVersion().GetLatest();
    }

    /**
     *
     * @return if the plugin is outdated
     */
    private boolean isOutdated() {
        if (actual != latest) {
            return actual <= latest;
        } else {
            return false;
        }
    }

    /**
     * Execute the version check task
     */
    public void checkVersion() {
        if (isOutdated()) {
            out.send("New version available for Supplies &f( &b" + new GetLatestVersion().getVersionString() + " &f)", WarningLevel.WARNING);
            out.send("&bDownload the latest version from " + new GetLatestVersion().getUpdateURL());
            new GetLatestVersion().getChangelogSpigot();
        } else {
            out.send("&6Supplies &7>> &aYou are using the latest version of Supplies &f( &b" + new GetLatestVersion().getVersionString() + " &f)");
        }
    }
}
