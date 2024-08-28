package uk.gov.hmcts.reform.demo.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.mapping.Any;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.demo.secrets.Secrets;
import uk.gov.hmcts.reform.demo.types.NearbyPlacesResponse;
import uk.gov.hmcts.reform.demo.utils.Utils;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GoogleApi {
    private final RestTemplate restTemplate;
    private final Secrets secrets = new Secrets();
    public GoogleApi() {
        this.restTemplate = new RestTemplate();
    }

    private Utils u = new Utils();
    /*
    *
    *
    * Note: Adding both `keyword` and `type` with the same value (`keyword=cafe&type=cafe` or `keyword=parking&type=parking`) can yield `ZERO_RESULTS`.
    *
    *
    * */


    class NearbyPlaces{

        private List<uk.gov.hmcts.reform.demo.types.NearbyPlacesResponse.Result> places;
        NearbyPlaces(){
            this.places = new ArrayList<uk.gov.hmcts.reform.demo.types.NearbyPlacesResponse.Result>();
        }
        public List<uk.gov.hmcts.reform.demo.types.NearbyPlacesResponse.Result> getPlaces() {
            return this.places;
        }
        public void addPlace(List<uk.gov.hmcts.reform.demo.types.NearbyPlacesResponse.Result> places){

            for(int i = 0; i < places.size(); i ++){
                this.places.add(places.get(i));
            }

        }

    }


    public List
        <uk.gov.hmcts.reform.demo.types.NearbyPlacesResponse.Result> queryNearbyPLaces(float longitude, float latitude, float radius, List<String> types) throws Exception {
       NearbyPlaces res = new NearbyPlaces();
        for(int  i = 0; i < types.size();i++){ // since only one type can be specified but we need more than on type we will expose an endpoint for getting all types
            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                "?location=" + String.valueOf(longitude) + "," + String.valueOf(latitude) +
                "&radius=" + String.valueOf((int)radius) +
                "&type=" + types.get(i) + // note only one type can be specified
                "&key=" + secrets.googleMapsApiKey;
            System.out.println(url);
            ObjectMapper objMapper = new com.fasterxml.jackson.databind.ObjectMapper();
            try{
                String apiRes = restTemplate.getForObject(url, String.class);
                NearbyPlacesResponse response =  objMapper.readValue(apiRes, NearbyPlacesResponse.class);

                System.out.println(apiRes);

                res.addPlace(response.results); // TODO: create an object with only what you need
            }catch(Exception e){
                e.printStackTrace();
                throw new Exception("query nearby places failed");
            }
        }

        return res.getPlaces();
    }




    public String reverseGeocoding(){
        String latitude = "40.714224";
        String longitude = "-73.961452";
        String apiKey = "AIzaSyCl1ONEKSrMWbNlMuGmQnZsEDWVMDU9GmU";

        String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s&key=%s",
                                   latitude, longitude, apiKey);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response status code: " + response.statusCode());
            System.out.println("Response body: " + response.body());
            return response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "b";
        }


    }


}
