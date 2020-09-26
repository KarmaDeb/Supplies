package ml.karmaconfigs.Supplies.Utils.InventoryMaker.Utils;

import ml.karmaconfigs.Supplies.Suministry;

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

public class SkinData implements Suministry {

   private final MojangAPI getMojangAPI = new MojangAPI();

   /**
    * This methods seeks out players actual skin (chosen or not) and returns
    * either null (if no skin data found) or the property object conatining all
    * the skin data.
    * <p>
    * Also, it schedules a skin update to stay up to date with skin changes.
    *
    * @param name   Player name to search skin for
    * @return Property    Platform specific Property Object
    **/
   public Object createSkinData(String name) {
      return getMojangAPI.getSkinProperty(getMojangAPI.getUUID(name));
   }
}
