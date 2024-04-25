import java.sql.DriverManager
import java.sql.ResultSet

fun main() {
    println("Starting main function...")
    // Load the PostgreSQL JDBC driver
    Class.forName("org.postgresql.Driver")

    // Database connection details
    val url = "jdbc:postgresql://localhost:5432/usr_cred"
    val user = "postgres"
    val password = "hitmanagent47"

    val testUsername = "testUser"
    val testPassword = "testPassword"
    val testEmail = "test@example.com"

    println("Signing up test user...")
    // Sign up a test user
    signUp(testUsername, testPassword, testEmail, url, user, password)
    
    println("Testing login with correct credentials...")
    // Test login with correct credentials
    val loggedIn = login(testUsername, testPassword, url, user, password)
    if (loggedIn) {
        println("Login successful")
    } else {
        println("Login failed")
    }
    
    println("Testing login with incorrect credentials...")
    // Test login with incorrect credentials
    val wrongPassword = "wrongPassword"
    val wrongLogin = login(testUsername, wrongPassword, url, user, password)
    if (wrongLogin) {
        println("Login successful with wrong password (This shouldn't happen)")
    } else {
        println("Login failed with wrong password")
    }

    
    println("Main function completed.")
}

// Function to register a new user
fun signUp(username: String, password: String, email: String, url: String, user: String, passwordDb: String) {
    // Establish a connection to the database and insert user credentials
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
fun login(username: String, password: String, url: String, user: String, passwordDb: String): Boolean {
    DriverManager.getConnection(url, user, passwordDb).use { conn ->
        // Prepare SQL statement to select password based on username
        conn.prepareStatement("SELECT password FROM user_credentials WHERE username = ?").use { stmt ->
            stmt.setString(1, username)
            val resultSet: ResultSet = stmt.executeQuery()
            if (resultSet.next()) {
                val storedPassword = resultSet.getString("password")
                // Compare the provided password with the stored password directly
                return storedPassword == password
            }
        }
    }
    // Return false if the user authentication fails
    return false
}

