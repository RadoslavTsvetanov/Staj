package uk.gov.hmcts.reform.demo.external_apis;



import io.swagger.v3.core.util.Json;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;



public class google_api {
    @GetMapping("/searchNearby")
    public ResponseEntity<String> searchNearby(
        @RequestParam double latitude,
        @RequestParam double longitude,
        @RequestParam int radius,
        @RequestParam int maxResultCount) {

        String url = "https://places.googleapis.com/v1/places:searchNearby?fields=*";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + "ya29.a0AcM612yH6ZshaCSSkcEsFggHPz_vkYOjxo21JzXRKtJeB38eEIB3T_rCgy5KB3LOEL22yJldYl-3UfeGEDNXG5hThI7FOUrP0jbdOROYclg1jPLVd5-Vj1hrP9gowYpT121j1tTgSNIfo-tmOUPSMzJM1G30a4H3-ZJqFcoaCgYKASoSARASFQHGX2MiB5uChSUHClsQHtikbgd9sw0174");
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/json");

        String body = String.format(
            "{\"includedTypes\":[\"restaurant\"],\"maxResultCount\":%d,\"locationRestriction\":{\"circle\":{\"center\":{\"latitude\":%f,\"longitude\":%f},\"radius\":%d}}}",
            maxResultCount, latitude, longitude, radius);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }
}
