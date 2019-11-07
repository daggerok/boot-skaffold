package com.github.daggerok.skaffold.boot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class IndexPage {

    @GetMapping
    fun index() = mapOf("nello" to "skaffold")
}

@SpringBootApplication
class BootSkaffoldApplication

fun main(args: Array<String>) {
    runApplication<BootSkaffoldApplication>(*args)
}
