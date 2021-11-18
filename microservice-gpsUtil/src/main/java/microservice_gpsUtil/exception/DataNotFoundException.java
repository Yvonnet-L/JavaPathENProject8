package microservice_gpsUtil.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DataNotFoundException extends RuntimeException{

    private static final Logger logger = LogManager.getLogger(DataNotFoundException.class);

    public DataNotFoundException(String message) {
        super(message);
        logger.error("  **--> " + message);
    }
}
