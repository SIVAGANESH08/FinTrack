package com.financely.tracker.service;

import com.financely.tracker.entity.MyUser;
import com.financely.tracker.entity.Transaction;
import com.financely.tracker.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction addTransaction(Transaction tx){
        return transactionRepository.save(tx);
    }

    public List<Transaction> getTransactionForUser(MyUser user){
        return transactionRepository.findByUser(user);
    }

    public List<Transaction> getTransactionForUserBetween(MyUser user, LocalDate startDate,LocalDate endDate){
        return transactionRepository.findByUserAndDateBetween(user,startDate,endDate);
    }

    public Optional<Transaction> getTransactionById(Long id){
        return transactionRepository.findById(id);
    }

    public Transaction deleteTransaction(Long id){
        transactionRepository.deleteById(id);
        return null;
    }

    public Transaction updateTransaction(Long id,Transaction updatedTransaction){
        Transaction existingTransaction = transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found"));
        existingTransaction.setAmount(updatedTransaction.getAmount());
        existingTransaction.setTitle(updatedTransaction.getTitle());
        existingTransaction.setType(updatedTransaction.getType());
        existingTransaction.setCategory(updatedTransaction.getCategory());
        existingTransaction.setCategory(updatedTransaction.getDescription());
        existingTransaction.setDate(updatedTransaction.getDate());
        return transactionRepository.save(existingTransaction);
    }
}
