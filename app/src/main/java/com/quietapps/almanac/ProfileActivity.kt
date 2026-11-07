package com.quietapps.almanac

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val name = intent.getStringExtra("PROFILE_NAME") ?: "Пользователь"
        supportActionBar?.title = "Профиль"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val initial = name.first().toString().uppercase()

        findViewById<TextView>(R.id.profileAvatar).apply { text = initial }
        findViewById<TextView>(R.id.profileName).apply { text = name }
        findViewById<TextView>(R.id.profileId).apply { text = "ID: user_${name.lowercase()}" }

        findViewById<LinearLayout>(R.id.btnMessage).setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("CONTACT_NAME", name)
            startActivity(intent)
            finish()
        }

        findViewById<LinearLayout>(R.id.btnCall).setOnClickListener {
            Toast.makeText(this, "Звонок $name (скоро)", Toast.LENGTH_SHORT).show()
        }

        findViewById<LinearLayout>(R.id.btnBlock).setOnClickListener {
            Toast.makeText(this, "$name заблокирован", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
