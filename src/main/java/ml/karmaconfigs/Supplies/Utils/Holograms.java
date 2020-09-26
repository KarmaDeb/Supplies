package ml.karmaconfigs.Supplies.Utils;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import ml.karmaconfigs.Supplies.Suministry;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Beacon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

public class Holograms implements Suministry {

    private static final List<Hologram> holograms = new ArrayList<>();

    private static final HashMap<Beacon, Hologram> holos = new HashMap<>();

    private final Beacon beacon;
    private String text;
    private List<String> texts;

    public Holograms(Beacon beacon, List<String> text) {
        this.beacon = beacon;
        this.texts = text;
    }

    public Holograms(Beacon beacon, String text) {
        this.beacon = beacon;
        this.text = StringUtils.toColor(text);
    }

    public void createHologram(Location location) {
        Hologram hologram = HologramsAPI.createHologram(plugin, location);
        hologram.appendTextLine(text);

        holograms.add(hologram);
        holos.put(beacon, hologram);
    }

    public void updateText() {
        holograms.remove(holos.get(beacon));
        holos.get(beacon).delete();
        holos.remove(beacon);

        World w = beacon.getLocation().getWorld();
        double x = beacon.getLocation().getX() + 0.5;
        double y = beacon.getLocation().getY() + 1.65;
        double z = beacon.getLocation().getZ() + 0.5;

        Location loc = new Location(w, x, y, z);

        Hologram hologram = HologramsAPI.createHologram(plugin, loc);
        for (String str : texts) {
            hologram.appendTextLine(StringUtils.toColor(str));
        }

        holograms.add(hologram);
        holos.put(beacon, hologram);
    }

    public static Hologram getHologram(Beacon beacon) {
        return holos.get(beacon);
    }

    public static List<Hologram> getHolograms() {
        return holograms;
    }
}
