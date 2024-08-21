package uk.gov.hmcts.reform.demo.utils;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class ApiTypes {
    private static Dictionary<String, List<String>> CustomApiTypesToGoogleApiTypesDict = new Hashtable() {{
        put("sport", Arrays.asList("bowling_alley", "sporting_goods_store", "gym", "golf_course", "fitness_centre", "athletic_field", "stadium", "sports_club", "sports_complex", "swimming_pool"));
        put("books", Arrays.asList("library", "bookstore"));
        put("education", Arrays.asList("museum", "library","university"));
        put("entertainment", Arrays.asList("amusement_center"));
        put("hiking",Arrays.asList("hiking_area"));
        put("history",Arrays.asList("museum","cultural_center","national_park","tourist_attraction"));
        put("movies", Arrays.asList("movie_central","movie_theater"));
        put("theater",Arrays.asList("theater"));
        put("animals",Arrays.asList("aquarium","dog_park","national_park","zoo","pet_store"));
        put("shopping",Arrays.asList("clothing-store"));
        put("relax",Arrays.asList("spa"));
        put("religion",Arrays.asList("church","mosque","hindu_temple","synagogue"));
        put("flora",Arrays.asList("national_park","florist"));
    }};
}
