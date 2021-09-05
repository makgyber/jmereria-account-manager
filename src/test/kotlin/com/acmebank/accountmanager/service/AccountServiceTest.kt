package com.acmebank.accountmanager.service

import com.acmebank.accountmanager.model.Account
import com.acmebank.accountmanager.repository.AccountRepository
import com.acmebank.accountmanager.request.TransferInstructionRequest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext

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
        assertEquals(2, accounts.count())
        assert(accounts.any() { it == account1 })
        assert(accounts.any() { it == account2 })
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
    @DirtiesContext
    fun `try to deposit success`() {
        val account = accountService.deposit("12345678", 10000.0)
        assertEquals(account?.balance, 1010000.0)
    }

    @Test
    @DirtiesContext
    fun `try to withdraw success`() {
        val account = accountService.withdraw("88888888", 10000.0, customerId = 10001)
        assertEquals(account?.balance, 990000.0)
    }

    @Test
    fun `try to withdraw from non existent account returns exception`() {
        val exception = assertThrows(Exception::class.java) {
            accountService.withdraw("55555555", 10000.0, customerId = 10001)
        }
        assertEquals(exception.message, "Account not found")
    }

    @Test
    fun `try to withdraw but insufficient funds in account returns exception`() {
        val exception = assertThrows(Exception::class.java) {
            accountService.withdraw("88888888", 999999999.0, customerId = 10001)
        }
        assertEquals(exception.message, "Balance not enough")
    }

    @Test
    fun `transfer to another account from insufficiently funded account returns exception`() {
        val instruction = TransferInstructionRequest(
            "12345678",
            "88888888",
            9999999999.99,
            10001
        )
        val exception = assertThrows(Exception::class.java) {
            accountService.transfer(instruction)
        }
        assertEquals(exception.message, "Balance not enough")
    }

    @Test
    @DirtiesContext
    fun `transfer to another account success`() {
        val instruction = TransferInstructionRequest(
            "12345678",
            "88888888",
            10000.0,
            10001
        )
        val accounts = accountService.transfer(instruction)

        assertNotNull(accounts)
        assertEquals(accounts.sourceAccount.balance, 990000.0)
        assertEquals(accounts.targetAccount.balance, 1010000.0)
    }
}