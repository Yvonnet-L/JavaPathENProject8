package tourGuide;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tourGuide.beans.VisitedLocation;
import tourGuide.dto.*;
import tourGuide.exceptions.DataNotConformException;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;

import javax.validation.Valid;
import java.util.List;

@RestController
public class TourGuideController {

	@Autowired
	TourGuideService tourGuideService;

    private Logger logger = LoggerFactory.getLogger(TourGuideController.class);

    /**
     * Home page
     *
     * @return String "Greetings from TourGuide!"
     */
    @RequestMapping("/")
    public String index() {
        logger.info(" ---> launch index - /");
        return "Greetings from TourGuide!";
    }


    /**
     * Return user's Location
     *
     * @param userName
     * @return JsonStream.serialize(visitedLocation.location)
     */
    @RequestMapping("/getLocation")
    public LocationDTO getLocation(@RequestParam String userName) {
        logger.info(" ---> launch getLocation - /getLocation - (@RequestParam String userName)");
        if ( userName.length() == 0){
            throw new DataNotConformException("Username is necessary!");
        }
    	VisitedLocation visitedLocation = tourGuideService.getUserLocation(userName);
		return new LocationDTO( visitedLocation.location.latitude, visitedLocation.location.longitude);
    }

    /**
     * Returns a list of the 5 closest attractions, with no distance limit between them
     *
     * @param userName
     * @return List<AttractionNearDTO>
     */
    @RequestMapping("/getNearbyAttractions") 
    public List<AttractionNearDTO> getNearbyAttractions(@RequestParam String userName) {
        logger.info(" ---> launch getNearbyAttractions - /getNearbyAttractions - (@RequestParam String userName)");
        if ( userName.length() == 0){
            throw new DataNotConformException("Username is necessary!");
        }
        return tourGuideService.getNearByAttractions(userName);
    }

    /**
     * return the list of rewards for a user
     *
     * @param userName
     * @return List<UserReward>
     */
    @RequestMapping("/getRewards") 
    public List<UserRewardDTO> getRewards(@RequestParam String userName) {
        logger.info(" ---> launch getRewards - /getRewards - (@RequestParam String userName)");
        if ( userName.length() == 0){
            throw new DataNotConformException("Username is necessary!");
        }
    	return tourGuideService.getUserRewards(userName);
    }

    /**
     * returns the latest locations of all users
     *
     * @return List<UserLocationDTO>
     */
    @RequestMapping("/getAllCurrentLocations")
    public List<UserLocationDTO> getAllCurrentLocations() {
        logger.info(" ---> launch  /getAllCurrentLocations");
        List<UserLocationDTO> userLocationDTOS = tourGuideService.getAllCurrentLocations();
        return userLocationDTOS;
    }

    /**
     * returns the tripdeals of a user through a list of providers
     *
     * @param userName
     * @return List<Provider> providers
     */
    @RequestMapping("/getTripDeals")
    public List<ProviderDTO> getTripDeals(@RequestParam String userName) {
        logger.info(" ---> launch /getTripDeals with @RequestParam String userName: " + userName);
        if ( userName.length() == 0) {
            throw new DataNotConformException("Username is necessary!");
        }
    	return tourGuideService.getTripDeals(userName);
    }

    /**
     * returns the user found using its name
     *
     * @param userName
     * @return User
     */
    @RequestMapping("/getUser")
    public User getUser(@RequestParam String userName) {
        logger.info(" ---> launch /getUser with @RequestParam String userName: " + userName);
        if ( userName.length() == 0){
            throw new DataNotConformException("Username is necessary!");
        }
    	return tourGuideService.getUser(userName);
    }

    /**
     * returns all users found
     *
     * @return List<User>
     */
    @RequestMapping("/getUsers")
    public List<User> getUsers() {
        logger.info(" ---> launch /getUsers");
        List<User> users = tourGuideService.getAllUsers();
        return users;
    }

    /**
     * returns the user's preferences found using its name
     *
     * @param userName
     * @return UserPreferencesDTO
     */
    @RequestMapping("/getUserPreferences")
    public UserPreferencesDTO getUserPreferences(@RequestParam String userName) {
        logger.info(" ---> launch /getUserPreferences with @RequestParam String userName: " + userName);
        if ( userName.length() == 0){
            throw new DataNotConformException("Username is necessary!");
        }
        return  tourGuideService.getUserPreferences(userName);
    }

    /**
     * updates the preferences of the user given by name
     *
     * @param userName
     * @param userPreferencesDTO
     * @return UserPreferences
     */
    @PutMapping("/addUserPreferences")
    private UserPreferencesDTO addUserPreferences(@Valid @RequestBody UserPreferencesDTO userPreferencesDTO, @RequestParam String userName ) {
        logger.info(" ---> launch /addUserPreferences");
        if ( userName.length() == 0){
            throw new DataNotConformException("Username is necessary!");
        }
        return tourGuideService.addUserPreferences( userName, userPreferencesDTO);
    }

}