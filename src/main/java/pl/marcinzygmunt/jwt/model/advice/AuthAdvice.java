package pl.marcinzygmunt.jwt.model.advice;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.marcinzygmunt.jwt.model.exception.UserExistException;
import pl.marcinzygmunt.jwt.model.exception.UserNotFoundException;

@ControllerAdvice
public class AuthAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class, UserExistException.class})
    public ResponseEntity<Object> handleUserExceptions(
            RuntimeException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
