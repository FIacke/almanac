package com.quietapps.almanac

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

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
        val sendButton = findViewById<Button>(R.id.sendButton)

        sendButton.setOnClickListener {
            val text = input.text.toString().trim()
            if (text.isNotEmpty()) {
                addMessage(text)
                input.text.clear()
            }
        }
    }

    private fun addMessage(text: String) {
        val textView = TextView(this).apply {
            this.text = text
            textSize = 16f
            setTextColor(Color.BLACK)
            setPadding(32, 24, 32, 24)

            // Красивый пузырь (скругленные углы)
            val background = GradientDrawable().apply {
                setColor(Color.parseColor("#DCF8C6")) // Светло-зеленый
                cornerRadius = 32f
            }
            this.background = background

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 16)
            params.gravity = Gravity.END // Выравнивание по правому краю
            layoutParams = params
        }

        messagesContainer.addView(textView)

        // Прокручиваем вниз к новому сообщению
        messagesScroll.post {
            messagesScroll.fullScroll(View.FOCUS_DOWN)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
