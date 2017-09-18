
package us.kulakov.lunch.api.places.schema.geocode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GeocodeResults {
    @SerializedName("results")
    @Expose
    private List<GeocodeResult> mGeocodeResults = new ArrayList<>();
    @SerializedName("status")
    @Expose
    private String status;

    public List<GeocodeResult> getResults() {
        return mGeocodeResults;
    }

    public String getStatus() {
        return status;
    }
}
