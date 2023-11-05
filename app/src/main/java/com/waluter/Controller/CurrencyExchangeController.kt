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
            // Jeśli jest dostęp do internetu, wykonaj zapytanie NBP
            mainActivity.displayError("", true)  // Ukryj komunikat o błędzie
            mainActivity.displayExchangeRates(emptyList()) // Wyzeruj tabelę
            fetchExchangeRates()
        } else {
            // Jeśli nie ma dostępu do internetu, wyświetl komunikat o błędzie
            mainActivity.displayError("404 Not Found", true)
        }
    }

    private fun fetchExchangeRates() {
        val call = nbpService.getExchangeRates()

        call.enqueue(object : Callback<List<NBPResponse>> {
            override fun onResponse(call: Call<List<NBPResponse>>, response: Response<List<NBPResponse>>) {
                if (response.isSuccessful) {
                    val nbpResponseList = response.body()
                    if (nbpResponseList != null && nbpResponseList.isNotEmpty()) {
                        val currencyExchangeRates = nbpResponseList[0].rates.map { nbpRate ->
                            CurrencyExchangeRate(
                                currency = nbpRate.currency,
                                code = nbpRate.code,
                                mid = nbpRate.mid
                            )
                        }
                        mainActivity.displayExchangeRates(currencyExchangeRates)
                        mainActivity.displayError("404 Not Found", true)
                    }
                }
            }

            override fun onFailure(call: Call<List<NBPResponse>>, t: Throwable) {
                mainActivity.displayError("404 Not Found", true)
            }
        })
    }
}
