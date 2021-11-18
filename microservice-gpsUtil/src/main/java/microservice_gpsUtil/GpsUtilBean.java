package microservice_gpsUtil;

import gpsUtil.GpsUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * je l'ai nomé Bean mais dans TourGuide c'est Module, --> "il y a t-il une nomenclature particulière?"
 */
@Configuration
public class GpsUtilBean {

    /**
     *
     * @return new GpsUtil()
     */
    @Bean
    public GpsUtil getGpsUtil() {
        return new GpsUtil();
    }

}
