package microservice_RewardCentral.controller;


import microservice_RewardCentral.service.IRewardCentralService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/RewardCentral")
public class RewardCentralController {

  @Autowired
  private IRewardCentralService rewardCentralService;

  private static final Logger LOGGER = LogManager.getLogger(RewardCentralController.class);

  @GetMapping("/attractionRewardPoints/{attractionId}/{userid}")
    public int getAttractionRewardPoints(@PathVariable("attractionId") UUID attractionId , @PathVariable("userid") UUID userId ){

      LOGGER.info("----> Launch /attractionRewardPoints/{attractionId}{userid}");
      int resultat = rewardCentralService.getAttractionRewardPoints(attractionId,userId);
      return resultat;
  }


}
