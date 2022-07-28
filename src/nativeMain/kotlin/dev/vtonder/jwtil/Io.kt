package dev.vtonder.jwtil

import platform.posix.fdopen
import platform.posix.fflush
import platform.posix.fprintf

private val stderr = fdopen(2, "w")

actual fun eprintln(message: String) {
    fprintf(stderr, "%s\n", message)
    fflush(stderr)
}
