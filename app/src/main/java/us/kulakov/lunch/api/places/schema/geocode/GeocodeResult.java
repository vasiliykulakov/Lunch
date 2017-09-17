
package us.kulakov.lunch.api.places.schema.geocode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeocodeResult {
    @SerializedName("geometry")
    @Expose
    private Geometry geometry;
    @SerializedName("place_id")
    @Expose
    private String placeId;
    @SerializedName("formatted_address")
    @Expose
    private String formattedAddress;

    public Geometry getGeometry() {
        return geometry;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }
}
