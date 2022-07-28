package dev.vtonder.jwtil

import kotlin.test.Test
import kotlin.test.assertEquals

class Base64Tests {

    @Test
    fun `should decode empty string`() {
        with(Base64Decoder) {
            val encoded = ""
            val expected = ""
            val result = encoded.toBase64DecodedString()
            assertEquals(expected, result.getOrThrow())
        }
    }

    @Test
    fun `should decode short string`() {
        with(Base64Decoder) {
            val encoded = "YQ=="
            val expected = "a"
            val result = encoded.toBase64DecodedString()
            assertEquals(expected, result.getOrThrow())
        }
    }

    @Test
    fun `should decode string`() {
        with(Base64Decoder) {
            val encoded = "eyJzdWIiOiIxMjM0NTY3ODkwIn0"
            val expected = """{"sub":"1234567890"}"""
            val result = encoded.toBase64DecodedString()
            assertEquals(expected, result.getOrThrow())
        }
    }

    @Test
    fun `should decode long string`() {
        with(Base64Decoder) {
            val encoded = """
                eyJyYW5kb20iOiI1NyIsInJhbmRvbSBmbG9hdCI6Ijg0LjIwMSIsImJvb2wiOiJ0cnVlIiwiZGF0ZSI6IjE5OTYtMDMtMjEiLCJyZWdF
                eCI6ImhlbGxvb29vb29vb29vb29vb29vb29vb29vb29vb29vb29vb29vb29vb29vb29vb29vb29vb29vb29vb29vb29vb29vb29vb29v
                b29vb29vb29vb29vb29vb29vb29vb29vb29vIHdvcmxkIiwiZW51bSI6ImdlbmVyYXRvciIsImZpcnN0bmFtZSI6IlVscmlrZSIsImxh
                c3RuYW1lIjoiSmFsYmVydCIsImNpdHkiOiJTYW4gU2FsdmFkb3IiLCJjb3VudHJ5IjoiTWFydGluaXF1ZSIsImNvdW50cnlDb2RlIjoi
                SE4iLCJlbWFpbCB1c2VzIGN1cnJlbnQgZGF0YSI6IlVscmlrZS5KYWxiZXJ0QGdtYWlsLmNvbSIsImVtYWlsIGZyb20gZXhwcmVzc2lv
                biI6IlVscmlrZS5KYWxiZXJ0QHlvcG1haWwuY29tIiwiYXJyYXkiOlsiSGFycmlldHRhIiwiQ2VsZXN0eW5hIiwiQ2FtaWxlIiwiRXZp
                dGEiLCJKZXNzYW15biJdLCJhcnJheSBvZiBvYmplY3RzIjpbeyJpbmRleCI6IjAiLCJpbmRleCBzdGFydCBhdCA1IjoiNSJ9LHsiaW5k
                ZXgiOiIxIiwiaW5kZXggc3RhcnQgYXQgNSI6IjYifSx7ImluZGV4IjoiMiIsImluZGV4IHN0YXJ0IGF0IDUiOiI3In1dLCJGYXduZSI6
                eyJhZ2UiOiI2OSJ9fQ==
                """.trimIndent().replace("\n", "")
            val expected = """
                {"random":"57","random float":"84.201","bool":"true","date":"1996-03-21","regEx":"helloooooooooooooooooo
                ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo world","enum":"generator
                ","firstname":"Ulrike","lastname":"Jalbert","city":"San Salvador","country":"Martinique","countryCode":"
                HN","email uses current data":"Ulrike.Jalbert@gmail.com","email from expression":"Ulrike.Jalbert@yopmail
                .com","array":["Harrietta","Celestyna","Camile","Evita","Jessamyn"],"array of objects":[{"index":"0","in
                dex start at 5":"5"},{"index":"1","index start at 5":"6"},{"index":"2","index start at 5":"7"}],"Fawne":
                {"age":"69"}}
                """.trimIndent().replace("\n", "")
            val result = encoded.toBase64DecodedString()
            assertEquals(expected, result.getOrThrow())
        }
    }

    @Test
    fun `should ignore non-base64-alphabet characters`() {
        with(Base64Decoder) {
            val encoded = "%Y#WJ!!jREVG@"
            val expected = "abcDEF"
            val result = encoded.toBase64DecodedString()
            assertEquals(expected, result.getOrThrow())
        }
    }
}
