package microservice_gpsUtil.uTest;

import microservice_gpsUtil.controller.GpsUtilController;
import microservice_gpsUtil.dto.AttractionDTO;
import microservice_gpsUtil.dto.Location;
import microservice_gpsUtil.dto.VisitedLocationDTO;
import microservice_gpsUtil.service.IGpsUtilService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = GpsUtilController.class)
@AutoConfigureMockMvc
public class GpsUtilControllerTest {

    @MockBean
    private IGpsUtilService gpsUtilService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    private final Logger logger = LogManager.getLogger(GpsUtilControllerTest.class);


    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }


    //---------Get----getAttractions()------/attractions-----------------------------------------------------------
    @Test
    @DisplayName("Test response 200 on getAttractions")
    public void testGetAttractions() throws Exception {
       AttractionDTO attractionDTO1 = new AttractionDTO("Disneyland", "Anaheim", "CA",
               UUID.randomUUID(),  new Location(-29.211451, 88.888888));
        List<AttractionDTO> attractionDTOS = Arrays.asList(attractionDTO1,attractionDTO1);
        Mockito.when(gpsUtilService.getAttractions()).thenReturn(attractionDTOS);

        mockMvc.perform(MockMvcRequestBuilders.get("/gpsUtil/attractions")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Test response 404 on getAttractions with listResult empty")
    public void testGetAttractionsWithListEmpty() throws Exception {
        List<AttractionDTO> attractionDTOS = new ArrayList<>();
        Mockito.when(gpsUtilService.getAttractions()).thenReturn(attractionDTOS);

        mockMvc.perform(MockMvcRequestBuilders.get("/gpsUtil/attractions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isNotFound());

    }

    //---------Get----getUserLocation()------/userLocation/{userId}-----------------------------------------------------------

    @Test
    @DisplayName("Test response 200 on getUserLocation()")
    public void testGetUserLocationOk() throws Exception {
        UUID uuid = UUID.randomUUID();
        VisitedLocationDTO userLocation = new VisitedLocationDTO(uuid,
                                                new Location(144.109457,-60.620536), new Date());
        Mockito.when(gpsUtilService.getUserLocation(uuid)).thenReturn(userLocation);

        mockMvc.perform(MockMvcRequestBuilders.get("/gpsUtil/userLocation/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


    }

    @Test
    @DisplayName("Test response 404 on getUserLocation() with userLocation=null")
    public void testGetUserLocationWithUserLocationNotFound() throws Exception {
        UUID uuid = UUID.randomUUID();
        VisitedLocationDTO userLocation = null;
        Mockito.when(gpsUtilService.getUserLocation(uuid)).thenReturn(userLocation);

        mockMvc.perform(MockMvcRequestBuilders.get("/gpsUtil/userLocation/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("Test response bad request on getUserLocation() with bad Param")
    public void testGetUserLocationWithBadParam() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/gpsUtil/userLocation/BadParam")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isBadRequest());

    }

}
