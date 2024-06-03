package com.ecommerce.ecommerce_platform.advices;


import com.ecommerce.ecommerce_platform.exceptions.CustomerNotFoundException;
import com.ecommerce.ecommerce_platform.exceptions.OrderNotFoundException;
import com.ecommerce.ecommerce_platform.exceptions.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class EcommerceExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {ProductNotFoundException.class, CustomerNotFoundException.class, OrderNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(RuntimeException exception) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("Message", exception.getMessage());
        errorMap.put("TimeStamp", new Date().toString());
        errorMap.put("httpStatus", HttpStatus.NOT_FOUND.toString());

        return new ResponseEntity<>(errorMap, HttpStatus.NOT_FOUND);
    }
}
