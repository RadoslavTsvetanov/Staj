package uk.gov.hmcts.reform.demo.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.demo.secrets.Secrets;
import uk.gov.hmcts.reform.demo.types.NearbyPlacesResponse;
import uk.gov.hmcts.reform.demo.utils.Utils;

import java.lang.reflect.Array;
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
<<<<<<< HEAD
            ObjectMapper objMapper = new ObjectMapper();
=======
            ObjectMapper objMapper = new com.fasterxml.jackson.databind.ObjectMapper();
>>>>>>> a75daa6f87d7b9432c2d07570ee511b127f4932c
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
}
