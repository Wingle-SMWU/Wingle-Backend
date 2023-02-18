package kr.co.wingle.member;

import java.io.FileInputStream;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import kr.co.wingle.member.dto.SignupRequestDto;
import kr.co.wingle.member.entity.Authority;
import kr.co.wingle.member.entity.Member;

public class MemberTemplate {
	public static final String EMAIL = "wingle@example.com";
	public static final String ANOTHER_EMAIL = "mingle@example.com";
	public static final String PASSWORD = "Wingle1234!$";
	public static final String NAME = "윙글";
	public static final String NICKNAME = "nickname";
	public static final boolean GENDER = true;
	public static final String NATION = "KR";

	private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

	public static Member makeTestMember() {
		return Member.createMember(NAME, "imageUrl", EMAIL, bCryptPasswordEncoder.encode(PASSWORD),
			Authority.ROLE_USER);
	}

	public static Member makeTestMember2() {
		return Member.createMember(NAME, "imageUrl", ANOTHER_EMAIL, bCryptPasswordEncoder.encode(PASSWORD),
			Authority.ROLE_USER);
	}

	public static SignupRequestDto makeTestSignUpRequestDto() throws Exception {
		return new SignupRequestDto(getTestIdCardImage(), EMAIL, PASSWORD, NAME, NICKNAME, GENDER, NATION, true, true);
	}

	public static MockMultipartFile getTestIdCardImage() throws Exception {
		String fileName = "testImage";
		String contentType = "png";
		String filePath = "src/test/resources/images/" + fileName + "." + contentType;
		FileInputStream fileInputStream = new FileInputStream(filePath);
		return new MockMultipartFile("idCardImage", fileName + "." + contentType, contentType, fileInputStream);
	}
}
