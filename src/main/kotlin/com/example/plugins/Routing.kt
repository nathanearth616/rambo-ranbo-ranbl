package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.doublereceive.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(DoubleReceive)
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        //  get("/test1") {
        //     val text = "test1"
        //     val type = ContentType.parse("text/html")
        //     call.respondText(text, type)
        // }

        post("/double-receive") {
            val first = call.receiveText()
            val theSame = call.receiveText()
            call.respondText(first + " " + theSame)
        }
    }
}
