package com.acmebank.accountmanager.controller

import com.acmebank.accountmanager.model.Account
import com.acmebank.accountmanager.request.TransferInstructionRequest
import com.acmebank.accountmanager.response.TransferInstructionResponse
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.serialization.encodeToString
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
import org.springframework.http.*
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
        val headers = HttpHeaders()
        headers.set("X-Access-Token", "abcdef-1234-567890")
        headers.contentType = MediaType.APPLICATION_JSON

        val httpEntity = HttpEntity<String>(headers)
        val entity = client.exchange("/api/accounts", HttpMethod.GET, httpEntity, String::class.java )

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(entity.body).contains(account1.accountNumber)
        Assertions.assertThat(entity.body).contains(account2.accountNumber)
    }

    @Test
    fun `fetch one account Of existing Customer`() {

        val headers = HttpHeaders()
        headers.set("X-Access-Token", "abcdef-1234-567890")
        headers.contentType = MediaType.APPLICATION_JSON

        val httpEntity = HttpEntity<String>(headers)
        val entity = client.exchange("/api/accounts/88888888", HttpMethod.GET, httpEntity, String::class.java )

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(entity.body).contains(account2.accountNumber)
    }

    @Test
    @DirtiesContext
    fun `transfer `() {

        val transferInstruction = TransferInstructionRequest(
            "12345678", "88888888", 1.0, customerId = 10001)

        val headers = HttpHeaders()
        headers.set("X-Access-Token", "abcdef-1234-567890")
        headers.contentType = MediaType.APPLICATION_JSON
        val httpEntity = HttpEntity<String>(Json.encodeToString(transferInstruction), headers)

        val entity = client.exchange("/api/accounts/transfer", HttpMethod.POST, httpEntity, String::class.java )

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

        val headers = HttpHeaders()
        headers.set("X-Access-Token", "abcdef-1234-567890")
        headers.contentType = MediaType.APPLICATION_JSON
        val httpEntity = HttpEntity<String>(Json.encodeToString(transferInstruction), headers)

        val entity = client.exchange("/api/accounts/transfer", HttpMethod.POST, httpEntity, String::class.java )

        assertNotNull(entity)
        assertEquals(entity.statusCode, HttpStatus.BAD_REQUEST)

    }

    @Test
    fun `transfer failed when source and target are same`() {
        val transferInstruction = TransferInstructionRequest(
            "12345678", "12345678", 999999.0, customerId = 10001)


        val headers = HttpHeaders()
        headers.set("X-Access-Token", "abcdef-1234-567890")
        headers.contentType = MediaType.APPLICATION_JSON
        val httpEntity = HttpEntity<String>(Json.encodeToString(transferInstruction), headers)

        val entity = client.exchange("/api/accounts/transfer", HttpMethod.POST, httpEntity, String::class.java )

        assertNotNull(entity)
        assertEquals(entity.statusCode, HttpStatus.BAD_REQUEST)

    }

    @Test
    fun `transfer failed with insufficient funds`() {
        val transferInstruction = TransferInstructionRequest(
            "12345678", "88888888", 99999999.0, customerId = 10001)
        val headers = HttpHeaders()
        headers.set("X-Access-Token", "abcdef-1234-567890")
        headers.contentType = MediaType.APPLICATION_JSON
        val httpEntity = HttpEntity<String>(Json.encodeToString(transferInstruction), headers)

        val entity = client.exchange("/api/accounts/transfer", HttpMethod.POST, httpEntity, String::class.java )

        assertNotNull(entity)
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, entity.statusCode)

    }
}
