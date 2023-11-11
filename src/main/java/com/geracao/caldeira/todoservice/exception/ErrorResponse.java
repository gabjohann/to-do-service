package com.geracao.caldeira.todoservice.exception;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ErrorResponse {
    private String message;
    private int code;
    private String status;
    private List<CustomError> errorList;

    public ErrorResponse() {
        this.errorList = new ArrayList<>();
    }
}
