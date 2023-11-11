package com.geracao.caldeira.todoservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ValidationExceptionHandler {
    @ExceptionHandler(ValidationException.class) // Substitua "ValidationException" pela exceção que deseja tratar.
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(ValidationException ex) {
        ErrorResponse response = new ErrorResponse();
        response.setMessage("Requisição possui campos inválidos");
        response.setCode(400);
        response.setStatus("Bad Request");

        CustomError error = new CustomError();
        error.setMessage(ex.getMessage());

        return response;
    }
}
