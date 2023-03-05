package kr.co.wingle.profile.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileViewRequestDto {
	@NotNull
	private Boolean myProfile;
	private Long userId;

}
