package pl.marcinzygmunt.jwt.model.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.marcinzygmunt.jwt.model.exception.RefreshTokenNotFoundException;

@ControllerAdvice
public class RefreshTokenAdvice extends ResponseEntityExceptionHandler  {
    @ExceptionHandler({RefreshTokenNotFoundException.class})
    public ResponseEntity<Object> handleRefreshTokenNotFound(
            RuntimeException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
