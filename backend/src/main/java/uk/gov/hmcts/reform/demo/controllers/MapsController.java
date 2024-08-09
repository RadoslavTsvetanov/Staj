package uk.gov.hmcts.reform.demo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.demo.services.GoogleApi;
import uk.gov.hmcts.reform.demo.types.NearbyPlacesResponse;
import uk.gov.hmcts.reform.demo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

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

            return ResponseEntity.ok(u.JsonStringify(res));
        } catch (Exception e) {
            return ResponseEntity.ofNullable("not found or internal error idk, check code");
        }
    }
}
