package com.example.safezone

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class BlankFragmentHome : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_blank_home, container, false)

        // Find buttons and set click listeners
        val emergencyBtn = view.findViewById<Button>(R.id.rescue_btn)
        val locationBtn = view.findViewById<Button>(R.id.Locationview_btn)

        emergencyBtn.setOnClickListener {
            val intent = Intent(requireContext(), EmergencyCall::class.java)
            startActivity(intent)
        }

        locationBtn.setOnClickListener {
            val intent = Intent(requireContext(), Location_activity::class.java)
            startActivity(intent)
        }

        return view
    }
}