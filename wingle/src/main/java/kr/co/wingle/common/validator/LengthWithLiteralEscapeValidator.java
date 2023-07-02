package kr.co.wingle.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LengthWithLiteralEscapeValidator implements
	ConstraintValidator<LengthWithLiteralEscape, String> {

	int min;
	int max;

	@Override
	public void initialize(LengthWithLiteralEscape constraintAnnotation) {
		this.min = constraintAnnotation.min();
		this.max = constraintAnnotation.max();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		if (value == null)
			return false;

		System.out.println("before: " + value.length());
		String replacedValue = value.replaceAll("\\\\t|\\\\n|\\\\x0B|\\\\f|\\\\r", " ");
		System.out.println("after: " + replacedValue.length());
		return min <= replacedValue.length() && replacedValue.length() <= max;
	}
}
