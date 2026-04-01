package com.example.safezone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class WomenLawsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_women_laws)

        val textView = findViewById<TextView>(R.id.tvWomenLaws)
        textView.text = """
            • The Protection against Harassment of Women at Workplace Act, 2010 – for prevention and protection of women against harassment at the workplace.
            
            • The Acid Control and Acid Crime Prevention Act, 2011 (Criminal Law Second Amendment Act, 2011) – for prevention of acid crimes and strict punishments.
            
            • Prevention of Anti-Women Practices Act, 2011 (Criminal Law Third Amendment Act, 2011) – for prevention of customary practices such as giving a female in marriage or otherwise in badla-e-sulh, wanni, or swara; depriving women from inheriting property; forced marriages; and marriage of females with the Holy Quran.
            
            • Protection of Women (Criminal Laws Amendment) Act, 2006 – makes rape a criminal offence punishable with death or imprisonment for not less than ten years.
        """.trimIndent()
    }
}
