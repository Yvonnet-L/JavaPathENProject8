package microservice_TripPricer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tripPricer.TripPricer;

@Configuration
public class TripPricerBean {

    @Bean
    public TripPricer getTripPricer(){
        return new TripPricer();
    }

}
