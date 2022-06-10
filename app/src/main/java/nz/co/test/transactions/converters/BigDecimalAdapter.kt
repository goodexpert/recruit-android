package nz.co.test.transactions.converters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.math.BigDecimal

object BigDecimalAdapter {

    @FromJson fun fromJson(double: Double) = BigDecimal(double)

    @ToJson fun toJson(value: BigDecimal) = value.toDouble()
}