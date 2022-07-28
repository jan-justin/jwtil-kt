package dev.vtonder.jwtil

internal data class Jwt(
    val header: Header,
    val body: Body,
    val signature: Signature,
) {
    value class Header(val string: String)
    value class Body(val string: String)
    value class Signature(val string: String)
}

internal object JwtDecoder {

    sealed class Error : Throwable()
    object InvalidJwtError : Error()

    private value class Segmented(val segments: List<String>)

    fun String.toDecodedJwt(): Result<Jwt> =
        try {
            Result.success(decode(this))
        } catch (error: IllegalArgumentException) {
            Result.failure(InvalidJwtError)
        }

    private inline fun decode(string: String) =
        string.segmented()
            .decoded()

    private inline fun String.segmented() =
        this.split(".")
            .takeIf { it.size == 3 }
            ?.let(::Segmented)
            ?: throw IllegalArgumentException()

    private inline fun Segmented.decoded() =
        this.segments.let { (header, body, signature) -> listOf(base64Decoded(header), base64Decoded(body), signature) }
            .let(::toJwt)

    private inline fun base64Decoded(string: String) =
        with(Base64Decoder) {
            string.toBase64DecodedString()
                .getOrThrow()
        }

    private inline fun toJwt(segments: List<String>) =
        segments.let { (header, body, signature) ->
            Jwt(
                header = Jwt.Header(header),
                body = Jwt.Body(body),
                signature = Jwt.Signature(signature)
            )
        }
}
