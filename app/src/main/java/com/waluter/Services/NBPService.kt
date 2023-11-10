package com.waluter.Services

import retrofit2.Call
import retrofit2.http.GET

interface NBPService {
    @GET("/api/exchangerates/tables/a")
    fun getExchangeRates(): Call<List<NBPResponse>>
}
