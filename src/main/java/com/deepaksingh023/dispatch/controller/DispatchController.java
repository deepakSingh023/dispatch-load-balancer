package com.deepaksingh023.dispatch.controller;


import com.deepaksingh023.dispatch.dto.DispatchPlan;
import com.deepaksingh023.dispatch.service.DispatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dispatch/")
public class DispatchController {

    private final DispatchService dispatchService;

    public DispatchController(DispatchService dispatchService){
         this.dispatchService= dispatchService;
    }

    @GetMapping("/plan")
    public ResponseEntity<DispatchPlan> returnDispatchPlan(){

        DispatchPlan response = dispatchService.sendDispatchPlan();

        return ResponseEntity.ok(response);
    }
}
