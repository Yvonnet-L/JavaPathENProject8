package microservice_TripPricer.service;

import microservice_TripPricer.dto.ProviderDTO;
import microservice_TripPricer.tool.BuilderDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TripPricerService implements ITripPricerService {

    @Autowired
    TripPricer tripPricer;

    @Autowired
    BuilderDTO builderDTO;

    private static final Logger LOGGER = LogManager.getLogger(TripPricerService.class);

    /**
     *
     * @param apiKey
     * @param attractionId
     * @param adults
     * @param children
     * @param nightsStay
     * @param rewardsPoints
     * @return  List<ProviderDTO>
     */
    @Override
    public List<ProviderDTO> getPrice(String apiKey, UUID attractionId, int adults, int children, int nightsStay, int rewardsPoints) {
        LOGGER.info("----> Launch getPrice of TripPricerService");
        List<Provider> providers = tripPricer.getPrice(apiKey, attractionId, adults, children, nightsStay, rewardsPoints);
        List<ProviderDTO> providerDTOS = new ArrayList<>();
        for(Provider provider: providers ){
            providerDTOS.add(builderDTO.buildProviderDto(provider));
        }
        return providerDTOS;
    }
/**
    @Override
    public String getProviderName(String apiKey, int adults) {
        return null;
    } */
}
