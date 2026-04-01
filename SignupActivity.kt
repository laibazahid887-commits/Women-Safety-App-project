package com.example.safezone

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference

        val signupBtn = findViewById<Button>(R.id.signupButton)

        signupBtn.setOnClickListener {
            val email = findViewById<EditText>(R.id.signupEmail).text.toString()
            val password = findViewById<EditText>(R.id.signupPassword).text.toString()
            val phone = findViewById<EditText>(R.id.signupPhone).text.toString()
            val birthdate = findViewById<EditText>(R.id.signupBirthdate).text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val uid = auth.currentUser!!.uid
                            val userMap = mapOf(
                                "email" to email,
                                "password" to password,
                                "phone" to phone,
                                "birthdate" to birthdate,
                                "role" to "user" // 👈 Add this line!
                            )
                            db.child("Users").child(uid).setValue(userMap)
                            Toast.makeText(this, "Signup Success", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, Frontpage::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Signup Failed: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
