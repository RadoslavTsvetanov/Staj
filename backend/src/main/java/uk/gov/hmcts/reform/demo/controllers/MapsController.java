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

    @Autowired
    private JwtUtil jwtUtil;
    // TODO : make it cleaner since its not easy tp read and functionns are all over the place
    GoogleApi googleApi = new GoogleApi();
    Utils u = new Utils();


    @PostMapping("/nearby")
    public ResponseEntity<String> getMaps(@RequestBody MapsHelper.LocationRequest loc,
                                          @RequestHeader(value = "Authorization", required = false) String authorizationHeader ) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization token is missing or invalid.");
        }

        String token = authorizationHeader.substring(7);
        String currentUsername = jwtUtil.getUsernameFromToken(token);

        if (currentUsername == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
        }

        Optional<User> userOpt = userService.findByUsername(currentUsername);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        User currentUser = userOpt.get();

        Preferences preferences = userService.getPreferencesByUserId(currentUser.getId());
        LocalDate birthDate = currentUser.getDateOfBirth(); // Assuming the User class has a getBirthDate() method
        Boolean isAgeRestrictionRequired = isUserUnder18(birthDate);

        System.out.println("list, " + preferences);

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

    private Boolean isUserUnder18(LocalDate birthDate) {
        if (birthDate == null) {
            return false; // Assuming no restriction if birthDate is not available
        }

        LocalDate today = LocalDate.now();
        Period age = Period.between(birthDate, today);
        return age.getYears() < 18;
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
