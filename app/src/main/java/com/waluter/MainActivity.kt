package com.waluter

import CurrencyExchangeAdapter
import android.content.Context
import android.graphics.ColorSpace.Model
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waluter.Controller.CurrencyExchangeController
import com.waluter.DAO.CurrencyExchangeModel
import com.waluter.DAO.CurrencyExchangeRate
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var  model : CurrencyExchangeModel
    lateinit var  controller : CurrencyExchangeController
    private lateinit var fetchDataButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CurrencyExchangeAdapter
    private lateinit var exchangeRateTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model = CurrencyExchangeModel(this)
        controller = CurrencyExchangeController(model, this)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        exchangeRateTextView = findViewById(R.id.errorTextView)

        adapter = CurrencyExchangeAdapter()
        recyclerView.adapter = adapter

        fetchDataButton = findViewById(R.id.fetchDataButton)
        fetchDataButton.setOnClickListener {
            onFetchDataButtonClick()
        }
    }

    fun onFetchDataButtonClick() {
        if (isInternetOn()) {
            displayError("", true)
            controller.fetchDataFromNBP()
        } else {
            displayError("", true)

            controller.fetchDataFromNBP()
            // odczytanie lokalnej bazy danych
            lifecycleScope.launch {
                val exchangeRates = model.getExchangeRates()
                if (exchangeRates.isNotEmpty()) {
                    displayExchangeRates(exchangeRates)
                } else {
                    displayError("404 Not Found", false)
                }
            }
        }
    }

    fun displayExchangeRates(rates: List<CurrencyExchangeRate>) {
        adapter.updateData(rates)
        recyclerView.visibility = View.VISIBLE
        exchangeRateTextView.visibility = View.INVISIBLE
    }

    fun displayError(errorMessage: String, hide: Boolean) {
        var errorToDisplay = errorMessage

        if (hide) {
            if (adapter.itemCount == 0) {
                errorToDisplay = "404 Not Found"
            }
        }

        exchangeRateTextView.text = errorToDisplay
        exchangeRateTextView.visibility = if (hide) View.GONE else View.VISIBLE
        recyclerView.visibility = if (hide) View.VISIBLE else View.GONE
    }

    private fun isInternetOn(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}