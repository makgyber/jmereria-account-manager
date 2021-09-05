package com.acmebank.accountmanager.service

import com.acmebank.accountmanager.repository.CustomerRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = [
        "spring.datasource.url=jdbc:h2:mem:testdb"
    ]
)
internal class CustomerServiceTest@Autowired constructor(
    val customerService: CustomerService,
    val db: CustomerRepository
) {
    @Test
    fun `should return false for invalid token`() {

        val customer = customerService.fetchCustomerByAccessToken("invalid-token")
        Assertions.assertNull(customer)
    }

    @Test
    fun `should return customer for valid token`() {

        val customer = customerService.fetchCustomerByAccessToken("abcdef-1234-567890")
        Assertions.assertNotNull(customer)
    }
}