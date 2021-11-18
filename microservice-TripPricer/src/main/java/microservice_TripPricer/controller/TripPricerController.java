package microservice_TripPricer.controller;

import microservice_TripPricer.dto.ProviderDTO;
import microservice_TripPricer.service.ITripPricerService;
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
@RequestMapping("/tripPricer")
public class TripPricerController {

    private static final Logger LOGGER = LogManager.getLogger(TripPricerController.class);

    @Autowired
    private ITripPricerService tripPricerService;

    /**   -----------------------------------------------------------------------------------------------------
     *
     * @param apiKey
     * @param attractionId
     * @param adults
     * @param children
     * @param nightsStay
     * @param rewardsPoints
     * @return List<ProviderDTO>
     */
    @GetMapping("/providers/{apiKey}/{attractionId}/{adults}/{children}/{nightsStay}/{rewardsPoints}")
    public List<ProviderDTO> getPrice(@PathVariable("apiKey") String apiKey,
                                              @PathVariable("attractionId") UUID attractionId,
                                              @PathVariable("adults") int adults,
                                              @PathVariable("children") int children,
                                              @PathVariable("nightsStay") int nightsStay,
                                              @PathVariable("rewardsPoints") int rewardsPoints ) {

        return tripPricerService.getPrice(apiKey,attractionId,adults,children, nightsStay,rewardsPoints);
    }



    /**-----------------------------------------------------------------------------------------------------
     *
     * Test Controller IN
     */
    @GetMapping("/in")
    public String testInControllerOk( ) {
        LOGGER.info("----> testInControllerOk");
        return "In Controller, Test ok !";
    }
}
