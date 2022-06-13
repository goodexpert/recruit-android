package nz.co.test.transactions.extensions

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Currency

fun BigDecimal.toCurrency(): String {
    val nf = NumberFormat.getCurrencyInstance().apply {
        maximumFractionDigits = 2
        currency = Currency.getInstance("USD")
    }
    return nf.format(toDouble())
}