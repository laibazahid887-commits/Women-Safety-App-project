package com.example.safezone

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BlankFragmentMore : Fragment() {

    private lateinit var btnWomenLaws: Button
    private lateinit var btnSafetyTips: Button
    private lateinit var recyclerView: RecyclerView

    private val imageIds = listOf(
        R.drawable.lawpic,
        R.drawable.pic2,
        R.drawable.law
    )

    private var currentPosition = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_blank_more, container, false)

        // Initialize Views
        btnWomenLaws = view.findViewById(R.id.btnwomenLaws)
        btnSafetyTips = view.findViewById(R.id.btnsafetyTips)
        recyclerView = view.findViewById(R.id.imageSliderRecyclerView)

        // Setup Image Slider (Horizontal)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = ImageSliderAdapter(imageIds)

        // Auto-scroll every 3 seconds
        startAutoScroll()

        // Button Clicks
        btnWomenLaws.setOnClickListener {
            startActivity(Intent(requireContext(), WomenLawsActivity::class.java))
        }

        btnSafetyTips.setOnClickListener {
            startActivity(Intent(requireContext(), SafetyTipsActivity::class.java))
        }

        return view
    }

    private fun startAutoScroll() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (imageIds.isNotEmpty()) {
                    currentPosition = (currentPosition + 1) % imageIds.size
                    recyclerView.smoothScrollToPosition(currentPosition)
                }
                handler.postDelayed(this, 3000) // 3 seconds delay
            }
        }, 3000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null) // Stop scrolling when fragment is destroyed
    }
}
