package com.acmebank.accountmanager.controller

import com.acmebank.accountmanager.model.Account
import com.acmebank.accountmanager.request.TransferInstructionRequest
import com.acmebank.accountmanager.response.TransferInstructionResponse
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import org.assertj.core.api.Assertions
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = [
        "spring.datasource.url=jdbc:h2:mem:testdb"
    ]
)
internal class AccountControllerTest @Autowired  constructor(
    val client: TestRestTemplate,
    val objectMapper: ObjectMapper)
{

    val account1 = Account(1, "12345678", 1000000.0,"HKD", 10001 )
    val account2 = Account(2, "88888888", 1000000.0,"HKD", 10001 )

    @Test
    fun `fetch Accounts Of existing Customer`() {
        val entity = client.getForEntity<String>("/api/customers/10001/accounts")

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(entity.body).contains(account1.accountNumber)
        Assertions.assertThat(entity.body).contains(account2.accountNumber)
    }

    @Test
    fun `fetch one account Of existing Customer`() {
        val entity = client.getForEntity<String>("/api/customers/10001/accounts/88888888")

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(entity.body).contains(account2.accountNumber)
    }

    @Test
    @DirtiesContext
    fun `transfer `() {
        val transferInstruction = TransferInstructionRequest(
            "12345678", "88888888", 1.0, customerId = 10001)
        val entity = client.postForEntity<String>("/api/customers/accounts/transfer", transferInstruction)
        val expectedTarget = Account(1, "12345678", 1000001.0,"HKD", 10001)
        val expectedSource = Account(2, "88888888", 999999.0,"HKD", 10001)
        assertNotNull(entity)
        assertEquals(HttpStatus.OK, entity.statusCode)

        JSONObject(entity.body).let {
            val transferInstructionResponse = Json.decodeFromJsonElement<TransferInstructionResponse>(Json.parseToJsonElement(it.toString()))
            assertEquals(expectedTarget, transferInstructionResponse.targetAccount)
            assertEquals(expectedSource, transferInstructionResponse.sourceAccount)
        }

    }

    @Test
    fun `transfer failed with missing parameter`() {
        val transferInstruction = TransferInstructionRequest(
            "12345678", "", 999999.0, customerId = 10001)
        val entity = client.postForEntity<String>("/api/customers/accounts/transfer", transferInstruction)

        assertNotNull(entity)
        assertEquals(entity.statusCode, HttpStatus.BAD_REQUEST)

    }

    @Test
    fun `transfer failed when source and target are same`() {
        val transferInstruction = TransferInstructionRequest(
            "12345678", "12345678", 999999.0, customerId = 10001)
        val entity = client.postForEntity<String>("/api/customers/accounts/transfer", transferInstruction)

        assertNotNull(entity)
        assertEquals(entity.statusCode, HttpStatus.BAD_REQUEST)

    }

    @Test
    fun `transfer failed with insufficient funds`() {
        val transferInstruction = TransferInstructionRequest(
            "12345678", "88888888", 99999999.0, customerId = 10001)
        val entity = client.postForEntity<String>("/api/customers/accounts/transfer", transferInstruction)

        assertNotNull(entity)
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, entity.statusCode)

    }
}
