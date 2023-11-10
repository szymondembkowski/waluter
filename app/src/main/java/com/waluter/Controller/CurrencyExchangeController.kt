package com.waluter.Controller

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.lifecycleScope
import com.waluter.DAO.CurrencyExchangeModel
import com.waluter.DAO.CurrencyExchangeRate
import com.waluter.MainActivity
import com.waluter.Services.NBPResponse
import com.waluter.Services.NBPService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CurrencyExchangeController(private val model: CurrencyExchangeModel, private val mainActivity: MainActivity) {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://api.nbp.pl/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val nbpService = retrofit.create(NBPService::class.java)

    fun fetchDataFromNBP() {
        val connectivityManager = mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        if (networkInfo != null && networkInfo.isConnected) {
            // jak jest net, zapytaj rest api
            mainActivity.displayError("", true)
            mainActivity.displayExchangeRates(emptyList())
            fetchExchangeRates()
        } else {
            // brak neta - odczytaj z lokalnej bazy danych
            mainActivity.displayExchangeRates(emptyList())
            fetchExchangeRatesFromDatabase()
        }
    }

    private fun fetchExchangeRates() {
        val call = nbpService.getExchangeRates()

        call.enqueue(object : Callback<List<NBPResponse>> {
            override fun onResponse(call: Call<List<NBPResponse>>, response: Response<List<NBPResponse>>) {
                if (response.isSuccessful) {
                    println("Data successfully fetched from NBP API")
                    val nbpResponseList = response.body()
                    if (nbpResponseList != null && nbpResponseList.isNotEmpty()) {
                        val currencyExchangeRates = nbpResponseList[0].rates.map { nbpRate ->
                            CurrencyExchangeRate(
                                currency = nbpRate.currency,
                                code = nbpRate.code,
                                mid = nbpRate.mid
                            )
                        }
                        clearAllExchangeRates()
                        updateDatabaseAndDisplayExchangeRates(currencyExchangeRates)
                    } else {
                        fetchExchangeRatesFromDatabase()
                    }
                }
            }

            override fun onFailure(call: Call<List<NBPResponse>>, t: Throwable) {
                fetchExchangeRatesFromDatabase()
            }
        })
    }

    private fun fetchExchangeRatesFromDatabase() {
//        mainActivity.displayError("", true)
//        mainActivity.displayExchangeRates(emptyList())

        mainActivity.lifecycleScope.launch {
            val exchangeRates = model.getExchangeRates()
            if (exchangeRates.isNotEmpty()) {
                mainActivity.displayExchangeRates(exchangeRates)
            } else {
                mainActivity.displayError("404 Not Found", true)
            }
        }
    }

    private fun updateDatabaseAndDisplayExchangeRates(rates: List<CurrencyExchangeRate>) {
        mainActivity.lifecycleScope.launch {
            model.updateExchangeRates(rates)
        }
        mainActivity.displayExchangeRates(rates)
    }

    private fun clearAllExchangeRates() {
        mainActivity.lifecycleScope.launch {
            model.clearAllExchangeRates()
        }
    }
}