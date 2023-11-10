import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.waluter.DAO.CurrencyExchangeRate
import com.waluter.R

class CurrencyExchangeAdapter : RecyclerView.Adapter<CurrencyExchangeAdapter.CurrencyViewHolder>() {

    private val currencyRates: MutableList<CurrencyExchangeRate> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.currency_item, parent, false)
        return CurrencyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val rate = currencyRates[position]
        holder.bind(rate)
    }

    override fun getItemCount(): Int {
        return currencyRates.size
    }

    fun updateData(newData: List<CurrencyExchangeRate>) {
        currencyRates.clear()
        currencyRates.addAll(newData)
        notifyDataSetChanged()
    }

    inner class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val currencyTextView: TextView = itemView.findViewById(R.id.currencyTextView)
        private val codeTextView: TextView = itemView.findViewById(R.id.codeTextView)
        private val rateTextView: TextView = itemView.findViewById(R.id.rateTextView)

        fun bind(rate: CurrencyExchangeRate) {
            currencyTextView.text = rate.currency
            codeTextView.text = rate.code
            rateTextView.text = rate.mid.toString()
        }
    }
}
