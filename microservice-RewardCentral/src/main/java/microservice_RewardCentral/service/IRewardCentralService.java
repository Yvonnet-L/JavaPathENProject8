package microservice_RewardCentral.service;

import java.util.UUID;


public interface IRewardCentralService {

    int getAttractionRewardPoints(UUID attractionId, UUID userId);
}
