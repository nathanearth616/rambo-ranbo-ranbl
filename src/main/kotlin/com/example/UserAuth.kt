package com.example

import java.sql.DriverManager
import java.sql.ResultSet
// import org.mindrot.jbcrypt.BCrypt


// Database connection details
val url = "jdbc:postgresql://localhost:5432/usr_cred"
val user = "postgres"
val password = "hitmanagent47"

// Function to register a new user
fun signUp(username: String, password: String, email: String) {
    // Hash the provided password using BCrypt
    // val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())

    //TODO; FIND OUT IF YOU CN USE BCRYPT TO HASH EMAIL TOO OR WHETHER YOU NEED SOMETHIG ELSE

    // Establish a connection to the database and insert user credentials
    DriverManager.getConnection(url, user, password).use { conn ->
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
    DriverManager.getConnection(url, user, password).use { conn ->
        // Prepare SQL statement to select hashed password based on username
        conn.prepareStatement("SELECT password FROM user_credentials WHERE username = ?").use { stmt ->
            stmt.setString(1, username)
            val resultSet: ResultSet = stmt.executeQuery()
            if (resultSet.next()) {
                val Password = resultSet.getString("password")// Retrieve hashed password from database
                // Check if the provided password matches the hashed password using BCrypt
                return password == Password
            }
        }
    }
    // Return false if the user authentication fails
    return false
}



fun main() {
    Class.forName("org.postgresql.Driver")
    val testUsername = "testUser"
    val testPassword = "testPassword"
    val testEmail = "test@example.com"

    // Sign up a test user
    signUp(testUsername, testPassword, testEmail)
    
    // Test login with correct credentials
    val loggedIn = login(testUsername, testPassword)
    if (loggedIn) {
        println("Login successful")
    } else {
        println("Login failed")
    }
    
    // Test login with incorrect credentials
    val wrongPassword = "wrongPassword"
    val wrongLogin = login(testUsername, wrongPassword)
    if (wrongLogin) {
        println("Login successful with wrong password (This shouldn't happen)")
    } else {
        println("Login failed with wrong password")
    }
}