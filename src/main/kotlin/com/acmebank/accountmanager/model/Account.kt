package com.acmebank.accountmanager.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
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
