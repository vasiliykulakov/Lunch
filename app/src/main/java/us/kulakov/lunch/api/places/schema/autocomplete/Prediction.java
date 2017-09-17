
package us.kulakov.lunch.api.places.schema.autocomplete;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Prediction {

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("place_id")
    @Expose
    private String placeId;

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getPlaceId() {
        return placeId;
    }

    public Prediction(String description, String id, String placeId) {
        this.description = description;
        this.id = id;
        this.placeId = placeId;
    }
}
