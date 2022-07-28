import dev.vtonder.jwtil.JsonFormatter
import dev.vtonder.jwtil.JwtDecoder
import dev.vtonder.jwtil.eprintln
import kotlin.system.exitProcess

private object InvalidUsageError : Throwable()

fun main(args: Array<String>) {
    if (args.isEmpty()) runWith(listOf(readln()))
    else runWith(args.toList())
}

private fun runWith(args: List<String>) {
    if (args.isEmpty() || args.first().isBlank()) exitDueToError(InvalidUsageError)
    else args.let { (jwt) -> printFormatted(jwt) }
}

private fun printFormatted(jwt: String) {
    with(JwtDecoder) {
        with(JsonFormatter) {
            jwt.toDecodedJwt()
                .onFailure(::exitDueToError)
                .getOrThrow().body.string
                .toFormattedString()
                .onFailure(::exitDueToError)
                .onSuccess(::println)
        }
    }
}

internal fun exitDueToError(error: Throwable): Nothing {
    when (error) {
        is JwtDecoder.InvalidJwtError, JsonFormatter.InvalidJsonError -> {
            eprintln("error: Invalid JWT provided")
            exitProcess(3)
        }
        is InvalidUsageError -> {
            eprintln("usage: jwtil JWT")
            exitProcess(2)
        }
        else -> {
            eprintln("error: Unexpected error occurred")
            exitProcess(1)
        }
    }
}
