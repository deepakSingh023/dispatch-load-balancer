package com.deepaksingh023.dispatch.controller;
import com.deepaksingh023.dispatch.dto.FullResponse;
import com.deepaksingh023.dispatch.dto.PlanResponse;
import com.deepaksingh023.dispatch.service.DispatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dispatch/")
public class DispatchController {

    private final DispatchService dispatchService;

    public DispatchController(DispatchService dispatchService){
         this.dispatchService= dispatchService;
    }

    @GetMapping("/plan")
    public ResponseEntity<FullResponse> returnDispatchPlan(){

         FullResponse res = dispatchService.sendDispatchPlan();

        return ResponseEntity.ok(res);
    }
}
