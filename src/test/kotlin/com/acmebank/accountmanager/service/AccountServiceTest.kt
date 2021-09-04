package com.acmebank.accountmanager.service

import com.acmebank.accountmanager.model.Account
import com.acmebank.accountmanager.repository.AccountRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = [
        "spring.datasource.url=jdbc:h2:mem:testdb"
    ]
)
internal class AccountServiceTest @Autowired constructor(val db: AccountRepository){

    val accountService = AccountService(db)

    val account1 = Account(1, "12345678", 1000000.0,"HKD", 10001 )
    val account2 = Account(2, "88888888", 1000000.0,"HKD", 10001 )


    @Test
    fun `should return accounts for customer`() {

        val accounts = accountService.getAccountsByCustomerId(10001);

        assert(accounts.isNotEmpty())
        assert(accounts.contains(account1))
        assert(accounts.contains(account2))
    }

    @Test
    fun `when no accounts found`() {
        val accounts = accountService.getAccountsByCustomerId(10002);

        assert(accounts.isEmpty())
    }

    @Test
    fun `find specific account `() {
        val account = accountService.findByAccountNumber("88888888");
        assertEquals(account, account2)
    }

    @Test
    fun `specific account not found`() {
        val account = accountService.findByAccountNumber("55555555");
        assertNull(account)
    }

}