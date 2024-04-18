import java.sql.DriverManager
import java.sql.ResultSet
import org.mindrot.jbcrypt.BCrypt

fun signUp(username: String, password: String, email: String) {
    val url = "jdbc:postgresql://localhost:5432/usr_cred"
    val user = "postgres"
    val password = "hitmanagent47"

    val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())

    DriverManager.getConnection(url, user, password).use { conn ->
        conn.prepareStatement("INSERT INTO user_credentials (username, password) VALUES (?, ?)").use { stmt ->
            stmt.setString(1, username)
            stmt.setString(2, hashedPassword)
            stmt.setString(3, email)
            stmt.executeUpdate()
        }
    }
}

fun login(username: String, password: String): Boolean {
    val url = "jdbc:postgresql://localhost:5432/usr_cred"
    val user = "postgres"
    val password = "hitmanagent47"

    DriverManager.getConnection(url, user, password).use { conn ->
        conn.prepareStatement("SELECT * FROM user_credentials WHERE username = ? AND password = ?").use { stmt ->
            stmt.setString(1, username)
            stmt.setString(2, password)
            val resultSet: ResultSet = stmt.executeQuery()
            return resultSet.next()
        }
    }
}


