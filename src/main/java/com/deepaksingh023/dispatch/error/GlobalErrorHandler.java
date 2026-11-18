package com.deepaksingh023.dispatch.error;


import com.deepaksingh023.dispatch.dto.ErrorResponse;
import com.deepaksingh023.dispatch.error.exceptions.DuplicateOrderIdException;
import com.deepaksingh023.dispatch.error.exceptions.DuplicateVehicleException;
import com.deepaksingh023.dispatch.error.exceptions.NoOrdersToProcess;
import com.deepaksingh023.dispatch.error.exceptions.NoVehicleToProcessOrders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Instant;

@RestControllerAdvice
public class GlobalErrorHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validationExceptionHandler(MethodArgumentNotValidException ex){

        ErrorResponse errorResponse = new ErrorResponse(
                "VALIDATION_FAILED",
                "The request was bad" ,
                Instant.now()
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(DuplicateOrderIdException.class)
    public ResponseEntity<ErrorResponse> duplicateOrderIdExceptionHandler(DuplicateOrderIdException ex){
        ErrorResponse errorResponse = new ErrorResponse(
                "DUPLICATE_ORDER_ID",
                "The request contains duplicate orderId",
                Instant.now()
        );

        return ResponseEntity.badRequest().body(errorResponse);

    }

    @ExceptionHandler(DuplicateVehicleException.class)
    public ResponseEntity<ErrorResponse> duplicateVehicleIdExceptionHandler(DuplicateVehicleException ex){
        ErrorResponse errorResponse = new ErrorResponse(
                "DUPLICATE_VEHICLE_ID",
                "The request contains duplicate vehicleIds",
                Instant.now()
        );

        return ResponseEntity.badRequest().body(errorResponse);

    }

    @ExceptionHandler(NoOrdersToProcess.class)
    public ResponseEntity<ErrorResponse> noOrdersToProcessExceptionHandler(NoOrdersToProcess noOrdersToProcess){

        ErrorResponse errorResponse = new ErrorResponse(
                "NO_ORDER_TO_PROCESS",
                "The database doesnt have any orders to process",
                Instant.now()
        );

        return ResponseEntity.ok(errorResponse);
    }

    @ExceptionHandler(NoVehicleToProcessOrders.class)
    public ResponseEntity<ErrorResponse> noVehicleToProcessOrders(NoVehicleToProcessOrders ex){

        ErrorResponse errorResponse = new ErrorResponse(
                "NO__VEHICLE_TO_CARRY_ORDERS",
                "There is no vehicle available in db to carry the orders",
                Instant.now()
        );

        return ResponseEntity.ok(errorResponse);
    }



}
