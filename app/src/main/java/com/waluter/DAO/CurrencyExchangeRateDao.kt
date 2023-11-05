package com.waluter.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CurrencyExchangeRateDao {
    @Insert
    suspend fun insertCurrencyExchangeRate(rate: CurrencyExchangeRate)

    @Query("SELECT * FROM currency_exchange_rates")
    suspend fun getCurrencyExchangeRates(): List<CurrencyExchangeRate>
}
