package uk.gov.hmcts.reform.demo.utils;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class ApiTypes {
    private static Dictionary<String, List<String>> CustomApiTypesToGoogleApiTypesDict = new Hashtable<String, List<String>>() {{
        put("Sport", Arrays.asList("bowling_alley", "sporting_goods_store", "gym", "golf_course", "fitness_centre", "athletic_field", "stadium", "sports_club", "sports_complex", "swimming_pool"));
        put("Books", Arrays.asList("library", "bookstore"));
        put("Education", Arrays.asList("museum", "library", "university"));
        put("Entertainment", Arrays.asList("amusement_center"));
        put("Hiking", Arrays.asList("hiking_area"));
        put("History", Arrays.asList("museum", "cultural_center", "national_park", "tourist_attraction"));
        put("Movies", Arrays.asList("movie_central", "movie_theater"));
        put("Theater", Arrays.asList("theater"));
        put("Animals", Arrays.asList("aquarium", "dog_park", "national_park", "zoo", "pet_store"));
        put("Shopping", Arrays.asList("clothing-store"));
        put("Relax", Arrays.asList("spa"));
        put("Religion", Arrays.asList("church", "mosque", "hindu_temple", "synagogue"));
        put("Flora", Arrays.asList("national_park", "florist"));
    }};

    public static Dictionary<String, List<String>> getCustomApiTypesToGoogleApiTypesDict() {
        return CustomApiTypesToGoogleApiTypesDict;
    }
}
