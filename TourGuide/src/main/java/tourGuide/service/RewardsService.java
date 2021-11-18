package tourGuide.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.beans.Attraction;
import tourGuide.beans.Location;
import tourGuide.beans.VisitedLocation;
import tourGuide.proxies.MicroserviceGpsUtilProxy;
import tourGuide.proxies.MicroserviceRewardCentralProxy;
import tourGuide.user.User;
import tourGuide.user.UserReward;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.*;

@Service
public class RewardsService {

	@Autowired
	MicroserviceGpsUtilProxy gpsUtil;

	@Autowired
	MicroserviceRewardCentralProxy rewardCentral;

    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
	// proximity in miles
    private int defaultProximityBuffer = 10;  // Tampon de proximité par défaut
	private int proximityBuffer = defaultProximityBuffer;
	private int attractionProximityRange = 200; //  ( attraction Gamme de proximité )

	private Logger logger = LoggerFactory.getLogger(RewardsService.class);

	private final ExecutorService executorService = Executors.newFixedThreadPool(160);

	public RewardsService(MicroserviceGpsUtilProxy gpsUtil, MicroserviceRewardCentralProxy rewardCentral) {
		this.gpsUtil = gpsUtil;
		this.rewardCentral = rewardCentral;
	}

    public RewardsService() {

    }

    public void setAttractionProximityRange(int attractionProximityRange) {
		this.attractionProximityRange = attractionProximityRange;
	}

	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}

	public void setDefaultProximityBuffer() {
		proximityBuffer = defaultProximityBuffer;
	}


	/** --------------------------------------------------------------------------------------------------------------
	 * Method used to calculate a user's rewards
	 *
	 * @param user
	 * @return
	 */
	public Void calculateRewards(User user) {
		logger.info(" ----> Launch calculateRewards");
		/**
		 * CopyOnWriteArrayList est une variante thread-safe d'ArrayList. A l'instar d'ArrayList,
		 * CopyOnWriteArray gère un tableau pour stocker ses éléments. La différence est que toutes les opérations
		 * mutatives telles que add, set, remove, clear, etc... créent une nouvelle copie du tableau qu'elle gère.
		 */
		CopyOnWriteArrayList<VisitedLocation> userLocations = new CopyOnWriteArrayList<>();
		userLocations.addAll( user.getVisitedLocations());

		CopyOnWriteArrayList<Attraction> attractions = new CopyOnWriteArrayList<>();
		attractions.addAll(gpsUtil.getAttractions());

		for(VisitedLocation visitedLocation : userLocations) {
			for(Attraction attraction : attractions) {
				/** here the r is a Reward from UserRewards */
				if(user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count()==0) {
					if(nearAttraction(visitedLocation, attraction)) {

						user.addUserReward(new UserReward(visitedLocation, attraction,
												getRewardPoints(attraction.attractionId, user.getUserId())));

					}
				}
			}
		}
		return null;
	}

	/** --------------------------------------------------------------------------------------------------------------
	 * Method wich allows the implementation of the parallelization of calculateRewards (User user)
	 * to calculate all user rewards asynchronously.
	 *
	 * @param users List<User>
	 */
	public void calculateRewardsForUsers(List<User> users) throws InterruptedException, ExecutionException {
		logger.info(" ----> Launch calculateRewardsForUsers(List<User> users)");
		/**
 		* Setting up the parallelization of calculateRewards (User user)
 		*/
		Set<Callable<Void>> callables = new HashSet<Callable<Void>>();
		for (User user: users) {
			callables.add(() -> calculateRewards(user));
		}
		List<Future<Void>> futures = executorService.invokeAll(callables);
		executorService.shutdown();

	}

	/** --------------------------------------------------------------------------------------------------------------
	 *
	 * @param attraction
	 * @param location
	 * @return boolean
	 */
	public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
		logger.info(" ----> Launch isWithinAttractionProximity(Attraction attraction, Location location)");
		return getDistance(attraction.location, location) > attractionProximityRange ? false : true;
	}

	/** --------------------------------------------------------------------------------------------------------------
	 * Method allowing to know if a given attraction is close to a location (user) by comparing the distance between
	 * the 2 and comparing it to the variable proximityBuffer defined above
	 *
	 * @param visitedLocation
	 * @param attraction
	 * @return boolean
	 */
	private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
		logger.info(" ----> Launch  nearAttraction");
		return getDistance(attraction.location, visitedLocation.location) > proximityBuffer ? false : true;
	}

	/** --------------------------------------------------------------------------------------------------------------
	 * Method which returns by appealing to rewardCentral the discount points that the given user obtains
	 * for a given attraction
	 *
	 * @param attractionId
	 * @param userId
	 * @return int
	 */
	public int getRewardPoints(UUID attractionId, UUID userId){
		logger.info(" ----> Launch  getRewardPoints");
		return rewardCentral.getAttractionRewardPoints(attractionId,userId);
	}

	/** --------------------------------------------------------------------------------------------------------------
	 * Method used to obtain the distance between 2 locations
	 *
	 * @param loc1
	 * @param loc2
	 * @return double
	 */
	public double getDistance(Location loc1, Location loc2) {
		logger.info(" ----> Launch  getDistance");
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                               + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
	}

	/** --------------------------------------------------------------------------------------------------------------
	 * Method used to obtain the list of all existing attractions in gpsUtil
	 *
	 * @return List<Attraction>
	 */
	public List<Attraction> getAllAttractions() {
		logger.info(" ----> Launch  getAllAttractions()");
		List<Attraction> attractions = gpsUtil.getAttractions();
		return attractions;
	}

}
