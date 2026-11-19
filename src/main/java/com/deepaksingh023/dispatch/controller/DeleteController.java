package com.deepaksingh023.dispatch.controller;


import com.deepaksingh023.dispatch.service.DeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/dispatch")
public class DeleteController {

    private final DeleteService deleteService;

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAllData(){

        deleteService.deleteAllEntries();

        return ResponseEntity.ok("Everything deleted successfully");

    }

}
