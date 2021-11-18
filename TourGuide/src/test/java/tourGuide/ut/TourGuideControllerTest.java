package tourGuide.ut;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsoniter.output.JsonStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tourGuide.TourGuideController;
import tourGuide.beans.Attraction;
import tourGuide.beans.Location;
import tourGuide.beans.Provider;
import tourGuide.beans.VisitedLocation;
import tourGuide.dto.*;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tourGuide.user.UserReward;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TourGuideController.class)
@AutoConfigureMockMvc
public class TourGuideControllerTest {

    @MockBean
    private TourGuideService tourGuideService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;


    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        /**
         List<Attraction> attractions = new ArrayList();
         attractions.add(new Attraction("Disneyland", "Anaheim", "CA", UUID.randomUUID(), new Location(210.0, 210.0)));
         attractions.add(new Attraction("Jackson Hole", "Jackson Hole", "WY",  UUID.randomUUID(), new Location(220.0, 220.0)));
         attractions.add(new Attraction("Mojave National Preserve", "Kelso", "CA",  UUID.randomUUID(), new Location(35.141689D, -115.510399D)));
         attractions.add(new Attraction("Joshua Tree National Park", "Joshua Tree National Park", "CA",  UUID.randomUUID(), new Location(300.0, 300.0)));
         attractions.add(new Attraction("Buffalo National River", "St Joe", "AR",  UUID.randomUUID(), new Location(230.0, 230.0)));
         attractions.add(new Attraction("Hot Springs National Park", "Hot Springs", "AR",  UUID.randomUUID(), new Location(34.52153D, -93.042267D)));
         attractions.add(new Attraction("Kartchner Caverns State Park", "Benson", "AZ",  UUID.randomUUID(), new Location(240.0, 240.0)));
         */

 }

    //----------------------/---------------------------------------------------------------------------------------
    @Test
    @DisplayName("testIndex /")
    public void testIndex() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk());
    }

    //----------------------/getLocation----------------------------------------------------------------------------
    @Test
    @Tag("/getLocation")
    @DisplayName("test /getLocation with param conform")
    public void testGetLocationWithConformParam() throws Exception {

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        Location location =new Location(200.0,200.0);
        VisitedLocation visitedLocation = new VisitedLocation(UUID.randomUUID(), location, new Date());
        String userName = "jon";
        Mockito.when(tourGuideService.getUser(any(String.class))).thenReturn(user);
        Mockito.when(tourGuideService.getUserLocation(any(String.class))).thenReturn(visitedLocation);
        
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/getLocation")
                .param("userName", userName))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).contains("200");
    }

    @Test
    @Tag("/getLocation")
    @DisplayName("test /getLocation with param not conform")
    public void testGetLocationWithNotConformParam() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/getLocation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userName", ""))
                .andExpect(status().isBadRequest());
    }
    //----------------------/getNearbyAttractions----------------------------------------------------------------------------
    @Test
    @Tag("getNearbyAttractions")
    @DisplayName("test /getNearbyAttractions with param conform")
    public void testGetNearbyAttractionsWitConformParam() throws Exception {
        /**
        User user = new User(UUID.randomUUID(), "jack", "000", "jack@tourGuide.com");
        VisitedLocation visitedLocation = new VisitedLocation(user.getUserId(),new Location(200.0,200.0), now());
        MicroserviceGpsUtilProxy gpsUtil = null;
        Mockito.when(tourGuideService.getUser(any(String.class))).thenReturn(user);
        Mockito.when(tourGuideService.getUserLocation(user)).thenReturn(visitedLocation);
        Mockito.when(gpsUtil.getAttractions()).thenReturn(attractions);
        */
        AttractionNearDTO attractionNearDTO = new AttractionNearDTO("Kartchner Caverns State Park", new Location(200.00,200.0),
                                                                    new Location(300.0,300.0), 250.00, 1000);
        List<AttractionNearDTO> attractionNearDTOS = new ArrayList<>();
        attractionNearDTOS.add(attractionNearDTO);
        Mockito.when(tourGuideService.getNearByAttractions(any(String.class))).thenReturn(attractionNearDTOS);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/getNearbyAttractions")
                .param("userName", "jack"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).contains("Kartchner Caverns State Park");
    }

    @Test
    @Tag("getNearbyAttractions")
    @DisplayName("test /getNearbyAttractions with param empty ---> Exception")
    public void testGetNearbyAttractionsWithBadParam() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/getNearbyAttractions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userName", ""))
                .andExpect(status().isBadRequest());
    }
    //----------------------/getTripDeals----------------------------------------------------------------------------
    @Test
    @Tag("getTripDeals")
    @DisplayName("test /getTripDeals with param conform")
    public void testGetTripDealsWithGoodParam() throws Exception {

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        ProviderDTO providerDTO = new ProviderDTO("Holiday Travels",500.0, UUID.randomUUID());
        List<ProviderDTO> providerDTOS = Arrays.asList(providerDTO, providerDTO);
        String userName = "jon";
        Mockito.when(tourGuideService.getUser(any(String.class))).thenReturn(user);
        Mockito.when(tourGuideService.getTripDeals(any(String.class))).thenReturn(providerDTOS);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/getTripDeals")
                        .param("userName", userName))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).contains("Holiday Travels");

    }
    @Test
    @Tag("getTripDeals")
    @DisplayName("test /getTripDeals with param empty ---> Exception")
    public void testGetTripDealsWithBadParam() throws Exception {
       mockMvc.perform(MockMvcRequestBuilders.get("/getTripDeals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userName", ""))
                        .andExpect(status().isBadRequest());
    }
    //-----------------/getRewards-------------------------------------------------------------------------------------
    @Test
    @Tag("getRewards")
    @DisplayName("test /getRewards with param conform")
    public void testGetRewardsWithConformParam() throws Exception {

        Location location =new Location(200.0,200.0);
        VisitedLocation visitedLocation = new VisitedLocation(UUID.randomUUID(), location, new Date());
        Attraction attraction = new Attraction("DisneylandParis", "Anaheim", "CA", UUID.randomUUID(),
                                            new Location(210.0, 210.0));
        UserRewardDTO userRewardDTO = new UserRewardDTO(visitedLocation, attraction, 1000);
        List<UserRewardDTO> userRewardDTOS = Arrays.asList(userRewardDTO,userRewardDTO);

        Mockito.when(tourGuideService.getUserRewards(any(String.class))).thenReturn(userRewardDTOS);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/getRewards")
                .param("userName", "jack"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).contains("DisneylandParis");
    }


    @Test
    @Tag("getRewards")
    @DisplayName("test /getRewards with param empty ---> Exception")
    public void testGetRewardsWithBadParam() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/getRewards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userName", ""))
                        .andExpect(status().isBadRequest());
    }

    //-----------------/getAllCurrentLocations-----------------------------------------------------------------------------
    @Test
    @Tag("getAllCurrentLocations")
    @DisplayName("test /getAllCurrentLocations")
    public void testGetAllCurrentLocations() throws Exception {

        List<UserLocationDTO> userLocationDTOS = new ArrayList<>();
        for (int i=0; i<5; i++){
            UserLocationDTO userLocationDTO = new UserLocationDTO( UUID.randomUUID(), new LocationDTO(500.0, 400.0 ));
            userLocationDTOS.add(userLocationDTO);
        }
        Mockito.when(tourGuideService.getAllCurrentLocations()).thenReturn(userLocationDTOS);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/getAllCurrentLocations"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).contains("500");
    }
    //-----------------/getUsers-------------------------------------------------------------------------------------
    @Test
    @DisplayName("test /getUsers ")
    public void testGetUsers() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/getUsers"))
                .andExpect(status().isOk());

    }
    //-----------------/getUser-------------------------------------------------------------------------------------
    @Test
    @Tag("getUser")
    @DisplayName("test /getUser with param not Conform ---> Exception")
    public void testGetUserWithBadParam() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/getUser")
                        .param("userName", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Tag("getUser")
    @DisplayName("test /getUser ")
    public void testGetUserWithConformParam() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/getUser")
                .param("userName", "jon"))
                .andExpect(status().isOk());

    }

    //-----------------/getUserPreferences------------------------------------------------------------------------------
    @Test
    @Tag("getUserPreferences")
    @DisplayName("test /getUserPreferences with param conform ")
    public void testGetUserPreferencesWithConformParam() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/getUserPreferences")
                .param("userName", "jon"))
                .andExpect(status().isOk());
    }

    @Test
    @Tag("getUserPreferences")
    @DisplayName("test /getUserPreferences with param not conform ")
    public void testGetUserPreferencesWithNotConformParam() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/getUserPreferences")
                .contentType(MediaType.APPLICATION_JSON)
                .param("userName", ""))
                .andExpect(status().isBadRequest());
    }
    //-----------------/addUserPreferences------------------------------------------------------------------------------
    @Test
    @Tag("updateUserPreferences")
    @DisplayName("test /getUserPreferences with param conform ")
    public void testAddUserPreferencesWithConformParam() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();;
        UserPreferencesDTO userPreferencesDTO = new UserPreferencesDTO(300,100,
                                            300,2,4,2,2);

        mockMvc.perform(MockMvcRequestBuilders.put("/addUserPreferences")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userPreferencesDTO))
                .param("userName", "jack"))
                .andExpect(status().isOk());
    }

    @Test
    @Tag("updateUserPreferences")
    @DisplayName("test /getUserPreferences with param UserName not conform ")
    public void testAddUserPreferencesWithUserNameNotConformParam() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();;
        UserPreferencesDTO userPreferencesDTO = new UserPreferencesDTO(300,100,
                300,2,4,2,2);

        mockMvc.perform(MockMvcRequestBuilders.put("/addUserPreferences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPreferencesDTO))
                        .param("userName", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Tag("updateUserPreferences")
    @DisplayName("test /getUserPreferences with param UserPreferencesDTO not conform ")
    public void testAddUserPreferencesWithUserPreferencesDTONameNotConformParam() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();;
        UserPreferencesDTO userPreferencesDTONotConform = new UserPreferencesDTO(-300,-100,
                -300,2,4,2,2);

        mockMvc.perform(MockMvcRequestBuilders.put("/addUserPreferences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPreferencesDTONotConform))
                        .param("userName", "jack"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Tag("updateUserPreferences")
    @DisplayName("test /getUserPreferences with param UserPreferencesDTO null ")
    public void testAddUserPreferencesWithUserPreferencesDTONull() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.put("/addUserPreferences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null))
                        .param("userName", "jack"))
                .andExpect(status().isBadRequest());
    }
}

