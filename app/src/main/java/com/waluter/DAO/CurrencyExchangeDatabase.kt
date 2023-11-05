package com.waluter.DAO

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CurrencyExchangeRate::class], version = 2)
abstract class CurrencyExchangeDatabase : RoomDatabase() {
    abstract fun currencyExchangeRateDao(): CurrencyExchangeRateDao

    companion object{
        @Volatile
        private var INSTANCE: CurrencyExchangeDatabase? = null

        fun getDatabase(context: Context) : CurrencyExchangeDatabase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CurrencyExchangeDatabase::class.java,
                    "currency_exchhange_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}