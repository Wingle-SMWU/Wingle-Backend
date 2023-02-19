package kr.co.wingle.member.service;

import java.io.UnsupportedEncodingException;

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
import kr.co.wingle.member.mailVo.CodeMail;
import kr.co.wingle.member.mailVo.Mail;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
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
				final String code = mail.getValue();
				redisUtil.setDataExpire(to, code, CodeMail.VALID_TIME);
			}
			MimeMessage message = createMessage(to);
			emailSender.send(message);
		} catch (MailException | MessagingException | UnsupportedEncodingException es) {
			throw new CustomException(ErrorCode.EMAIL_SEND_FAIL);
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
		final String name = "value";
		Context context = new Context();
		if (!mail.getValue().isEmpty())
			context.setVariable(name, mail.getValue());
		return templateEngine.process(mail.getFileName(), context);
	}
}
