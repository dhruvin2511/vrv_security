package com.example.vrv_security

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vrv_security.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    // Firebase Authentication and Database instances
    private lateinit var auth: FirebaseAuth
    private  lateinit var database: DatabaseReference

    // View binding for accessing UI elements
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

//         Check if the user is already logged in
        checkIfUserIsLoggedIn()

        // Firebase setup
        initializeFirebase()

        // Setup button click listeners
        setupListeners()

    }

//    Initializes Firebase Authentication and Database instances.

    private fun initializeFirebase() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
    }

//    Configures click listeners for login and navigation buttons.

    private fun setupListeners() {
        binding.loginButton.setOnClickListener {
            handleLogin()
        }

        binding.donthavebutton.setOnClickListener {
            navigateToSignup()
        }
    }

//    Handles login by validating input and attempting Firebase Authentication.

    private fun handleLogin() {
        val email = binding.emailAddresss.text.toString().trim()
        val password = binding.passwordd.text.toString().trim()

        if (validateInput(email, password)) {
            authenticateUser(email, password)
        }
    }

//    Validates user input for email and password.

    private fun validateInput(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                showToast("Please enter your email.")
                false
            }
            password.isEmpty() -> {
                showToast("Please enter your password.")
                false
            }
            else -> true
        }
    }


//    Authenticates the user with Firebase using email and password.

    private fun authenticateUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        fetchUserDetails(userId)
                    }
                } else {
                    showToast("Login failed: ${task.exception?.message}")
                }
            }
    }

//    Fetches user details from Firebase Realtime Database and navigates to the appropriate activity based on the user's role.

    private fun fetchUserDetails(userId: String) {

        if (!::database.isInitialized) {
            Log.e("LoginActivity", "Database not initialized!")
            return
        }

        val userRef = database.child("user").child(userId)

        userRef.get()
            .addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    val name = dataSnapshot.child("name").value.toString()
                    val role = dataSnapshot.child("role").value.toString()

                    showToast("Welcome, $name!")
                    navigateBasedOnRole(role)
                } else {
                    showToast("User not found in the database.")
                }
            }
            .addOnFailureListener { exception ->
                showToast("Failed to fetch user details: ${exception.message}")
            }
    }


    private fun checkIfUserIsLoggedIn() {
        try {
            // Initialize Firebase Authentication
            val currentUser = FirebaseAuth.getInstance().currentUser

            if (currentUser != null) {
                // User is logged in; fetch their details from the database
                val userId = currentUser.uid
                fetchUserDetails(userId)
            }
        } catch (e: Exception) {
            // Log and show any unexpected errors
            e.printStackTrace()
            showToast("An error occurred: ${e.message}")
        }
    }




//    Navigates to the appropriate activity based on the user's role.

    private fun navigateBasedOnRole(role: String) {

        when(role){
            "Farmer" -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            "Customer" -> {
                startActivity(Intent(this, CustomerActivity::class.java))
                finish()
            }
            else -> {
                showToast("Unknown role: $role")
            }
        }

    }

//    Navigates to the SignupActivity.

    private fun navigateToSignup() {
        startActivity(Intent(this, SignupActivity::class.java))
    }



//     Displays a short toast message.

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}