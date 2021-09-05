package com.acmebank.accountmanager.service

import com.acmebank.accountmanager.model.Account
import com.acmebank.accountmanager.repository.AccountRepository
import com.acmebank.accountmanager.request.TransferInstructionRequest
import com.acmebank.accountmanager.response.TransferInstructionResponse
import org.springframework.stereotype.Service

@Service
class AccountService(val db: AccountRepository) {

    fun getAccountsByCustomerId(customerId: Long): Collection<Account> = db.fetchAccountsByCustomerId(customerId)

    fun findByAccountNumberAndCustomerId(accountNumber: String, customerId: Long): Account? = db.findByAccountNumberAndCustomerId(accountNumber, customerId)

    fun findByAccountNumber(accountNumber: String): Account? = db.findByAccountNumber(accountNumber)

    fun deposit(accountNumber: String, amount: Double): Account? {

        val account = db.findByAccountNumber(accountNumber)

        if(null != account) {
            val newBalance = account.balance.plus(amount)
            return updateBalance(account, newBalance)
        } else {
            throw Exception("Account not found")
        }

    }

    fun withdraw(accountNumber: String, amount: Double, customerId: Long): Account? {
        val account = db.findByAccountNumberAndCustomerId(accountNumber, customerId)

        if(null != account) {
            val newBalance = account.balance.minus(amount)
            if (newBalance >= 0) {
                return updateBalance(account, newBalance)
            } else {
                throw Exception("Balance not enough")
            }
        } else {
            throw Exception("Account not found")
        }
    }

    private fun updateBalance(
        account: Account,
        newBalance: Double
    ): Account {
        val updated = Account(account.id, account.accountNumber, newBalance, account.currency, account.customerId)
        db.save(updated)
        return updated
    }

    fun transfer(instruction: TransferInstructionRequest): TransferInstructionResponse {
        try {
            val source = withdraw(instruction.sourceAccount, instruction.amount, instruction.customerId)!!
            val target = deposit(instruction.targetAccount, instruction.amount)!!
            return TransferInstructionResponse(
                target,
                source,
            )
        } catch (e: Exception) {
            throw e
        }
    }



}