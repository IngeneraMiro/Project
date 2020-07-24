package presentation.demo.advizors;

import com.google.gson.JsonSyntaxException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestControllerErrorHandling {

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String internalServerError(Exception ex) {
        System.out.println(ex.getClass().getCanonicalName());
        return "Internal error";
    }

    @ExceptionHandler(value = {JsonSyntaxException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String badParameters(JsonSyntaxException ex) {
        System.out.println(ex.getClass().getCanonicalName());
        return "Bad Request parameters !";
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String internalServerError(AccessDeniedException ex) {
        System.out.println(ex.getClass().getCanonicalName());
        return "No Permission !!!";
    }

}
