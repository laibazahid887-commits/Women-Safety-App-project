package com.example.safezone

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ViewUsersActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var userAdapter: UserAdapter
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_users)

        recyclerView = findViewById(R.id.usersRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        userList = ArrayList()
        userAdapter = UserAdapter(userList)
        recyclerView.adapter = userAdapter

        databaseRef = FirebaseDatabase.getInstance().getReference("Users")

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("ViewUsers", "Children count: ${snapshot.childrenCount}") // ✅ log total nodes
                userList.clear()

                for (userSnap in snapshot.children) {
                    val user = userSnap.getValue(User::class.java)
                    if (user == null) {
                        Log.w("ViewUsers", "Failed to parse user at key: ${userSnap.key}") // ✅ log parse failure
                    } else {
                        Log.d("ViewUsers", "Loaded user: ${user.email}") // ✅ log user email
                        userList.add(user)
                    }
                }

                userAdapter.notifyDataSetChanged()
                Toast.makeText(
                    this@ViewUsersActivity,
                    "Loaded ${userList.size} users",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ViewUsers", "Load failed: ${error.message}") // ✅ log read error
                Toast.makeText(
                    this@ViewUsersActivity,
                    "Failed to load users: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
