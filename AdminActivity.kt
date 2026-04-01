package com.example.safezone

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class AdminActivity : AppCompatActivity() {

    private lateinit var logoutBtn: Button
    private lateinit var viewUsersBtn: Button
    // Remove viewStatsBtn declaration since you no longer have that button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        logoutBtn = findViewById(R.id.logoutBtn)
        viewUsersBtn = findViewById(R.id.viewUsersBtn)
        // Remove initialization of viewStatsBtn

        logoutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        viewUsersBtn.setOnClickListener {
            startActivity(Intent(this, ViewUsersActivity::class.java)) // your existing user list activity
        }

        // Remove the viewStatsBtn click listener since button is removed
    }
}
