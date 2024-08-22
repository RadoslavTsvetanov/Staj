package uk.gov.hmcts.reform.demo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.demo.services.GoogleApi;
import uk.gov.hmcts.reform.demo.types.NearbyPlacesResponse;
import uk.gov.hmcts.reform.demo.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/maps")
public class MapsController {
    GoogleApi googleApi = new GoogleApi();
    Utils u = new Utils();
    public static class LocationRequest {
        public float latitude;
        public float longitude;
        public float radius;
        public List<String> types; // make private later
        public List<String> wantedInterests;

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




    private Boolean shouldPlaceBeDiscarded(uk.gov.hmcts.reform.demo.types.NearbyPlacesResponse.Result place){ // to filter by some global filter which is universal, for example we dont need to return places that are permamently closed
        if(place.permanently_closed){
            return true;
        }


        return false;
    }

    @PostMapping("/nearby")
    public ResponseEntity<String> getMaps(@RequestBody LocationRequest loc) {


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
                    .collect(Collectors.toList());
            return ResponseEntity.ok(u.JsonStringify(res));
        } catch (Exception e) {
            return ResponseEntity.ofNullable("not found or internal error idk, check code");
        }
    }
}
