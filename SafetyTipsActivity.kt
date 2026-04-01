package com.example.safezone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class SafetyTipsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_safety_tips)

        val textView = findViewById<TextView>(R.id.tvSafetyTips)
        textView.text = """
            Given below are some measures to be followed by ladies for their safety:

            • Keep the least amount of money while shopping. Use plastic money (Debit/Credit card) as much as possible.
            
            • Wear the least amount of jewellery while shopping.
            
            • Always hang your purse through neck at front.
            
            • Keep the vehicle doors locked while driving.
            
            • Take necessary precautions while withdrawing huge amount from bank.
            
            • Do not accept edibles from strangers, and also train the children about it.
            
            • Do not allow strangers to enter home without identification.
            
            • Make sure that NIC of your servant is attested by NADRA. Do not employ servants without identification.
            
            • Do not leave small kids alone in the custody of single male/female servants.
            
            • Train the kids to keep distance from strangers.
        """.trimIndent()
    }
}
