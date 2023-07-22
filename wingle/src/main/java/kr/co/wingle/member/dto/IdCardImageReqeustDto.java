package kr.co.wingle.member.dto;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class IdCardImageReqeustDto {
	@NotNull(message = "파일이 없습니다.")
	private MultipartFile idCardImage;
}
