package com.waluter.DAO

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_exchange_rates")
data class CurrencyExchangeRate(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val currency: String,
    val code: String,
    val mid: Double
)
// {
////    override fun toString(): String {
////        return "Waluta: $currency, Skrót: $code, Kurs: $mid"
////    }
//}