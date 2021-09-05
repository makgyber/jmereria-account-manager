package com.acmebank.accountmanager.model

import kotlinx.serialization.Serializable
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Serializable
@Entity(name="account")
@Table(name="account")
data class Account(
    @Id
    @GeneratedValue
    val id: Long,
    val accountNumber: String,
    val balance: Double,
    val currency: String,
    val customerId: Long
)
