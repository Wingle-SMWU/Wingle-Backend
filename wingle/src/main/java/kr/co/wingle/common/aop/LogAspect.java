package kr.co.wingle.common.aop;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Aspect
@Component
public class LogAspect {
	private static final String LOG_MESSAGE_FORMAT = "{} {} : {}.{}() took {}ms. (group by '{}', traceID: {})";

	@Pointcut("execution(* kr.co.wingle..*Controller.*(..)) || execution(* kr.co.wingle..*Service.*(..)) || execution(* kr.co.wingle..*Repository.*(..))")
	private void onRequest() {
	}

	@Pointcut("execution(* kr.co.wingle.common.exception..*(..))")
	private void onException() {
	}

	@Around("onRequest() || onException()")
	public Object apiLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		long start = System.currentTimeMillis();
		Object result = proceedingJoinPoint.proceed();
		String traceId = MDC.get("traceId");
		long end = System.currentTimeMillis();
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();

		try {
			String className = proceedingJoinPoint.getSignature().getDeclaringType().getName();
			String methodName = proceedingJoinPoint.getSignature().getName();
			long executionTime = end - start;
			log.info(LOG_MESSAGE_FORMAT, request.getMethod(), request.getRequestURI(),
				className, methodName, executionTime, group(className), traceId);
		} catch (Exception e) {
			log.error("LogAspect error", e);
		}

		return result;
	}

	private String group(String className) {
		String group = "";
		if (className.contains("Controller")) {
			group = "Controller";
		} else if (className.contains("Service")) {
			group = "Service";
		} else if (className.contains("Repository")) {
			group = "Repository";
		} else if (className.contains("Exception")) {
			group = "ExceptionHandler";
		}
		return group;
	}
}
