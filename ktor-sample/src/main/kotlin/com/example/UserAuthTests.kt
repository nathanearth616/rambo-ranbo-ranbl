fun main() {
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
