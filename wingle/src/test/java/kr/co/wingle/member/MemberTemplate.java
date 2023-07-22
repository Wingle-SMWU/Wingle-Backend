package kr.co.wingle.member;

import static org.mockito.Mockito.*;

import java.io.FileInputStream;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import kr.co.wingle.affliation.entity.School;
import kr.co.wingle.member.dto.SignupRequestDto;
import kr.co.wingle.member.entity.Authority;
import kr.co.wingle.member.entity.Member;

public class MemberTemplate {
	public static final String EMAIL = "wingle@example.com";
	public static final String EMAIL2 = "wingle2@example.com";
	public static final String EMAIL3 = "wingle3@example.com";
	public static final String EMAIL4 = "wingle4@example.com";
	public static final String PASSWORD = "Wingle1234!$";
	public static final String NAME = "윙글";
	public static final String NICKNAME = "nickname";
	public static final boolean GENDER = true;
	public static final String NATION = "KR";
	public static final String DEPARTMENT = "학과";
	public static final String STUDENT_NUMBER = "0101010";
	public static final School SCHOOL = mock(School.class);
	private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

	public static Member makeTestMember() {
		return Member.createMember(NAME, "imageUrl", EMAIL, bCryptPasswordEncoder.encode(PASSWORD),
			Authority.ROLE_USER, SCHOOL, DEPARTMENT, STUDENT_NUMBER);
	}

	public static Member makeTestMember2() {
		return Member.createMember(NAME, "imageUrl", EMAIL2, bCryptPasswordEncoder.encode(PASSWORD),
			Authority.ROLE_USER, SCHOOL, DEPARTMENT, STUDENT_NUMBER);
	}

	public static Member makeTestMember3() {
		return Member.createMember(NAME, "imageUrl", EMAIL3, bCryptPasswordEncoder.encode(PASSWORD),
			Authority.ROLE_USER, SCHOOL, DEPARTMENT, STUDENT_NUMBER);
	}

	public static Member makeTestAdminMember() {
		return Member.createMember(NAME, "imageUrl", EMAIL4, bCryptPasswordEncoder.encode(PASSWORD),
			Authority.ROLE_ADMIN, SCHOOL, DEPARTMENT, STUDENT_NUMBER);
	}

	public static SignupRequestDto makeTestSignUpRequestDto() throws Exception {
		return new SignupRequestDto(getTestIdCardImage(), EMAIL, PASSWORD, NAME, true, NICKNAME, GENDER, NATION, true,
			true, false, SCHOOL.getId(), DEPARTMENT, STUDENT_NUMBER);
	}

	public static MockMultipartFile getTestIdCardImage() throws Exception {
		String fileName = "testImage";
		String contentType = "png";
		String filePath = "src/test/resources/images/" + fileName + "." + contentType;
		FileInputStream fileInputStream = new FileInputStream(filePath);
		return new MockMultipartFile("idCardImage", fileName + "." + contentType, contentType, fileInputStream);
	}

	public static MockMultipartFile getProfileImage() throws Exception {
		String fileName = "testImage";
		String contentType = "png";
		String filePath = "src/test/resources/images/" + fileName + "." + contentType;
		FileInputStream fileInputStream = new FileInputStream(filePath);
		return new MockMultipartFile("profileImage", fileName + "." + contentType, contentType, fileInputStream);
	}
}
