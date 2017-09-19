package us.kulakov.lunch.landing;

import javax.inject.Inject;

import us.kulakov.lunch.R;
import us.kulakov.lunch.api.places.models.PlaceSearchSortMethod;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;

/**
 * Provides interaction between tests and landing page of the application
 */
public class LandingPage {
    @Inject
    public LandingPage() {
    }

    public LandingPage checkSearchButtonShown() {
        // TODO: We really technically should not have assertions in pages, they'll go into tests once there are more pages
        onView(withId(R.id.button_search)).check(matches(isDisplayed()));
        return this;
    }

    public LandingPage checkSelectedSortOrder(PlaceSearchSortMethod sortOrder) {
        onView(withId(R.id.sort_order_spinner)).check(matches(withSpinnerText(sortOrder.name())));
        return this;
    }
}
