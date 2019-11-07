package com.Dimes.Repositories;

import java.util.List;

import com.Dimes.Models.Loan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LoanRepository extends JpaRepository<Loan, Integer> {

    @Query(value = "SELECT * FROM loans WHERE lender=?1", nativeQuery = true)
    List<Loan> findAllByLender(Integer id);

}
