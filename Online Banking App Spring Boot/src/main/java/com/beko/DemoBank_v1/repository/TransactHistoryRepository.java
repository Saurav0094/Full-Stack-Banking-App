package com.beko.DemoBank_v1.repository;

import com.beko.DemoBank_v1.models.TransactionHistory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactHistoryRepository extends CrudRepository<TransactionHistory, Integer> {

    @Query(value = "SELECT t.* FROM transact t JOIN account a ON t.account_id = a.account_id WHERE a.user_id = :user_id", nativeQuery = true)
    List<TransactionHistory> getTransactionRecordsById(@Param("user_id") String user_id);

    @Query(value = "SELECT * FROM transact WHERE account_id = :account_id", nativeQuery = true)
    List<TransactionHistory> getTransactionRecordsByAccountId(@Param("account_id") int account_id);
}