package kr.co.wingle.affliation.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.wingle.affliation.dto.SchoolResponseDto;
import kr.co.wingle.affliation.entity.School;
import kr.co.wingle.affliation.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SchoolService {
	private final SchoolRepository schoolRepository;

	@Transactional(readOnly = true)
	public List<SchoolResponseDto> getSchools() {
		List<School> schools = schoolRepository.findAll(Sort.by(Sort.Direction.ASC, "code"));
		return schools.stream()
			.map(x -> SchoolResponseDto.from(x.getId(), x.getCode(), x.getName()))
			.collect(Collectors.toList());
	}
}
