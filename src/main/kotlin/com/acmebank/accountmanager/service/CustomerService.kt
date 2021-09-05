package com.acmebank.accountmanager.service

import com.acmebank.accountmanager.model.Customer
import com.acmebank.accountmanager.repository.CustomerRepository
import org.springframework.stereotype.Service

@Service
class CustomerService(val db: CustomerRepository) {

    fun fetchCustomerByAccessToken(accessToken: String): Customer? = db.fetchCustomerByAccessToken(accessToken)


}