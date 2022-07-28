package dev.vtonder.jwtil

import dev.vtonder.jwtil.JsonFormatter.toFormattedString
import kotlin.test.Test
import kotlin.test.assertEquals

class JsonTests {

    @Test
    fun `should format empty string`() {
        with(JsonFormatter) {
            val json = ""
            val result = json.toFormattedString()
            assertEquals("", result.getOrNull())
        }
    }

    @Test
    fun `should format null`() {
        with(JsonFormatter) {
            val json = "null"
            val result = json.toFormattedString()
            assertEquals("null", result.getOrNull())
        }
    }

    @Test
    fun `should format number`() {
        with(JsonFormatter) {
            val example1 = "123"
            val example2 = "3.145123"
            val result1 = example1.toFormattedString()
            val result2 = example2.toFormattedString()
            assertEquals("123", result1.getOrNull())
            assertEquals("3.145123", result2.getOrNull())
        }
    }

    @Test
    fun `should format boolean`() {
        val json = "true"
        val result = json.toFormattedString()
        assertEquals("true", result.getOrNull())
    }

    @Test
    fun `should format string`() {
        val json = """"test""""
        val result = json.toFormattedString()
        assertEquals(""""test"""", result.getOrNull())
    }

    @Test
    fun `should format json object`() {
        val expected = """
            {
               "key1": 1,
               "key2": "test"
            }
            """.trimIndent()
        val json = expected.replace("\\s".toRegex(), "")
        val result = json.toFormattedString()
        assertEquals(expected, result.getOrNull())
    }

    @Test
    fun `should format json array`() {
        val expected = """
            [
               "value1",
               "value2",
               "value3"
            ]
            """.trimIndent()
        val json = expected.replace("\\s".toRegex(), "")
        val result = json.toFormattedString()
        assertEquals(expected, result.getOrNull())
    }

    @Test
    fun `should format mixed example`() {
        val expected = """
            {
               "name": "externalparamrule",
               "description": "Sample...",
               "enabled": true,
               "externalParamConfigRule": [
                  {
                     "conditionGroup": [
                        {
                           "condition": [
                              {
                                 "conditionName": "OS",
                                 "conditionValue": "win",
                                 "contains": true
                              },
                              {
                                 "conditionName": "patchlevel",
                                 "lowerThreshold": "45",
                                 "lessThanOrEqual": true
                              }
                           ],
                           "nextGroupCondition": "OR",
                           "operation": "AND",
                           "priority": 0
                        },
                        {
                           "condition": [
                              {
                                 "conditionName": "gradelevel",
                                 "lowerThreshold": "3",
                                 "higherThreshold": "8",
                                 "lessThan": true,
                                 "greaterThan": true
                              }
                           ],
                           "nextGroupCondition": "OR",
                           "operation": "AND",
                           "priority": 0
                        }
                     ],
                     "fetchFromParamSource": true,
                     "negateResult": false,
                     "paramSource": [
                        {
                           "authenticationType": "None",
                           "dataFormat": "JSON",
                           "requestMethod": "POST",
                           "requestParameter": [
                              {
                                 "name": "testparam",
                                 "staticValue": "teststring",
                                 "contextValue": null
                              }
                           ],
                           "requestTimeout": 30000,
                           "url": "http://external.site.com/rest/user/encodedUserId"
                        }
                     ]
                  }
               ]
            }
        """.trimIndent()
        val json = expected.replace("\\s".toRegex(), "")
        val result = json.toFormattedString()
        assertEquals(expected, result.getOrNull())
    }

    @Test
    fun `should truncate when exceeding max depth`() {
        val json = """
            {
               "key1": {
                  "key2": {
                     "key3": {
                        "key4": {
                           "key5": {
                              "key6": {
                                 "key7": {
                                    "key8": {
                                       "key9": {
                                          "key10": {
                                             "key11": {
                                                "key12": "I'm in too deep..."
                                             }
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
            """.trimIndent().replace("\\s".toRegex(), "")
        val expected = """
            {
               "key1": {
                  "key2": {
                     "key3": {
                        "key4": {
                           "key5": {
                              "key6": {
                                 "key7": {
                                    "key8": {
                                       "key9": {
                                          "key10": {
                                             "key11": ...
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
            """.trimIndent()
        val result = json.toFormattedString()
        assertEquals(expected, result.getOrNull())
    }

    @Test
    fun `should yield unsuccessful result when given invalid json`() {
        val notJson = "this is not valid { 'json' }"
        val result = notJson.toFormattedString()
        assertEquals(JsonFormatter.InvalidJsonError, result.exceptionOrNull())
    }
}
