package kr.co.wingle.profile;

import org.springframework.stereotype.Service;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.exception.DuplicateException;
import kr.co.wingle.member.entity.Member;
import kr.co.wingle.member.service.AuthService;
import kr.co.wingle.profile.entity.Language;
import kr.co.wingle.profile.entity.MemberInterest;
import kr.co.wingle.profile.entity.Profile;
import kr.co.wingle.profile.entity.Sns;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {
	private final AuthService authService;
	private final ProfileRepository profileRepository;
	private final LanguageRepository languageRepository;
	private final MemberInterestRepository memberInterestRepository;
	private final SnsRepository snsRepository;
	// private Profile profile;

	public ProfileResponseDto save(ProfileRequestDto request) {
		Member member = authService.findMember();
		String idCardImageUrl = member.getIdCardImageUrl();


		if (profileRepository.existsByNickname(request.getNickname()))
			throw new DuplicateException(ErrorCode.DUPLICATE_NICKNAME);
		else {
			Profile profile = Profile.createProfile(member, request.getNickname(), request.isGender(),
				request.getNation());
			//registration 여부 true
			profile.isRegistration();

			profileRepository.save(profile);
		}

		//Language, interest는 배열인데 이렇게 해도 되는지..
		//근데 List<Language>로 하면 languageRepository에서 에러가..
		Language language = Language.createLanguage(member, request.getName(), request.getOrderNumber());
		languageRepository.save(language);

		MemberInterest interests = MemberInterest.createMemberInterest(member, request.getInterest());
		memberInterestRepository.save(interests);


		String introduce = request.getIntroduction();
		// profileRepository.save(introduction);

		Sns sns = Sns.createSns(member, request.getUrl());
		snsRepository.save(sns);

		return ProfileResponseDto.of(idCardImageUrl, request.getNickname(), language,interests,introduce,sns);

	}
}
