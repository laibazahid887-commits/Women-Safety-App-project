package com.example.safezone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var loginButton: MaterialButton
    private lateinit var signUpText: TextView
    private lateinit var forgotPasswordText: TextView
    private lateinit var rememberMeCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Link UI elements
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.loginbutton)
        signUpText = findViewById(R.id.signup)
        forgotPasswordText = findViewById(R.id.forgetpassword)
        rememberMeCheckBox = findViewById(R.id.rememberme)

        // Login Button Click
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = auth.currentUser?.uid
                        val database = FirebaseDatabase.getInstance().getReference("Users") // 🔥 Capital 'U'

                        if (uid != null) {
                            database.child(uid).get()
                                .addOnSuccessListener { snapshot ->
                                    if (snapshot.exists()) {
                                        val role = snapshot.child("role").value.toString()
                                        Log.d("LOGIN_DEBUG", "User Role: $role")

                                        if (role == "admin") {
                                            Toast.makeText(this, "Welcome Admin!", Toast.LENGTH_SHORT).show()
                                            startActivity(Intent(this, AdminActivity::class.java))
                                        } else {
                                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                                            startActivity(Intent(this, Frontpage::class.java))
                                        }
                                        finish()
                                    } else {
                                        Toast.makeText(this, "User data not found in database", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Failed to get user role: ${it.message}", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // Sign Up Redirect
        signUpText.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        // Forgot Password
        forgotPasswordText.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Enter your email to reset password", Toast.LENGTH_SHORT).show()
            } else {
                auth.sendPasswordResetEmail(email)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Reset email sent", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
