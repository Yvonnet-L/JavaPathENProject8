package tourGuide.service;

import tourGuide.beans.Attraction;
import tourGuide.beans.Provider;
import tourGuide.beans.VisitedLocation;
import tourGuide.dto.*;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;
import tourGuide.user.UserReward;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ITourGuideService {

    List<UserRewardDTO> getUserRewards(String userName);

    VisitedLocation getUserLocation(String userName);

    User getUser(String userName);

    List<User> getAllUsers();

    void addUser(User user);

    UserPreferencesDTO addUserPreferences(String userName, UserPreferencesDTO userPreferencesDTO);

    List<ProviderDTO> getTripDeals(String userName);

    VisitedLocation trackUserLocation(User user);

    List<UserLocationDTO> getAllCurrentLocations();

    void trackListUserLocation(List<User> userList) throws InterruptedException, ExecutionException;

    List<AttractionNearDTO> getNearByAttractions(String userName);
}
