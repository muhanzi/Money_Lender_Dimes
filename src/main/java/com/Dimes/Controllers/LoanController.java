package com.Dimes.Controllers;

import com.Dimes.Models.Loan;
import com.Dimes.Services.LoanService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class LoanController {

    private LoanService loanService;

    Gson gson = new Gson();

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/createLoan")
    public ResponseEntity<String> createLoan(@RequestBody Loan loan) {

        if (loanService.createLoan(loan)) {
            return new ResponseEntity<>(gson.toJson("Loan created successfully"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(gson.toJson("An error occurred while creating loan"),
                    HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping("/deleteLoan/{id}")
    public ResponseEntity<String> deleteLoan(@PathVariable int id) {
        if (loanService.getLoanById(id) == null) {
            return new ResponseEntity<>(gson.toJson("Loan does not exist"), HttpStatus.BAD_REQUEST);
        }
        loanService.deleteLoan(id);
        return ResponseEntity.ok(gson.toJson("Loan deleted successfully"));
    }

    @GetMapping("/getAllLoans")
    public List<Loan> getAllLoans() {
        return loanService.getAllLoans();
    }

    @GetMapping("/getLoan/{id}")
    public Loan getLoanById(@PathVariable int id) {
        return loanService.getLoanById(id);
    }

    // lebon added this // to get all loans of a particular lender
    @GetMapping("/showLenderLoans")
    public List<Loan> LenderLoans(@RequestParam(value = "LenderId") int LenderId) {
        return loanService.getLenderLoans(LenderId);
    }

}
