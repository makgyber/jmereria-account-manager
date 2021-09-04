package com.acmebank.accountmanager.controller

import com.acmebank.accountmanager.model.Account
import com.fasterxml.jackson.module.kotlin.jsonMapper
import io.mockk.InternalPlatformDsl.toArray
import jdk.jfr.ContentType
import org.assertj.core.api.Assertions
import org.json.JSONArray
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath
import javax.management.Query.value

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = [
        "spring.datasource.url=jdbc:h2:mem:testdb"
    ]
)
internal class AccountControllerTest(@Autowired val client: TestRestTemplate) {

    val account1 = Account(1, "12345678", 1000000.0,"HKD", 10001 )
    val account2 = Account(2, "88888888", 1000000.0,"HKD", 10001 )

    @Test
    fun `fetch Accounts Of existing Customer`() {
        val entity = client.getForEntity<String>("/api/customers/10001")

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(entity.body).contains(account1.accountNumber)
        Assertions.assertThat(entity.body).contains(account2.accountNumber)
    }

    @Test
    fun `fetch Accounts Of non-existent customer`() {
        val entity = client.getForEntity<String>("/api/customers/10002")
        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }


}