package dev.vtonder.jwtil

import kotlin.test.Test
import kotlin.test.assertEquals

class JwtTests {

    @Test
    fun `should throw invalid input error when given empty string`() {
        with(JwtDecoder) {
            val encoded = ""
            val result = encoded.toDecodedJwt()
            assertEquals(JwtDecoder.InvalidJwtError, result.exceptionOrNull())
        }
    }

    @Test
    fun `should throw invalid input error when given non-jwt string`() {
        with(JwtDecoder) {
            val encoded = "onlyHeader.andBodyButNoSignature"
            val result = encoded.toDecodedJwt()
            assertEquals(JwtDecoder.InvalidJwtError, result.exceptionOrNull())
        }
    }

    @Test
    fun `should decode valid jwt string`() {
        with(JwtDecoder) {
            val encoded = """
                eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9
                .eyJzdWIiOiIxMjM0NTY3ODkwIn0
                .dozjgNryP4J3jVmNHl0w5N_XgL0n3I9PlFUP0THsR8U
                """.trimIndent().replace("\n", "")
            val jwt = encoded.toDecodedJwt().getOrThrow()
            assertEquals("""{"alg":"HS256","typ":"JWT"}""", jwt.header.string)
            assertEquals("""{"sub":"1234567890"}""", jwt.body.string)
            assertEquals("dozjgNryP4J3jVmNHl0w5N_XgL0n3I9PlFUP0THsR8U", jwt.signature.string)
        }
    }
}