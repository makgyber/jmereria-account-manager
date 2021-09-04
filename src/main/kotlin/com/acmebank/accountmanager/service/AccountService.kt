package com.acmebank.accountmanager.service

import com.acmebank.accountmanager.model.Account
import com.acmebank.accountmanager.repository.AccountRepository
import com.acmebank.accountmanager.request.TransferInstructionRequest
import javassist.NotFoundException
import org.springframework.stereotype.Service

@Service
class AccountService(val db: AccountRepository) {

    fun getAccountsByCustomerId(customerId: Long): Collection<Account> = db.fetchAccountsByCustomerId(customerId)

    fun findByAccountNumber(accountNumber: String): Account? = db.findByAccountNumber(accountNumber)

    fun deposit(accountNumber: String, amount: Double): Account? {
        val account = db.findByAccountNumber(accountNumber)

        if(null != account) {
            val newBalance = account.balance.plus(amount)
            db.update(newBalance, accountNumber)
        } else {
            throw Exception("Account not found")
        }

        return db.findByAccountNumber(accountNumber)
    }

    fun withdraw(accountNumber: String, amount: Double): Account? {
        val account = db.findByAccountNumber(accountNumber)

        if(null != account) {
            val newBalance = account.balance.minus(amount)
            if (newBalance >= 0) {
                db.update(newBalance, accountNumber)
            } else {
                throw Exception("Balance not enough")
            }
        } else {
            throw Exception("Account not found")
        }

        return db.findByAccountNumber(accountNumber)
    }

    fun transfer(instruction: TransferInstructionRequest): List<Account>? {
        try {
            val accounts = ArrayList<Account>()
            withdraw(instruction.sourceAccount, instruction.amount)?.let { accounts.add(it) }
            deposit(instruction.targetAccount, instruction.amount)?.let { accounts.add(it) }
            return accounts
        } catch (e: Exception) {
            throw e
        }

    }

}