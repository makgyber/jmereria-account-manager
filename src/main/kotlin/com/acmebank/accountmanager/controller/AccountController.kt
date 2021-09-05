package com.acmebank.accountmanager.controller

import com.acmebank.accountmanager.model.Account
import com.acmebank.accountmanager.request.TransferInstructionRequest
import com.acmebank.accountmanager.response.TransferInstructionResponse
import com.acmebank.accountmanager.service.AccountService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("api/customers")
class AccountController(val accountService: AccountService){

    @GetMapping("/{customerId}/accounts")
    fun fetchAccountsOfCustomer(@PathVariable customerId: Long): Collection<Account> {
        val accounts = accountService.getAccountsByCustomerId(customerId)
        if (accounts.isEmpty())
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find accounts for customer $customerId")
        return accounts
    }

    @GetMapping("/{customerId}/accounts/{accountNumber}")
    fun fetchAccountOfCustomerByNumber(@PathVariable customerId: Long, @PathVariable accountNumber: String): Account =
        accountService.findByAccountNumberAndCustomerId(accountNumber, customerId) ?:
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find account $accountNumber for customer $customerId")


    @PostMapping("/accounts/transfer", consumes = ["application/json"], produces =["application/json"])
    fun transfer(@RequestBody instruction: TransferInstructionRequest): TransferInstructionResponse? {

        if (instruction.validated()) {
            try {
                return accountService.transfer(instruction)
            } catch (ex : Exception) {
                throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, ex.message)
            }
        } else {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid instruction")
        }

    }

}