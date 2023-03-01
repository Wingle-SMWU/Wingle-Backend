package kr.co.wingle.profile.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

	@NotBlank(message = "닉네임이 없습니다.")
	private String nickname;
}
