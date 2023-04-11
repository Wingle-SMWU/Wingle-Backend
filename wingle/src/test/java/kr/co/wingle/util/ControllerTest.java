package kr.co.wingle.util;

import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public abstract class ControllerTest {

	@Autowired
	protected ObjectMapper mapper;

	@Autowired
	protected MockMvc mockMvc;

	protected String createJson(Object dto) throws JsonProcessingException {
		return mapper.writeValueAsString(dto);
	}
}
