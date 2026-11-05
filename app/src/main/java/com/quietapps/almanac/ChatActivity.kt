package com.quietapps.almanac

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var messagesContainer: LinearLayout
    private lateinit var messagesScroll: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val contactName = intent.getStringExtra("CONTACT_NAME") ?: "Чат"
        supportActionBar?.title = contactName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        messagesContainer = findViewById(R.id.messagesContainer)
        messagesScroll = findViewById(R.id.messagesScroll)

        val input = findViewById<EditText>(R.id.messageInput)
        val sendButton = findViewById<ImageButton>(R.id.sendButton)

        // Тестовое входящее сообщение
        addMessage("Привет! Это тестовое сообщение из тёмного чата.", isOutgoing = false)

        sendButton.setOnClickListener {
            val text = input.text.toString().trim()
            if (text.isNotEmpty()) {
                addMessage(text, isOutgoing = true)
                input.text.clear()
            }
        }
    }

    private fun addMessage(text: String, isOutgoing: Boolean) {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTime = timeFormat.format(Date())

        // 1. Контейнер для пузыря
        val bubbleLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = if (isOutgoing) Gravity.END else Gravity.START

            val leftMargin = if (isOutgoing) 60 else 0
            val rightMargin = if (isOutgoing) 0 else 60

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            // 4 аргумента: left, top, right, bottom
            params.setMargins(leftMargin, 0, rightMargin, 12)
            params.gravity = if (isOutgoing) Gravity.END else Gravity.START
            layoutParams = params
        }

        // 2. Сам пузырь
        val textView = TextView(this).apply {
            this.text = text
            textSize = 16f
            setTextColor(Color.WHITE)
            setPadding(24, 16, 24, 16)

            val bgColor = if (isOutgoing) Color.parseColor("#2B5278") else Color.parseColor("#182533")
            val background = GradientDrawable().apply {
                setColor(bgColor)
                cornerRadius = 32f
                if (isOutgoing) {
                    cornerRadii = floatArrayOf(32f, 32f, 32f, 32f, 32f, 32f, 0f, 0f)
                } else {
                    cornerRadii = floatArrayOf(32f, 32f, 32f, 32f, 0f, 0f, 32f, 32f)
                }
            }
            this.background = background
        }

        // 3. Время
        val timeView = TextView(this).apply {
            this.text = currentTime
            textSize = 11f
            setTextColor(Color.parseColor("#8B9BA5"))
            setPadding(8, 0, 16, 4)
            gravity = Gravity.BOTTOM
        }

        bubbleLayout.addView(textView)
        bubbleLayout.addView(timeView)

        messagesContainer.addView(bubbleLayout)

        messagesScroll.post {
            messagesScroll.fullScroll(View.FOCUS_DOWN)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
