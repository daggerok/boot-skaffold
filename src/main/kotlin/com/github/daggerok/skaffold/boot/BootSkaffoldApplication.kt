package com.github.daggerok.skaffold.boot

import org.apache.logging.log4j.LogManager
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@RestController
class IndexPage(private val env: Environment) {

    private val log = LogManager.getLogger()

    @GetMapping
    fun index() = Mono.just(mapOf("nello" to "skaffold"))
            .doOnEach { log.info("{}: I'm in at {}!", env["HOSTNAME"] ?: "Unknown", LocalDateTime.now()) }
}

@SpringBootApplication
class BootSkaffoldApplication

fun main(args: Array<String>) {
    runApplication<BootSkaffoldApplication>(*args)
}
