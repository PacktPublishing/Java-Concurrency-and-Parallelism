package com.example;
import java.util.Optional;


class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}

class OptimisticLockingException extends RuntimeException {
    public OptimisticLockingException(String message) {
        super(message);
    }
}

public class AccountService {

    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void deductBalance(Long userId, Long amount) throws InsufficientBalanceException {
        Optional<UserAccount> accountOptional = accountRepository.findById(userId);
        if (!accountOptional.isPresent()) {
            throw new IllegalArgumentException("User not found");
        }
        UserAccount account = accountOptional.get();
        // rest of your code
    
        if (account.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        Long expectedVersion = account.getVersion();
        int rowsUpdated = accountRepository.deductBalance(userId, amount, expectedVersion);

        if (rowsUpdated != 1) {
            throw new OptimisticLockingException("Balance update failed, retry");
        }
    }
}
