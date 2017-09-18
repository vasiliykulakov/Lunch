
package us.kulakov.lunch.api.places.schema;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OpeningHours {

    @SerializedName("open_now")
    @Expose
    private Boolean openNow;
    @SerializedName("weekday_text")
    @Expose
    private List<String> weekdayText = null;

    public Boolean getOpenNow() {
        return openNow;
    }

    public List<String> getWeekdayText() {
        return weekdayText;
    }

}
