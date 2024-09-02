package uk.gov.hmcts.reform.demo.controllers;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.hmcts.reform.demo.controllers.helpers.MapsHelper;
import uk.gov.hmcts.reform.demo.models.Preferences;
import uk.gov.hmcts.reform.demo.models.User;
import uk.gov.hmcts.reform.demo.types.NearbyPlacesResponse;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MapsHelperTest {


    MapsHelper mapsHelper = new MapsHelper();
    @Test
    public void testGetSection() {
        String[][] arrays = {{"A", "B", "C"}, {"B", "C", "D"}, {"C", "B", "E"}};
        String[] result = MapsHelper.getSection(arrays);
        assertArrayEquals(new String[]{"B", "C"}, result);
    }

    @Test
    public void testGetSectionWithNoCommonElements() {
        String[][] arrays = {{"A", "B", "C"}, {"D", "E", "F"}, {"G", "H", "I"}};
        String[] result = MapsHelper.getSection(arrays);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testGetSectionWithEmptyArray() {
        String[][] arrays = {};
        String[] result = MapsHelper.getSection(arrays);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testShouldAgeRestrictionApply() {
        User user = new User();
        user.setDateOfBirth(LocalDate.now().minusYears(17)); // 17 years old
        List<Optional<User>> users = Collections.singletonList(Optional.of(user));

        Boolean result = mapsHelper.ShouldAgeRestrictionApply(users);

        assertTrue(result);
    }

    @Test
    public void testShouldAgeRestrictionApplyWhenAllUsersAreAdults() {
        User user = new User();
        user.setDateOfBirth(LocalDate.now().minusYears(20)); // 20 years old
        List<Optional<User>> users = Collections.singletonList(Optional.of(user));

        Boolean result = mapsHelper.ShouldAgeRestrictionApply(users);

        assertFalse(result);
    }

    @Test
    public void testGatherUserInterests() {
        Preferences prefs = new Preferences();
        prefs.setInterests(Arrays.asList("Music", "Travel"));

        User user = new User();
        user.setPreferences(prefs);

        List<Optional<User>> users = Collections.singletonList(Optional.of(user));

        List<List<String>> result = mapsHelper.gatherUserInterests(users);

        assertEquals(1, result.size());
        assertTrue(result.get(0).contains("Music"));
        assertTrue(result.get(0).contains("Travel"));
    }

    @Test
    public void testGatherUserInterestsWhenNoInterestsPresent() {
        User user = new User();
        user.setPreferences(new Preferences());

        List<Optional<User>> users = Collections.singletonList(Optional.of(user));

        List<List<String>> result = mapsHelper.gatherUserInterests(users);

        assertEquals(1, result.size());
        assertTrue(result.get(0).isEmpty());
    }

    // Test for shouldPlaceBeDiscarded method
    @Test
    public void testShouldPlaceBeDiscarded() {
        NearbyPlacesResponse.Result place = new NearbyPlacesResponse.Result();
        place.permanently_closed = true;

        Boolean result = mapsHelper.shouldPlaceBeDiscarded(place);

        assertTrue(result);
    }

    @Test
    public void testShouldPlaceNotBeDiscarded() {
        NearbyPlacesResponse.Result place = new NearbyPlacesResponse.Result();
        place.permanently_closed = false;

        Boolean result = mapsHelper.shouldPlaceBeDiscarded(place);

        assertFalse(result);
    }

    // Test for isPlaceAgeRestricted method
    @Test
    public void testIsPlaceAgeRestricted() {
        String placeType = "bar";

        Boolean result = mapsHelper.isPlaceAgeRestricted(placeType);

        assertTrue(result);
    }

    @Test
    public void testIsPlaceNotAgeRestricted() {
        String placeType = "restaurant";

        Boolean result = mapsHelper.isPlaceAgeRestricted(placeType);

        assertFalse(result);
    }

    // Test for filterPlaces method
    @Test
    public void testFilterPlaces() {
        NearbyPlacesResponse.Result place1 = new NearbyPlacesResponse.Result();
        place1.permanently_closed = false;
        place1.reference = "restaurant";

        NearbyPlacesResponse.Result place2 = new NearbyPlacesResponse.Result();
        place2.permanently_closed = false;
        place2.reference = "bar";

        List<NearbyPlacesResponse.Result> places = Arrays.asList(place1, place2);

        List<NearbyPlacesResponse.Result> result = mapsHelper.filterPlaces(places, true);

        assertEquals(1, result.size());
        assertEquals("restaurant", result.get(0).reference);
    }

    @Test
    public void testFilterPlacesWhenNoAgeRestriction() {
        NearbyPlacesResponse.Result place1 = new NearbyPlacesResponse.Result();
        place1.permanently_closed = false;
        place1.reference = "restaurant";

        List<NearbyPlacesResponse.Result> places = Collections.singletonList(place1);

        List<NearbyPlacesResponse.Result> result = mapsHelper.filterPlaces(places, false);

        assertEquals(1, result.size());
        assertEquals("restaurant", result.get(0).reference);
    }

    // Test for prepareFrontendResults method
    @Test
    public void testPrepareFrontendResults() {
        NearbyPlacesResponse.Result place = new NearbyPlacesResponse.Result();
        place.geometry = new NearbyPlacesResponse.Geometry();
        place.geometry.location = new NearbyPlacesResponse.Location();
        place.geometry.location.lat = 51.5074;
        place.geometry.location.lng = -0.1278;
        place.price_level = 2;
        place.icon = "iconUrl";
        place.types = Arrays.asList("restaurant");

        List<NearbyPlacesResponse.Result> places = Collections.singletonList(place);

        List<MapsHelper.PlaceResultWithOnlyFrontendRelevantData> result = mapsHelper.prepareFrontendResults(places);

        assertEquals(1, result.size());
        assertEquals(51.5074, result.get(0).coordinates.lat);
        assertEquals(-0.1278, result.get(0).coordinates.lon);
        assertEquals(2, result.get(0).cost);
        assertEquals("iconUrl", result.get(0).iconUrl);
        assertFalse(result.get(0).isAgeRestricted);
        assertEquals(9.0f, result.get(0).rating);
    }
}
