# 자바 17 버전
FROM openjdk:17-jdk-alpine
# 관리자 이메일
LABEL maintainer="silver36c@gmail.com"
# 변수 정의
ARG JAR_FILE=wingle/build/libs/wingle-0.0.1-SNAPSHOT.jar
# 로컬의 JAR_FILE을 컨테이너에 app.jar이라는 이름으로 복사
COPY ${JAR_FILE} app.jar
# 8080 포트 열어주기
EXPOSE 8080
# 타임존 설정
ENV TZ=Asia/Seoul
# 컨테이너에 실행할 명령어
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 컨테이너 실행 시 반드시 실행될 명령어
# -Djava.security.egd=file:/dev/./urandom 옵션: 세션 ID를 생성할 때 필요한 난수 파일을 미리 적용시켜 톰캣의 실행 속도를 빠르게 함
ENTRYPOINT ["java","-Xmx400M","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar","--spring.profiles.active=docker"]
