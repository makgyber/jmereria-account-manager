package com.acmebank.accountmanager.service

import com.acmebank.accountmanager.model.Account
import com.acmebank.accountmanager.repository.AccountRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.rest.webmvc.support.ExcerptProjector

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = [
        "spring.datasource.url=jdbc:h2:mem:testdb"
    ]
)
internal class AccountServiceTest @Autowired constructor(
    val accountService: AccountService,
    val db: AccountRepository
){

    val account1 = Account(1, "12345678", 1000000.0,"HKD", 10001 )
    val account2 = Account(2, "88888888", 1000000.0,"HKD", 10001 )

    @Test
    fun `should return accounts for customer`() {

        val accounts = accountService.getAccountsByCustomerId(10001)

        assert(accounts.isNotEmpty())
        assert(accounts.contains(account1))
        assert(accounts.contains(account2))
    }

    @Test
    fun `when no accounts found`() {
        val accounts = accountService.getAccountsByCustomerId(10002)

        assert(accounts.isEmpty())
    }

    @Test
    fun `find specific account `() {
        val account = accountService.findByAccountNumber("88888888")
        assertEquals(account, account2)
    }

    @Test
    fun `specific account not found`() {
        val account = accountService.findByAccountNumber("55555555")
        assertNull(account)
    }

    @Test
    fun `try to deposit to non existent account`() {
        val exception = assertThrows(Exception::class.java) {
           accountService.deposit("55555555", 10000.0)
        }
        assertEquals(exception.message, "Account not found")
    }

    @Test
    fun `try to withdraw from non existent account`() {
        val exception = assertThrows(Exception::class.java) {
            accountService.withdraw("55555555", 10000.0)
        }
        assertEquals(exception.message, "Account not found")
    }

    @Test
    fun `try to withdraw but insufficient funds in account`() {
        val exception = assertThrows(Exception::class.java) {
            accountService.withdraw("88888888", 999999999.0)
        }
        assertEquals(exception.message, "Balance not enough")
    }

}