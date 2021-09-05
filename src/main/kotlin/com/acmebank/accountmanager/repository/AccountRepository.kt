package com.acmebank.accountmanager.repository

import com.acmebank.accountmanager.model.Account
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository: CrudRepository<Account, Long> {

    @Query("FROM account where customerId = ?1")
    fun fetchAccountsByCustomerId(customerId: Long): Collection<Account>

    @Query("FROM account A WHERE A.accountNumber = ?1")
    fun findByAccountNumber(accountNumber: String): Account?

    @Query("FROM account A WHERE A.accountNumber = ?1 and A.customerId = ?2")
    fun findByAccountNumberAndCustomerId(accountNumber: String, customerId: Long): Account?

}