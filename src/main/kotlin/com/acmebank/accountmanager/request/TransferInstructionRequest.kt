package com.acmebank.accountmanager.request

class TransferInstructionRequest (
    val targetAccount: String,
    val sourceAccount: String,
    val amount: Double) {

    fun validated():Boolean = ( targetAccount.isNotBlank()  && sourceAccount.isNotBlank() && amount > 0 )
}