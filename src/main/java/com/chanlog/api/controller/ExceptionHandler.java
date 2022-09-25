package com.chanlog.api.controller;


import com.chanlog.api.exception.ChanException;
import com.chanlog.api.exception.InvalidRequest;
import com.chanlog.api.exception.PostNotFound;
import com.chanlog.api.response.ErrorResponse;
import com.chanlog.api.response.ValidationTuple;
import org.modelmapper.ModelMapper;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {

//        List<ValidationTuple> errorList = new ArrayList<ValidationTuple>();
//        for (FieldError fieldError : e.getFieldErrors()) {
//            errorList.add(new ValidationTuple(fieldError.getField(), fieldError.getDefaultMessage()));
//        }
        List<ValidationTuple> errorList = mapValidToErrorResponse(e);


        ErrorResponse response = ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다.")
                .validation(errorList)
                .build();

        return response;
    }

    private List<ValidationTuple> mapValidToErrorResponse(MethodArgumentNotValidException e) {
        List<ValidationTuple> errorList = e.getFieldErrors().stream()
                .map(x -> new ValidationTuple(x.getField(), x.getDefaultMessage()))
                .collect(Collectors.toList());
        return errorList;
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(ChanException.class)
    public ResponseEntity<ErrorResponse> postNotFound(ChanException e){
        Integer statusCode = e.getStatusCode();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        ResponseEntity<ErrorResponse> response = ResponseEntity.status(statusCode)
                .body(body);

        return response;
    }
}
