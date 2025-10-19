package com.outh.backend.services;

import com.outh.backend.models.Transaction;
import com.outh.backend.models.TransactionType;
import com.outh.backend.models.User;
import com.outh.backend.repository.UserRepository;
import com.outh.backend.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class UserService {

    private final UserRepository repo;
    private final TransactionRepository transactionRepository;

    public UserService(UserRepository repo, TransactionRepository transactionRepository) {

        this.repo = repo;
        this.transactionRepository = transactionRepository;
    }

    public User createOrUpdateUser(String firebaseId, String email) {
        return repo.findById(firebaseId)
                .map(user -> {
                    // Update existing user if needed
                    if (user.getUsername() == null) {
                        user.setUsername(email);
                    }
                    if (user.getAccount_balance() == null) {
                        user.setAccount_balance(BigDecimal.ZERO);
                    }
                    return repo.save(user);
                })
                .orElseGet(() -> {
                    // Create new user
                    User newUser = new User(firebaseId, email, "ROLE_USER");
                    newUser.setAccount_balance(BigDecimal.ZERO);
                    return repo.save(newUser); // joined will be set automatically by @PrePersist
                });
    }

    @Transactional
    public User updateUserBalance(String firebaseId, BigDecimal amount) {
        User user = repo.findById(firebaseId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + firebaseId));

        // Add the deposit amount to the existing balance
        BigDecimal currentBalance = user.getAccount_balance() != null ? user.getAccount_balance() : BigDecimal.ZERO;
        user.setAccount_balance(currentBalance.add(amount));

        return repo.save(user);
    }


    public User findById(String firebaseId) {
        return repo.findById(firebaseId).orElse(null);
    }

    public void addDeposit(String userId, BigDecimal amount) {
        User user = repo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setAccount_balance(user.getAccount_balance().add(amount));
        repo.save(user);

        Transaction tx = new Transaction();
        tx.setUserId(userId);
        tx.setTransactionType(TransactionType.DEPOSIT);
        tx.setAmount(amount);
        transactionRepository.save(tx);
    }

    public void withdrawVoucher(String userId, BigDecimal amount, String voucherCode) {
        User user = repo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getAccount_balance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        user.setAccount_balance(user.getAccount_balance().subtract(amount));
        repo.save(user);

        Transaction tx = new Transaction();
        tx.setUserId(userId);
        tx.setTransactionType(TransactionType.VOUCHER_WITHDRAWAL);
        tx.setAmount(amount.negate()); // negative for withdrawal
        tx.setVoucherCode(voucherCode);
        transactionRepository.save(tx);
    }
}

