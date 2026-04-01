package com.example.safezone

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class BlankFragmentProfile : Fragment() {

    private lateinit var auth: FirebaseAuth

    private lateinit var emailEditText: TextInputEditText
    private lateinit var birthDateEditText: TextInputEditText
    private lateinit var phoneEditText: TextInputEditText
    private lateinit var submitButton: MaterialButton
    private lateinit var logoutButton: MaterialButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_blank_profile, container, false)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Bind views from layout
        emailEditText = view.findViewById(R.id.enteremail)
        birthDateEditText = view.findViewById(R.id.enterbirthdate)
        phoneEditText = view.findViewById(R.id.enternumbr)
        submitButton = view.findViewById(R.id.buttonSubmit)
        logoutButton = view.findViewById(R.id.logoutButton)

        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()

            // Go to LoginActivity
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        submitButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val birthDate = birthDateEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()

            if (email.isNotEmpty() && birthDate.isNotEmpty() && phone.isNotEmpty()) {
                saveUserProfile(email, birthDate, phone)
            } else {
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun saveUserProfile(email: String, birthDate: String, phone: String) {
        val uid = auth.currentUser?.uid

        if (uid != null) {
            val userMap = mapOf(
                "email" to email,
                "birthDate" to birthDate,
                "phone" to phone
            )

            FirebaseDatabase.getInstance().reference
                .child("Users")
                .child(uid)
                .setValue(userMap)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Profile saved successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to save user data", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }
}
