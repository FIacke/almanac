package com.quietapps.almanac

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatActivity : AppCompatActivity() {

    private lateinit var messagesContainer: LinearLayout
    private lateinit var messagesScroll: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val contactName = intent.getStringExtra("CONTACT_NAME") ?: "Чат"

        findViewById<TextView>(R.id.contactName).text = contactName

        messagesContainer = findViewById(R.id.messagesContainer)
        messagesScroll = findViewById(R.id.messagesScroll)

        val input = findViewById<EditText>(R.id.messageInput)
        val sendButton = findViewById<Button>(R.id.sendButton)

        findViewById<TextView>(R.id.backButton).setOnClickListener {
            finish()
        }

        sendButton.setOnClickListener {
            val text = input.text.toString().trim()

            if (text.isNotEmpty()) {
                addMessage(text)
                input.text.clear()
            }
        }

        input.setOnEditorActionListener { _, _, _ ->
            val text = input.text.toString().trim()

            if (text.isNotEmpty()) {
                addMessage(text)
                input.text.clear()
            }

            true
        }
    }

    private fun addMessage(text: String) {

        val time = SimpleDateFormat(
            "HH:mm",
            Locale.getDefault()
        ).format(Date())

        val bubble = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 11, 16, 8)

            val shape = GradientDrawable().apply {
                setColor(Color.parseColor("#354B3A"))
                cornerRadius = 22f
            }

            background = shape
        }

        val messageText = TextView(this).apply {
            this.text = text
            textSize = 15f
            setTextColor(Color.WHITE)
            setLineSpacing(2f, 1f)
        }

        val timeText = TextView(this).apply {
            this.text = time
            textSize = 10f
            setTextColor(Color.parseColor("#B6C4B8"))
            gravity = Gravity.END
            setPadding(0, 5, 0, 0)
        }

        bubble.addView(messageText)
        bubble.addView(timeText)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.END
            setMargins(45, 0, 0, 12)
        }

        messagesContainer.addView(bubble, params)

        messagesScroll.post {
            messagesScroll.fullScroll(View.FOCUS_DOWN)
        }
    }
}