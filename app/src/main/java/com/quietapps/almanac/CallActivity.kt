package com.quietapps.almanac

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CallActivity : AppCompatActivity() {

    private var isMuted = false
    private var isSpeakerOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call)

        val name = intent.getStringExtra("CONTACT_NAME") ?: "Пользователь"
        val callAvatar = findViewById<TextView>(R.id.callAvatar)
        val callName = findViewById<TextView>(R.id.callName)
        val callStatus = findViewById<TextView>(R.id.callStatus)

        callName.text = name
        callAvatar.text = name.first().toString().uppercase()

        // Симуляция соединения
        callStatus.text = "Соединение..."
        callStatus.postDelayed({ callStatus.text = "00:00" }, 2000)

        val btnMute = findViewById<ImageButton>(R.id.btnMute)
        val btnSpeaker = findViewById<ImageButton>(R.id.btnSpeaker)

        btnMute.setOnClickListener {
            isMuted = !isMuted
            btnMute.alpha = if (isMuted) 0.5f else 1.0f
            Toast.makeText(this, if (isMuted) "Микрофон выключен" else "Микрофон включен", Toast.LENGTH_SHORT).show()
        }

        btnSpeaker.setOnClickListener {
            isSpeakerOn = !isSpeakerOn
            btnSpeaker.alpha = if (isSpeakerOn) 0.5f else 1.0f
            Toast.makeText(this, if (isSpeakerOn) "Динамик включен" else "Динамик выключен", Toast.LENGTH_SHORT).show()
        }

        findViewById<ImageButton>(R.id.btnVideo).setOnClickListener {
            Toast.makeText(this, "Видеозвонок (скоро)", Toast.LENGTH_SHORT).show()
        }

        findViewById<ImageButton>(R.id.btnEndCall).setOnClickListener {
            Toast.makeText(this, "Вызов завершён", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
