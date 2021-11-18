package tourGuide.ut;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import tourGuide.beans.Attraction;
import tourGuide.beans.Location;
import tourGuide.beans.VisitedLocation;
import tourGuide.dto.AttractionNearDTO;
import tourGuide.dto.UserLocationDTO;
import tourGuide.dto.UserPreferencesDTO;
import tourGuide.exceptions.DataAlreadyExistException;
import tourGuide.exceptions.DataNotFoundException;
import tourGuide.proxies.MicroserviceGpsUtilProxy;
import tourGuide.proxies.MicroserviceTripPricerProxy;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.tool.DtoBuilder;
import tourGuide.tool.ModelBuilder;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TourGuideServiceTest {

    @InjectMocks
    private TourGuideService tourGuideService;

    @Mock
    private  MicroserviceGpsUtilProxy gpsUtil;

    @Mock
    private MicroserviceTripPricerProxy tripPricer;

    @Mock
    ModelBuilder modelBuilder;

    @Mock
    DtoBuilder dtoBuilder;

    @Mock
    public RewardsService rewardsService;

    private List<User> userList;

    private Map<String, User> internalUser;

    private CurrencyUnit currency = Monetary.getCurrency("USD");

    User user;
    UserPreferencesDTO userPreferencesDTO;
    UserPreferences userPreferences;

    @BeforeEach
    public void init(){
        user = new User(UUID.randomUUID(), "lolo", "000", "internalUser1@tourGuide.com");
        userPreferences = new UserPreferences(200, Money.of(40, currency),
                Money.of(500, currency),1,5,2,3);
        userPreferencesDTO = new UserPreferencesDTO(200, 40, 500,
                1,5,2,3);
        tourGuideService.addUser(user);
    }

    //---------- addUserPreferences------------------------------------------------------------------------------------
    @Test
    public void addUserPreferencesTest(){

        when(modelBuilder.buildUserPreferences(userPreferencesDTO)).thenReturn(userPreferences);

        UserPreferencesDTO userPreferencesDTOAdd = tourGuideService.addUserPreferences("lolo", userPreferencesDTO);

        assertEquals(user.getUserPreferences().getTicketQuantity(), userPreferences.getTicketQuantity());
        assertEquals(user.getUserPreferences().getNumberOfAdults(), 2);

    }
    //---------- addUser-----------------------------------------------------------------------------------------------
    @Test
    public void addUserTest(){
        int numberUserBeforeAdd = tourGuideService.getAllUsers().size();
        tourGuideService.addUser("Dan");
        int numberUserAfterAdd = tourGuideService.getAllUsers().size();


        assertEquals(numberUserAfterAdd,numberUserBeforeAdd + 1);
    }
    @Test
    public void addUserWithUserAlreadyExistTest(){
        int numberUserBeforeAdd = tourGuideService.getAllUsers().size();
        assertThrows(DataAlreadyExistException.class, () -> tourGuideService.addUser("lolo"));
        int numberUserAfterAdd = tourGuideService.getAllUsers().size();
        assertEquals(numberUserAfterAdd,numberUserBeforeAdd );
    }
    //---------- addUser-----------------------------------------------------------------------------------------------
    @Test
    public void getUserPreferencesTest(){
        tourGuideService.addUserPreferences("lolo",userPreferencesDTO);

        when(dtoBuilder.buildUserPreferencesDTO(user.getUserPreferences())).thenReturn(userPreferencesDTO);
        UserPreferencesDTO userPreferencesDTOAfterAdd = tourGuideService.getUserPreferences("lolo");

        assertEquals(userPreferencesDTOAfterAdd.getAttractionProximity(), userPreferencesDTO.getAttractionProximity());
    }
    //---------- getUser-----------------------------------------------------------------------------------------------
    @Test
    public void getUserTestWithUserNameNotExit() {
        assertThrows(DataNotFoundException.class, () -> tourGuideService.getUserRewards("UserNameNotExist"));
    }

    //---------- getAllCurrentLocations-----------------------------------------------------------------------------------------------
    @Test
    public void getAllCurrentLocationsTest() {

        List<User> users = tourGuideService.getAllUsers();
        VisitedLocation visitedLocation = users.get(10).getLastVisitedLocation();
        List<UserLocationDTO> userLocationDTOS = tourGuideService.getAllCurrentLocations();

        Double d1 = userLocationDTOS.get(10).getLocationDTO().getLatitude();
        Double d2 = visitedLocation.getLocation().getLatitude();

        assertEquals(userLocationDTOS.size(), tourGuideService.getAllUsers().size());
        assertEquals(d1,d2);
    }

    //---------- getAllAttractions-------------------------------------------------------------------------------------
    @Test
    public void getAllAttractionsTest() {
        List<Attraction> attractions = tourGuideService.getAllAttractions();
        assertThat(attractions.size() > 10);
    }

    //---------- getAttractionsByIdOfListAttraction---------------------------------------------------------------------
    @Test
    public void getAttractionsByIdOfListAttractionTest() {
        Attraction attraction1 = new Attraction("Disneyland", "Anaheim", "CA", UUID.randomUUID(), new Location(33.817595D, -117.922008D));
        Attraction attraction2 = new Attraction( "Jackson Hole", "Jackson Hole", "WY", UUID.randomUUID(), new Location(43.582767D, -110.821999D));
        Attraction attraction3 = new Attraction( "Mojave","Kelso", "CA", UUID.randomUUID(), new Location(35.141689D, -115.510399D));
        Attraction attraction4 = new Attraction( "Kartchner Caverns", "Benson", "AZ", UUID.randomUUID(), new Location(31.837551D, -110.347382D));
        List<Attraction> attractions = Arrays.asList(attraction1, attraction2, attraction3, attraction4);


        Attraction attractionFind = tourGuideService.getAttractionsByIdOfListAttraction(attractions, attraction1.attractionId);

        assertEquals(attractionFind.attractionName, attraction1.attractionName);

    }

    //---------- getNearByAttractions---------------------------------------------------------------------
    //@Test
    public void getNearByAttractionsTest() {
        // Near
        Attraction attraction1 = new Attraction("Disneyland", "Anaheim", "CA", UUID.randomUUID(),
                                                     new Location(33.817595D, -117.922008D));
        Attraction attraction2 = new Attraction( "Jackson Hole", "Jackson Hole", "WY", UUID.randomUUID(),
                                                    new Location(110.00, -110.00));
        Attraction attraction3 = new Attraction( "Mojave","Kelso", "CA", UUID.randomUUID(),
                                                    new Location(120.00, 120.00));
        Attraction attraction4 = new Attraction( "Kartchner Caverns", "Benson", "AZ", UUID.randomUUID(),
                                                    new Location(-20.00, 60.00));
        Attraction attraction5 = new Attraction("Legend Valley", "Thornville", "OH", UUID.randomUUID(),
                                                    new Location( 80.00, -50.00));
        // not Near
        Attraction attraction6 = new Attraction("Flowers Bakery of London", "Flowers Bakery of London", "KY", UUID.randomUUID(),
                                                    new Location(180.00, 180.00));
        Attraction attraction7 = new Attraction("McKinley Tower", "Anchorage", "AK", UUID.randomUUID(),
                                                    new Location(-180.00, 180.00));
        Attraction attraction8 = new Attraction("Flatiron Building", "New York City", "NY", UUID.randomUUID(),
                                                    new Location(180.00, -180.00));

        List<Attraction> attractions = Arrays.asList(attraction1, attraction2, attraction3, attraction4, attraction5,
                                                        attraction6, attraction7, attraction8);
        String name = user.getUserName();
        UUID userUUID = user.getUserId();
        VisitedLocation visitedLocation = new VisitedLocation(UUID.randomUUID(),
                                            new Location(80.00, 80.00),
                                                new Date());
        double res = 100.0;
        user.addToVisitedLocations(visitedLocation);
        //Mockito.when(rewardsService.calculateRewards(user));
        when(tourGuideService.getUserLocation(user.getUserName())).thenReturn(visitedLocation);
        when(rewardsService.getDistance(attraction1.location,visitedLocation.location)).thenReturn(res);
        when(rewardsService.getDistance(attraction2.location,visitedLocation.location)).thenReturn(res);
        when(rewardsService.getDistance(attraction3.location,visitedLocation.location)).thenReturn(res);
        when(rewardsService.getDistance(attraction4.location,visitedLocation.location)).thenReturn(res);
        when(gpsUtil.getAttractions()).thenReturn(attractions);

        List<AttractionNearDTO> attractionsNear = tourGuideService.getNearByAttractions("internalUser1");
        for(AttractionNearDTO a: attractionsNear){
            System.out.println(" ***//--------->>>>>>>>>>>>> " + a.getAttractionName() + " - distance: " +  a.getDistance());
        }
        assertEquals(attractionsNear.size(), 5);


    }
}
