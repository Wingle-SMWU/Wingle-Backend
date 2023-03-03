package kr.co.wingle.profile.dto;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LanguagesRequestDto {
	@NotNull
	@Size(min = 1)
	private List<String> languages;
}
