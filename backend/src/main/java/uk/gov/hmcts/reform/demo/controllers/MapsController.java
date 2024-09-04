package uk.gov.hmcts.reform.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import uk.gov.hmcts.reform.demo.controllers.helpers.MapsHelper;
import uk.gov.hmcts.reform.demo.models.Preferences;
import uk.gov.hmcts.reform.demo.models.User;
import uk.gov.hmcts.reform.demo.services.GoogleApi;
import uk.gov.hmcts.reform.demo.services.UserService;
import uk.gov.hmcts.reform.demo.utils.ApiTypes;
import uk.gov.hmcts.reform.demo.utils.JwtUtil;
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

    MapsHelper mapsHelper = new MapsHelper();
    @Autowired
    UserService userService;
    // TODO : make it cleaner since its not easy tp read and functionns are all over the place
    GoogleApi googleApi = new GoogleApi();
    Utils u = new Utils();


    @PostMapping("/nearby")
    public ResponseEntity<String> getMaps(@RequestBody MapsHelper.LocationRequest loc) {

//         Extract users from the tokens
        List<Optional<User>> users = mapsHelper.extractUsersFromTokens(loc.authTokens);
        // Determine if age restriction should apply
        Boolean isAgeRestrictionRequired = mapsHelper.ShouldAgeRestrictionApply(users);

        // Gather all users' interests
        List<List<String>> listWithAllUsersInterests = mapsHelper.gatherUserInterests(users);

        System.out.println("list, " + listWithAllUsersInterests);

        try {
            // Query nearby places
            List<uk.gov.hmcts.reform.demo.types.NearbyPlacesResponse.Result> nearbyPlaces = mapsHelper.queryNearbyPlaces(loc);

            // Filter places based on interests and age restriction
            List<uk.gov.hmcts.reform.demo.types.NearbyPlacesResponse.Result> filteredPlaces = mapsHelper.filterPlaces(nearbyPlaces, isAgeRestrictionRequired);

            // Prepare results for frontend
            List<MapsHelper.PlaceResultWithOnlyFrontendRelevantData> frontendResults = mapsHelper.prepareFrontendResults(nearbyPlaces);

            // Return the response
            return ResponseEntity.ok(u.JsonStringify(frontendResults));

        } catch (Exception e) {
            return ResponseEntity.ofNullable("not found or internal error idk, check code" + "error is " + e.toString());
        }
    }



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
            System.out.println("ok[");
             return googleApi.reverseGeocoding(lat,lon);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
