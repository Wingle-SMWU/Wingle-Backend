package kr.co.wingle.common.jwt;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kr.co.wingle.member.dto.TokenDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TokenProvider {
	private final Key key;

	public TokenProvider(@Value("${jwt.secret}") String secret) {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	public TokenDto generateTokenDto(Authentication authentication) {
		String authorities = authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));

		long now = (new Date()).getTime();
		Date accessTokenExpiration = new Date(now + TokenInfo.ACCESS_TOKEN_EXPIRE_TIME);
		String accessToken = Jwts.builder()
			.setSubject(authentication.getName())
			.claim(TokenInfo.AUTHORITIES_KEY, authorities)
			.setExpiration(accessTokenExpiration)
			.signWith(key, SignatureAlgorithm.HS512)
			.compact();

		Date refreshTokenExpiration = new Date(now + TokenInfo.REFRESH_TOKEN_EXPIRE_TIME);
		String refreshToken = Jwts.builder()
			.setExpiration(refreshTokenExpiration)
			.signWith(key, SignatureAlgorithm.HS512)
			.compact();

		return TokenDto.of(accessToken, refreshToken);
	}

	public Authentication getAuthentication(String accessToken) {
		Claims claims = parseClaims(accessToken);

		if (claims.get(TokenInfo.AUTHORITIES_KEY) == null) {
			throw new IllegalArgumentException("권한 정보가 없는 토큰입니다.");
		}

		Collection<? extends GrantedAuthority> authorities =
			Arrays.stream(claims.get(TokenInfo.AUTHORITIES_KEY).toString().split(","))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		UserDetails principal = new User(claims.getSubject(), "", authorities);

		return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
	}

	public boolean validateToken(String token, HttpServletRequest request) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (io.jsonwebtoken.security.SignatureException | MalformedJwtException exception) {
			logInfo("잘못된 JWT 서명을 가진 토큰입니다.", request);
			throw exception;
		} catch (ExpiredJwtException exception) {
			logInfo("만료된 JWT 토큰입니다.", request);
			throw exception;
		} catch (UnsupportedJwtException exception) {
			logInfo("지원하지 않는 JWT 토큰입니다.", request);
			throw exception;
		} catch (IllegalArgumentException exception) {
			logInfo("잘못된 JWT 토큰입니다.", request);
			throw exception;
		}
	}

	private Claims parseClaims(String accessToken) {
		try {
			return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}

	private void logInfo(String message, HttpServletRequest request) {
		log.info("{} {} : {}", request.getMethod(), request.getRequestURI(), message);
	}
}
