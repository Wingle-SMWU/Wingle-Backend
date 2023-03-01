package kr.co.wingle.profile.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IntroductionRequestDto {
	@NotNull
	private String introduction;
}
