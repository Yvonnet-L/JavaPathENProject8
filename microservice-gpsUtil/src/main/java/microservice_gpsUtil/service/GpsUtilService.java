package microservice_gpsUtil.service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import microservice_gpsUtil.dto.AttractionDTO;
import microservice_gpsUtil.dto.VisitedLocationDTO;
import microservice_gpsUtil.tool.BuilderDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GpsUtilService implements IGpsUtilService{


    @Autowired
    private GpsUtil gpsUtil;

    @Autowired
    public BuilderDTO builderDTO;

    private static final Logger LOGGER = LogManager.getLogger(GpsUtilService.class);


    /** ----------------------------------------------------------------------------------------------------------
     *
     * @return List<Attraction>
     */
    public List<AttractionDTO> getAttractions() {

        LOGGER.info("----> GpsService.getAttractions");

        List<Attraction> attractions = gpsUtil.getAttractions();
        List<AttractionDTO> attractionDTOS = new ArrayList<>();
        for (Attraction attraction: attractions) {
            attractionDTOS.add(builderDTO.buildAttractionDto(attraction));
        }
        return attractionDTOS;
    }

    /** ----------------------------------------------------------------------------------------------------------
     *
     * @param userId
     * @return VisitedLocationDTO
     */
    public VisitedLocationDTO getUserLocation(UUID userId) {

        VisitedLocationDTO userLocationDTO = builderDTO.buildVisitedLocationDto(gpsUtil.getUserLocation(userId));

        return userLocationDTO;
    }
}
