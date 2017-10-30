package answer.king.validator;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import answer.king.model.Item;

@Component("beforeSaveItemValidator")
public class ItemValidator implements Validator {
 
    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.equals(clazz);
    }
 
    @Override
    public void validate(Object obj, Errors errors) {
        Item item = (Item) obj;
        if (checkInputString(item.getName())) {
            errors.rejectValue("name", "name.empty");
        }
    
        if (checkInputValue(item.getPrice())) {
            errors.rejectValue("price", "price.empty");
        }
        
        if (item.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            errors.rejectValue("price", "price.belowZero");
        }
        
        if (checkNumberOfDecimalPlaces(item.getPrice())) {
            errors.rejectValue("price", "price.needs2dp");
        }
    }
 
    private boolean checkInputString(String input) {
        return (input == null || input.trim().length() == 0);
    }
    
    private boolean checkInputValue(BigDecimal input) {
    	return input == null;
    }
    
    private boolean checkNumberOfDecimalPlaces(BigDecimal input) {
        String inputAsString = input.stripTrailingZeros().toPlainString();
        int indexOfDecimal = inputAsString.indexOf(".");
        int numberOfDp = indexOfDecimal < 0 ? 0 : inputAsString.length() - indexOfDecimal - 1;
        return numberOfDp < 3;
    }
}

