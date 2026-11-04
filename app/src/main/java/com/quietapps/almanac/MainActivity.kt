package com.quietapps.almanac

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.chatAlexey).setOnClickListener {
            onChatClicked("Алексей")
        }
        findViewById<View>(R.id.chatMaria).setOnClickListener {
            onChatClicked("Мария")
        }
        findViewById<View>(R.id.chatDmitry).setOnClickListener {
            onChatClicked("Дмитрий")
        }
    }

    private fun onChatClicked(name: String) {
        Toast.makeText(this, "Чат: $name", Toast.LENGTH_SHORT).show()
        // Позже: открыть экран конкретного чата, передать name/id
    }
}
