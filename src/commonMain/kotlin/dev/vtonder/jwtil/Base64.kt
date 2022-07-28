package dev.vtonder.jwtil

private const val BASE64_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"

internal object Base64Decoder {

    private value class Sanitized(val string: String)
    private value class Padded(val string: String)
    private value class PaddingLength(val length: Int)

    fun String.toBase64DecodedString(): Result<String> =
        Result.success(decode(this))

    private inline fun decode(string: String): String =
        string.sanitized()
            .padded()
            .let { (padded, padding) -> padded.base64Decoded().dropLast(padding.length) }

    private inline fun String.sanitized() =
        Sanitized(this.replace("[^$BASE64_ALPHABET=]".toRegex(), ""))

    private inline fun Sanitized.padded(): Pair<Padded, PaddingLength> {
        val expectedPadding = expectedPaddingFor(this)
        val numberOfCharactersExceedingDecodingBlockBounds = this.string.length % 4
        val requiresUnexpectedPadding = expectedPadding.isEmpty() && numberOfCharactersExceedingDecodingBlockBounds != 0
        val padding =
            if (requiresUnexpectedPadding) "A".repeat(4 - numberOfCharactersExceedingDecodingBlockBounds)
            else expectedPadding
        val padded = Padded(this.string.dropLast(expectedPadding.length) + padding)
        val paddingLength = PaddingLength(padding.length)
        return Pair(padded, paddingLength)
    }

    private inline fun expectedPaddingFor(sanitized: Sanitized) =
        with(sanitized.string) {
            if (lastOrNull() == '=' && this[lastIndex - 1] == '=') "AA"
            else if (lastOrNull() == '=') "A"
            else ""
        }

    private inline fun Padded.base64Decoded(): String {
        var decoded = ""
        for (index in this.string.indices step 4)
            decoded += this.decodedBlockStartingAt(index)
        return decoded
    }

    private inline fun Padded.decodedBlockStartingAt(index: Int): String {
        val bits0 = BASE64_ALPHABET.indexOf(this.string[index]) shl 18
        val bits1 = BASE64_ALPHABET.indexOf(this.string[index + 1]) shl 12
        val bits2 = BASE64_ALPHABET.indexOf(this.string[index + 2]) shl 6
        val bits3 = BASE64_ALPHABET.indexOf(this.string[index + 3])
        val word = bits0 + bits1 + bits2 + bits3
        return "${(word ushr 16 and 0xFF).toChar()}${(word ushr 8 and 0xFF).toChar()}${(word and 0xFF).toChar()}"
    }
}

