package com.greek.shop.validator.constraints;

import com.greek.shop.validator.annotaion.Phone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author Zhaofeng Zhou
 * @date 2021/11/9/009 14:37
 */
public class PhoneNumberValidator implements ConstraintValidator<Phone, String> {

    private static final Pattern TEL_PATTERN = Pattern.compile("^[1][3,4,5,6,7,8,9][0-9]{9}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return TEL_PATTERN.matcher(value).find();
    }

}
