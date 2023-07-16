package kr.co.wingle.profile.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequestDto {
	private MultipartFile image;

	@NotNull
	private boolean imageDelete;

	@Pattern(regexp = "^[0-9a-zA-Z가-힣]{2,10}$", message = "닉네임은 한글/영문/숫자 2자 이상 10자 미만이어야 합니다.")
	private String nickname;
}
