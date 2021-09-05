package com.acmebank.accountmanager.request

data class TransferInstructionRequest (
    val targetAccount: String,
    val sourceAccount: String,
    val amount: Double,
    val customerId: Long) {

    fun validated():Boolean = ( targetAccount.isNotBlank()  && sourceAccount.isNotBlank() && amount > 0 && !targetAccount.contentEquals(sourceAccount))
}