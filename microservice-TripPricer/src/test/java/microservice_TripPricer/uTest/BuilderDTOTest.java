package microservice_TripPricer.uTest;

import microservice_TripPricer.dto.ProviderDTO;
import microservice_TripPricer.tool.BuilderDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import tripPricer.Provider;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;


@ExtendWith(MockitoExtension.class)
public class BuilderDTOTest {

    @InjectMocks
    BuilderDTO builderDTO;

    //-------Test ---------buildProviderDTO()-----------------------------------------------------------------------
    @Test
    @DisplayName("buildProviderDTO() test with Provider valid")
    public void buildProviderDtoTestWithParamValid(){
        // GIVEN    String name, double price, UUID tripId
        UUID tripId = UUID.randomUUID();
        Provider provider = new Provider(tripId,"Sunny Days", 50.00);
        // WHEN
        ProviderDTO providerDTO = builderDTO.buildProviderDto(provider);
        // THEN
        assertThat(providerDTO.tripId).isEqualTo(provider.tripId);
        assertThat(providerDTO.price).isEqualTo(provider.price);
        assertThat(providerDTO.name).isEqualTo(provider.name);
    }

    @Test
    @DisplayName("buildProviderDTO() test with Provider not valid")
    public void buildProviderDtoTestWithBadParam(){
        // THEN
        assertThatNullPointerException().isThrownBy(() -> builderDTO.buildProviderDto(null));
    }

}
