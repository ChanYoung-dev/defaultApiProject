package com.chanlog.api.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * {
 *     "code": "400",
 *     "message": "잘못된 요청입니다.",
 *     "validation":{
 *         "title": "값을 입력해주세요.",
 *     }
 * }
 */

@Getter
//@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {

    private final String code;
    private final String message;
    //private final List<ValidationTuple> validation;

    private final List<ValidationTuple> validation;

    public void addValidation(String fieldName, String errorMessage){
        this.validation.add(new ValidationTuple(fieldName, errorMessage));
    }


    @Builder
    public ErrorResponse(String code, String message, List<ValidationTuple> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation != null ? validation: new ArrayList<ValidationTuple>();
    }
}
