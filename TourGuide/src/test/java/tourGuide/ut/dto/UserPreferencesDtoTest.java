package tourGuide.ut.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tourGuide.dto.UserPreferencesDTO;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserPreferencesDtoTest {

    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    //---------- All Good---------------------------------------------------------------------------------
    @Test
    public void bidListDtoAllPasramsGoodTest() {
        // GIVEN
        UserPreferencesDTO userPreferencesDTO = new UserPreferencesDTO(200,50,300,1,4,2,2);
        // WHEN
        Set<ConstraintViolation< UserPreferencesDTO>> constraintViolations =
                validator.validate(userPreferencesDTO);
        // THEN
        assertEquals(0, constraintViolations.size());
    }
    //---------- All Empty---------------------------------------------------------------------------------
    @Test
    public void bidListDtoAllPasramsEmptyTest() {
        // GIVEN
        UserPreferencesDTO userPreferencesDTO = new UserPreferencesDTO();
        // WHEN
        Set<ConstraintViolation< UserPreferencesDTO>> constraintViolations =
                validator.validate(userPreferencesDTO);
        // THEN
        assertEquals(4, constraintViolations.size());
    }
    //---------- All Bad Params---------------------------------------------------------------------------------
    @Test
    public void bidListDtoAllNotConformParamsEmptyTest() {
        // GIVEN
        UserPreferencesDTO userPreferencesDTO = new UserPreferencesDTO(-200, -200,-500,0,0,-1,-2);
        // WHEN
        Set<ConstraintViolation< UserPreferencesDTO>> constraintViolations =
                validator.validate(userPreferencesDTO);
        // THEN
        assertEquals(7, constraintViolations.size());
    }
}
