package com.hshim.asciiart

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AsciiartApplication

fun main(args: Array<String>) {
    runApplication<AsciiartApplication>(*args)
}
