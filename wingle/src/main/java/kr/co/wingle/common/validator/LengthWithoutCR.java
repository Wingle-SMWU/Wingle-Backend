package kr.co.wingle.common.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;

/**
 * The annotated element length must be between the specified boundaries (included).
 * The length will be calculated without the escape sequence character "/r" a.k.a. CR.
 * Supported types are:
 * CharSequence (length of character sequence is evaluated)
 * null elements are considered valid.
 * Author:
 * Minji Kim
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LengthWithoutCRValidator.class)
public @interface LengthWithoutCR {
	String message() default "size must be between {min} and {max}";

	Class[] groups() default {};

	Class[] payload() default {};

	/**
	 * @return size the element must be higher or equal to
	 */
	int min() default 0;

	/**
	 * @return size the element must be lower or equal to
	 */
	int max() default Integer.MAX_VALUE;
}
