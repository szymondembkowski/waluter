import com.waluter.DAO.CurrencyExchangeRate
import retrofit2.Call
import retrofit2.http.GET

interface NBPService {
    @GET("exchangerates/tables/A?format=json")
    fun getExchangeRates(): Call<List<CurrencyExchangeRate>>
}
