package ml.karmaconfigs.Supplies.Version;

import ml.karmaconfigs.Supplies.Suministry;
import ml.karmaconfigs.Supplies.Utils.Server.WarningLevel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
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
public class GetLatestVersion implements Suministry {

    public int latest;

    private String version = "";
    private String updateURL = "";

    private final List<String> replaced = new ArrayList<>();

    /**
     * Starts retrieving the info from the html file
     */
    public GetLatestVersion() {
        try {
            URL url = new URL("https://karmaconfigs.github.io/updates/Suministry/latest.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String word;
            List<String> lines = new ArrayList<>();
            while ((word = reader.readLine()) != null)
                if (!lines.contains(word)) {
                    lines.add(word);
                }
            reader.close();
            for (String str : lines) {
                if (!replaced.contains(str)) {
                    replaced.add(str
                            .replace("[", "{replace-one}")
                            .replace("]", "{replace-two}")
                            .replace(",", "{replace-comma}")
                            .replace("_", "&"));
                }
            }
            this.latest = Integer.parseInt(replaced.get(0).replaceAll("[aA-zZ]", "").replace(".", "").replace(" ", ""));
            this.version = replaced.get(0);
            this.updateURL = replaced.get(1);
        } catch (IOException ex) {
            out.send("Could not make a connection with the update system", WarningLevel.ERROR);
        }
    }

    /**
     * @return the latest version as integer
     */
    public int GetLatest() {
        return latest;
    }

    /**
     *
     * @return the latest version status (Beta - Alpha - Release) and his version int
     */
    public String getVersionString() {
        String url = version.replaceAll("[A-z]", "");
        String versionTxt = version.replaceAll("[0-9]", "").replace(".", "").replace(" ", "");
        if (!versionTxt.isEmpty()) {
            return versionTxt + " / " + url.replace(" ", "");
        } else {
            return url.replace(" ", "");
        }
    }

    /**
     * Get the update URL
     *
     * @return a String
     */
    public String getUpdateURL() {
        return updateURL;
    }

    /**
     * Send the list of changes to the Spigot's console
     */
    public void getChangelogSpigot() {
        for (int i = 0; i < replaced.size(); i++) {
            if (i == 0) {
                out.send("&b--------- &eChangeLog &6: &a{version} &b---------"
                        .replace("{version}", replaced.get(0)));
            } else {
                if (i != 1) {
                    out.send("&e" + replaced.get(i)
                            .replace("{replace-one}", "[")
                            .replace("{replace-two}", "]")
                            .replace("{replace-comma}", ","));
                }
            }
        }
    }
}
