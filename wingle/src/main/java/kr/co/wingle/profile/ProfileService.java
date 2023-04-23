package kr.co.wingle.profile;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.exception.DuplicateException;
import kr.co.wingle.common.exception.NotFoundException;
import kr.co.wingle.common.util.S3Util;
import kr.co.wingle.member.entity.Member;
import kr.co.wingle.member.service.AuthService;
import kr.co.wingle.profile.dto.InterestsRequestDto;
import kr.co.wingle.profile.dto.InterestsResponseDto;
import kr.co.wingle.profile.dto.IntroductionRequestDto;
import kr.co.wingle.profile.dto.IntroductionResponseDto;
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
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {
	private final AuthService authService;
	private final ProfileRepository profileRepository;
	private final LanguageRepository languageRepository;
	private final MemberInterestRepository memberInterestRepository;
	private final InterestRepository interestRepository;
	private final SnsRepository snsRepository;
	private final S3Util s3Util;

	@Transactional
	public ProfileResponseDto saveProfile(ProfileRequestDto request) {
		Member member = authService.findMember();

		//TODO: setRegistration

		Profile profile = getProfile(member);

		if (!profile.getNickname().equals(request.getNickname()) &&
			profileRepository.existsByNickname(request.getNickname())) {
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
		Member member = authService.findMember();

		// delete all memberLanguage
		List<Language> allByMember = languageRepository.findAllByMember(member);
		languageRepository.deleteAll(allByMember);

		// save
		int order = 1;
		for (String languageCode : request.getLanguages()) {
			Language language = Language.createLanguage(member, languageCode, order);
			languageRepository.save(language);
			order++;
		}

		List<LanguagesResponseDto.LanguageDto> languages = languageRepository.findAllByMemberOrderByOrderNumberAsc(
				member).stream()
			.map(language ->
				LanguagesResponseDto.LanguageDto.of(language.getOrderNumber(), language.getName())
			).toList();
		return LanguagesResponseDto.of(languages);
	}

	@Transactional
	public IntroductionResponseDto saveIntroduction(IntroductionRequestDto request) {
		Member member = authService.findMember();

		Profile profile = getProfile(member);
		profile.setIntroduction(request.getIntroduction());

		return IntroductionResponseDto.of(profile.getIntroduction());
	}

	@Transactional
	public InterestsResponseDto saveInterests(InterestsRequestDto request) {
		Member member = authService.findMember();

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

	private Profile getProfile(Member member) {
		return profileRepository.findByMember(member)
			.orElseThrow(() -> new NotFoundException(ErrorCode.PROFILE_NOT_FOUND));
	}

	public Profile getProfileByMemberId(Long memberId) {
		return profileRepository.findByMemberId(memberId)
			.orElseThrow(() -> new NotFoundException(
				ErrorCode.NO_PROFILE));
	}

	private String uploadProfileImage(MultipartFile idCardImage) {
		return s3Util.profileImageUpload(idCardImage);
	}

	private void setRegistration(Member member) {
		// Profile profile = getProfile(member);
		// profile.setRegistration(true);
	}

	@Transactional
	public ProfileGetResponseDto getProfile() {
		Member member = authService.findMember();
		Profile profile = getProfile(member);

		String imageUrl = profile.getImageUrl();
		String nickname = profile.getNickname();
		Boolean gender = profile.isGender();
		String nation = profile.getNation();

		ProfileGetResponseDto response = ProfileGetResponseDto.of(imageUrl, nickname, gender, nation);

		return response;
	}

	public ProfileRegistrationResponseDto isRegister() {
		Member member = authService.findMember();
		Profile profile = getProfile(member);

		Boolean registration = profile.isRegistration();
		ProfileRegistrationResponseDto response = ProfileRegistrationResponseDto.of(registration);
		return response;
	}

	public ProfileViewResponseDto getProfileDetail() {
		Member member = authService.findMember();
		Profile profile = getProfile(member);
		String image = member.getIdCardImageUrl();
		String nation = profile.getNation();
		String nickname = profile.getNickname();
		Boolean gender = profile.isGender();
		List<LanguagesResponseDto.LanguageDto> languages = languageRepository.findAllByMemberOrderByOrderNumberAsc(
				member).stream()
			.map(language ->
				LanguagesResponseDto.LanguageDto.of(language.getOrderNumber(), language.getName())
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
