package com.Dimes.Services;

import com.Dimes.Models.Loan;
import com.Dimes.Repositories.LoanRepository;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class LoanService {

    private LoanRepository loanRepository;

    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public boolean createLoan(Loan loan) {

        Objects.requireNonNull(loan, "Loan can not be null");

        Loan loan1 = loanRepository.save(loan);
        if (loan1 != null) {
            return true;
        }
        return false;
    }

    public void deleteLoan(int id) {
        // Objects.requireNonNull(id,"Id can not be null");
        loanRepository.deleteById(id);
    }

    public Loan getLoanById(int id) {
        // Objects.requireNonNull(id,"Id can not be null");

        return loanRepository.findById(id).orElse(null);
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    // lebon added this
    public List<Loan> getLenderLoans(int LenderId) {
        return loanRepository.findAllByLender(LenderId);
    }

}
