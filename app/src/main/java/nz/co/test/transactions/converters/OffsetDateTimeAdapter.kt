package nz.co.test.transactions.converters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object OffsetDateTimeAdapter {
    private const val pattern = "yyyy-MM-dd'T'HH:mm:ss"

    @FromJson
    fun fromJson(string: String) = LocalDateTime.parse(string, DateTimeFormatter.ofPattern(pattern)).atOffset(
        ZoneOffset.UTC)

    @ToJson
    fun toJson(value: OffsetDateTime) = value.format(DateTimeFormatter.ofPattern(pattern))
}