package us.kulakov.lunch.uitest.test;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import us.kulakov.lunch.api.places.models.PlaceSearchSortMethod;
import us.kulakov.lunch.landing.MainActivity;

import static us.kulakov.lunch.uitest.di.EspressoPages.getPagesComponent;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SearchViewTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    /**
     * Just a placeholder test to get the boilerplate going -- verifies that UI elements are set up
     * correctly and present.
     */
    @Test
    public void testSearchElemenets() throws Exception {
        getPagesComponent().landingPage()
                .checkSearchButtonShown()
                .checkSelectedSortOrder(PlaceSearchSortMethod.DEFAULT);
    }
}
