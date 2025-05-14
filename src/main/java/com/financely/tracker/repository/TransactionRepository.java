package com.financely.tracker.repository;

import com.financely.tracker.entity.MyUser;
import com.financely.tracker.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByUser(MyUser user);
    List<Transaction> findByUserAndDateBetween(MyUser user, LocalDate startDate,LocalDate endDate);
}
