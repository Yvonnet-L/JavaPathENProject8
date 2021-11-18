package microservice_TripPricer.service;

import microservice_TripPricer.dto.ProviderDTO;

import java.util.List;
import java.util.UUID;

public interface ITripPricerService {

     List<ProviderDTO> getPrice(String apiKey, UUID attractionId, int adults, int children, int nightsStay, int rewardsPoints);

    // String getProviderName(String apiKey, int adults);
}
