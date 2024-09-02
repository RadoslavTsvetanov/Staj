package uk.gov.hmcts.reform.demo.controllers.helpers;

import groovyjarjarpicocli.CommandLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.demo.controllers.MapsController;
import uk.gov.hmcts.reform.demo.models.Preferences;
import uk.gov.hmcts.reform.demo.models.User;
import uk.gov.hmcts.reform.demo.services.GoogleApi;
import uk.gov.hmcts.reform.demo.services.UserService;
import uk.gov.hmcts.reform.demo.utils.JwtUtil;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
@Service
public class MapsHelper {


    public static class PlaceResultWithOnlyFrontendRelevantData {

        public PlaceResultWithOnlyFrontendRelevantData(Coords coords, int cost, String imageUrl, Boolean isAgeRestricted, String description,float rating){
            coordinates = coords;
            this.cost = cost;
            this.iconUrl = imageUrl;
            this.isAgeRestricted = isAgeRestricted;
            this.description = description;
            this.rating = rating;
        }

        public Coords coordinates;
        public int cost;
        public String iconUrl;
        public Boolean isAgeRestricted;
        public String description;
        public float rating;
    }
    public static class LocationRequest {
        public float latitude;
        public float longitude;
        public float radius;
        public List<String> types; // make private later
        public List<String> authTokens;

        // Getters and Setters
        public float getLatitude() {
            return latitude;
        }

        public void setLatitude(float latitude) {
            this.latitude = latitude;
        }

        public float getLongitude() {
            return longitude;
        }

        public void setLongitude(float longitude) {
            this.longitude = longitude;
        }

        public float getRadius() {
            return radius;
        }

        public void setRadius(float radius) {
            this.radius = radius;
        }
    }



    public static String[] getSection(String[][] arrays) {
        if (arrays == null || arrays.length == 0) {
            return new String[0];
        }

        Set<String> commonElements = new HashSet<>(Arrays.asList(arrays[0]));

        for (int i = 1; i < arrays.length; i++) {
            commonElements.retainAll(Arrays.asList(arrays[i]));
        }

        return commonElements.toArray(new String[0]);
    }


    public static class Coords{
        Coords(double lat,double lng){
            this.lat = lat;
            this.lon = lng;
        }
        public double lon;
        public double lat;
    }
    @Autowired
    UserService userService;
    public List<Optional<User>> extractUsersFromTokens(List<String> authTokens) {
        JwtUtil jwtUtil = new JwtUtil();
        return authTokens.stream()
            .map(token -> jwtUtil.getUsernameFromToken(token))
            .map(username -> userService.findByUsername(username))
            .collect(Collectors.toList());
    }

    public Boolean ShouldAgeRestrictionApply(List<Optional<User>> users) {
        AtomicReference<Boolean> shouldAgeRestrictionApply = new AtomicReference<>(false);

        users.stream().forEach(user -> {
            LocalDate dateOfBirth = user.get().getDateOfBirth();
            LocalDate currentDate = LocalDate.now();
            int age = Period.between(dateOfBirth, currentDate).getYears();
            if (age < 18) {
                shouldAgeRestrictionApply.set(true);
            }


        });

        return shouldAgeRestrictionApply.get();
    }

    public List<List<String>> gatherUserInterests(List<Optional<User>> users) {
        List<List<String>> listWithAllUsersInterests = new ArrayList<>();
        users.stream().forEach(user -> {
            Preferences prefs = user.get().getPreferences();
            try {
                listWithAllUsersInterests.add(prefs.getInterests());
            } catch (Exception e) {
                // Handle exception
            }
        });
        return listWithAllUsersInterests;
    }


    public Boolean shouldPlaceBeDiscarded(uk.gov.hmcts.reform.demo.types.NearbyPlacesResponse.Result place){ // to filter by some global filter which is universal, for example we dont need to return places that are permamently closed
        if(place.permanently_closed){
            return true;
        }


        return false;
    }

    public Boolean isPlaceAgeRestricted(String placeType){
        if(placeType.contains("bar") || placeType.contains("night_club") || placeType.contains("casino")) {
            return true;
        }

        return false;
    }



    public List<uk.gov.hmcts.reform.demo.types.NearbyPlacesResponse.Result> queryNearbyPlaces(LocationRequest loc) throws Exception {
        GoogleApi googleApi = new GoogleApi(); // Assuming GoogleApi is the correct API class
        return googleApi.queryNearbyPLaces(loc.getLatitude(), loc.getLongitude(), loc.getRadius(), loc.types);
    }

    public List<uk.gov.hmcts.reform.demo.types.NearbyPlacesResponse.Result> filterPlaces(
        List<uk.gov.hmcts.reform.demo.types.NearbyPlacesResponse.Result> places, Boolean shouldAgeRestrictionApply) {
        return places.stream()
            .filter(place -> !shouldPlaceBeDiscarded(place))
            .map(place -> {
                if (shouldAgeRestrictionApply && isPlaceAgeRestricted(place.reference)) {
                    return null;
                }
                return place;
            })
            .collect(Collectors.toUnmodifiableList());
    }

    public List<PlaceResultWithOnlyFrontendRelevantData> prepareFrontendResults(
        List<uk.gov.hmcts.reform.demo.types.NearbyPlacesResponse.Result> filteredPlaces) {
        return filteredPlaces.stream()
            .map(place -> {
                Boolean isAgeRestricted = false;
                for (String type : place.types) {
                    if (isPlaceAgeRestricted(type)) {
                        isAgeRestricted = true;
                    }
                }
                return new PlaceResultWithOnlyFrontendRelevantData(
                    new Coords(place.geometry.location.lat, place.geometry.location.lng),
                    place.price_level,
                    place.icon,
                    isAgeRestricted,
                    "problem most places dont have description",
                    9.0f
                );
            }).collect(Collectors.toUnmodifiableList());
    }

}
