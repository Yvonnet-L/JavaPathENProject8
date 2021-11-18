package microservice_RewardCentral.uTest;


import microservice_RewardCentral.controller.RewardCentralController;
import microservice_RewardCentral.service.IRewardCentralService;
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

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RewardCentralController.class)
@AutoConfigureMockMvc
public class RewardCentralControllerTest {

    @MockBean
    private IRewardCentralService rewardCentralService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    //---------Get----getAttractionRewardPoints()------/attractionRewardPoints/{attractionId}/{userid}-------------------------------------------------------
    @Test
    @DisplayName("Test response 200 on getAttractionRewardPoints")
    public void testGetAttractionRewardPointsWithGoodParameters() throws Exception {
        UUID uuid = UUID.randomUUID();
        int resultat = 1 + (int)(Math.random() * ((1000 - 1) + 1));
        Mockito.when(rewardCentralService.getAttractionRewardPoints(uuid,uuid)).thenReturn(resultat);
        mockMvc.perform(MockMvcRequestBuilders.get("/RewardCentral/attractionRewardPoints/"+uuid+"/"+uuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("Test response 404 on getAttractionRewardPoints with bad parametter")
    public void testGetAttractionRewardPointsWithBadParameters() throws Exception {
        int uuidInteger = 1 + (int)(Math.random() * ((1000 - 1) + 1));
        mockMvc.perform(MockMvcRequestBuilders.get("/RewardCentral/attractionRewardPoints/"+uuidInteger+"/"+uuidInteger)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


}
