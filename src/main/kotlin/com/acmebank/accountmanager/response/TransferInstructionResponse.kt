package com.acmebank.accountmanager.response

import com.acmebank.accountmanager.model.Account
import kotlinx.serialization.Serializable

@Serializable
data class TransferInstructionResponse(
    val targetAccount: Account,
    val sourceAccount: Account
)