import java.sql.DriverManager
import java.sql.ResultSet
import org.mindrot.jbcrypt.BCrypt

// Database connection details
val url = "jdbc:postgresql://localhost:5432/usr_cred"
val user = "postgres"
val password = "hitmanagent47"

// Function to register a new user
fun signUp(username: String, password: String, email: String) {
    // Hash the provided password using BCrypt
    val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())

    // Establish a connection to the database and insert user credentials
    DriverManager.getConnection(url, user, password).use { conn ->
        conn.prepareStatement("INSERT INTO user_credentials (username, password, email) VALUES (?, ?, ?)").use { stmt ->
            stmt.setString(1, username)
            stmt.setString(2, hashedPassword)
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
                val hashedPassword = resultSet.getString("password")// Retrieve hashed password from database
                // Check if the provided password matches the hashed password using BCrypt
                return BCrypt.checkpw(password, hashedPassword)
            }
        }
    }
    // Return false if the user authentication fails
    return false
}


