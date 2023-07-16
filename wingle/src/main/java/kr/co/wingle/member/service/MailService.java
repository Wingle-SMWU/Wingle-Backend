package kr.co.wingle.member.service;

import java.io.UnsupportedEncodingException;
import java.util.Map;

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
import kr.co.wingle.common.exception.BadRequestException;
import kr.co.wingle.common.util.RedisUtil;
import kr.co.wingle.member.mailVo.CodeMail;
import kr.co.wingle.member.mailVo.Mail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {
	private final MailConfig mailConfig;
	private final RedisUtil redisUtil;
	private final JavaMailSender emailSender;
	private final SpringTemplateEngine templateEngine;
	private Mail mail;

	public String sendEmail(String to, Mail mail) {
		try {
			this.mail = mail;
			if (mail instanceof CodeMail) {
				final String code = mail.getValues().get("code");
				redisUtil.setDataExpire(to, code, CodeMail.VALID_TIME);
			}
			MimeMessage message = createMessage(to);
			emailSender.send(message);
		} catch (MailException | MessagingException | UnsupportedEncodingException es) {
			log.error(es.getMessage());
			log.error(es.getStackTrace().toString());
			throw new BadRequestException(ErrorCode.EMAIL_SEND_FAIL);
		}
		return to;
	}

	private MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {
		final String from = mailConfig.getUsername();
		final String fromName = mailConfig.getName();

		MimeMessage message = emailSender.createMimeMessage();
		message.addRecipients(RecipientType.TO, to);
		message.setSubject(mail.getTitle());
		message.setText(setContext(), "utf-8", "html");
		message.setFrom(new InternetAddress(from, fromName));

		return message;
	}

	private String setContext() {
		Context context = new Context();
		Map<String, String> values = mail.getValues();

		if (!values.isEmpty()) {
			for (String key : values.keySet())
				context.setVariable(key, values.get(key));
		}

		return templateEngine.process(mail.getFileName(), context);
	}
}
