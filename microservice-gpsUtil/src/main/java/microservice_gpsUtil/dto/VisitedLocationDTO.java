package microservice_gpsUtil.dto;


import java.util.Date;
import java.util.UUID;

public class VisitedLocationDTO {
    public final UUID userId;
    public final Location location;
    public final Date timeVisited;


    public VisitedLocationDTO(UUID userId, Location location, Date timeVisited) {
        this.userId = userId;
        this.location = location;
        this.timeVisited = timeVisited;
    }

}
