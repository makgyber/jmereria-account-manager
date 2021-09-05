package com.acmebank.accountmanager.controller

import com.acmebank.accountmanager.model.Account
import com.acmebank.accountmanager.request.TransferInstructionRequest
import com.acmebank.accountmanager.response.TransferInstructionResponse
import com.acmebank.accountmanager.service.AccountService
import com.acmebank.accountmanager.service.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("api")
class AccountController(val accountService: AccountService, val customerService: CustomerService){

    @GetMapping("/accounts")
    fun fetchAccountsOfCustomer(@RequestHeader("X-Access-Token") accessToken: String): Collection<Account> {

        val customer = customerService.fetchCustomerByAccessToken(accessToken)?:
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized")

        val accounts = accountService.getAccountsByCustomerId(customer.id)

        if (accounts.isEmpty())
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find accounts for customer")
        return accounts
    }

    @GetMapping("/accounts/{accountNumber}")
    fun fetchAccountOfCustomerByNumber(@PathVariable accountNumber: String,
                                       @RequestHeader("X-Access-Token") accessToken: String): Account {

        val customer = customerService.fetchCustomerByAccessToken(accessToken)?:
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized")
        return accountService.findByAccountNumberAndCustomerId(accountNumber, customer.id) ?:
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find account $accountNumber for customer")
    }



    @PostMapping("/accounts/transfer", consumes = ["application/json"], produces =["application/json"])
    fun transfer(@RequestBody instruction: TransferInstructionRequest,
                 @RequestHeader("X-Access-Token") accessToken: String): TransferInstructionResponse? {

        val customer = customerService.fetchCustomerByAccessToken(accessToken)?:
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized")

        if (customer.id != instruction.customerId)
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized")

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