package kr.co.wingle.profile.dto;

import kr.co.wingle.common.validator.LengthWithLiteralEscape;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IntroductionRequestDto {

	@LengthWithLiteralEscape(min = 1, max = 400, message = "자기소개는 400자 이하만 가능합니다.")
	private String introduction;
}
