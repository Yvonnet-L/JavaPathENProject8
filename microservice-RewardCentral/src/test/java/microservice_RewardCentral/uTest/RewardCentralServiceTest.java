package microservice_RewardCentral.uTest;


import microservice_RewardCentral.service.RewardCentralService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import rewardCentral.RewardCentral;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class RewardCentralServiceTest {

    @InjectMocks
    RewardCentralService rewardCentralService;

    @Mock
    private RewardCentral rewardCentral;

    //-------Test-------getAttractionRewardPoints()------------------------------------------------------------------------
    @Test
    @DisplayName("Test on getAttractionRewardPoints")
    public void getAttractionRewardPointsTestWithGoodParameters(){
        // GIVEN
        UUID AttractionId = UUID.randomUUID();
        UUID UserId = UUID.randomUUID();
        int resultatRewardPoints = 1 + (int)(Math.random() * ((1000 - 1) + 1));
        // WHEN
        Mockito.when(rewardCentral.getAttractionRewardPoints(AttractionId,UserId)).thenReturn(resultatRewardPoints);
        // THEN
        assertThat(rewardCentralService.getAttractionRewardPoints(AttractionId,UserId)).isEqualTo(resultatRewardPoints);
    }

}
