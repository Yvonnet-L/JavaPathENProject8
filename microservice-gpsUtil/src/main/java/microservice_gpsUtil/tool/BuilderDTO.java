package microservice_gpsUtil.tool;

import gpsUtil.location.Attraction;

import gpsUtil.location.VisitedLocation;
import microservice_gpsUtil.dto.AttractionDTO;
import microservice_gpsUtil.dto.Location;
import microservice_gpsUtil.dto.VisitedLocationDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class BuilderDTO {

    private static Logger LOGGER = LogManager.getLogger(BuilderDTO.class);

    public BuilderDTO() {
    }

    public AttractionDTO buildAttractionDto (Attraction attraction){
        LOGGER.info("----> launch buildAttractionDto");
       return new AttractionDTO(attraction.attractionName, attraction.city, attraction.state, attraction.attractionId,
                                new Location(attraction.latitude, attraction.longitude));

    }

    public VisitedLocationDTO buildVisitedLocationDto (VisitedLocation visitedLocation){
        LOGGER.info("----> launch buildVisitedLocationDto ");
        return new VisitedLocationDTO( visitedLocation.userId, new Location(visitedLocation.location.latitude,
                                            visitedLocation.location.longitude), visitedLocation.timeVisited);

    }
}
