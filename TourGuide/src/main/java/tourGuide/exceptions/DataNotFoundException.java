package tourGuide.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DataNotFoundException extends RuntimeException{

    private static Logger logger = LogManager.getLogger(DataNotFoundException.class);

    private static final long serialVersionUID = -3988552783621921445L;

    public DataNotFoundException(String message) {
        super(message);
        logger.error("  **--> " + message);
    }
}

