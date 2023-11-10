package com.waluter.DAO;

import android.content.Context;

import androidx.room.Room;
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CurrencyExchangeModel(context: Context) {
    private val database = Room.databaseBuilder(
        context,
        CurrencyExchangeDatabase::class.java,
        "currency_exchange_database"
    ).build()

    private val exchangeRateDao = database.currencyExchangeRateDao()

    suspend fun getExchangeRates(): List<CurrencyExchangeRate> {
        return withContext(Dispatchers.IO) {
            exchangeRateDao.getCurrencyExchangeRates()
        }
    }

    suspend fun updateExchangeRates(rates: List<CurrencyExchangeRate>) {
        withContext(Dispatchers.IO) {
            for (rate in rates) {
                exchangeRateDao.insertCurrencyExchangeRate(rate)
            }
        }
    }

    suspend fun clearAllExchangeRates(){
        withContext(Dispatchers.IO){
            exchangeRateDao.clearAllExchangeRates()
        }
    }
}