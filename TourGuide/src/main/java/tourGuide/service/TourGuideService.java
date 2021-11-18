package tourGuide.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.beans.Attraction;
import tourGuide.beans.Location;
import tourGuide.beans.Provider;
import tourGuide.beans.VisitedLocation;
import tourGuide.dto.*;
import tourGuide.exceptions.DataAlreadyExistException;
import tourGuide.exceptions.DataNotFoundException;
import tourGuide.helper.InternalTestHelper;
import tourGuide.proxies.MicroserviceGpsUtilProxy;
import tourGuide.proxies.MicroserviceTripPricerProxy;
import tourGuide.tool.DtoBuilder;
import tourGuide.tool.ModelBuilder;
import tourGuide.tracker.Tracker;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;
import tourGuide.user.UserReward;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 */
@Service
public class TourGuideService implements  ITourGuideService{

	@Autowired
	private final MicroserviceGpsUtilProxy gpsUtil;

	@Autowired
	private MicroserviceTripPricerProxy tripPricer;

	@Autowired
	ModelBuilder modelBuilder;

	@Autowired
	DtoBuilder dtoBuilder;

	@Autowired
	public final RewardsService rewardsService;

	public Tracker tracker;
	boolean testMode = true;


	private Logger logger = LoggerFactory.getLogger(TourGuideService.class);


	private ExecutorService executorService = Executors.newFixedThreadPool(800);

	/**
	 * Constructor ot TTourGuideService
	 *
	 * @param gpsUtil
	 * @param rewardsService
	 * @param tripPricer
	 * @param modelBuilder
	 * @param dtoBuilder
	 */
	public TourGuideService(MicroserviceGpsUtilProxy gpsUtil,
							RewardsService rewardsService,
							MicroserviceTripPricerProxy tripPricer,
							ModelBuilder modelBuilder,
							DtoBuilder dtoBuilder) {

		this.gpsUtil = gpsUtil;
		this.rewardsService = rewardsService;
		this.tripPricer = tripPricer;
		this.modelBuilder = modelBuilder;
		this.dtoBuilder = dtoBuilder;
		
		if(testMode) {
			logger.info("TestMode enabled");
			logger.debug("Initializing users");
			initializeInternalUsers();
			logger.debug("Finished initializing users");
		}
		tracker = new Tracker(this);
		addShutDownHook();
	}

	/** -------------------------------------------------------------------------------------------------------------
	 * Method used to retrieve a user's list of rewards
	 *
	 * @param userName
	 * @return List<UserRewardDTO>
	 */
	public List<UserRewardDTO> getUserRewards(String userName) {
		logger.info(" ---> Launch getUserRewards(String userName) with userName = " + userName);
		User user = getUser(userName);
		List<UserRewardDTO> userRewardDTOS = new ArrayList<>();
		for(UserReward userReward: user.getUserRewards()){
			if(userReward != null) {
				userRewardDTOS.add(dtoBuilder.buildUserRewardDTO(userReward));
			}
		}
		return  userRewardDTOS ;
	}

	/** -------------------------------------------------------------------------------------------------------------
	 * Method that allows the retrieval of the location of a user
	 *
	 * @param userName
	 * @return VisitedLocation
	 */
	public VisitedLocation getUserLocation(String userName) {
		logger.info(" ---> Launch getUserLocations(String userName) with userName = " + userName);
		User user = getUser(userName);
		VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ?
			user.getLastVisitedLocation() :
			trackUserLocation(user);
		return visitedLocation;
	}

	/** -------------------------------------------------------------------------------------------------------------
	 * Method that allows the recovery of a user
	 *
	 * @param userName
	 * @return User
	 */
	public User getUser(String userName) {
		logger.info(" ---> Launch getUser(String userName) with userName = " + userName);
		User user = internalUserMap.get(userName);
		if ( user == null ){
			throw new DataNotFoundException("Username Not found !");
		}
		return user;
	}
	/** -------------------------------------------------------------------------------------------------------------
	 * Method that allows recovery of all users
	 *
	 * @return List<User>
	 */
	public List<User> getAllUsers() {
		logger.info(" ---> Launch getAllUsers()");
		List<User> users = internalUserMap.values().stream().collect(Collectors.toList());
		return users;
	}
	/** -------------------------------------------------------------------------------------------------------------
	 *  Allows the addition of a user after checking with internalUserMap if an identical name does not exist,
	 *  if the one if there is a DataAlreadyExistException is thrown
	 *
	 * @param user
	 */
	public void addUser(User user) {
		logger.info(" ---> Launch addUser(User user) with userName = " + user.getUserName());
		if(!internalUserMap.containsKey(user.getUserName())) {
			internalUserMap.put(user.getUserName(), user);
		}else {
			throw new DataAlreadyExistException("Username already exist, choose another !");
		}
	}

	/** -------------------------------------------------------------------------------------------------------------
	 * Allows the addition of a user after checking with internalUserMap if an identical name does not exist,
	 * if the one if there is a DataAlreadyExistException is thrown
	 *
	 * @param userName
	 */
	public void addUser(String userName) {
		logger.info(" ---> Launch addUser((String userName) with userName = " + userName);
		if(!internalUserMap.containsKey(userName)) {
			User user = new User(UUID.randomUUID(), userName, "00 00 00 00", "@email.com");
			generateUserLocationHistory(user);
			addUser(user);
		}else {
			throw new DataAlreadyExistException("Username already exist, choose another !");
		}

	}

	/** -------------------------------------------------------------------------------------------------------------
	 * Method which allows the retrieval of a user's preferences
	 *
	 * @param userName
	 * @return UserPreferencesDTO
	 */
	public UserPreferencesDTO getUserPreferences(String userName) {
		logger.info(" ---> Launch getUserPreferences((String userName) with userName = " + userName);
		User user = getUser(userName);
		return dtoBuilder.buildUserPreferencesDTO(getUser(userName).getUserPreferences());
	}
	/** -------------------------------------------------------------------------------------------------------------
	 * Method used to update a user's preferences
	 *
	 * @param userName
	 * @param userPreferencesDTO
	 * @return userPreferencesDTO
	 */
	public UserPreferencesDTO addUserPreferences(String userName, UserPreferencesDTO userPreferencesDTO) {
		logger.info(" ---> Launch addUserPreferences(String userName, UserPreferencesDTO userPreferencesDTO) with userName = " + userName);
		User user = getUser(userName);
		UserPreferences userPreferences = modelBuilder.buildUserPreferences(userPreferencesDTO);
		user.setUserPreferences(userPreferences);
		return dtoBuilder.buildUserPreferencesDTO(getUser(userName).getUserPreferences());
	}
	/** -------------------------------------------------------------------------------------------------------------
	 * Method used to retrieve travel offers from a user
	 *
	 * @param userName
	 * @return List<Provider>
	 */
	public List<ProviderDTO> getTripDeals(String userName) {
		logger.info(" ---> Launch getTripDeals(String userName) with userName = " + userName);
		User user = getUser(userName);
		int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
		List<Provider> providers = tripPricer.getPrice(tripPricerApiKey, user.getUserId(), user.getUserPreferences().getNumberOfAdults(),
														user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(),
															cumulatativeRewardPoints);
		user.setTripDeals(providers);
		List<ProviderDTO> providerDTOS = new ArrayList<>();
		for(Provider provider: providers){
			providerDTOS.add(dtoBuilder.buildProviderDTO(provider));
		}
		return providerDTOS;
	}
	/** --------------------------------------------------------------------------------------------------------------
	 *
	 *
	 * @param user
	 * @return VisitedLocation
	 */
	public VisitedLocation trackUserLocation(User user) {
		logger.info(" ---> Launch trackUserLocation(User user) with userName = " + user.getUserName());
		VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
		user.addToVisitedLocations(visitedLocation);
		rewardsService.calculateRewards(user);
		return visitedLocation;
	}

	/** -------------------------------------------------------------------------------------------------------------
	 * Method which returns the last location of all Users through
	 * of the associated DTO to return the user's uuii with its location
	 *
	 * @return List<UserLocationDTO>
	 */
	public List<UserLocationDTO> getAllCurrentLocations() {
		logger.info(" ---> Launch getAllCurrentLocations()");
		List<User> users = getAllUsers();
		List<UserLocationDTO> userLocationDTOS = new ArrayList<>();

		for (User user : users) {
			VisitedLocation visitedLoc= new VisitedLocation();
			LocationDTO locationDTO = new LocationDTO();

			if(user.getVisitedLocations().size() > 0) {
				visitedLoc = user.getLastVisitedLocation();
				locationDTO = new LocationDTO(visitedLoc.location.latitude,visitedLoc.location.longitude);
			}
			userLocationDTOS.add(new UserLocationDTO(user.getUserId(),locationDTO));

		}
		return userLocationDTOS;
	}

	/** --------------------------------------------------------------------------------------------------------------
	 * implementation of parallelization with the trackListUserLocation method
	 * which allows the implementation of the executorService in order to work on several Threads
	 *
	 * @param userList ( List<User> )
	 * @return
	 */
	public void trackListUserLocation(List<User> userList) throws InterruptedException, ExecutionException {
		logger.info(" ---> Launch trackListUserLocation(List<User> userList)");
		Set<Callable<VisitedLocation>> callables = new HashSet<Callable<VisitedLocation>>();
			for (User user: userList) {
					callables.add(new Callable<VisitedLocation>() {
						public VisitedLocation call() throws Exception {
							return trackUserLocation(user);
						}
					});
			}

		List<Future<VisitedLocation>> futures = executorService.invokeAll(callables);

		executorService.shutdown();

	}

	/** --------------------------------------------------------------------------------------------------------------
	 * Method to Get the closest five tourist attractions to the user - no matter how far away they are.
	 * We retrieve in a TreeMap all the id of the attractions of the List with the distances in key.
	 * Then we go through the first 5 results of the Treemap which will be ordered by the distance,
	 * at this moment we build the AttractionsNearDTO results that we integrate into a list
	 *
	 * @param userName
	 * @return  List<AttractionNearDTO>
	 */
	public List<AttractionNearDTO> getNearByAttractions(String userName) {
		logger.info(" ---> Launch trackUserLocation(User user) with userName = " + userName);
		User user = getUser(userName);
		VisitedLocation visitedLocation = getUserLocation(userName);
		System.out.println("**----->>>>>>>>>>>>>>>>>>>>" + visitedLocation.getLocation().latitude + " - " + visitedLocation.getLocation().longitude);
		List<Attraction> attractionList = gpsUtil.getAttractions();

		Map<String, Double> values = new HashMap<String, Double>();
		TreeMap tm = new TreeMap();
		for(Attraction attraction : attractionList) {

			double distance = rewardsService.getDistance(attraction.location, visitedLocation.location);
			int idAttraction = attractionList.indexOf(attraction);
			System.out.println("**----->>>>> distance " + distance +" id " + idAttraction);
			tm.put(distance,idAttraction);
		}

		Set set = tm.entrySet();
		Iterator iterator = set.iterator();
		int i = 0;
		List<AttractionNearDTO> attractionNearDTOS = new ArrayList<>();
		while(iterator.hasNext() & i<5) {
			Map.Entry mapentry = (Map.Entry)iterator.next();

			int idAttractionList = Integer.valueOf(mapentry.getValue().toString());
			double distance = Double.valueOf(mapentry.getKey().toString());

			Attraction attraction = attractionList.get(idAttractionList);
			int reward = rewardsService.getRewardPoints(user.getUserId(),attraction.attractionId);

			AttractionNearDTO attractionNearDTO = new AttractionNearDTO( attraction.attractionName, attraction.location,
														visitedLocation.location, distance, reward);
			attractionNearDTOS.add(attractionNearDTO);
			i++;
		}
		return attractionNearDTOS;
	}

	/** -------------------------------------------------------------------------------------------------------------
	 * Method that extracts an attration from its UUID from a list of attractions
	 *
	 * @param uuid
	 * @return
	 */
	public Attraction getAttractionsByIdOfListAttraction (List<Attraction> attractions, UUID uuid) {
		logger.info(" ---> Launch getAttractionsByIdOfListAttraction (List<Attraction> attractions, UUID uuid)");
		Attraction attractionfind=null;
		for (Attraction attraction: attractions) {
			if (attraction.attractionId.equals(uuid)){
				attractionfind=attraction;
			}
		}
		return attractionfind;
	}

	/** -------------------------------------------------------------------------------------------------------------
	 * Method of retrieving all existing attractions in gpsUtil
	 *
	 * @return List<Attraction>
	 */
	public List<Attraction> getAllAttractions() {
		logger.info(" ---> Launch  getAllAttractions()");
		List<Attraction> attractions = gpsUtil.getAttractions();
		return attractions;
	}

	/** --------------------------------------------------------------------------------------------------------------
	 * Method that adds the stop hook that stops the Tracker
	 *
	 */
	private void addShutDownHook() {
		logger.info(" ---> Launch  addShutDownHook()");
		Runtime.getRuntime().addShutdownHook(new Thread() { 
		      public void run() {
		        tracker.stopTracking();
		      }
		    }); 
	}
	/**********************************************************************************
	 * 
	 * Methods Below: For Internal Testing
	 * 
	 **********************************************************************************/
	private static final String tripPricerApiKey = "test-server-api-key";
	// Database connection will be used for external users, but for testing purposes internal users are provided and stored in memory
	public final Map<String, User> internalUserMap = new HashMap<>();
	public void initializeInternalUsers() {
		/** here the users are created according to the number chosen in InternalTestHelper.getInternalUserNumber ()
		 */
		IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourGuide.com";

			User user = new User(UUID.randomUUID(), userName, phone, email);
			generateUserLocationHistory(user);

			internalUserMap.put(userName, user);
		});
		logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
	}

	/**
	 * Method that randomly generates a visit location history
	 *
	 * @param user
	 */
	public void generateUserLocationHistory(User user) {
		IntStream.range(0, 3).forEach(i-> {
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(), new Location(generateRandomLatitude(), generateRandomLongitude()),getRandomTime()));
		System.out.println("-TourGuideService-  VisitedLocations: " + user.getUserId() + " - " + generateRandomLatitude() + " - " + generateRandomLongitude() + " - " + getRandomTime());
		});
	}

	/**
	 * Method which randomly generates a Longitude
	 *
	 * @return Longitude double
	 */
	private double generateRandomLongitude() {
		double leftLimit = -180;
	    double rightLimit = 180;
	    return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	/**
	 * Method which randomly generates a Latitude
	 *
	 * @return Latitude Double
	 */
	private double generateRandomLatitude() {
		double leftLimit = -85.05112878;
	    double rightLimit = 85.05112878;
	    return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	/**
	 *
	 * @return date
	 */
	private Date getRandomTime() {
		Locale.setDefault(Locale.US);
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
	    return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}


}
