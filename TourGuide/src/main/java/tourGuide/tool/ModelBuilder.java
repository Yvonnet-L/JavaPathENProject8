package tourGuide.tool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Component;
import tourGuide.dto.UserPreferencesDTO;
import tourGuide.user.UserPreferences;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

/**
 * This class allows the construction of a Model from a DTO
 */

@Component
public class ModelBuilder {

    private static Logger logger = LogManager.getLogger(DtoBuilder.class);

    private CurrencyUnit currency = Monetary.getCurrency("USD");

    public UserPreferences buildUserPreferences(final UserPreferencesDTO updto) {
        logger.info( " ---> Launch buildUserPreferences");
        return new UserPreferences(updto.getAttractionProximity(), currency, Money.of(updto.getLowerPricePoint(), currency),
                                    Money.of(updto.getHighPricePoint(), currency), updto.getTripDuration(),
                                        updto.getTicketQuantity(), updto.getNumberOfAdults(), updto.getNumberOfChildren());

    }
}
