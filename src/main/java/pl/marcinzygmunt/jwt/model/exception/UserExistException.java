package pl.marcinzygmunt.jwt.model.exception;

public class UserExistException extends RuntimeException {
    private static final String USER_EXIST_EXCEPTION_MESSAGE = "User already exists";

    public UserExistException(String message) {
        super(message);
    }

    public UserExistException() {
        super(USER_EXIST_EXCEPTION_MESSAGE);
    }


}


