package com.quietapps.almanac

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        supportActionBar?.title = "О приложении"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<LinearLayout>(R.id.itemTerms).setOnClickListener {
            Toast.makeText(this, "Условия использования (демо)", Toast.LENGTH_SHORT).show()
        }
        findViewById<LinearLayout>(R.id.itemPrivacyPolicy).setOnClickListener {
            Toast.makeText(this, "Политика конфиденциальности (демо)", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
