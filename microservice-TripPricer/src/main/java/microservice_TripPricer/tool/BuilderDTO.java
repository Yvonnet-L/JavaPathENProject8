package microservice_TripPricer.tool;

import microservice_TripPricer.dto.ProviderDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import tripPricer.Provider;

@Component
public class BuilderDTO {

    private static Logger LOGGER = LogManager.getLogger(BuilderDTO.class);

    public BuilderDTO() {
    }

    public ProviderDTO buildProviderDto (Provider provider){
        LOGGER.info("----> launch buildProviderDTO");
        return new ProviderDTO(provider.name, provider.price, provider.tripId);
    }

}
