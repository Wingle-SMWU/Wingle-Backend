package kr.co.wingle.member;

import org.springframework.web.bind.annotation.RestController;

import kr.co.wingle.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;
}
