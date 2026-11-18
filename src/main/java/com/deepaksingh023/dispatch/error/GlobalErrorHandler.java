package com.deepaksingh023.dispatch.error;


import com.deepaksingh023.dispatch.dto.ErrorResponse;
import com.deepaksingh023.dispatch.error.exceptions.DuplicateOrderIdException;
import com.deepaksingh023.dispatch.error.exceptions.DuplicateVehicleException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalErrorHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validationExceptionHandler(MethodArgumentNotValidException ex){

        List<String> err = ex.getBindingResult().getFieldErrors().stream()
                .map(x -> x.getField() + ":" + x.getDefaultMessage())
                .toList();

        ErrorResponse errorResponse = new ErrorResponse(
                "VALIDATION_FAILED",
                "The request was bad",
                err,
                Instant.now()
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(DuplicateOrderIdException.class)
    public ResponseEntity<ErrorResponse> duplicateOrderIdExceptionHandler(DuplicateOrderIdException ex){
        ErrorResponse errorResponse = new ErrorResponse(
                "DUPLICATE_ORDER_ID",
                "The request contains duplicate orderId",
                null,
                Instant.now()
        );

        return ResponseEntity.badRequest().body(errorResponse);

    }

    @ExceptionHandler(DuplicateVehicleException.class)
    public ResponseEntity<ErrorResponse> duplicateVehicleIdExceptionHandler(DuplicateVehicleException ex){
        ErrorResponse errorResponse = new ErrorResponse(
                "DUPLICATE_VEHICLE_ID",
                "The request contains duplicate vehicleIds",
                null,
                Instant.now()
        );

        return ResponseEntity.badRequest().body(errorResponse);

    }

}
