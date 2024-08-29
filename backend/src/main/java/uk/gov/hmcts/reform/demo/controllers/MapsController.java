package uk.gov.hmcts.reform.demo.controllers;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.hmcts.reform.demo.models.Preferences;
import uk.gov.hmcts.reform.demo.models.User;
import uk.gov.hmcts.reform.demo.services.GoogleApi;
import uk.gov.hmcts.reform.demo.services.UserService;
import uk.gov.hmcts.reform.demo.utils.ApiTypes;
import uk.gov.hmcts.reform.demo.utils.JwtUtil;
import uk.gov.hmcts.reform.demo.utils.Utils;

import java.io.Console;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/maps")
public class MapsController {
    @Autowired
    UserService userService;
    // TODO : make it cleaner since its not easy tp read and functionns are all over the place
    GoogleApi googleApi = new GoogleApi();
    Utils u = new Utils();
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

    private Boolean isPlaceAgeRestricted(String placeType){
        if(placeType.contains("bar") || placeType.contains("night_club") || placeType.contains("casino")) {
            return true;
        }

        return false;
    }


    private Boolean shouldPlaceBeDiscarded(uk.gov.hmcts.reform.demo.types.NearbyPlacesResponse.Result place){ // to filter by some global filter which is universal, for example we dont need to return places that are permamently closed
        if(place.permanently_closed){
            return true;
        }


        return false;
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

    public static void main(String[] args) {
        String[][] arrays = {
            {"apple", "banana", "orange"},
            {"banana", "orange", "grape"},
            {"orange", "banana", "kiwi"}
        };

        String[] result = getSection(arrays);

        System.out.println("Common elements: " + Arrays.toString(result));
    }

    public static class Coords{
        Coords(double lat,double lng){
            this.lat = lat;
            this.lon = lng;
        }
        public double lon;
        public double lat;
    }
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


    @PostMapping("/nearby")
    public ResponseEntity<String> getMaps(@RequestBody LocationRequest loc) {
         ApiTypes apiTypes = new ApiTypes();
         JwtUtil jwtUtil = new JwtUtil();
        AtomicReference<Boolean> shouldAgeRestrictionApply = new AtomicReference<>(false);



        List<Optional<User>> users = loc.authTokens.stream()
            .map(token -> jwtUtil.getUsernameFromToken(token))
            .map(username ->  userService.findByUsername(username))
            .collect(Collectors.toList());

        users.stream().forEach(user -> {
            LocalDate dateOfBirth = user.get().getDateOfBirth();
            LocalDate currentDate = LocalDate.now();
            int age = Period.between(dateOfBirth, currentDate).getYears();
            if(age < 18){
                shouldAgeRestrictionApply.set(true);
            }
        });
        List<List<String>> listWithAllUsersInterests = Collections.emptyList();
        users.stream().forEach(user -> {

            Preferences prefs = user.get().getPreferences();
            try{
                listWithAllUsersInterests.add(prefs.getInterests()); // yay no indication of error throw
            }catch(Exception e){

            }
        });

        System.out.println("list, " + listWithAllUsersInterests);


        try {
            List
                <uk.gov.hmcts.reform.demo.types.NearbyPlacesResponse.Result> res = googleApi.queryNearbyPLaces(
                loc.getLatitude(),
                loc.getLongitude(),
                loc.getRadius(),
                loc.types
            );
            List<uk.gov.hmcts.reform.demo.types.NearbyPlacesResponse.Result> filteredInteresstOnlyConatiningPlacesThatMAtchSelectedInterersts =
                res.stream()
                    .filter(place -> !shouldPlaceBeDiscarded(place))
                    .collect(Collectors.toList())
                    .stream().map(place -> {
                        if (shouldAgeRestrictionApply.get() && isPlaceAgeRestricted(place.reference)) {
                            return null;
                        }
                        return place;
                    }).collect(Collectors.toUnmodifiableList());


            // -----------------------


            filteredInteresstOnlyConatiningPlacesThatMAtchSelectedInterersts.stream().map(place -> {
                return place.types;
            });



            // --------------------

            List<PlaceResultWithOnlyFrontendRelevantData> resWithOnlyFrontendRealtiveData = filteredInteresstOnlyConatiningPlacesThatMAtchSelectedInterersts.stream()
                .map(place -> {
                    Boolean isAgeRestricted = false;
                    for(String type : place.types) {
                        if(isPlaceAgeRestricted(type)){
                            isAgeRestricted = true;
                        }
                    }

                    return new PlaceResultWithOnlyFrontendRelevantData(new Coords(place.geometry.location.lat,place.geometry.location.lng),place.price_level,place.icon,isAgeRestricted,"problem most places dont have description",9.0f
                    );
                }).collect(Collectors.toUnmodifiableList());







            return ResponseEntity.ok(u.JsonStringify(resWithOnlyFrontendRealtiveData));

        } catch (Exception e) {

            return ResponseEntity.ofNullable("not found or internal error idk, check code" + "error is "+ e.toString());

        }
    }


    private final String externalServiceUrl = "https://3wvp0z6w-3005.asse.devtunnels.ms/upload";
    @PostMapping("/upload")
    public String uploadFile(
        @RequestParam("file") MultipartFile file,
        @RequestParam("keyPrefix") String keyPrefix) {
        try {
            byte[] b = file.getBytes();
            return Utils.uploadFile(b, LocalDateTime.now().toString(), "staj"); // make it so that it gives time also
        }catch (Exception e){
            System.out.println(e.toString());
        }
        return "didnt upload";
    }



    @GetMapping("/geolocate")
    public String geolocate(
@RequestParam("lon") Float lon,
@RequestParam("lat") Float lat
    ){
        try{
             return googleApi.reverseGeocoding(lat,lon);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
