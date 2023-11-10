package com.waluter.DAO

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CurrencyExchangeRate::class], version = 1)
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
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}