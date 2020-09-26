package ml.karmaconfigs.Supplies.Utils.Server;

import ml.karmaconfigs.Supplies.Suministry;
import ml.karmaconfigs.Supplies.Utils.StringUtils;

/**
 * Private GSA code
 *
 * The use of this code
 * without GSA team authorization
 * will be a violation of
 * terms of use determined
 * in <a href="https://karmaconfigs.ml/license/"> here </a>
 */
public class Console implements Suministry {

    /**
     * Send a message to the
     * plugin server
     */
    public void send(String message) {
        plugin.getServer().getConsoleSender().sendMessage(StringUtils.toColor(message)
                .replace("{0}", name)
                .replace("{1}", version));
    }

    /**
     * Send an alert to the plugin console
     *
     * @param message the message
     * @param level the warning level
     */
    public void send(String message, WarningLevel level) {
        switch (level) {
            case ERROR:
                send("&f&l&o( &cSupplies &f&l&o) &4ERROR&7: &c" + message);
                break;
            case WARNING:
                send("&f&l&o( &cSupplies &f&l&o) &6WARNING&7: &e" + message);
                break;
        }
    }
}
