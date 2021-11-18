package tourGuide.integration;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tourGuide.beans.Location;
import tourGuide.beans.VisitedLocation;
import tourGuide.dto.AttractionNearDTO;
import tourGuide.dto.ProviderDTO;
import tourGuide.dto.UserPreferencesDTO;
import tourGuide.helper.InternalTestHelper;
import tourGuide.proxies.MicroserviceGpsUtilProxy;
import tourGuide.proxies.MicroserviceTripPricerProxy;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.tool.DtoBuilder;
import tourGuide.tool.ModelBuilder;
import tourGuide.user.User;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTourGuideService {

	@Autowired
	TourGuideService tourGuideService;
	@Autowired
	private MicroserviceGpsUtilProxy gpsUtil;
	@Autowired
	public RewardsService rewardsService;
	@Autowired
	private MicroserviceTripPricerProxy tripPricer;
	@Autowired
	DtoBuilder dtoBuilder;
	@Autowired
	ModelBuilder modelBuilder;



   @Before
   public void init (){
	   Locale.setDefault(Locale.US);
   }



	@Test
	public void getUserLocation() {
		InternalTestHelper.setInternalUserNumber(20);
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
		tourGuideService.tracker.stopTracking();
		assertTrue(visitedLocation.userId.equals(user.getUserId()));
	}

	@Test
	public void addUser() {

	    InternalTestHelper.setInternalUserNumber(20);
		tourGuideService = new TourGuideService(gpsUtil,rewardsService,tripPricer, modelBuilder, dtoBuilder);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);
		
		User retrivedUser = tourGuideService.getUser(user.getUserName());
		User retrivedUser2 = tourGuideService.getUser(user2.getUserName());
		tourGuideService.tracker.stopTracking();
		
		assertEquals(user, retrivedUser);
		assertEquals(user2, retrivedUser2);
	}

	@Test
	public void getAllUsers() {

		InternalTestHelper.setInternalUserNumber(20);
		tourGuideService = new TourGuideService(gpsUtil,rewardsService,tripPricer, modelBuilder, dtoBuilder);

		User user1 = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");
		List<User> allUsers = this.tourGuideService.getAllUsers();
		this.tourGuideService.addUser(user1);
		this.tourGuideService.addUser(user2);
		allUsers = this.tourGuideService.getAllUsers();

		assertFalse(allUsers.isEmpty());
		assertTrue(allUsers.contains(user1));
		assertTrue(allUsers.contains(user2));
	}

	@Test
	public void trackUser() {
		InternalTestHelper.setInternalUserNumber(20);
		tourGuideService = new TourGuideService(gpsUtil,rewardsService,tripPricer, modelBuilder, dtoBuilder);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);

		tourGuideService.tracker.stopTracking();
		
		assertEquals(user.getUserId(), visitedLocation.userId);
	}
	

	@Test
	public void getNearbyAttractions() {

		InternalTestHelper.setInternalUserNumber(20);

		User user = new User(UUID.randomUUID(), "lolo", "000", "jon@tourGuide.com");
		tourGuideService.addUser(user);
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
		List<AttractionNearDTO> attractions = tourGuideService.getNearByAttractions(user.getUserName());
		tourGuideService.tracker.stopTracking();

		assertEquals(5, attractions.size());
	}

	@Test
	public void getTripDeals() {

		rewardsService = new RewardsService();

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		tourGuideService.addUser(user);
		List<ProviderDTO> providers = tourGuideService.getTripDeals(user.getUserName());
		tourGuideService.tracker.stopTracking();

		/** 5 providers Max in getPrice
		 *  for(int i = 0; i < 5; ++i) {
		 */
		assertEquals(5, providers.size());

	}
	@Test
	public void addUserPreferencesTest(){

		UserPreferencesDTO userPreferencesDTO = new UserPreferencesDTO(300,100,600,1,5,2,3);

		UserPreferencesDTO userPreferencesResult =tourGuideService.addUserPreferences("internalUser10", userPreferencesDTO);
		User user = tourGuideService.getUser("internalUser10");

		assertEquals(user.getUserPreferences().getNumberOfAdults(), userPreferencesDTO.getNumberOfAdults());
		assertEquals(user.getUserPreferences().getTicketQuantity(), userPreferencesDTO.getTicketQuantity());
		assertEquals(user.getUserPreferences().getAttractionProximity(), userPreferencesDTO.getAttractionProximity());
	}

	@Test
	public void getNearByAttractionsWithLastLocationUserIsNearDisneylandTest() {
		//Recovery of user internalUser10 then injections from its last location very near to DisneyLand
		//in gpsUtil, Attraction("Disneyland", "Anaheim", "CA", 33.817595D, -117.922008D),
		// here latidute and longitude are reversed
		User user = tourGuideService.getUser("internalUser10");
		VisitedLocation visitedLocation = new VisitedLocation(UUID.randomUUID(),
											new Location(-118.10, 34.10),
												new Date());
		user.addToVisitedLocations(visitedLocation);

		List<AttractionNearDTO> attractionsNear = tourGuideService.getNearByAttractions("internalUser10");

		assertEquals(attractionsNear.size(), 5);
		assertEquals(attractionsNear.get(0).getAttractionName(),"Disneyland");
		assertTrue(attractionsNear.get(0).getDistance()<attractionsNear.get(1).getDistance());
		assertEquals(attractionsNear.get(1).getAttractionName(),"San Diego Zoo");
		assertTrue(attractionsNear.get(1).getDistance()<attractionsNear.get(2).getDistance());
		assertEquals(attractionsNear.get(2).getAttractionName(),"Joshua Tree National Park");
		assertTrue(attractionsNear.get(2).getDistance()<attractionsNear.get(3).getDistance());
		assertEquals(attractionsNear.get(3).getAttractionName(),"Mojave National Preserve");
		assertTrue(attractionsNear.get(3).getDistance()<attractionsNear.get(4).getDistance());
		assertEquals(attractionsNear.get(4).getAttractionName(),"Kartchner Caverns State Park");
	}

	@Test
	public void getNearByAttractionsWithLastLocationUserIsNearOfMcKinleyTowerTest() {
		//Recovery of user internalUser11 then injections from its last location very near to "McKinley Tower"
		//in gpsUtil, Attraction("McKinley Tower", "Anchorage", "AK", 61.218887D, -149.877502D),
		// here latidute and longitude are reversed
		User user = tourGuideService.getUser("internalUser11");
		VisitedLocation visitedLocation = new VisitedLocation(UUID.randomUUID(),
											new Location(-150.10, 61.10),
												new Date());
		user.addToVisitedLocations(visitedLocation);

		List<AttractionNearDTO> attractionsNear = tourGuideService.getNearByAttractions("internalUser11");
		for(AttractionNearDTO a: attractionsNear){
			System.out.println(" ***//--------->>>>>>>>>>>>> " + a.getAttractionName() + " - distance: " +  a.getDistance());
		}
		assertEquals(attractionsNear.size(), 5);
		assertEquals(attractionsNear.get(0).getAttractionName(),"McKinley Tower");
		assertTrue(attractionsNear.get(0).getDistance()<attractionsNear.get(1).getDistance());
		assertEquals(attractionsNear.get(1).getAttractionName(),"Jackson Hole");
		assertTrue(attractionsNear.get(1).getDistance()<attractionsNear.get(2).getDistance());
		assertEquals(attractionsNear.get(2).getAttractionName(),"Mojave National Preserve");
		assertTrue(attractionsNear.get(2).getDistance()<attractionsNear.get(3).getDistance());
		assertEquals(attractionsNear.get(3).getAttractionName(),"Disneyland");
		assertTrue(attractionsNear.get(3).getDistance()<attractionsNear.get(4).getDistance());
		assertEquals(attractionsNear.get(4).getAttractionName(),"Joshua Tree National Park");
	}
}
