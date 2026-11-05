package com.quietapps.almanac

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.chatAlexey).setOnClickListener {
            openChat("Алексей")
        }
        findViewById<View>(R.id.chatMaria).setOnClickListener {
            openChat("Мария")
        }
        findViewById<View>(R.id.chatDmitry).setOnClickListener {
            openChat("Дмитрий")
        }
    }

    private fun openChat(name: String) {
        // Здесь больше нет Toast! Только переход на новый экран.
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("CONTACT_NAME", name)
        startActivity(intent)
    }
}
