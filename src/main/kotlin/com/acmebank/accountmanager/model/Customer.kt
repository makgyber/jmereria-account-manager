package com.acmebank.accountmanager.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name="customer")
class Customer (
    @Id
    @GeneratedValue
    val id: Long,
    val name: String,
    val accessToken: String
)