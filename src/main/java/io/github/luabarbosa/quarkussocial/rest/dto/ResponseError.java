package io.github.luabarbosa.quarkussocial.rest.dto;

import javax.validation.ConstraintViolation;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ResponseError {  //representa objeto de retorno quando ocorre o erro
    private String message;
    private Collection<FieldError> errors;

    public ResponseError(String message, List<FieldError> errors) {
        this.message = message;
    }

    public static <T> ResponseError createFromValidation(
            Set<ConstraintViolation<T>> violations){
        List<FieldError> errors = violations
                .stream()
                .map(cv -> new FieldError(cv.getPropertyPath().toString(), cv.getMessage()))
                .collect(Collectors.toList());
        String message="Validations Error";
        var responseError = new ResponseError(message, errors);
        return responseError;

    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
