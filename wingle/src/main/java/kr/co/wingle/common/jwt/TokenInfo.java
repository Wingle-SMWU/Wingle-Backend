package kr.co.wingle.common.jwt;

public class TokenInfo {
	private TokenInfo() {
	}

	public static final String AUTHORITIES_KEY = "AUTH";
	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String BEARER_TYPE = "Bearer";
	public static final int START_TOKEN_LOCATION = 7;
	// public static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30L;             // 30분
	public static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 60 * 24 * 365L; // 1년
	// public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7L;  // 1주
	public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 60 * 24 * 365L; // 1년
}
