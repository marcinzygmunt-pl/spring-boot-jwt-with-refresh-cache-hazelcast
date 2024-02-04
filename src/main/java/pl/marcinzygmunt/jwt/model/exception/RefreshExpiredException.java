package pl.marcinzygmunt.jwt.model.exception;

public class RefreshExpiredException extends RuntimeException{
    private static final String REFRESH_TOKEN_EXPIRED_MESSAGE = "Refresh token was expired. Please make a new signin request";

    public RefreshExpiredException(String message) {
        super(message);
    }

    public RefreshExpiredException() {
        super(REFRESH_TOKEN_EXPIRED_MESSAGE);
    }
}
