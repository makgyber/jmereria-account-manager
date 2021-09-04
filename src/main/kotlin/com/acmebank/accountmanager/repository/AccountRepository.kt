package com.acmebank.accountmanager.repository

import com.acmebank.accountmanager.model.Account
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface AccountRepository: CrudRepository<Account, Long> {

    @Query("FROM account where customerId = ?1")
    fun fetchAccountsByCustomerId(customerId: Long): Collection<Account>

    @Query("FROM account A WHERE A.accountNumber = ?1")
    fun findByAccountNumber(accountNumber: String): Account?

    @Transactional
    @Modifying
    @Query("UPDATE account A SET A.balance = ?1 WHERE A.accountNumber = ?2")
    fun update(newBalance: Double, accountNumber: String)

}