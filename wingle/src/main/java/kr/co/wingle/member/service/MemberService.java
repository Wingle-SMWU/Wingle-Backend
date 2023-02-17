package kr.co.wingle.member.service;

import org.springframework.stereotype.Service;

import kr.co.wingle.member.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
}
