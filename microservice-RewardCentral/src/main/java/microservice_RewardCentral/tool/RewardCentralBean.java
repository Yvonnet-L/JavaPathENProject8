package microservice_RewardCentral.tool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rewardCentral.RewardCentral;

@Configuration
public class RewardCentralBean {

    /**
     *
     * @return  new RewardCentral()
     */
    @Bean
    public RewardCentral getRewardCentral(){
        return new RewardCentral();
    }
}
