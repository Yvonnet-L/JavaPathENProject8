package tourGuide.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tourGuide.beans.Attraction;
import tourGuide.beans.VisitedLocation;

/**
 * Data Transfer Object, userreward data receptacle
 * @See UserReward
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRewardDTO {

    private VisitedLocation visitedLocation;

    private Attraction attraction;

    private int rewardPoints;

}
