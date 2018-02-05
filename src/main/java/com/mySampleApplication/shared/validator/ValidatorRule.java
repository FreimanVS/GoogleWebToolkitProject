package com.mySampleApplication.shared.validator;


import com.mySampleApplication.shared.model.Employee;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class ValidatorRule {

    public static boolean isValid(Employee employee){
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        return violations.isEmpty();
    }
}
