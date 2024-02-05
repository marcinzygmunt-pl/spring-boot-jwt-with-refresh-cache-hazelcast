package pl.marcinzygmunt.jwt.model.exception;

public class JwtValidationException extends RuntimeException{
    public JwtValidationException(String message) {
        super(message);
    }

}
