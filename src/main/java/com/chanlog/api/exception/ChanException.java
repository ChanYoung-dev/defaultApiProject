package com.chanlog.api.exception;

import com.chanlog.api.response.ValidationTuple;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public abstract class ChanException extends RuntimeException{

    public final List<ValidationTuple> validation = new ArrayList<ValidationTuple>();
    public ChanException(String message) {
        super(message);
    }

    public ChanException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();
    public void addValidation(String fieldName, String message){
        validation.add(new ValidationTuple(fieldName, message));
    }
}
