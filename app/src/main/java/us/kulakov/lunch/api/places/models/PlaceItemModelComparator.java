package us.kulakov.lunch.api.places.models;

import javax.inject.Inject;

public class PlaceItemModelComparator {
    @Inject
    public PlaceItemModelComparator() {
    }

    public Integer compareByRank(PlaceSearchItemModel left, PlaceSearchItemModel right) {
        return Float.valueOf(right.getRating()).compareTo(left.getRating());
    }
}
