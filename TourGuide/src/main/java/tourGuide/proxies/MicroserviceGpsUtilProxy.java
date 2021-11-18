package tourGuide.proxies;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tourGuide.beans.Attraction;
import tourGuide.beans.VisitedLocation;

import java.util.List;
import java.util.UUID;

/**
 * Permits to connection between TourGuide application and microservice-gpsUtil
 * 2 lignes Feign
 *     - the first for a connection direct with micro-services
 *     - Second is the configuration docker
 */
//@FeignClient(name="microservice-gpsUtil", url = "localhost:9001/gpsUtil")
@FeignClient(name="microservice-gpsUtil", url = "ms-gpsutil:9001")
public interface MicroserviceGpsUtilProxy {

    /**
     *
     * @return List<Attraction>
     */
    @GetMapping("/gpsUtil/attractions")
    List<Attraction> getAttractions();

    /**
     *
     * @param userId
     * @return VisitedLocation
     */
    @GetMapping("/gpsUtil/userLocation/{userId}")
    VisitedLocation getUserLocation(@PathVariable("userId") UUID userId);
}
