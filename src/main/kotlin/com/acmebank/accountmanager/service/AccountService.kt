package com.acmebank.accountmanager.service

import com.acmebank.accountmanager.model.Account
import com.acmebank.accountmanager.repository.AccountRepository
import javassist.NotFoundException
import org.springframework.stereotype.Service

@Service
class AccountService(val db: AccountRepository) {

    fun getAccountsByCustomerId(customerId: Long): Collection<Account> = db.fetchAccountsByCustomerId(customerId)

    fun findByAccountNumber(accountNumber: String): Account? = db.findByAccountNumber(accountNumber)

}