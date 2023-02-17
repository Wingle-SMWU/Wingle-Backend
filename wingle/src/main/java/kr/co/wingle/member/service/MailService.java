package kr.co.wingle.member.service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import kr.co.wingle.common.config.MailConfig;
import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.exception.CustomException;
import kr.co.wingle.common.util.RedisUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {
	private final MailConfig mailConfig;
	private final RedisUtil redisUtil;
	private final JavaMailSender emailSender;
	private final SpringTemplateEngine templateEngine;

	public String sendEmailCode(String to) {
		try {
			final String code = createCode();
			redisUtil.setDataExpire(to, code, mailConfig.getValidTime());
			MimeMessage message = createMessage(to, code);
			emailSender.send(message);
		} catch (MailException | MessagingException | UnsupportedEncodingException es) {
			throw new CustomException(ErrorCode.EMAIL_SEND_FAIL);
		}
		return to;
	}

	private String createCode() {
		final int CODE_LENGTH = 4;
		String code = "";
		Random random = new Random();
		for (int i = 0; i < CODE_LENGTH; i++) {
			code += random.nextInt(10); // 0~9
		}
		return code;
	}

	private MimeMessage createMessage(String to, String code) throws MessagingException, UnsupportedEncodingException {
		final String title = mailConfig.getTitle();
		final String from = mailConfig.getUsername();
		final String fromName = mailConfig.getName();

		MimeMessage message = emailSender.createMimeMessage();
		message.addRecipients(RecipientType.TO, to);
		message.setSubject(title);
		message.setText(setContext(code), "utf-8", "html");
		message.setFrom(new InternetAddress(from, fromName));

		return message;
	}

	private String setContext(String code) {
		Context context = new Context();
		context.setVariable("code", code);
		return templateEngine.process("mail", context); //mail.html
	}
}