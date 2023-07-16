package kr.co.wingle.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LengthWithoutCRValidator implements
	ConstraintValidator<LengthWithoutCR, String> {

	int min;
	int max;

	@Override
	public void initialize(LengthWithoutCR constraintAnnotation) {
		this.min = constraintAnnotation.min();
		this.max = constraintAnnotation.max();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		if (value == null)
			return false;

		String replacedValue = value.replaceAll("\\r", "");
		return min <= replacedValue.length() && replacedValue.length() <= max;
	}
}
