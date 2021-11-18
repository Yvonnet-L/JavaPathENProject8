package microservice_TripPricer.uTest;


import microservice_TripPricer.dto.ProviderDTO;
import microservice_TripPricer.service.TripPricerService;
import microservice_TripPricer.tool.BuilderDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class TripPricerServiceTest {

    @InjectMocks
    TripPricerService tripPricerService;

    @Mock
    BuilderDTO builderDTO;

    @Mock
    TripPricer tripPricer;

    //---Test-------getPrice(String apiKey, UUID attractionId, int adults, int children, int nightsStay, int rewardsPoints)------------------------------------------------------------------------
    @Test
    @DisplayName("Test on getPrice()")
    public void getPriceTestOkWithGoodParam(){
        // preparation de la list résultat
        Provider provider = new Provider(UUID.randomUUID(),"Sunny Days", 50.0);
        List<Provider> providers = Arrays.asList(provider, provider, provider);
        ProviderDTO providerDTO = new ProviderDTO("Sunny Days", 50.0, UUID.randomUUID());
        List<ProviderDTO> providerDTOS = Arrays.asList(providerDTO, providerDTO, providerDTO);
        // initialisation de toutes les variables pour plus de clarté
        String apiKey = "apikey";
        UUID attractionId = UUID.randomUUID();
        int adults = 2, children = 2, nightsStay = 3, rewardsPoints = 200;
        // WHEN
        Mockito.when(tripPricer.getPrice(apiKey, attractionId, adults, children, nightsStay, rewardsPoints)).thenReturn(providers);
        Mockito.when(builderDTO.buildProviderDto(provider)).thenReturn(providerDTO);
        // THEN
        assertThat(tripPricerService.getPrice(apiKey, attractionId, adults, children, nightsStay, rewardsPoints)).isEqualTo(providerDTOS);
    }



}
