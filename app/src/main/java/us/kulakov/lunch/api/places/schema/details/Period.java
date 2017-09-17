
package us.kulakov.lunch.api.places.schema.details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Period {

    @SerializedName("close")
    @Expose
    private OpenCloseTime close;
    @SerializedName("open")
    @Expose
    private OpenCloseTime open;

    public OpenCloseTime getClose() {
        return close;
    }

    public OpenCloseTime getOpen() {
        return open;
    }

}
