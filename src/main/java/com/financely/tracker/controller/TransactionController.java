package com.financely.tracker.controller;

import com.financely.tracker.dto.ApiResponse;
import com.financely.tracker.entity.MyUser;
import com.financely.tracker.entity.Transaction;
import com.financely.tracker.repository.UserRepository;
import com.financely.tracker.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    public Transaction add(@RequestBody Transaction transaction,Authentication authentication){
        MyUser user = getUser(authentication);
        transaction.setUser(user);
        return transactionService.addTransaction(transaction);
    }

    @PostMapping("/update/{id}")
    public Transaction updateTransaction(@PathVariable Long id,@RequestBody Transaction transaction,Authentication authentication) throws AccessDeniedException {
        MyUser user = getUser(authentication);

        Optional<Transaction> existing = transactionService.getTransactionById(id);
        if(existing.isPresent()){
            if(!existing.get().getUser().getUsername().equals(user.getUsername())){
                throw new AccessDeniedException("Unauthorized access to transaction");
            }
            transaction.setUser(user);
            return transactionService.updateTransaction(id,transaction);
        }else{
            throw new RuntimeException("Transaction id is not present");
        }
    }

    @PostMapping("/delete")
    public Transaction delete(@RequestBody Transaction transaction,Authentication authentication){
        return transactionService.deleteTransaction(transaction.getId());
    }

    @GetMapping
    public List<Transaction> getUserTransactions(Authentication authentication){
        MyUser user = getUser(authentication);
        return transactionService.getTransactionForUser(user);
    }

    @GetMapping("/{id}")
    public Optional<Transaction> getTransactionById(@PathVariable Long id, Authentication authentication){
        return transactionService.getTransactionById(id);
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<Transaction>>> filterByDate(@RequestParam("from") String from,
                                                                      @RequestParam("to") String to,
                                                                      Authentication authentication){
        try {
            MyUser user = getUser(authentication);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate startDate = LocalDate.parse(from,formatter);
            LocalDate endDate = LocalDate.parse(to,formatter);
            if(startDate.isAfter(endDate)){
                return ResponseEntity.badRequest().body(new ApiResponse<>("Start date is after the end date"));
            }
            List<Transaction> transactions = transactionService.getTransactionForUserBetween(user, startDate, endDate);
            return ResponseEntity.ok(new ApiResponse<>(transactions));
        } catch (DateTimeParseException e) {
            return  ResponseEntity.badRequest().body(new ApiResponse<>("Error parsing time"));
        }
    }

    private MyUser getUser(Authentication authentication) {
        String username;

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            username = (String) principal;
        } else {
            throw new UsernameNotFoundException("Unknown principal type: " + principal.getClass());
        }

        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }



}
