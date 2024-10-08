package uk.gov.hmcts.reform.demo.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

public class NearbyPlacesResponse {
    public List<Object> html_attributions;
    public List<Result> results;
    public String status;
    public String next_page_token;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        public String business_status;
        public Geometry geometry;
        public String icon;
        public String icon_background_color;
        public String icon_mask_base_uri;
        public String name;
        public OpeningHours opening_hours;
        public List<Photo> photos;
        public String place_id;
        public PlusCode plus_code;
        public int price_level;
        public double rating;
        public String reference;
        public String scope;
        public List<String> types;
        public int user_ratings_total;
        public String vicinity;
        public boolean permanently_closed;
    }

    public static class Geometry {
        public Location location;
        public Viewport viewport;
    }

    public static class Location {
        public double lat;
        public double lng;
    }

    public static class Viewport {
        public Northeast northeast;
        public Southwest southwest;
    }

    public static class Northeast {
        public double lat;
        public double lng;
    }

    public static class Southwest {
        public double lat;
        public double lng;
    }

    public static class OpeningHours {
        public boolean open_now;
    }

    public static class Photo {
        public int height;
        public List<String> html_attributions;
        public String photo_reference;
        public int width;
    }

    public static class PlusCode {
        public String compound_code;
        public String global_code;
    }
}
