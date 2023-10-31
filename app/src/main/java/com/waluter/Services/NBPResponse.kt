package com.waluter.Services

import com.waluter.DAO.CurrencyExchangeRate

data class NBPResponse(
    val table: String,
    val currency: String,
    val code: String,
    val rates: List<CurrencyExchangeRate>
)
