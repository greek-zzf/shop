package com.greek.shop.validator.annotaion;

import com.greek.shop.validator.constraints.PhoneNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 自定义手机参数校验注解
 *
 * @author Zhaofeng Zhou
 * @date 2021/11/9/009 14:36
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface Phone {

    String message() default "手机号码不合法";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
