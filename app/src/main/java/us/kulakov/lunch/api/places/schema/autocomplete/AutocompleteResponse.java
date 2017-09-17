
package us.kulakov.lunch.api.places.schema.autocomplete;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AutocompleteResponse {

    @SerializedName("predictions")
    @Expose
    private List<Prediction> predictions = new ArrayList<>();
    @SerializedName("status")
    @Expose
    private String status;

    public List<Prediction> getPredictions() {
        return predictions;
    }

    public String getStatus() {
        return status;
    }

    public AutocompleteResponse(List<Prediction> predictions, String status) {
        this.predictions = predictions;
        this.status = status;
    }
}
