package nz.co.test.transactions.extensions

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

fun OffsetDateTime.format(pattern: String): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return formatter.format(this)
}