package kr.co.wingle.profile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.exception.DuplicateException;
import kr.co.wingle.common.exception.NotFoundException;
import kr.co.wingle.common.util.AES256Util;
import kr.co.wingle.common.util.S3Util;
import kr.co.wingle.member.entity.Member;
import kr.co.wingle.member.service.AuthService;
import kr.co.wingle.member.service.MemberService;
import kr.co.wingle.profile.dto.InterestsRequestDto;
import kr.co.wingle.profile.dto.InterestsResponseDto;
import kr.co.wingle.profile.dto.IntroductionRequestDto;
import kr.co.wingle.profile.dto.IntroductionResponseDto;
import kr.co.wingle.profile.dto.LanguageDto;
import kr.co.wingle.profile.dto.LanguagesRequestDto;
import kr.co.wingle.profile.dto.LanguagesResponseDto;
import kr.co.wingle.profile.dto.ProfileGetResponseDto;
import kr.co.wingle.profile.dto.ProfileRegistrationResponseDto;
import kr.co.wingle.profile.dto.ProfileRequestDto;
import kr.co.wingle.profile.dto.ProfileResponseDto;
import kr.co.wingle.profile.dto.ProfileViewResponseDto;
import kr.co.wingle.profile.entity.Interest;
import kr.co.wingle.profile.entity.Language;
import kr.co.wingle.profile.entity.MemberInterest;
import kr.co.wingle.profile.entity.Profile;
import kr.co.wingle.profile.util.ProfileUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {
	private final AuthService authService;
	private final MemberService memberService;
	private final ProfileRepository profileRepository;
	private final LanguageRepository languageRepository;
	private final MemberInterestRepository memberInterestRepository;
	private final InterestRepository interestRepository;
	private final SnsRepository snsRepository;
	private final S3Util s3Util;
	private final AES256Util aes;
	private final ProfileUtil profileUtil;

	@Transactional
	public ProfileResponseDto saveProfile(ProfileRequestDto request) {
		Member member = authService.findAcceptedLoggedInMember();

		//TODO: setRegistration

		Profile profile = getProfileEntity(member);

		if (profileUtil.isDuplicatedNicknameByMemberId(request.getNickname(), member.getId())) {
			throw new DuplicateException(ErrorCode.DUPLICATE_NICKNAME);
		}

		// delete image
		if (request.isImageDelete()) {
			profile.setImageUrl(null);
		}
		// save image
		if (request.getImage() != null) {
			String imageUrl = uploadProfileImage(request.getImage());
			profile.setImageUrl(imageUrl);
		}

		// save nickname if change
		if (!profile.getNickname().equals(request.getNickname())) {
			profile.setNickname(request.getNickname());
		}

		return ProfileResponseDto.of(profile.getImageUrl(), profile.getNickname());
	}

	@Transactional
	public LanguagesResponseDto saveLanguages(LanguagesRequestDto request) {
		Member member = authService.findAcceptedLoggedInMember();

		// delete all memberLanguage
		List<Language> allByMember = languageRepository.findAllByMember(member);
		languageRepository.deleteAll(allByMember);

		// save
		int order = 1;
		Set<String> requestLanguageSet = new HashSet<>(request.getLanguages());
		for (String languageCode : requestLanguageSet) {
			Language language = Language.createLanguage(member, languageCode, order);
			languageRepository.save(language);
			order++;
		}

		List<LanguageDto> languages = languageRepository.findAllByMemberOrderByOrderNumberAsc(
				member).stream()
			.map(language ->
				LanguageDto.of(language.getOrderNumber(), language.getName())
			).toList();
		return LanguagesResponseDto.of(languages);
	}

	@Transactional
	public IntroductionResponseDto saveIntroduction(IntroductionRequestDto request) {
		Member member = authService.findAcceptedLoggedInMember();

		Profile profile = getProfileEntity(member);
		profile.setIntroduction(request.getIntroduction());

		return IntroductionResponseDto.of(profile.getIntroduction());
	}

	@Transactional
	public InterestsResponseDto saveInterests(InterestsRequestDto request) {
		Member member = authService.findAcceptedLoggedInMember();

		// delete all memberInterest
		List<MemberInterest> allByMember = memberInterestRepository.findAllByMember(member);
		memberInterestRepository.deleteAll(allByMember);

		// save
		request.getInterests().forEach(item -> {
			Interest interest = interestRepository.findByName(item).orElseGet(() -> createInterest(item));
			MemberInterest memberInterest = MemberInterest.createMemberInterest(member, interest);
			memberInterestRepository.save(memberInterest);
		});

		List<String> interests = new ArrayList<>();
		memberInterestRepository.findAllByMember(member)
			.forEach(memberInterest -> interests.add(memberInterest.getInterest().getName()));
		return InterestsResponseDto.of(interests);
	}

	private Interest createInterest(String item) {
		Interest interest = Interest.createInterest(item);
		return interestRepository.save(interest);
	}

	private Profile getProfileEntity(Member member) {
		return profileRepository.findByMember(member)
			.orElseThrow(() -> new NotFoundException(ErrorCode.PROFILE_NOT_FOUND));
	}

	public Profile getProfileByMemberId(Long memberId) {
		return profileRepository.findByMemberId(memberId)
			.orElseThrow(() -> new NotFoundException(
				ErrorCode.NO_PROFILE));
	}

	private String uploadProfileImage(MultipartFile profileImage) {
		return s3Util.profileImageUpload(profileImage);
	}

	private void setRegistration(Member member) {
		// Profile profile = getProfile(member);
		// profile.setRegistration(true);
	}

	public ProfileGetResponseDto getProfile() {
		Member member = authService.findAcceptedLoggedInMember();
		return getProfile(member);
	}

	@Transactional
	public ProfileGetResponseDto getProfile(Member member) {
		Profile profile = getProfileEntity(member);

		String imageUrl = profile.getImageUrl();
		String nickname = profile.getNickname();
		Boolean gender = profile.isGender();
		String nation = profile.getNation();
		String introduction = profile.getIntroduction();

		List<LanguageDto> languages = languageRepository.findAllByMemberOrderByOrderNumberAsc(member)
			.stream()
			.map(language ->
				LanguageDto.of(language.getOrderNumber(), language.getName()))
			.toList();

		List<String> interests = memberInterestRepository.findAllByMember(member)
			.stream()
			.map(interest -> interest.getInterest().getName())
			.toList();

		ProfileGetResponseDto response = ProfileGetResponseDto.of(
			imageUrl, nickname, gender, nation, languages,
			introduction, interests);

		return response;
	}

	public ProfileGetResponseDto getProfile(Long id) {
		Member member = memberService.findMemberByMemberId(id);

		ProfileGetResponseDto response = getProfile(member);

		return response;
	}

	public ProfileRegistrationResponseDto isRegister() {
		Member member = authService.findAcceptedLoggedInMember();
		Profile profile = getProfileEntity(member);

		Boolean registration = profile.isRegistration();
		ProfileRegistrationResponseDto response = ProfileRegistrationResponseDto.of(registration);
		return response;
	}

	public ProfileViewResponseDto getProfileDetail() {
		Member member = authService.findAcceptedLoggedInMember();
		Profile profile = getProfileEntity(member);
		String image = profile.getImageUrl();
		String nation = profile.getNation();
		String nickname = profile.getNickname();
		Boolean gender = profile.isGender();
		List<LanguageDto> languages = languageRepository.findAllByMemberOrderByOrderNumberAsc(
				member).stream()
			.map(language ->
				LanguageDto.of(language.getOrderNumber(), language.getName())
			).toList();
		List<MemberInterest> memberInterests = memberInterestRepository.findAllByMember(member);
		List<String> interests = memberInterests.stream()
			.map(memberInterest -> memberInterest.getInterest().getName())
			.toList();

		String introduce = profile.getIntroduction();
		String sns = snsRepository.findAllByMember(member);

		return ProfileViewResponseDto.of(image, nation, nickname, gender, languages, interests, introduce, sns);
	}
}
