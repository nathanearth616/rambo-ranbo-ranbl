package com.example.plugins

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.sql.DriverManager
import java.sql.ResultSet

fun Application.configureRouting() {
    routing {
        post("/signup") {
            val params = call.receiveParameters()
            val username = params["username"]
            val password = params["password"]
            val email = params["email"]
            
            if (username != null && password != null && email != null) {
                signUp(username, password, email)
                call.respondText("User signed up successfully", status = HttpStatusCode.Created)
            } else {
                call.respondText("Invalid parameters", status = HttpStatusCode.BadRequest)
            }
        }
        
        post("/login") {
            val params = call.receiveParameters()
            val username = params["username"]
            val password = params["password"]
            
            if (username != null && password != null) {
                val loggedIn = login(username, password)
                if (loggedIn) {
                    call.respondText("Login successful")
                } else {
                    call.respondText("Login failed", status = HttpStatusCode.Unauthorized)
                }
            } else {
                call.respondText("Invalid parameters", status = HttpStatusCode.BadRequest)
            }
        }
    }
}

// Function to register a new user
fun signUp(username: String, password: String, email: String) {
    // Database connection details
    val url = "jdbc:postgresql://localhost:5432/usr_cred"
    val user = "postgres"
    val passwordDb = "hitmanagent47"
    
    DriverManager.getConnection(url, user, passwordDb).use { conn ->
        conn.prepareStatement("INSERT INTO user_credentials (username, password, email) VALUES (?, ?, ?)").use { stmt ->
            stmt.setString(1, username)
            stmt.setString(2, password)
            stmt.setString(3, email)
            stmt.executeUpdate()
        }
    }
}

// Function to authenticate a user
fun login(username: String, password: String): Boolean {
    // Database connection details
    val url = "jdbc:postgresql://localhost:5432/usr_cred"
    val user = "postgres"
    val passwordDb = "hitmanagent47"
    
    DriverManager.getConnection(url, user, passwordDb).use { conn ->
        conn.prepareStatement("SELECT password FROM user_credentials WHERE username = ?").use { stmt ->
            stmt.setString(1, username)
            val resultSet: ResultSet = stmt.executeQuery()
            if (resultSet.next()) {
                val storedPassword = resultSet.getString("password")
                return storedPassword == password
            }
        }
    }
    return false
}
