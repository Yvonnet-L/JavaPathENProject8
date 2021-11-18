package tourGuide.integration;

import org.checkerframework.checker.units.qual.A;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tourGuide.beans.Attraction;
import tourGuide.beans.VisitedLocation;
import tourGuide.dto.UserRewardDTO;
import tourGuide.helper.InternalTestHelper;
import tourGuide.proxies.MicroserviceGpsUtilProxy;
import tourGuide.proxies.MicroserviceRewardCentralProxy;
import tourGuide.proxies.MicroserviceTripPricerProxy;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.tool.DtoBuilder;
import tourGuide.tool.ModelBuilder;
import tourGuide.user.User;
import tourGuide.user.UserReward;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRewardsService {

	@Autowired
	MicroserviceGpsUtilProxy gpsUtil;
	@Autowired
	private MicroserviceTripPricerProxy tripPricer;
	@Autowired
	MicroserviceRewardCentralProxy rewardCentral;
	@Autowired
	RewardsService rewardsService;
	@Autowired
	TourGuideService tourGuideService;
	@Autowired
	DtoBuilder dtoBuilder;
	@Mock
	ModelBuilder modelBuilder;

	@Before
	public void init (){
		Locale.setDefault(Locale.US);
	}

	@Test
	public void userGetRewards() {
		RewardsService rewardsService = new RewardsService(gpsUtil, rewardCentral);

		InternalTestHelper.setInternalUserNumber(20);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService, tripPricer, modelBuilder, dtoBuilder);
		User user = new User(UUID.randomUUID(), "john", "000", "jon@tourGuide.com");
		Attraction attraction = gpsUtil.getAttractions().get(0);
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction.location, new Date()));
		tourGuideService.trackUserLocation(user);
		List<UserReward> userRewards = user.getUserRewards();
		tourGuideService.tracker.stopTracking();

		assertEquals(userRewards.size(),1);
	}
	
	@Test
	public void isWithinAttractionProximity() {
		Attraction attraction = gpsUtil.getAttractions().get(0);
		assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction.location));
	}

	@Test
	public void nearAllAttractions() {

		/** on augmente au max le Proxi */
		rewardsService.setProximityBuffer(Integer.MAX_VALUE);

		tourGuideService.tracker.stopTracking();
		User user = tourGuideService.getAllUsers().get(0);
		rewardsService.calculateRewards(user);
		List<UserRewardDTO> userRewards = tourGuideService.getUserRewards(user.getUserName());

		assertEquals(gpsUtil.getAttractions().size(), userRewards.size());
	}
	
}
