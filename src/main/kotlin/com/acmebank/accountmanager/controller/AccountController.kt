package com.acmebank.accountmanager.controller

import com.acmebank.accountmanager.model.Account
import com.acmebank.accountmanager.service.AccountService
import javassist.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("api/customers")
class AccountController(val accountService: AccountService){

    @GetMapping("/{customerId}")
    fun fetchAccountsOfCustomer(@PathVariable customerId: Long): Collection<Account> {
        val accounts = accountService.getAccountsByCustomerId(customerId)
        if (accounts.isEmpty())
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find accounts for customer $customerId")
        return accounts
    }



}