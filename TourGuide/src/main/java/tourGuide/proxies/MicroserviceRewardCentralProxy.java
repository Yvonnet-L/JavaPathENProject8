package tourGuide.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

/**
 * Permits to connection between TourGuide application and microservice-RewardCentral
 * 2 lignes Feign
 *     - the first for a connection direct with micro-services
 *     - Second is the configuration docker
 */
//@FeignClient(name="microservice-gpsUtil", url = "localhost:9002/RewardCentral")
@FeignClient(name="microservice-RewardCentral", url = "ms-rewardcentral:9002")
public interface MicroserviceRewardCentralProxy {

    /**
     *
     * @param attractionId
     * @param userId
     * @return integer
     */
    @GetMapping("/RewardCentral/attractionRewardPoints/{attractionId}/{userid}")
    int getAttractionRewardPoints(@PathVariable("attractionId") UUID attractionId , @PathVariable("userid") UUID userId );
}
