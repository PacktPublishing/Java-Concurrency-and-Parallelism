package com.example;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<UserAccount, Long> {
    @Modifying
    @Query("UPDATE UserAccount ua SET ua.balance = ua.balance - :amount, ua.version = ua.version + 1 WHERE ua.id = :userId AND ua.version = :expectedVersion")
    int deductBalance(@Param("userId") Long userId, @Param("amount") Long amount,
            @Param("expectedVersion") Long expectedVersion);
}
