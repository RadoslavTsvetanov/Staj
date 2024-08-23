package uk.gov.hmcts.reform.demo.services;

import uk.gov.hmcts.reform.demo.models.*;

import java.util.List;
import java.util.stream.Collectors;

public class EntityToDtoMapper {

    public static PlanDTO toPlanDTO(Plan plan) {
        PlanDTO planDTO = new PlanDTO();
        planDTO.setId(plan.getId());
        planDTO.setEstCost(plan.getEstCost());
        planDTO.setBudget(plan.getBudget());
        planDTO.setName(plan.getName());
        planDTO.setPlaces(plan.getPlaces().stream()
                              .map(EntityToDtoMapper::toPlaceDTO)
                              .collect(Collectors.toList()));
        planDTO.setHistory(toHistoryDTO(plan.getHistory()));
        return planDTO;
    }

    public static PlaceDTO toPlaceDTO(Place place) {
        PlaceDTO placeDTO = new PlaceDTO();
        placeDTO.setId(place.getId());
        placeDTO.setName(place.getName());
        placeDTO.setPlanId(place.getPlan().getId());
        placeDTO.setDateWindow(toDateWindowDTO(place.getDateWindow()));
        placeDTO.setPlaceLocations(place.getPlaceLocations().stream()
                                       .map(EntityToDtoMapper::toPlaceLocationDTO)
                                       .collect(Collectors.toList()));
        return placeDTO;
    }

    public static PlaceLocationDTO toPlaceLocationDTO(PlaceLocation placeLocation) {
        PlaceLocationDTO placeLocationDTO = new PlaceLocationDTO();
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

    public static HistoryDTO toHistoryDTO(History history) {
        if (history == null) {
            return null;
        }

        HistoryDTO historyDTO = new HistoryDTO();
        historyDTO.setId(history.getId());
        historyDTO.setMemories(history.getMemories().stream()
                                   .map(EntityToDtoMapper::toMemoryDTO)
                                   .collect(Collectors.toList()));
        return historyDTO;
    }

    public static MemoryDTO toMemoryDTO(Memory memory) {
        MemoryDTO memoryDTO = new MemoryDTO();
        memoryDTO.setId(memory.getId());
        memoryDTO.setDate(memory.getDate());
        memoryDTO.setImage(memory.getImage());
        memoryDTO.setPlace(memory.getPlace());
        memoryDTO.setDescription(memory.getDescription());
        return memoryDTO;
    }

    public static DateWindowDTO toDateWindowDTO(DateWindow dateWindow) {
        if (dateWindow == null) {
            return null;
        }

        DateWindowDTO dateWindowDTO = new DateWindowDTO();
        dateWindowDTO.setStartDate(dateWindow.getStartDate());
        dateWindowDTO.setEndDate(dateWindow.getEndDate());
        return dateWindowDTO;
    }
}
