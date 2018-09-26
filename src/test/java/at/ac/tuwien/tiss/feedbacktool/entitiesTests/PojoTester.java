package at.ac.tuwien.tiss.feedbacktool.entitiesTests;

import at.ac.tuwien.tiss.feedbacktool.entities.Answer;
import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.PojoValidator;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import org.junit.Test;

/**
 * Tests all Pojo Classes
 */
public class PojoTester {
    // The package to be tested
    private String packageName = "at.ac.tuwien.tiss.feedbacktool.entities";
    @Test
    public void validate() {
        Validator validator = ValidatorBuilder.create()
                .with(new SetterMustExistRule(),
                        new GetterMustExistRule())
                .with(new SetterTester(),
                        new GetterTester())
                .build();
        validator.validate(packageName);

    }
}
