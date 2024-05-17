package com.vietanhcoder.svnhotel.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundExeption.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> resourceNotFoundExeption(ResourceNotFoundExeption ex) {
        Map<String, String> map = new HashMap<>();
        map.put("message", ex.getMessage());

        return map;
    }

    @ExceptionHandler(PhotoRetrievingExeption.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> photoRetrievingExeption(PhotoRetrievingExeption ex) {
        Map<String, String> map = new HashMap<>();
        map.put("message", ex.getMessage());

        return map;
    }

    @ExceptionHandler(InvalidBookingRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> InvalidBookingRequestException(PhotoRetrievingExeption ex) {
        Map<String, String> map = new HashMap<>();
        map.put("message", ex.getMessage());

        return map;
    }


}
