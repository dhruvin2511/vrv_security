package com.example.vrv_security

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.RadioGroup
import android.widget.Toast
import com.example.vrv_security.Model.UserInfo
import com.example.vrv_security.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var userName: String
    private lateinit var database: DatabaseReference
    private lateinit var role: String

    private val binding: ActivitySignupBinding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Initialize Firebase Authentication and Realtime Database
        auth = Firebase.auth
        database = Firebase.database.reference

        // RadioGroup for selecting user role
        val radioGroup = findViewById<RadioGroup>(R.id.radio_group)

        // Listener for creating a new account
        binding.createNewAccount.setOnClickListener {

            // Get values from EditText fields
            userName = binding.userName.text.toString().trim()
            email = binding.emailAddress.text.toString().trim()
            password = binding.password.text.toString().trim()

            // Get selected role from RadioGroup
            val selectedRadioButtonId = radioGroup.checkedRadioButtonId
            if (selectedRadioButtonId != -1) {
                val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
                role = selectedRadioButton.text.toString().trim()
            } else {
                Toast.makeText(this, "Please select a role", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check for empty fields
            if (userName.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check if user already exists
            checkIfUserExists(email, userName)

            // Validate password length
            if (!isPasswordValid(password)) {
                Toast.makeText(
                    this,
                    "Password must be at least 6 characters long",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Create the user account
            createAccount(email, password)
        }

        // Listener to navigate to the login screen
        binding.alreadyhavebutton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    // Create a new user account using Firebase Authentication

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Successfully created the account
                Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                saveUserData()

                // Navigate to login page
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Account creation failed
                Toast.makeText(this, "Account Creation Failed", Toast.LENGTH_SHORT).show()
                Log.d("Account", "CreateAccount: Failure", task.exception)
            }
        }
    }

    // Save user data to Firebase Realtime Database

    private fun saveUserData() {
        val user = UserInfo(userName, email, password, role)
        val userId: String = FirebaseAuth.getInstance().currentUser!!.uid

        // Save user data to Firebase Database
        database.child("user").child(userId).setValue(user)
    }
    // checks if tthe user already exists in the database

    private fun checkIfUserExists(email: String, userName: String) {
        // Check if email already exists
        database.child("user").orderByChild("email").equalTo(email).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors (e.g., network issues)
                Log.e("SignupActivity", "Error checking if user exists", exception)
            }

        // Check if username already exists
        database.child("user").orderByChild("userName").equalTo(userName).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors
                Log.e("SignupActivity", "Error checking if username exists", exception)
            }
    }

    // This function checks if the provided password meets the minimum length requirement.

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }
}