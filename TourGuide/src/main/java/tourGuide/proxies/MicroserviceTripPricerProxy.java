package tourGuide.proxies;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tourGuide.beans.Provider;

import java.util.List;
import java.util.UUID;

/**
 *  Permits to connection between TourGuide application and microservice-TripPricer
 * 2 lignes Feign
 *     - the first for a connection direct with micro-services
 *     - Second is the configuration docker
 */
//@FeignClient(name="microservice-gpsUtil", url = "localhost:9003/tripPricer")
@FeignClient(name="microservice-TripPricer", url = "ms-trippricer:9003")
public interface MicroserviceTripPricerProxy {

    /**
     *
     * @param apiKey
     * @param attractionId
     * @param adults
     * @param children
     * @param nightsStay
     * @param rewardsPoints
     * @return List<Provider>
     */
    @GetMapping("/tripPricer/providers/{apiKey}/{attractionId}/{adults}/{children}/{nightsStay}/{rewardsPoints}")
    public List<Provider> getPrice(@PathVariable("apiKey") String apiKey,
                                       @PathVariable("attractionId") UUID attractionId,
                                       @PathVariable("adults") int adults,
                                       @PathVariable("children") int children,
                                       @PathVariable("nightsStay") int nightsStay,
                                       @PathVariable("rewardsPoints") int rewardsPoints );


}
