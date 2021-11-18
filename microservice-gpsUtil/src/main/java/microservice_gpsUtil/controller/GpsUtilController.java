package microservice_gpsUtil.controller;


import microservice_gpsUtil.dto.AttractionDTO;
import microservice_gpsUtil.dto.VisitedLocationDTO;
import microservice_gpsUtil.exception.DataNotFoundException;
import microservice_gpsUtil.service.IGpsUtilService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/gpsUtil")
public class GpsUtilController {

    private static final Logger LOGGER = LogManager.getLogger(GpsUtilController.class);

    @Autowired
    private IGpsUtilService gpsUtilService;


    /** ----------------/gpsUtil/attractions---------------------------------------------------------------------------------------------
     *
     * @return List<Attraction>
     */
    @GetMapping("/attractions")
    public List<AttractionDTO> getAttractions() {
        LOGGER.info("----> /attractions -- getAttractions()");

        List<AttractionDTO> attractions = gpsUtilService.getAttractions();

        if (attractions.isEmpty()){
            throw new DataNotFoundException("User location not found");
        }
        return attractions;
    }

    /** -------------------------------------------------------------------------------------------------------------
     *
     * @param userId
     * @return VisitedLocation
     */
    @GetMapping("/userLocation/{userId}")
    public VisitedLocationDTO getUserLocation(@PathVariable("userId") UUID userId) {
        LOGGER.info("---> Launch /userLocation/{userId} -- getUserLocation() with userID: " + userId.toString());

        VisitedLocationDTO userLocation = gpsUtilService.getUserLocation(userId);

        if (userLocation==null){
            throw new DataNotFoundException("User location not found");
        }

        return userLocation;
    }
}
