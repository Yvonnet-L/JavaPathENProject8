package microservice_RewardCentral.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;

import java.util.UUID;

@Service
public class RewardCentralService implements IRewardCentralService{


    @Autowired
    private RewardCentral rewardCentral;

    /**
     *
     * RewardCentral's method returns the number of reward points for an attraction
     *
     * @param attractionId
     * @param userId
     * @return Integer resultatRewardPoints
     */
    @Override
    public int getAttractionRewardPoints(UUID attractionId, UUID userId) {
        int resultatRewardPoints = rewardCentral.getAttractionRewardPoints(attractionId,userId);

        return resultatRewardPoints;
    }
}
