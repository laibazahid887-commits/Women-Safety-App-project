package com.example.safezone

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class StatsActivity : AppCompatActivity() {

    private lateinit var totalUsersText: TextView
    private lateinit var totalAdminsText: TextView
    private lateinit var totalRegularUsersText: TextView

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        totalUsersText = findViewById(R.id.totalUsers)
        totalAdminsText = findViewById(R.id.totalAdmins)
        totalRegularUsersText = findViewById(R.id.totalRegularUsers)

        dbRef = FirebaseDatabase.getInstance().getReference("Users")

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var totalUsers = 0
                var totalAdmins = 0
                var totalRegulars = 0

                for (userSnap in snapshot.children) {
                    totalUsers++
                    val role = userSnap.child("role").getValue(String::class.java)
                    if (role == "admin") {
                        totalAdmins++
                    } else {
                        totalRegulars++
                    }
                }

                totalUsersText.text = "Total Users: $totalUsers"
                totalAdminsText.text = "Total Admins: $totalAdmins"
                totalRegularUsersText.text = "Regular Users: $totalRegulars"
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@StatsActivity, "Failed to fetch stats", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
