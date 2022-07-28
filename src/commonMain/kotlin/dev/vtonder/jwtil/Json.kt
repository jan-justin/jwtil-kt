package dev.vtonder.jwtil

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

@Suppress("LocalVariableName")
internal object JsonFormatter {

    private const val INDENT_SIZE: UByte = 3u
    private const val MAX_DEPTH: UByte = 10u

    sealed class Error : Throwable()
    object InvalidJsonError : Error()

    fun String.toFormattedString(): Result<String> =
        try {
            Result.success(format(this))
        } catch (error: SerializationException) {
            Result.failure(InvalidJsonError)
        }

    private inline fun format(string: String) =
        if (string.isBlank()) ""
        else Json.parseToJsonElement(string)
            .formatted()

    private fun JsonElement.formatted(depth: UByte = 0u): String =
        if (depth > MAX_DEPTH) "..."
        else when (this) {
            is JsonArray -> this.formattedWith(depth.inc())
            is JsonObject -> this.formattedWith(depth.inc())
            else -> this.toString()
        }

    private inline fun indentFor(depth: UByte) =
        if (depth.toInt() == 0) ""
        else " ".repeat((depth * INDENT_SIZE).toInt())

    private fun JsonObject.formattedWith(depth: UByte) =
        buildString {
            val `__` = indentFor(depth)
            val keyValuePairs = entries
            appendLine("{")
            keyValuePairs.forEachIndexed { index, (key, value: JsonElement) ->
                val formattedValue = value.formatted(depth)
                append("""$`__`"$key": $formattedValue""")
                if (index < keyValuePairs.size - 1) appendLine(",")
                else appendLine()
            }
            val `_` = indentFor(depth.dec())
            append("$`_`}")
        }

    private fun JsonArray.formattedWith(depth: UByte) =
        buildString {
            val `__` = indentFor(depth)
            appendLine("[")
            this@formattedWith.forEachIndexed { index, value ->
                val formattedValue = value.formatted(depth)
                append("""$`__`$formattedValue""")
                if (index != this@formattedWith.lastIndex) appendLine(",")
                else appendLine()
            }
            val `_` = indentFor(depth.dec())
            append("$`_`]")
        }
}
