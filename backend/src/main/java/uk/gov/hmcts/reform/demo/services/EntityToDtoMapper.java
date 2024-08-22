package uk.gov.hmcts.reform.demo.services;

import uk.gov.hmcts.reform.demo.models.*;

import java.util.List;
import java.util.stream.Collectors;

public class EntityToDtoMapper {

    public static PlaceDTO toPlaceDTO(Place place) {
        PlaceDTO placeDTO = new PlaceDTO();
        placeDTO.setId(place.getId());
        placeDTO.setName(place.getName());
        placeDTO.setPlaceLocations(place.getPlaceLocations().stream()
                                       .map(EntityToDtoMapper::toPlaceLocationDTO)
                                       .collect(Collectors.toList()));
        return placeDTO;
    }

    public static PlaceLocationDTO toPlaceLocationDTO(PlaceLocation placeLocation) {
        PlaceLocationDTO placeLocationDTO = new PlaceLocationDTO();
        placeLocationDTO.setId(placeLocation.getId());
        placeLocationDTO.setDay(placeLocation.getDay());
        placeLocationDTO.setLocation(toLocationDTO(placeLocation.getLocation()));
        return placeLocationDTO;
    }

    public static LocationDTO toLocationDTO(Location location) {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setId(location.getId());
        locationDTO.setName(location.getName());
        locationDTO.setAgeRestriction(location.getAgeRestriction());
        locationDTO.setType(location.getType());
        locationDTO.setCost(location.getCost());
        locationDTO.setDay(location.getDay());
        return locationDTO;
    }
}
