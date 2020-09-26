package ml.karmaconfigs.Supplies.Utils.InventoryMaker.Utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.Serializable;

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

/**
 * Created by McLive on 28.02.2019.
 */
public class Property implements Serializable {
    private String name;
    private String value;
    private String signature;

    boolean valuesFromJson(JsonObject obj) {
        if (obj.has("properties")) {
            JsonArray properties = obj.getAsJsonArray("properties");
            JsonObject propertiesObject = properties.get(0).getAsJsonObject();

            String signature = propertiesObject.get("signature").getAsString();
            String value = propertiesObject.get("value").getAsString();

            this.setSignature(signature);
            this.setValue(value);

            return true;
        }

        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
