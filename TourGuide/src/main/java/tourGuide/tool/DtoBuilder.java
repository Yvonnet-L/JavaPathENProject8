package tourGuide.tool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import tourGuide.beans.Provider;
import tourGuide.dto.ProviderDTO;
import tourGuide.dto.UserPreferencesDTO;
import tourGuide.dto.UserRewardDTO;
import tourGuide.user.UserPreferences;
import tourGuide.user.UserReward;


/**
 * This class allows the construction of a DTO from a Model
 */
@Component
public class DtoBuilder {

    private static Logger logger = LogManager.getLogger(DtoBuilder.class);


    /**----------------------UserPreferencesDTO--------------------------------------------------------------------------
     *
     * @param up UserPreferences
     * @return UserPreferencesDTO
     */
    public UserPreferencesDTO buildUserPreferencesDTO(final UserPreferences up) {
        logger.info( " ---> Launch buildUserPreferencesDTO");
        return new UserPreferencesDTO(up.getAttractionProximity(),up.getLowerPricePoint().getNumber().intValue(),
                                        up.getHighPricePoint().getNumber().intValue(), up.getTripDuration(),
                                        up.getTicketQuantity(), up.getNumberOfAdults(), up.getNumberOfChildren());
    }

    /**----------------------UserRewardDTO--------------------------------------------------------------------------
     *
     * @param ur UserReward
     * @return UserRewardDTO
     */
    public UserRewardDTO buildUserRewardDTO(final UserReward ur){
        logger.info( " ---> Launch buildUserRewardDTO");
        if(ur!=null) {
            return new UserRewardDTO(ur.getVisitedLocation(), ur.getAttraction(), ur.getRewardPoints());
        }else{
            return null;
        }
    }

    /**----------------------ProviderDTO-------------------------------------------------------------------------------
     *
     * @param provider
     * @return
     */
    public ProviderDTO buildProviderDTO(final Provider provider){
        logger.info( " ---> Launch buildProviderDTO");
        return new ProviderDTO( provider.name, provider.price, provider.tripId );
    }
}
