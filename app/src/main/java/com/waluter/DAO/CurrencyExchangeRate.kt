package com.waluter.DAO

import com.waluter.Services.NBPResponse

data class CurrencyExchangeRate(
    val currency: String,
    val code: String,
    val mid: Double
) {
    override fun toString(): String {
        return "Waluta: $currency, Skrót: $code, Kurs: $mid"
    }
}

class CurrencyExchangeModel {
    private val exchangeRates: MutableList<CurrencyExchangeRate> = mutableListOf()

    fun getExchangeRates(): List<CurrencyExchangeRate> {
        return exchangeRates
    }

    fun updateExchangeRates(rates: List<CurrencyExchangeRate>) {
        exchangeRates.clear()
        exchangeRates.addAll(rates)
    }



}
