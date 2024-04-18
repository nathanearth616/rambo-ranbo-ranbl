import java.sql.DriverManager
import java.sql.ResultSet
import org.mindrot.jbcrypt.BCrypt


val url = "jdbc:postgresql://localhost:5432/usr_cred"
val user = "postgres"
val password = "hitmanagent47"

fun signUp(username: String, password: String, email: String) {
    val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())

    DriverManager.getConnection(url, user, password).use { conn ->
        conn.prepareStatement("INSERT INTO user_credentials (username, password, email) VALUES (?, ?, ?)").use { stmt ->
            stmt.setString(1, username)
            stmt.setString(2, hashedPassword)
            stmt.setString(3, email)
            stmt.executeUpdate()
        }
    }
}

fun login(username: String, password: String): Boolean {
    DriverManager.getConnection(url, user, password).use { conn ->
        conn.prepareStatement("SELECT password FROM user_credentials WHERE username = ?").use { stmt ->
            stmt.setString(1, username)
            val resultSet: ResultSet = stmt.executeQuery()
            if (resultSet.next()) {
                val hashedPassword = resultSet.getString("password")
                return BCrypt.checkpw(password, hashedPassword)
            }
        }
    }
    return false
}


