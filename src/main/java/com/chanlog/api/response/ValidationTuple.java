package com.chanlog.api.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ValidationTuple {
    private final String fieldName;
    private final String errorMessage;
}
