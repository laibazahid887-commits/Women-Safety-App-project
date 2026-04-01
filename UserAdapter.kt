package com.example.safezone

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class UserAdapter(private val userList: List<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val emailText: TextView = itemView.findViewById(R.id.emailText)
        val phoneText: TextView = itemView.findViewById(R.id.phoneText)
        val birthText: TextView = itemView.findViewById(R.id.birthText)
        val roleText: TextView = itemView.findViewById(R.id.roleText)
        val editBtn: Button = itemView.findViewById(R.id.editBtn)
        val deleteBtn: Button = itemView.findViewById(R.id.deleteBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]

        // ✅ Safe DOB handling
        val dob = user.birthdate ?: user.birthDate ?: "Not Provided"

        holder.emailText.text = "Email: ${user.email}"
        holder.phoneText.text = "Phone: ${user.phone}"
        holder.birthText.text = "Birthdate: $dob"
        holder.roleText.text = "Role: ${user.role}"

        // 🔹 Edit Button Click
        holder.editBtn.setOnClickListener {
            Toast.makeText(
                holder.itemView.context,
                "Edit clicked for ${user.email}",
                Toast.LENGTH_SHORT
            ).show()

            // TODO: Open Edit Dialog or Activity
        }

        // 🔻 Delete Button Click
        holder.deleteBtn.setOnClickListener {
            val context = holder.itemView.context
            AlertDialog.Builder(context).apply {
                setTitle("Delete User")
                setMessage("Are you sure you want to delete this user?")
                setPositiveButton("Yes") { _, _ ->
                    FirebaseDatabase.getInstance()
                        .getReference("Users")
                        .orderByChild("email")
                        .equalTo(user.email)
                        .get()
                        .addOnSuccessListener { snapshot ->
                            if (snapshot.exists()) {
                                for (child in snapshot.children) {
                                    child.ref.removeValue()
                                }
                                Toast.makeText(context, "User deleted", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Error deleting user", Toast.LENGTH_SHORT).show()
                        }
                }
                setNegativeButton("Cancel", null)
                show()
            }
        }
    }

    override fun getItemCount(): Int = userList.size
}
