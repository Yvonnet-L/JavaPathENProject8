package microservice_TripPricer.uTest;


import groovy.transform.AutoImplement;
import microservice_TripPricer.controller.TripPricerController;
import microservice_TripPricer.dto.ProviderDTO;
import microservice_TripPricer.service.ITripPricerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tripPricer.Provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TripPricerController.class)
@AutoConfigureMockMvc
public class TripPricerControllerTest {

    @MockBean
    ITripPricerService tripPricerService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    //---------Get---- getPrice()------/providers-----------------------------------------------------------
    @Test
    @DisplayName("Test response 200 on  getPrice")
    public void testGetPrice() throws Exception {
        // preparation de la list résultat
        ProviderDTO providerDTO = new ProviderDTO("Sunny Days", 50.0, UUID.randomUUID());
        List<ProviderDTO> providerDTOS = Arrays.asList(providerDTO, providerDTO, providerDTO);
        // initialisation de toutes les variables pour plus de clarté
        UUID attractionId = UUID.randomUUID();
        int adults = 2, children = 2, nightsStay = 3, rewardsPoints = 200;
        Mockito.when(tripPricerService.getPrice("apikey",attractionId, adults, children, nightsStay, rewardsPoints))
                                .thenReturn(providerDTOS);

        mockMvc.perform(MockMvcRequestBuilders.get("/tripPricer/providers/apikey/"+ attractionId +
                                                    "/"+adults+"/"+ children+"/"+nightsStay+"/"+rewardsPoints )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("Test response bad request on GetPrice() with bad Param")
    public void testGetPriceWithBadParam() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tripPricer/providers/BadParam/BadParam/BadParam/BadParam/BadParam/BadParam")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isBadRequest());
    }

}
