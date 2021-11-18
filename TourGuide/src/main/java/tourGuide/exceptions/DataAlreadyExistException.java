package tourGuide.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.CONFLICT)
public class DataAlreadyExistException extends RuntimeException{

    private static Logger logger = LogManager.getLogger(DataNotFoundException.class);


    public DataAlreadyExistException(String message) {
        super(message);
        logger.error("  **--> " + message);
    }
}
