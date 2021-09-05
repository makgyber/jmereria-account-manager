package com.acmebank.accountmanager.repository

import com.acmebank.accountmanager.model.Customer
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository : CrudRepository<Customer, Long>{

    @Query("FROM customer where access_token = ?1")
    fun fetchCustomerByAccessToken(accessToken: String): Customer?

}