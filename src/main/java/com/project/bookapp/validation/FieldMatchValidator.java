package com.project.bookapp.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch,Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        this.firstFieldName = constraintAnnotation.first();
        this.secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Field firstField = o.getClass().getDeclaredField(firstFieldName);
            Field secondField = o.getClass().getDeclaredField(secondFieldName);

            firstField.setAccessible(true);
            secondField.setAccessible(true);

            Object firstValue = firstField.get(o);
            Object secondValue = secondField.get(o);

            return firstValue != null && firstValue.equals(secondValue);
        } catch (Exception e) {
            return false;
        }
    }
}
