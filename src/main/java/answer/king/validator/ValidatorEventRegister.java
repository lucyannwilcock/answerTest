package answer.king.validator;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;

@Configuration
public class ValidatorEventRegister implements InitializingBean {
 
    @Autowired
    ValidatingRepositoryEventListener validatingRepositoryEventListener;
 
    @Autowired
    private Map<String, Validator> validators;
 
    @Override
    public void afterPropertiesSet() throws Exception {
        List<String> events = Arrays.asList("beforeSave");
        for (Map.Entry<String, Validator> entry : validators.entrySet()) {
            events.stream()
              .filter(p -> entry.getKey().startsWith(p))
              .findFirst()
              .ifPresent(
                p -> validatingRepositoryEventListener
               .addValidator(p, entry.getValue()));
        }
    }
}
