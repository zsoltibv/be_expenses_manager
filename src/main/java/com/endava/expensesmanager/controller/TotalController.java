package com.endava.expensesmanager.controller;

import com.endava.expensesmanager.service.TotalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/total")
public class TotalController {

    TotalService totalService;

    @Autowired
    public TotalController(TotalService totalService) {
        this.totalService = totalService;
    }

    @GetMapping
    public ResponseEntity<?> getTotalSum(@RequestParam int userId, @RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate, @RequestParam String code) {

        return new ResponseEntity<>(totalService.totalExpenseSum(userId, startDate, endDate, code), HttpStatus.OK);
    }

    @GetMapping("/category")
    public ResponseEntity<?> getTotalSumCategory(@RequestParam int userId, @RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate, @RequestParam String code) {
        if (endDate.isAfter(LocalDateTime.now()))
            return new ResponseEntity<>("The end date is invalid", HttpStatus.BAD_REQUEST);


        return new ResponseEntity<>(totalService.totalExpenseCategory(userId, startDate, endDate, code), HttpStatus.OK);

    }

}
