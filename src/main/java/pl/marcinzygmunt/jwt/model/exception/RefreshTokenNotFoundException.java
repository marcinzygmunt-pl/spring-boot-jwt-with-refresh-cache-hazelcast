package pl.marcinzygmunt.jwt.model.exception;

public class RefreshTokenNotFoundException extends RuntimeException {
    private static final String REFRESH_TOKEN_NOT_FOUND_MESSAGE = "Refresh token not found!";

    public RefreshTokenNotFoundException(String message) {
        super(message);
    }

    public RefreshTokenNotFoundException() {
        super(REFRESH_TOKEN_NOT_FOUND_MESSAGE);
    }


}


