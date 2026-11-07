package com.quietapps.almanac

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var messagesContainer: LinearLayout
    private lateinit var messagesScroll: ScrollView
    private var contactName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        contactName = intent.getStringExtra("CONTACT_NAME") ?: "Чат"
        supportActionBar?.title = contactName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        messagesContainer = findViewById(R.id.messagesContainer)
        messagesScroll = findViewById(R.id.messagesScroll)

        val input = findViewById<EditText>(R.id.messageInput)
        val sendButton = findViewById<ImageButton>(R.id.sendButton)
        val attachButton = findViewById<ImageButton>(R.id.attachButton)

        addMessage("Привет! Это тестовое сообщение из тёмного чата.", isOutgoing = false)

        sendButton.setOnClickListener {
            val text = input.text.toString().trim()
            if (text.isNotEmpty()) {
                addMessage(text, isOutgoing = true)
                input.text.clear()
            }
        }

        attachButton.setOnClickListener { showAttachmentsMenu() }
    }

    private fun showAttachmentsMenu() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_attachments, null)
        val dialog = AlertDialog.Builder(this, R.style.Theme_Almanac_Dialog)
            .setView(dialogView).create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        dialogView.findViewById<LinearLayout>(R.id.attachPhoto).setOnClickListener {
            Toast.makeText(this, "Выбор фото (скоро)", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialogView.findViewById<LinearLayout>(R.id.attachFile).setOnClickListener {
            Toast.makeText(this, "Выбор файла (скоро)", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialogView.findViewById<LinearLayout>(R.id.attachLocation).setOnClickListener {
            Toast.makeText(this, "Отправка локации (скоро)", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { finish(); return true }
            R.id.action_profile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                intent.putExtra("PROFILE_NAME", contactName)
                startActivity(intent)
                return true
            }
            R.id.action_search -> { Toast.makeText(this, "Поиск в чате (скоро)", Toast.LENGTH_SHORT).show(); return true }
            R.id.action_clear -> { messagesContainer.removeAllViews(); Toast.makeText(this, "История очищена", Toast.LENGTH_SHORT).show(); return true }
            R.id.action_block -> { Toast.makeText(this, "Контакт заблокирован", Toast.LENGTH_SHORT).show(); return true }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addMessage(text: String, isOutgoing: Boolean) {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTime = timeFormat.format(Date())

        val bubbleLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = if (isOutgoing) Gravity.END else Gravity.START
            val leftMargin = if (isOutgoing) 60 else 0
            val rightMargin = if (isOutgoing) 0 else 60
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(leftMargin, 0, rightMargin, 12)
            params.gravity = if (isOutgoing) Gravity.END else Gravity.START
            layoutParams = params
        }

        val textView = TextView(this).apply {
            this.text = text; textSize = 16f; setTextColor(Color.WHITE); setPadding(24, 16, 24, 16)
            val bgColor = if (isOutgoing) Color.parseColor("#2B5278") else Color.parseColor("#182533")
            background = GradientDrawable().apply {
                setColor(bgColor); cornerRadius = 32f
                if (isOutgoing) cornerRadii = floatArrayOf(32f, 32f, 32f, 32f, 32f, 32f, 0f, 0f)
                else cornerRadii = floatArrayOf(32f, 32f, 32f, 32f, 0f, 0f, 32f, 32f)
            }
        }

        val timeView = TextView(this).apply {
            this.text = currentTime; textSize = 11f
            setTextColor(Color.parseColor("#8B9BA5"))
            setPadding(8, 0, 16, 4); gravity = Gravity.BOTTOM
        }

        bubbleLayout.addView(textView)
        bubbleLayout.addView(timeView)
        messagesContainer.addView(bubbleLayout)
        messagesScroll.post { messagesScroll.fullScroll(View.FOCUS_DOWN) }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
