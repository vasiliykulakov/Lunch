
package us.kulakov.lunch.api.places.schema.geocode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import us.kulakov.lunch.api.places.schema.Location;

public class Geometry {
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("location_type")
    @Expose
    private String locationType;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }
}
