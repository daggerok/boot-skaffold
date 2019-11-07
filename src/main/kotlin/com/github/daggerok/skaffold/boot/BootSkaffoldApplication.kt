package com.github.daggerok.skaffold.boot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BootSkaffoldApplication

fun main(args: Array<String>) {
    runApplication<BootSkaffoldApplication>(*args)
}
