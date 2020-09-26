package ml.karmaconfigs.Supplies.Utils;

import org.bukkit.ChatColor;

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
public interface StringUtils {

    static String toColor(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    static List<String> toColor(List<String> texts) {
        for (int i = 0; i < texts.size(); i++) {
            texts.set(i, toColor(texts.get(i)));
        }

        return texts;
    }

    static String noColor(String text) {
        return ChatColor.stripColor(toColor(text));
    }

    static String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }

    static List<String> capitalize(List<String> strings) {
        List<String> result = new ArrayList<>();
        for (String str : strings) {
            if (!result.contains(capitalize(str))) {
                result.add(capitalize(str));
            }
        }
        return result;
    }

    static String line(int length, ChatColor color) {
        if (length == 0) length = 7;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(ChatColor.BOLD).append(color).append("-");
        }

        return builder.toString();
    }
}
